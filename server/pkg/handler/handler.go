package handler

import (
	"net/http"
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

	r.Use(cors.Handler(cors.Options{
		AllowedOrigins:   []string{"https://*"},
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Accept", "Authorization", "Content-Type", "X-CSRF-Token"},
		ExposedHeaders:   []string{"Link"},
		AllowCredentials: false,
		MaxAge:           300,
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
