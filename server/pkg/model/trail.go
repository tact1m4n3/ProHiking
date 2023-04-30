package model

type Trail struct {
	Id     int      `json:"id" gorm:"primarykey"`
	Name   string   `json:"name"`
	From   string   `json:"from"`
	To     string   `json:"to"`
	Length float64  `json:"length"`
	Symbol string   `json:"symbol"`
	Points []*Point `json:"-"`
}

type Point struct {
	Id      int     `json:"-" gorm:"primarykey"`
	TrailId int     `json:"-"`
	Lat     float64 `json:"lat"`
	Long    float64 `json:"long"`
}
