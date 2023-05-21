#!/bin/bash

# should only be called once on the server otherwise fetch them

CERTS_FOLDER=${CERTS_FOLDER:-"certs"}
HOST=${HOST:-"IP:192.168.1.119"} # or DNS:smth.com; change the default!!

CA_CERT=$CERTS_FOLDER/ca-cert.pem
CA_KEY=$CERTS_FOLDER/ca-key.pem
SERVER_REQ=$CERTS_FOLDER/server-req.pem
SERVER_CERT=$CERTS_FOLDER/server-cert.pem
SERVER_KEY=$CERTS_FOLDER/server-key.pem

mkdir -pv $CERTS_FOLDER

openssl genrsa -out $CA_KEY 2048
openssl req -new -x509 -nodes -sha256 -days 365 \
    -key $CA_KEY \
    -out $CA_CERT \
    -subj '/O=Pro Hiking Inc./C=RO'

openssl req -newkey rsa:2048 -nodes -days 365 \
    -keyout $SERVER_KEY \
    -out $SERVER_REQ \
    -subj '/O=Pro Hiking Inc./C=RO'

openssl x509 -req -days 365 -set_serial 01 \
    -in $SERVER_REQ \
    -out $SERVER_CERT \
    -CA $CA_CERT \
    -CAkey $CA_KEY \
    -extensions SAN \
    -extfile <(printf "\n[SAN]\nsubjectAltName=$HOST\nextendedKeyUsage=serverAuth")
