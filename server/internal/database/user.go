package database

import (
	"prohiking-server/internal/model"
)

func GetUserById(id int) (*model.User, error) {
	user := &model.User{}
	err := Instance.Table("users").Where("id = ?", id).First(user).Error
	if err != nil {
		return nil, err
	}
	return user, nil
}

func GetUserByName(username string) (*model.User, error) {
	user := &model.User{}
	err := Instance.Table("users").Where("username = ?", username).First(user).Error
	if err != nil {
		return nil, err
	}
	return user, nil
}

func CreateUser(user *model.User) error {
	if err := Instance.Create(user).Error; err != nil {
		return err
	}
	return nil
}

func DeleteUser(id int) error {
	if err := Instance.Delete(&model.User{Id: id}).Error; err != nil {
		return err
	}
	return nil
}
