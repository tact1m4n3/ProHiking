package database

import (
	"prohiking-server/internal/model"
	"strings"
)

func CreateTrail(trail *model.Trail) error {
	if err := Instance.Create(trail).Error; err != nil {
		return err
	}
	return nil
}

func GetTrailById(id int) (*model.Trail, error) {
	trail := &model.Trail{}
	err := Instance.Table("trails").Where("id = ?", id).First(trail).Error
	if err != nil {
		return nil, err
	}
	return trail, nil
}

func GetTrailPath(id int) ([]*model.Point, error) {
	trail := &model.Trail{}
	err := Instance.Table("trails").Preload("Points").Where("id = ?", id).First(trail).Error
	if err != nil {
		return nil, err
	}
	return trail.Points, nil
}

func SearchTrails(
	limit int,
	offset int,
	name string,
	minLength float64,
	maxLength float64,
	bottomLeftLat float64,
	bottomLeftLon float64,
	topRightLat float64,
	topRightLon float64,
) ([]*model.Trail, error) {
	trails := []*model.Trail{}
	trailsQuery := Instance.Table("trails").Limit(limit).Offset(offset)

	if name != "" {
		trailsQuery.Where("name LIKE ?", "%"+strings.ReplaceAll(name, " ", "%")+"%")
	}

	trailsQuery.Where("length >= ? AND length <= ?", minLength, maxLength)

	pointQuery := Instance.Table("points").Distinct("trail_id").Where(
		"lat >= ? AND lat <= ? AND lon >= ? AND lon <= ?",
		bottomLeftLat, topRightLat, bottomLeftLon, topRightLon,
	)
	trailsQuery.Where("id IN (?)", pointQuery)

	if err := trailsQuery.Find(&trails).Error; err != nil {
		return nil, err
	}

	return trails, nil
}
