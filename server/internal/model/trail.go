package model

type Trail struct {
	Id     int      `json:"id" gorm:"primarykey"`
	Name   string   `json:"name"`
	From   string   `json:"from"`
	To     string   `json:"to"`
	Length float64  `json:"length" gorm:"type:decimal(5,2)"`
	Symbol string   `json:"symbol"`
	Point  *Point   `json:"point"`
	Points []*Point `json:"-"`
}

type Point struct {
	Id      int     `json:"-" gorm:"primarykey"`
	TrailId int     `json:"-"`
	Lat     float64 `json:"lat" gorm:"type:decimal(10,8)"`
	Lon     float64 `json:"lon" gorm:"type:decimal(11,8)"`
}
