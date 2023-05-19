package middleware

import (
	"context"
	"net/http"
	"prohiking-server/api/auth"
	"prohiking-server/api/response"
	"prohiking-server/internal/database"
)

type ctxAuthUserKey int

const AuthUserKey ctxAuthUserKey = 0

func Auth(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		if cookie, err := r.Cookie("jwt"); err == nil {
			if userId, err := auth.ParseJWT(cookie.Value); err == nil {
				if user, err := database.GetUserById(userId); err == nil {
					ctx := context.WithValue(r.Context(), AuthUserKey, user)
					next.ServeHTTP(w, r.WithContext(ctx))
					return
				}
			}
		}
		response.Error(w, http.StatusUnauthorized, "you are not logged in")
	})
}
