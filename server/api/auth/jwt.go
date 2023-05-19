package auth

import (
	"errors"
	"os"
	"time"

	"github.com/golang-jwt/jwt"
)

var ErrInvalidToken error = errors.New("invalid jwt")

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

func ParseJWT(tokenString string) (int, error) {
	token, err := jwt.Parse(tokenString, func(token *jwt.Token) (interface{}, error) {
		return []byte(os.Getenv("SECRET")), nil
	})

	if err == nil && token.Valid {
		if claims, ok := token.Claims.(jwt.MapClaims); ok {
			if userId, ok := claims["iss"]; ok {
				return int(userId.(float64)), nil // TODO: Find out why it's a float
			}
		}
	}

	return -1, ErrInvalidToken
}
