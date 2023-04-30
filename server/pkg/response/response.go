package response

import (
	"encoding/json"
	"net/http"
)

type O map[string]interface{}

var errorCodeToString = map[int]string{
	http.StatusBadRequest:          "bad request",
	http.StatusInternalServerError: "internal",
	http.StatusNotFound:            "not found",
	http.StatusUnauthorized:        "unauthorized",
}

func JSON(w http.ResponseWriter, code int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(code)
	json.NewEncoder(w).Encode(data)
}

func Error(w http.ResponseWriter, code int, message string) {
	_error, ok := errorCodeToString[code]
	if !ok {
		_error = "unknown"
	}

	JSON(w, code, O{
		"error":   _error,
		"message": message,
	})
}
