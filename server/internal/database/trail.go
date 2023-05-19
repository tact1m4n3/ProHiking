package database

import (
	"fmt"
	"prohiking-server/internal/model"
	"strings"
)

func SearchTrails(
	limit int,
	offset int,
	name string,
	minLength float64,
	maxLength float64,
	centerLat float64,
	centerLon float64,
	radius float64,
) ([]*model.Trail, error) {
	trails := []*model.Trail{}
	trailsQuery := Instance.Table("trails t").Limit(limit).Offset(offset)

	if name != "" {
		trailsQuery.Where("t.name LIKE ?", "%"+strings.ReplaceAll(name, " ", "%")+"%")
	}

	trailsQuery.Where("t.length >= ? AND t.length <= ?", minLength, maxLength)

	trailsQuery.Joins("LEFT JOIN points p ON p.trail_id = t.id").Where(
		"SQRT(POW(p.lat-?, 2) + POW(p.lon-?, 2)) < ?",
		centerLat, centerLon, radius,
	).Group("p.trail_id").Order(fmt.Sprintf(
		"SQRT(POW(p.lat-%v, 2) + POW(p.lon-%v, 2))",
		centerLat, centerLon,
	))

	if err := trailsQuery.Find(&trails).Error; err != nil {
		return nil, err
	}

	return trails, nil
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

func CreateTrail(trail *model.Trail) error {
	if err := Instance.Create(trail).Error; err != nil {
		return err
	}
	return nil
}
