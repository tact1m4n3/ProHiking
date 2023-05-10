package response

import (
	"encoding/json"
	"net/http"
	"strings"
)

type O map[string]interface{}

func JSON(w http.ResponseWriter, code int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(code)
	json.NewEncoder(w).Encode(data)
}

func Error(w http.ResponseWriter, code int, message string) {
	JSON(w, code, O{
		"status_code": code,
		"type":        strings.ToLower(http.StatusText(code)),
		"error":       message,
	})
}
