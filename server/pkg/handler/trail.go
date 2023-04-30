package handler

import (
	"errors"
	"net/http"
	"server/pkg/database"
	"server/pkg/model"
	"server/pkg/response"
	"strconv"
	"strings"

	"github.com/go-chi/chi"
	"gorm.io/gorm"
)

func searchTrails(w http.ResponseWriter, r *http.Request) {
	name := r.URL.Query().Get("name")
	if name == "" {
		response.Error(w, http.StatusBadRequest, "trail name not specified")
		return
	}

	trails := []*model.Trail{}
	searchQuery := "%" + strings.ReplaceAll(name, " ", "%") + "%"
	if err := database.Instance.Table("trails").Where("name LIKE ?", searchQuery).Find(&trails).Error; err != nil {
		response.Error(w, http.StatusInternalServerError, err.Error())
		return
	}

	response.JSON(w, http.StatusOK, trails)
}

func getTrail(w http.ResponseWriter, r *http.Request) {
	id, err := strconv.Atoi(chi.URLParam(r, "id"))
	if err != nil {
		response.Error(w, http.StatusBadRequest, "trail id not valid")
		return
	}

	trail := &model.Trail{}
	if err := database.Instance.Table("trails").Where("id = ?", id).First(trail).Error; err != nil {
		if errors.Is(err, gorm.ErrRecordNotFound) {
			response.Error(w, http.StatusNotFound, "trail not found")
		} else {
			response.Error(w, http.StatusInternalServerError, err.Error())
		}
		return
	}

	response.JSON(w, http.StatusOK, trail)
}

func getTrailPath(w http.ResponseWriter, r *http.Request) {
	id, err := strconv.Atoi(chi.URLParam(r, "id"))
	if err != nil {
		response.Error(w, http.StatusBadRequest, "trail id not valid")
		return
	}

	trail := &model.Trail{}
	if err := database.Instance.Table("trails").Where("id = ?", id).Preload("Points").First(trail).Error; err != nil {
		if errors.Is(err, gorm.ErrRecordNotFound) {
			response.Error(w, http.StatusNotFound, "trail not found")
		} else {
			response.Error(w, http.StatusInternalServerError, err.Error())
		}
		return
	}

	response.JSON(w, http.StatusOK, trail.Points)
}
