#!/bin/bash

# TODO: maybe add a passphrase

if [ "$#" -ne 1 ] || ![ -e "$1" ]; then
    echo "usage: ./tls-cert-gen [directory]" >&2
    exit 1
fi

OUTDIR=$1

mkdir -pv $OUTDIR

openssl genrsa -out $OUTDIR/ca-key.pem 2048
openssl req -new -x509 -nodes -sha256 -days 365 \
    -key $OUTDIR/ca-key.pem \
    -out $OUTDIR/ca-cert.pem \
    -subj '/O=Pro Hiking Inc./C=RO'

openssl req -newkey rsa:2048 -nodes -days 365 \
    -keyout $OUTDIR/server-key.pem \
    -out $OUTDIR/server-req.pem \
    -subj '/O=Pro Hiking Inc./C=RO'

openssl x509 -req -days 365 -set_serial 01 \
    -in $OUTDIR/server-req.pem \
    -out $OUTDIR/server-cert.pem \
    -CA $OUTDIR/ca-cert.pem \
    -CAkey $OUTDIR/ca-key.pem \
    -extensions SAN \
    -extfile <(printf "\n[SAN]\nsubjectAltName=DNS:host1.bastionxp.com\nextendedKeyUsage=serverAuth")

openssl req -newkey rsa:2048 -nodes -sha256 -days 365 \
    -keyout $OUTDIR/client-key.pem \
    -out $OUTDIR/client-req.pem \
    -subj '/O=Pro Hiking Inc./C=RO'

openssl x509 -req -sha256 -days 365 -set_serial 01 \
    -in $OUTDIR/client-req.pem \
    -out $OUTDIR/client-cert.pem \
    -CA $OUTDIR/ca-cert.pem \
    -CAkey $OUTDIR/ca-key.pem \
    -extensions SAN \
    -extfile <(printf "\n[SAN]\nsubjectAltName=DNS:host2.bastionxp.com\nextendedKeyUsage=clientAuth")
