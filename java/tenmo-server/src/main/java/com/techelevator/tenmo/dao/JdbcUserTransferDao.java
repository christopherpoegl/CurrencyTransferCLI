package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserTransfer;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcUserTransferDao implements UserTransferDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcUserTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    UserTransfer userTransfer = new UserTransfer();
    @Override
    public void setUserTransferInfo(long transferId) {
        String sql = "SELECT transfers.transfer_id, transfers.account_from, transfers.account_to, transfers.amount, users.username FROM transfers\n" +
                "JOIN accounts ON account_id = transfers.account_from OR transfers.account_to=accounts.account_id\n" +
                "JOIN users ON accounts.user_id = users.user_id\n" +
                "WHERE transfers.transfer_id = ?;";
        long id = jdbcTemplate.queryForObject(sql, Long.class, transferId);

    }

}
