package com.techelevator.tenmo.dao;


import com.techelevator.tenmo.model.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;

public class JdbcAccountDao implements AccountDao {

    JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {this.jdbcTemplate = jdbcTemplate;}

    public BigDecimal getBalance(long id) throws RuntimeException {
        String sql = "SELECT balance FROM accounts WHERE account_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, id);

        if(result.next()) {
            BigDecimal balance = new BigDecimal(result.getString("balance"));
            return balance;
        }
        else {
            System.out.println("Oh no we didn't get the balance!");
            throw new RuntimeException();
        }
    }

    public Account getAccountById(long id) throws RuntimeException {
        String sql = "SELECT * FROM accounts WHERE account_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, id);

        if(result.next()) {
            return makeAccount(result);
        }
        else {
            System.out.println("Oh no we didn't get the Account!");
            throw new RuntimeException();
        }
    }

    public Account getAccountByUserId(long userId) throws RuntimeException {
        String sql = "SELECT * FROM accounts WHERE user_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId);

        if(result.next()) {
            return makeAccount(result);
        }
        else {
            System.out.println("Oh no we didn't get the Account!");
            throw new RuntimeException();
        }
    }

    public void updateAccountBalances(long fromAccountId, long toAccountId, BigDecimal amount) {
       Account fromAccount = getAccountById(fromAccountId);
       Account toAccount = getAccountById(toAccountId);
        if (amount.compareTo(fromAccount.getBalance()) <= 0) {
            fromAccount.subtractAmount(amount);
            toAccount.addAmount(amount);

            String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";
            jdbcTemplate.update(sql, fromAccount.getBalance(), fromAccountId);
            jdbcTemplate.update(sql, toAccount.getBalance(), toAccountId);
        }


    }

    private Account makeAccount(SqlRowSet rowSet) {
        long id = rowSet.getLong("account_id");
        long userId = rowSet.getLong("user_id");
        BigDecimal balance = new BigDecimal(rowSet.getString("balance"));
        return new Account(id, userId, balance);

    }
}
