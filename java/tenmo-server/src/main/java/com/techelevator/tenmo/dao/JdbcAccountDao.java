package com.techelevator.tenmo.dao;


import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;

public class JdbcAccountDao implements AccountDao {

    JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {this.jdbcTemplate = jdbcTemplate;}

    public BigDecimal getBalance(long userId) throws RuntimeException {
        String sql = "SELECT balance FROM accounts WHERE user_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId);

        if(result.next()) {
            BigDecimal balance = new BigDecimal(result.getString("balance"));
            return balance;
        }
        else {
            System.out.println("Oh no we didn't get the balance!");
            throw new RuntimeException();
        }
    }
}
