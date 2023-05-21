package server

import (
	"crypto/tls"
	"log"
	"os"
)

func NewTLSConfig() *tls.Config {
	tlsCert, err := tls.LoadX509KeyPair(os.Getenv("TLS_SERVER_CERT"), os.Getenv("TLS_SERVER_KEY"))
	if err != nil {
		log.Fatalf("failed to load tls certificate: %v\n", err)
	}

	return &tls.Config{
		Certificates: []tls.Certificate{tlsCert},
	}
}
