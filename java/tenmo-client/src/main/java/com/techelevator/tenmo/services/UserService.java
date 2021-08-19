package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class UserService {

    private String BASE_URL;
    private RestTemplate restTemplate = new RestTemplate();
    private String AUTH_TOKEN;

    public UserService(String url) {
        BASE_URL = url+"account";
    }

    public void setAuthToken(String token) {
        AUTH_TOKEN = token;
    }

    public BigDecimal getBalance() throws UserServiceException {
        try {
            Account account = restTemplate.exchange(BASE_URL + "/balance", HttpMethod.GET, makeAuthEntity(), Account.class).getBody();
            return account.getBalance();
        } catch (RestClientResponseException e) {
            throw new UserServiceException(e.getRawStatusCode() + " : " + e.getResponseBodyAsString());
        }
    }

    /*public List<User> getUsers() throws UserServiceException {
        List<User> users = new ArrayList<>();
        try {

        }

    }*/

    private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AUTH_TOKEN);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }
}
