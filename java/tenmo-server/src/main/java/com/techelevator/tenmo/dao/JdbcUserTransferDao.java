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
    public UserTransfer getUserTransferInfo(long transferId) {
        UserTransfer userTransfer = null;
        String sql = "SELECT transfers.transfer_id, transfers.account_from, transfers.account_to, transfers.amount, users.username FROM transfers\n" +
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

    public List<UserTransfer> getUserTransferList(long accountId) {
        List<UserTransfer> userTransfers = new ArrayList<>();
        List<Transfer> transfers = transferDao.listTransfersByAccountId(accountId);
        for (Transfer transfer : transfers) {
            userTransfers.add(getUserTransferInfo(transfer.getTransfer_id()));
        }
        return userTransfers;
    }

    public UserTransfer mapRowsToUserTransfer(SqlRowSet rowSetFrom, SqlRowSet rowSetTo) {
        UserTransfer userTransfer = new UserTransfer();
        userTransfer.setTransferId(rowSetFrom.getLong("transfer_id"));
        userTransfer.setAccountFromId(rowSetFrom.getLong("account_from"));
        userTransfer.setAccountToId(rowSetFrom.getLong("account_to"));
        userTransfer.setAccountFromUserName(rowSetFrom.getString("username"));
        userTransfer.setAmount(rowSetFrom.getBigDecimal("amount"));
        userTransfer.setAccountToUsername(rowSetTo.getString("username"));
        return userTransfer;
    }

}
