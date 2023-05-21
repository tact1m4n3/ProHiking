#!/bin/bash

ENV_FILE=${ENV_FILE:-"server/.env"}
CERTS_FOLDER=${CERTS_FOLDER:-"certs"}

export PORT=${PORT:-8080}
export TLS_SERVER_CERT=${TLS_SERVER_CERT:-"../$CERTS_FOLDER/server-cert.pem"}
export TLS_SERVER_KEY=${TLS_SERVER_KEY:-"../$CERTS_FOLDER/server-key.pem"}
export SECRET=${SECRET:-"super secret secret"} # pls change for security reasons
export CORS_ORIGIN=${CORS_ORIGIN:-"https://"}
export DATABASE_URL=${DATABASE_URL:-"root:@tcp(localhost:3306)/prohiking"}

printenv \
    | grep "PORT\|TLS_SERVER_CERT\|TLS_SERVER_KEY\|SECRET\|CORS_ORIGIN\|DATABASE_URL" \
    | sed 's/\([^=]*=\)\(.*\)/\1"\2"/' \
    > $ENV_FILE
