package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserTransfer;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private TransferDao transferDao;

    public JdbcUserTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    UserTransfer userTransfer = new UserTransfer();
    @Override
    public UserTransfer getUserTransferInfoById(long transferId) {
        UserTransfer userTransfer = null;
        String sql = "SELECT transfers.transfer_id, transfers.transfer_type_id, transfers.transfer_status_id, " +
                "transfers.transfer_type_id, transfers.amount, users.username FROM transfers\n" +
                "JOIN accounts ON account_id = transfers.account_from\n" +
                "JOIN users ON accounts.user_id = users.user_id\n" +
                "WHERE transfers.transfer_id = ?;";
        SqlRowSet resultsFrom = jdbcTemplate.queryForRowSet(sql, transferId);
        sql = "SELECT transfers.transfer_id, transfers.account_from, transfers.account_to, transfers.amount, users.username FROM transfers\n" +
                "JOIN accounts ON account_id = transfers.account_to\n" +
                "JOIN users ON accounts.user_id = users.user_id\n" +
                "WHERE transfers.transfer_id = ?;";
        SqlRowSet resultsTo = jdbcTemplate.queryForRowSet(sql, transferId);
        if (resultsFrom.next() && resultsTo.next()) {
            userTransfer = mapRowsToUserTransfer(resultsFrom, resultsTo);
        }
        return userTransfer;
    }

    public List<UserTransfer> getUserTransferListByUserName(String userName) {
        List<UserTransfer> userTransfers = new ArrayList<>();
        UserTransfer userTransfer = null;
        String sql = "SELECT transfers.transfer_id, transfers.transfer_type_id, transfers.transfer_status_id, " +
                "transfers.transfer_type_id, transfers.amount, users.username FROM transfers\n" +
                "JOIN accounts ON account_id = transfers.account_from\n" +
                "JOIN users ON accounts.user_id = users.user_id\n" +
                "WHERE users.user_name = ?;";
        SqlRowSet resultsFrom = jdbcTemplate.queryForRowSet(sql, userName);
        sql = "SELECT transfers.transfer_id, transfers.account_from, transfers.account_to, transfers.amount, users.username FROM transfers\n" +
                "JOIN accounts ON account_id = transfers.account_to\n" +
                "JOIN users ON accounts.user_id = users.user_id\n" +
                "WHERE users.user_name = ?;";
        SqlRowSet resultsTo = jdbcTemplate.queryForRowSet(sql, userName);
        if (resultsFrom.next() && resultsTo.next()) {
            userTransfers.add(mapRowsToUserTransfer(resultsFrom, resultsTo));
        }
        return userTransfers;
    }

    public List<UserTransfer> getPendingUserTransferList(String userName) {
        List<UserTransfer> userTransfers = new ArrayList<>();
        String sql = "SELECT transfers.transfer_id, transfers.transfer_type_id, transfers.transfer_status_id, " +
                "transfers.transfer_type_id, transfers.amount, users.username FROM transfers\n" +
                "JOIN accounts ON account_id = transfers.account_to\n" +
                "JOIN users ON accounts.user_id = users.user_id\n" +
                "WHERE users.userName = ?;";
        SqlRowSet toResults = jdbcTemplate.queryForRowSet(sql, userName);
        while (toResults.next()) {
            String fromUserSql = "SELECT transfers.transfer_id, transfers.transfer_type_id, transfers.transfer_status_id, " +
                    "transfers.transfer_type_id, transfers.amount, users.username FROM transfers\n" +
                    "JOIN accounts ON account_id = transfers.account_to\n" +
                    "JOIN users ON accounts.user_id = users.user_id\n" +
                    "WHERE transfers.transfer_id= ?;";
            SqlRowSet fromResults = jdbcTemplate.queryForRowSet(sql, toResults.getLong("transfer_id"));
            userTransfers.add(mapRowsToUserTransfer(fromResults, toResults));
        }
        return userTransfers;
    }

    public String getTransferStatusDesc(long transferStatusId) {
        String sql = "SELECT transfer_status_desc FROM transfer_statuses " +
                "WHERE transfer_status_id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, transferStatusId);
    }

    public String getTransferTypeId(long transferTypeId) {
        String sql = "SELECT transfer_type_desc FROM transfer_types " +
                "WHERE transfer_type_id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, transferTypeId);
    }

    public UserTransfer mapRowsToUserTransfer(SqlRowSet rowSetFrom, SqlRowSet rowSetTo) {
        UserTransfer userTransfer = new UserTransfer();
        userTransfer.setTransferId(rowSetTo.getLong("transfer_id"));
        userTransfer.setTransferStatusId(rowSetTo.getLong("transfer_status_id"));
        userTransfer.setTransferStatusDesc(getTransferStatusDesc(userTransfer.getTransferStatusId()));
        userTransfer.setTransferTypeId(rowSetTo.getLong("transfer_type_id"));
        userTransfer.setTransferTypeDesc(userTransfer.getTransferTypeId());
        userTransfer.setAccountFromUserName(rowSetFrom.getString("username"));
        userTransfer.setAmount(rowSetTo.getBigDecimal("amount"));
        userTransfer.setAccountToUsername(rowSetTo.getString("username"));
        return userTransfer;
    }

}
