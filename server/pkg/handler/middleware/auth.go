package middleware

import (
	"net/http"
	"server/pkg/auth"
	"server/pkg/response"
)

func Auth(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		if cookie, err := r.Cookie("jwt"); err == nil {
			if _, err := auth.ParseJWT(cookie.Value); err == nil {
				next.ServeHTTP(w, r)
				return
			}
		}
		response.Error(w, http.StatusUnauthorized, "you are not logged in")
	})
}
