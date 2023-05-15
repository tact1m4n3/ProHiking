package main

import (
	"fmt"
	"log"
	"os"
	"prohiking-server/internal/database"
	"prohiking-server/internal/model"
	"strconv"

	geojson "github.com/paulmach/go.geojson"
)

func main() {
	if len(os.Args) < 3 {
		fmt.Fprintln(os.Stderr, "usage: ./mktraildb [input file] [db url]")
		os.Exit(1)
	}

	data, err := os.ReadFile(os.Args[1])
	if err != nil {
		log.Fatalf("failed to open file: %v\n", err)
	}

	if err := database.Init(os.Args[2]); err != nil {
		log.Fatalf("failed to initialize database: %v\n", err)
	}

	collection, err := geojson.UnmarshalFeatureCollection(data)
	if err != nil {
		log.Fatalf("failed to unmarshal feature collection: %v\n", err)
	}

	for _, feature := range collection.Features {
		// source := feature.PropertyMustString("source")
		// !strings.Contains(source, "Muntii Nostri gpx") ||
		if !feature.Geometry.IsLineString() {
			continue
		}

		name := feature.PropertyMustString("name", "")
		from := feature.PropertyMustString("from", "")
		to := feature.PropertyMustString("to", "")
		length, _ := strconv.ParseFloat(feature.PropertyMustString("distance", "0"), 64)
		symbol := feature.PropertyMustString("osmc:symbol", "")

		if name == "" || from == "" || to == "" || length == 0.0 || symbol == "" {
			continue
		}

		trail := &model.Trail{
			Name:   name,
			From:   from,
			To:     to,
			Length: length,
			Symbol: symbol,
		}

		for _, coords := range feature.Geometry.LineString {
			point := &model.Point{
				Lat: coords[1],
				Lon: coords[0],
			}
			trail.Points = append(trail.Points, point)
		}

		if err := database.CreateTrail(trail); err != nil {
			log.Fatalln(err)
		}
	}

	log.Println("everything is ok :))")
}
