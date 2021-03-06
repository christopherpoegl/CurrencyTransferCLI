package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserTransfer;
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

    public void setAuthToken(String token) { AUTH_TOKEN = token; }

    public BigDecimal getBalance() throws UserServiceException {
        try {
            return restTemplate.exchange(BASE_URL + "/balance", HttpMethod.GET, makeAuthEntity(), BigDecimal.class).getBody();
        } catch (RestClientResponseException e) {
            throw new UserServiceException(e.getRawStatusCode() + " : " + e.getResponseBodyAsString());
        }
    }

    public UserTransfer[] listTransfers() throws UserServiceException {
        UserTransfer[] userTransfers = null;
        try {
            return restTemplate.exchange(BASE_URL + "/transfers/list", HttpMethod.GET, makeAuthEntity(), UserTransfer[].class).getBody();
        } catch (RestClientResponseException e) {
            throw new UserServiceException(e.getRawStatusCode() + " : " + e.getResponseBodyAsString());
        }
    }

    public UserTransfer[] listPendingTransfers() throws UserServiceException {
        UserTransfer[] PendingUserTransfers = null;
        try {
            return restTemplate.exchange(BASE_URL + "/transfers/pending", HttpMethod.GET, makeAuthEntity(), UserTransfer[].class).getBody();
        } catch (RestClientResponseException e) {
            throw new UserServiceException(e.getRawStatusCode() + " : " + e.getResponseBodyAsString());
        }
    }

    public Transfer getTransferById(long id) throws UserServiceException {
        try {
            return restTemplate.exchange(BASE_URL + "/transfer/" + id, HttpMethod.GET, makeAuthEntity(), Transfer.class).getBody();
        } catch (RestClientResponseException e) {
            throw new UserServiceException(e.getRawStatusCode() + " : " + e.getResponseBodyAsString());
        }
    }

    public void acceptTransfer(long id) throws UserServiceException {
        try {
            restTemplate.exchange(BASE_URL + "/transfer/" + id +"/accept", HttpMethod.POST, makeAuthEntity(), Long.class).getBody();
        } catch (RestClientResponseException e) {
            throw new UserServiceException(e.getRawStatusCode() + " : " + e.getResponseBodyAsString());
        }
    }

    public void rejectTransfer(long id) throws UserServiceException {
        try {
            restTemplate.exchange(BASE_URL + "/transfer/" + id +"/reject", HttpMethod.POST, makeAuthEntity(), Long.class).getBody();
        } catch (RestClientResponseException e) {
            throw new UserServiceException(e.getRawStatusCode() + " : " + e.getResponseBodyAsString());
        }
    }


    public User[] getUsers() throws UserServiceException {
        User[] users = null;
        try {
            return restTemplate.exchange(BASE_URL + "/users", HttpMethod.GET, makeAuthEntity(), User[].class).getBody();
        } catch (RestClientResponseException e) {
            throw new UserServiceException(e.getRawStatusCode() + " : " + e.getResponseBodyAsString());
        }
    }

    public Transfer sendMoney(long sendingUserId, Long receivingUserId, BigDecimal amount) throws UserServiceException {


        Transfer transfer = new Transfer();
        transfer.setTransfer_status_desc("Pending");
        transfer.setAccount_from(sendingUserId);
        transfer.setAccount_to(receivingUserId);
        transfer.setTransfer_status_id(1);
        transfer.setTransfer_type_id(2);
        transfer.setTransfer_type_desc("Send");
        transfer.setAmount(amount);

        try {
            HttpEntity<Transfer> transferEntity = makeTransferEntity(transfer);
            transferEntity = restTemplate.exchange(BASE_URL + "/transfer/send", HttpMethod.POST, transferEntity, Transfer.class);
        } catch (RestClientResponseException e) {
            throw new UserServiceException(e.getRawStatusCode() + " : " + e.getResponseBodyAsString());
        }
        return transfer;
    }

    public Transfer requestMoney(long requestingId, Long receivingId, BigDecimal amount) throws UserServiceException {
        Transfer transfer = new Transfer();
        transfer.setTransfer_status_desc("Pending");
        transfer.setAccount_from(requestingId);
        transfer.setAccount_to(receivingId);
        transfer.setTransfer_status_id(1);
        transfer.setTransfer_type_id(1);
        transfer.setTransfer_type_desc("Request");
        transfer.setAmount(amount);

        try {
            HttpEntity<Transfer> transferEntity = makeTransferEntity(transfer);
            transferEntity = restTemplate.exchange(BASE_URL + "/transfer/request", HttpMethod.POST, transferEntity, Transfer.class);
        } catch (RestClientResponseException e) {
            throw new UserServiceException(e.getRawStatusCode() + " : " + e.getResponseBodyAsString());
        }
        return transfer;
    }

    private HttpEntity<String> makeStatusId(String string) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AUTH_TOKEN);
        HttpEntity<String> entity = new HttpEntity<>(string, headers);
        if (entity == null) System.out.println("Entity is Null");
        return entity;
    }

        private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AUTH_TOKEN);
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
        if (entity == null) System.out.println("Entity is Null");
        return entity;
    }

    private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AUTH_TOKEN);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }
}
