package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {

    private long id;
    private long userId;
    private BigDecimal balance;
    //private List<Transfer> transfers;

    public Account(long id, long userId) {
        this.id = id;
        this.userId = userId;
        this.balance = new BigDecimal("1000.00");
        //this.transfers = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    /* todo:
    public void addToBalance(Transfer transfer???){}
    public void subtractFromBalance(Transfer transfer???){}
     */


}
