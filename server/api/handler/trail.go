package handler

import (
	"encoding/json"
	"errors"
	"fmt"
	"net/http"
	"prohiking-server/api/response"
	"prohiking-server/internal/database"
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

type searchTrailsPayload struct {
	Limit  int           `json:"limit"`
	Offset int           `json:"offset"`
	Name   string        `json:"name"`
	Length [2]float64    `json:"length"`
	Bbox   [2][2]float64 `json:"bbox"`
}

func SearchTrails(w http.ResponseWriter, r *http.Request) {
	payload := &searchTrailsPayload{}
	if err := json.NewDecoder(r.Body).Decode(payload); err != nil {
		response.Error(w, http.StatusInternalServerError, err.Error())
		return
	}

	trails, err := database.SearchTrails(
		payload.Limit,
		payload.Offset,
		payload.Name,
		payload.Length,
		payload.Bbox,
	)
	if err != nil {
		response.Error(w, http.StatusInternalServerError, err.Error())
		return
	}

	response.JSON(w, http.StatusOK, trails)
}
