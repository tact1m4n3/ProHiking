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

type searchTrailsParams struct {
	limit int
	name  string
	bbox  [2][2]float64
}

func parseSearchTrailsParams(r *http.Request) (*searchTrailsParams, error) {
	limit := 10
	limitString := r.URL.Query().Get("limit")
	if limitString != "" {
		limitNew, err := strconv.Atoi(limitString)
		if err != nil {
			return nil, errors.New("limit not valid")
		}
		limit = limitNew
	}

	name := r.URL.Query().Get("name")

	bbox := [2][2]float64{}
	positionString := r.URL.Query().Get("position")
	if positionString != "" {
		coords := strings.Split(positionString, ",")
		if len(coords) != 4 {
			return nil, errors.New("position not valid")
		}

		startLat, err := strconv.ParseFloat(coords[0], 64)
		if err != nil {
			return nil, errors.New("start latitude in position not valid")
		}
		bbox[0][0] = startLat

		startLon, err := strconv.ParseFloat(coords[1], 64)
		if err != nil {
			return nil, errors.New("start longitude in position not valid")
		}
		bbox[0][1] = startLon

		endLat, err := strconv.ParseFloat(coords[2], 64)
		if err != nil {
			return nil, errors.New("end latitude in position not valid")
		}
		bbox[1][0] = endLat

		endLon, err := strconv.ParseFloat(coords[3], 64)
		if err != nil {
			return nil, errors.New("end longitude in position not valid")
		}
		bbox[1][1] = endLon
	}

	return &searchTrailsParams{limit, name, bbox}, nil
}

func SearchTrails(w http.ResponseWriter, r *http.Request) {
	params, err := parseSearchTrailsParams(r)
	if err != nil {
		response.Error(w, http.StatusBadRequest, err.Error())
	}

	trails, err := database.SearchTrails(params.limit, params.name, params.bbox)
	if err != nil {
		response.Error(w, http.StatusInternalServerError, err.Error())
		return
	}

	response.JSON(w, http.StatusOK, trails)
}
