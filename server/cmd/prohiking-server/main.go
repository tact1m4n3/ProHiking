package main

import (
	"context"
	"crypto/tls"
	"crypto/x509"
	"log"
	"net/http"
	"os"
	"os/signal"
	"prohiking-server/internal/database"
	"prohiking-server/internal/handler"
	"syscall"
	"time"

	_ "github.com/joho/godotenv/autoload"
)

func main() {
	log.Println("starting server...")

	if err := database.Init(); err != nil {
		log.Fatalf("failed to initialize database: %v\n", err)
	}

	server := http.Server{
		Addr:      ":" + os.Getenv("PORT"),
		Handler:   handler.New(),
		TLSConfig: newTLSConfig(),
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

func newTLSConfig() *tls.Config {
	tlsCert, err := tls.LoadX509KeyPair(os.Getenv("TLS_SERVER_CERT"), os.Getenv("TLS_SERVER_KEY"))
	if err != nil {
		log.Fatalf("failed to load tls certificate: %v\n", err)
	}

	certPool := x509.NewCertPool()
	caCertPEM, err := os.ReadFile(os.Getenv("TLS_CA_CERT"))
	if err != nil {
		log.Fatalf("failed to read tls ca certificate: %v\n", err)
	}

	if ok := certPool.AppendCertsFromPEM(caCertPEM); !ok {
		log.Fatalln("invalid ca certificate")
	}

	return &tls.Config{
		ClientAuth:   tls.RequireAndVerifyClientCert,
		ClientCAs:    certPool,
		Certificates: []tls.Certificate{tlsCert},
	}
}
