package handler

import (
	"fmt"
	"net/http"
	"prohiking-server/internal/response"
)

func RouteNotFound(w http.ResponseWriter, r *http.Request) {
	response.Error(w, http.StatusNotFound, fmt.Sprintf(
		"route '%v' not found",
		r.URL.Path,
	))
}

func MethodNotAllowed(w http.ResponseWriter, r *http.Request) {
	response.Error(w, http.StatusMethodNotAllowed, fmt.Sprintf(
		"method '%v' not allowed for route '%v'",
		r.Method, r.URL.Path,
	))
}

func TooManyRequests(w http.ResponseWriter, r *http.Request) {
	response.Error(w, http.StatusTooManyRequests, "too many requests... slow down")
}
