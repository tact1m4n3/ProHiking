package database

import (
	"os"
	"server/internal/model"

	"gorm.io/driver/mysql"
	"gorm.io/gorm"
	"gorm.io/gorm/logger"
)

var (
	ErrNotFound      = gorm.ErrRecordNotFound
	ErrDuplicatedKey = gorm.ErrDuplicatedKey
)

var Instance *gorm.DB

func Init() error {
	db, err := gorm.Open(mysql.Open(os.Getenv("DATABASE_URL")+"?parseTime=true"), &gorm.Config{
		Logger: logger.Default.LogMode(logger.Silent),

		PrepareStmt:    true,
		TranslateError: true,
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
