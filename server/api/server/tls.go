package server

import (
	"crypto/tls"
	"crypto/x509"
	"log"
	"os"
)

func NewTLSConfig() *tls.Config {
	tlsCert, err := tls.LoadX509KeyPair(os.Getenv("TLS_SERVER_CERT"), os.Getenv("TLS_SERVER_KEY"))
	if err != nil {
		log.Fatalf("failed to load tls certificate: %v\n", err)
	}

	certPool := x509.NewCertPool()
	caCertPEM, err := os.ReadFile(os.Getenv("TLS_CA_CERT"))
	if err != nil {
		log.Fatalf("failed to read tls ca certificate: %v\n", err)
	}

	if ok := certPool.AppendCertsFromPEM(caCertPEM); !ok {
		log.Fatalln("invalid ca certificate")
	}

	return &tls.Config{
		ClientAuth:   tls.RequireAndVerifyClientCert,
		ClientCAs:    certPool,
		Certificates: []tls.Certificate{tlsCert},
	}
}
