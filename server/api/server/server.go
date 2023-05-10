package server

import (
	"context"
	"log"
	"net/http"
	"os"
	"os/signal"
	"prohiking-server/api/handler"
	"prohiking-server/internal/database"
	"syscall"
	"time"

	_ "github.com/joho/godotenv/autoload"
)

func Run() {
	log.Println("starting server...")

	if err := database.Init(os.Getenv("DATABASE_URL")); err != nil {
		log.Fatalf("failed to initialize database: %v\n", err)
	}

	server := http.Server{
		Addr:      ":" + os.Getenv("PORT"),
		Handler:   handler.New(),
		TLSConfig: NewTLSConfig(),
	}

	go func() {
		err := server.ListenAndServeTLS("", "")
		if err != nil && err != http.ErrServerClosed {
			log.Fatalf("server error: %v\n", err)
		}
	}()

	log.Printf("listening on port %v\n", server.Addr)

	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	log.Println("shuting down server")
	if err := server.Shutdown(ctx); err != nil {
		log.Fatalf("failed to shutdown server: %v\n", err)
	}
}
