package database

import (
	"os"
	"server/pkg/model"

	"gorm.io/driver/mysql"
	"gorm.io/gorm"
	"gorm.io/gorm/logger"
)

var Instance *gorm.DB

func Init() error {
	db, err := gorm.Open(mysql.Open(os.Getenv("DATABASE_URL")+"?parseTime=true"), &gorm.Config{
		Logger: logger.Default.LogMode(logger.Silent),
	})

	if err != nil {
		return err
	}

	if err := db.AutoMigrate(&model.Trail{}, &model.Point{}, &model.User{}); err != nil {
		return err
	}

	Instance = db
	return nil
}
