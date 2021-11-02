package agh.edu.pl.diet.payloads.request;

import javax.validation.constraints.NotBlank;

public class CheckTokenValidityRequest {
    @NotBlank
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
