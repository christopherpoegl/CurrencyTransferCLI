package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

    public BigDecimal getBalance(long userId);

    public Account getAccountById(long id);

    public Account getAccountByUserId(long userId);

    public void updateAccountBalances(long fromAccountId, long toAccountId, BigDecimal amount);

    //public Transfer getTransfer();
    //public void transfer???;
}
