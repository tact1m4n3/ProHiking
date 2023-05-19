package handler

import (
	"errors"
	"fmt"
	"net/http"
	"prohiking-server/api/response"
	"prohiking-server/internal/database"
	"strconv"
	"strings"

	"github.com/go-chi/chi"
)

func SearchTrails(w http.ResponseWriter, r *http.Request) {
	limit, err := strconv.Atoi(r.URL.Query().Get("limit"))
	if err != nil {
		response.Error(w, http.StatusBadRequest, "limit parameter not valid")
		return
	}

	offset, err := strconv.Atoi(r.URL.Query().Get("offset"))
	if err != nil {
		response.Error(w, http.StatusBadRequest, "offset parameter not valid")
		return
	}

	name := r.URL.Query().Get("name")

	lengthRaw := strings.Split(r.URL.Query().Get("length"), ",")
	if len(lengthRaw) != 2 {
		response.Error(w, http.StatusBadRequest, "length parameter not valid")
		return
	}

	minLength, err := strconv.ParseFloat(lengthRaw[0], 64)
	if err != nil {
		response.Error(w, http.StatusBadRequest, "minimum length parameter not valid")
		return
	}

	maxLength, err := strconv.ParseFloat(lengthRaw[1], 64)
	if err != nil {
		response.Error(w, http.StatusBadRequest, "maximum length parameter not valid")
		return
	}

	centerRaw := strings.Split(r.URL.Query().Get("center"), ",")
	if len(centerRaw) != 2 {
		response.Error(w, http.StatusBadRequest, "center parameter not valid")
		return
	}

	centerLat, err := strconv.ParseFloat(centerRaw[0], 64)
	if err != nil {
		response.Error(w, http.StatusBadRequest, "center latitude parameter not valid")
		return
	}

	centerLon, err := strconv.ParseFloat(centerRaw[1], 64)
	if err != nil {
		response.Error(w, http.StatusBadRequest, "center longitude parameter not valid")
		return
	}

	radius, err := strconv.ParseFloat(r.URL.Query().Get("radius"), 64)
	if err != nil {
		response.Error(w, http.StatusBadRequest, "radius parameter not valid")
		return
	}

	trails, err := database.SearchTrails(
		limit,
		offset,
		name,
		minLength,
		maxLength,
		centerLat,
		centerLon,
		radius,
	)
	if err != nil {
		response.Error(w, http.StatusInternalServerError, err.Error())
		return
	}

	response.JSON(w, http.StatusOK, trails)
}

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
				"trail with id %v does not exist", id,
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
				"trail with id %v does not exist", id,
			))
		} else {
			response.Error(w, http.StatusInternalServerError, err.Error())
		}
		return
	}

	response.JSON(w, http.StatusOK, points)
}
