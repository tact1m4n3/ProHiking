package handler

import (
	"os"
	my_middleware "server/pkg/handler/middleware"
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

	origin := os.Getenv("CORS_ORIGIN")
	r.Use(cors.Handler(cors.Options{
		AllowedOrigins:   []string{origin},
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE"},
		AllowCredentials: true,
	}))

	r.NotFound(routeNotFound)
	r.MethodNotAllowed(methodNotAllowed)

	r.Mount("/api", routes())

	return r
}

func routes() chi.Router {
	r := chi.NewRouter()

	r.Route("/user", func(r chi.Router) {
		r.Post("/login", loginUser)
		r.Post("/register", registerUser)
		r.Post("/logout", logoutUser)
	})

	r.Group(func(r chi.Router) {
		r.Use(my_middleware.Auth)

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
