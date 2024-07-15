package com.account.model;

import java.io.Serial;
import java.io.Serializable;

public class AuthResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwtToken;

    public AuthResponse(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getToken() {
        return this.jwtToken;
    }
}