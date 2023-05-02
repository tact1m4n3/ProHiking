package handler

import (
	"log"
	"os"
	my_middleware "server/pkg/handler/middleware"
	"time"

	chi_middleware "github.com/go-chi/chi/middleware"

	"github.com/go-chi/chi"
	"github.com/go-chi/cors"
)

func New() chi.Router {
	r := chi.NewRouter()

	chi_middleware.DefaultLogger = chi_middleware.RequestLogger(
		&chi_middleware.DefaultLogFormatter{
			Logger:  log.Default(),
			NoColor: true,
		},
	)

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

	r.NotFound(RouteNotFound)
	r.MethodNotAllowed(MethodNotAllowed)

	r.Mount("/api", Routes())

	return r
}

func Routes() chi.Router {
	r := chi.NewRouter()

	r.Route("/user", func(r chi.Router) {
		r.Post("/login", LoginUser)
		r.Post("/register", RegisterUser)
		r.Post("/logout", LogoutUser)
	})

	r.Group(func(r chi.Router) {
		r.Use(my_middleware.Auth)

		r.Route("/users/{id}", func(r chi.Router) {
			r.Get("/", GetUserById)
		})

		r.Route("/trails", func(r chi.Router) {
			r.Get("/search", SearchTrails)

			r.Route("/{id}", func(r chi.Router) {
				r.Get("/", GetTrailById)
				r.Get("/path", GetTrailPath)
			})
		})
	})

	return r
}
