package com.example.gorent.data.models.auth;

public class JWTToken {

    private String id_token;

    public JWTToken() {
    }

    public JWTToken(String id_token) {
        this.id_token = id_token;
    }

    public String getId_token() {
        return id_token;
    }

    public void setId_token(String id_token) {
        this.id_token = id_token;
    }

    @Override
    public String toString() {
        return "JWTToken{" +
                "id_token='" + id_token + '\'' +
                '}';
    }
}
