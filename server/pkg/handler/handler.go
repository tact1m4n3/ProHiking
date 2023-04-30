package handler

import (
	"net/http"
	"os"
	my_middleware "server/pkg/handler/middleware"
	"server/pkg/response"
	"time"

	chi_middleware "github.com/go-chi/chi/middleware"

	"github.com/go-chi/chi"
	"github.com/go-chi/cors"
)

func New() chi.Router {
	r := chi.NewRouter()

	r.Use(chi_middleware.RequestID)
	r.Use(chi_middleware.RealIP)
	r.Use(chi_middleware.Logger)
	r.Use(chi_middleware.Recoverer)

	r.Use(chi_middleware.Timeout(30 * time.Second))

	// TODO: maybe better handlers
	r.NotFound(func(w http.ResponseWriter, r *http.Request) {
		response.Error(w, http.StatusMethodNotAllowed, "route not found")
	})

	r.MethodNotAllowed(func(w http.ResponseWriter, r *http.Request) {
		response.Error(w, http.StatusMethodNotAllowed, "method not allowed")
	})

	origin := os.Getenv("CORS_ORIGIN")
	r.Use(cors.Handler(cors.Options{
		AllowedOrigins:   []string{origin},
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE"},
		AllowCredentials: true,
	}))

	r.Mount("/api", Routes())

	return r
}

func Routes() chi.Router {
	r := chi.NewRouter()

	r.Route("/user", func(r chi.Router) {
		r.Post("/login", loginUser)
		r.Post("/register", registerUser)
		r.Post("/logout", logoutUser)
	})

	r.Group(func(r chi.Router) {
		r.Use(my_middleware.Auth)

		r.Get("/test", testHandler)

		r.Route("/users/{id}", func(r chi.Router) {
			r.Get("/", getUser)
		})

		r.Route("/trails", func(r chi.Router) {
			r.Get("/search", searchTrails)

			r.Route("/{id}", func(r chi.Router) {
				r.Get("/", getTrail)
				r.Get("/path", getTrailPath)
			})
		})
	})

	return r
}

func testHandler(w http.ResponseWriter, r *http.Request) {
	w.Write([]byte("Hello world"))
}
