package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class UserTransfer {

    private long transferId;
    private long transferStatusId;
    private St transferStatusDesc;
    private long transferTypeId;
    private long transferTypeDesc;
    private String accountFromUserName;
    private String accountToUsername;
    private BigDecimal amount;

    public void setTransferStatusDesc(long transferStatusDesc) {
        this.transferStatusDesc = transferStatusDesc;
    }

    public void setTransferTypeDesc(long transferTypeDesc) {
        this.transferTypeDesc = transferTypeDesc;
    }

    public void setTransferId(long transferId) {
        this.transferId = transferId;
    }

    public void setTransferStatusId(long transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public void setTransferTypeId(long transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public void setAccountFromUserName(String accountFromUserName) {
        this.accountFromUserName = accountFromUserName;
    }

    public void setAccountToUsername(String accountToUsername) {
        this.accountToUsername = accountToUsername;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public long getTransferStatusDesc() {
        return transferStatusDesc;
    }

    public long getTransferTypeDesc() {
        return transferTypeDesc;
    }

    public long getTransferId() {
        return transferId;
    }

    public long getTransferStatusId() {
        return transferStatusId;
    }

    public long getTransferTypeId() {
        return transferTypeId;
    }

    public String getAccountFromUserName() {
        return accountFromUserName;
    }

    public String getAccountToUsername() {
        return accountToUsername;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
