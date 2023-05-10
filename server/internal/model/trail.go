package model

type Trail struct {
	Id     int      `json:"id" gorm:"primarykey"`
	Name   string   `json:"name"`
	Start  string   `json:"start"`
	End    string   `json:"end"`
	Length float64  `json:"length" gorm:"type:decimal(10,6)"`
	Symbol string   `json:"symbol"`
	Points []*Point `json:"-"`
}

type Point struct {
	Id      int     `json:"-" gorm:"primarykey"`
	TrailId int     `json:"-"`
	Lat     float64 `json:"lat" gorm:"type:decimal(10,6)"`
	Lon     float64 `json:"lon" gorm:"type:decimal(10,6)"`
}
