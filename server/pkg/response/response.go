package response

import (
	"encoding/json"
	"net/http"
)

type StatusCode int

func (s StatusCode) String() string {
	switch s {
	case http.StatusBadRequest:
		return "bad request"
	case http.StatusInternalServerError:
		return "internal"
	case http.StatusNotFound:
		return "not found"
	case http.StatusUnauthorized:
		return "unauthorized"
	default:
		return "unknown"
	}
}

type O map[string]interface{}

func JSON(w http.ResponseWriter, code StatusCode, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(int(code))
	json.NewEncoder(w).Encode(data)
}

func Error(w http.ResponseWriter, code StatusCode, message string) {
	JSON(w, code, O{
		"error":   code.String(),
		"message": message,
	})
}
