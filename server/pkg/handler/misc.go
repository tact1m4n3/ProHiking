package handler

import (
	"fmt"
	"net/http"
	"server/pkg/response"
)

func routeNotFound(w http.ResponseWriter, r *http.Request) {
	response.Error(w, http.StatusNotFound, fmt.Sprintf(
		"route '%v' not found",
		r.URL.Path,
	))
}

func methodNotAllowed(w http.ResponseWriter, r *http.Request) {
	response.Error(w, http.StatusMethodNotAllowed, fmt.Sprintf(
		"method '%v' not allowed for route '%v'",
		r.Method, r.URL.Path,
	))
}
