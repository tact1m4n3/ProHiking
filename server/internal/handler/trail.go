package handler

import (
	"errors"
	"fmt"
	"net/http"
	"server/internal/database"
	"server/internal/response"
	"strconv"

	"github.com/go-chi/chi"
)

func GetTrailById(w http.ResponseWriter, r *http.Request) {
	id, err := strconv.Atoi(chi.URLParam(r, "id"))
	if err != nil {
		response.Error(w, http.StatusBadRequest, "trail id not valid")
		return
	}

	trail, err := database.GetTrailById(id)
	if err != nil {
		if errors.Is(err, database.ErrNotFound) {
			response.Error(w, http.StatusNotFound, fmt.Sprintf(
				"no trail found with id %v", id,
			))
		} else {
			response.Error(w, http.StatusInternalServerError, err.Error())
		}
		return
	}

	response.JSON(w, http.StatusOK, trail)
}

func GetTrailPath(w http.ResponseWriter, r *http.Request) {
	id, err := strconv.Atoi(chi.URLParam(r, "id"))
	if err != nil {
		response.Error(w, http.StatusBadRequest, "trail id not valid")
		return
	}

	points, err := database.GetTrailPath(id)
	if err != nil {
		if errors.Is(err, database.ErrNotFound) {
			response.Error(w, http.StatusNotFound, fmt.Sprintf(
				"no trail path found with id %v", id,
			))
		} else {
			response.Error(w, http.StatusInternalServerError, err.Error())
		}
		return
	}

	response.JSON(w, http.StatusOK, points)
}

func SearchTrails(w http.ResponseWriter, r *http.Request) {
	name := r.URL.Query().Get("name")
	if name == "" {
		response.Error(w, http.StatusBadRequest, "trail name not specified")
		return
	}

	trails, err := database.SearchTrails(name)
	if err != nil {
		response.Error(w, http.StatusInternalServerError, err.Error())
		return
	}

	response.JSON(w, http.StatusOK, trails)
}
