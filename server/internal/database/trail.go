package database

import (
	"prohiking-server/internal/model"
	"strings"
)

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
	err := Instance.Table("trails").Where("id = ?", id).Preload("Points").First(trail).Error
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

func SearchTrails(name string) ([]*model.Trail, error) {
	trails := []*model.Trail{}
	query := "%" + strings.ReplaceAll(name, " ", "%") + "%"
	err := Instance.Table("trails").Where("name LIKE ?", query).Find(&trails).Error
	if err != nil {
		return nil, err
	}
	return trails, nil
}
