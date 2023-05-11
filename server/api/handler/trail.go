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
	limit, err := strconv.Atoi(r.URL.Query().Get("limit"))
	if err != nil {
		response.Error(w, http.StatusBadRequest, "invalid limit parameter")
		return
	}

	offset, err := strconv.Atoi(r.URL.Query().Get("offset"))
	if err != nil {
		response.Error(w, http.StatusBadRequest, "invalid offset parameter")
		return
	}

	name := r.URL.Query().Get("name")

	lengthRaw := strings.Split(r.URL.Query().Get("length"), ",")
	if len(lengthRaw) != 2 {
		response.Error(w, http.StatusBadRequest, "invalid length parameter")
		return
	}

	minLength, err := strconv.ParseFloat(lengthRaw[0], 64)
	if err != nil {
		response.Error(w, http.StatusBadRequest, "invalid minimum length parameter")
		return
	}

	maxLength, err := strconv.ParseFloat(lengthRaw[1], 64)
	if err != nil {
		response.Error(w, http.StatusBadRequest, "invalid maximum length parameter")
		return
	}

	bboxRaw := strings.Split(r.URL.Query().Get("bbox"), ",")
	if len(bboxRaw) != 4 {
		response.Error(w, http.StatusBadRequest, "invalid position parameter")
		return
	}

	bottomLeftLat, err := strconv.ParseFloat(bboxRaw[0], 64)
	if err != nil {
		response.Error(w, http.StatusBadRequest, "invalid bottom left latitude parameter")
		return
	}

	bottomLeftLon, err := strconv.ParseFloat(bboxRaw[1], 64)
	if err != nil {
		response.Error(w, http.StatusBadRequest, "invalid bottom left longitude parameter")
		return
	}

	topRightLat, err := strconv.ParseFloat(bboxRaw[2], 64)
	if err != nil {
		response.Error(w, http.StatusBadRequest, "invalid top right latitude parameter")
		return
	}

	topRightLon, err := strconv.ParseFloat(bboxRaw[3], 64)
	if err != nil {
		response.Error(w, http.StatusBadRequest, "invalid top right longitude parameter")
		return
	}

	trails, err := database.SearchTrails(
		limit,
		offset,
		name,
		minLength,
		maxLength,
		bottomLeftLat,
		bottomLeftLon,
		topRightLat,
		topRightLon,
	)
	if err != nil {
		response.Error(w, http.StatusInternalServerError, err.Error())
		return
	}

	response.JSON(w, http.StatusOK, trails)
}
