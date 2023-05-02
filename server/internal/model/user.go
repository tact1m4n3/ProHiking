package model

type User struct {
	Id       int    `json:"id" gorm:"primarykey"`
	Username string `json:"username" gorm:"unique"`
	Email    string `json:"email" gorm:"unique"`
	Password string `json:"-"`
}
