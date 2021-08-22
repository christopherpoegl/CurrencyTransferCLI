package com.techelevator.tenmo.model;


import java.math.BigDecimal;

public class UserTransfer {

    private long transferId;
    private long transferStatusId;
    private long accountFromId;
    private long accountToId;
    private String accountFromUserName;
    private String accountToUsername;
    private BigDecimal amount;

    public UserTransfer() {
    }

    public long getTransferId() {
        return transferId;
    }

    public void setTransferId(long transferId) {
        this.transferId = transferId;
    }

    public long getAccountFromId() {
        return accountFromId;
    }

    public void setAccountFromId(long accountFromId) {
        this.accountFromId = accountFromId;
    }

    public long getAccountToId() {
        return accountToId;
    }

    public void setAccountToId(long accountToId) {
        this.accountToId = accountToId;
    }

    public String getAccountFromUserName() {
        return accountFromUserName;
    }

    public void setAccountFromUserName(String accountFromUserName) {
        this.accountFromUserName = accountFromUserName;
    }

    public String getAccountToUsername() {
        return accountToUsername;
    }

    public void setAccountToUsername(String accountToUsername) {
        this.accountToUsername = accountToUsername;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public long getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(long transferStatusId) {
        this.transferStatusId = transferStatusId;
    }
}
