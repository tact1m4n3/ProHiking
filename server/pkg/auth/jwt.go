package auth

import (
	"errors"
	"os"
	"time"

	"github.com/golang-jwt/jwt"
)

func GenerateJWT(userId int, expirationTime time.Duration) (string, error) {
	token := jwt.NewWithClaims(
		jwt.SigningMethodHS256,
		jwt.MapClaims{
			"iss": userId,
			"exp": time.Now().Add(expirationTime).Unix(),
		},
	)

	tokenString, err := token.SignedString([]byte(os.Getenv("SECRET")))
	if err != nil {
		return "", err
	}

	return tokenString, nil
}

func ParseJWT(tokenString string) (*jwt.Token, error) {
	token, err := jwt.Parse(tokenString, func(token *jwt.Token) (interface{}, error) {
		return []byte(os.Getenv("SECRET")), nil
	})

	if err != nil {
		return nil, err
	}

	if !token.Valid {
		return nil, errors.New("invalid jwt")
	}

	return token, nil
}
