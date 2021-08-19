package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate){this.jdbcTemplate = jdbcTemplate;}

    public BigDecimal send(Long sendingAccountId, Long receivingAccountId, BigDecimal amount){
        String get_transfer_type_id = "SELECT transfer_type_id FROM transfer_types WHERE transfer_type_desc = ?";
        long transfer_type_id = jdbcTemplate.queryForObject(get_transfer_type_id, Long.class, "Send");
        String get_transfer_status_id = "SELECT transfer_status_id FROM transfer_statuses WHERE transfer_status_desc = ?";
        long transfer_status_id = jdbcTemplate.queryForObject(get_transfer_status_id, Long.class, "Approved");

        String sql = "INSERT INTO transfers (transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?,?,?,?,?,?) RETURNING amount";
        BigDecimal transferAmount = jdbcTemplate.queryForObject(sql, BigDecimal.class, 0, transfer_type_id, transfer_status_id, sendingAccountId, receivingAccountId, amount);
        AccountDao accountDao = new JdbcAccountDao(jdbcTemplate);
        accountDao.updateAccountBalances(sendingAccountId, receivingAccountId, amount);

        return transferAmount;
    }


    public List<Transfer> listTransfersByAccountId(long accountId){
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, user_id\n" +
                "FROM accounts\n" +
                "JOIN transfers ON accounts.account_id = transfers.account_from\n" +
                "WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
        while(results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        sql = "SELECT transfer_id, user_id\n" +
                "FROM accounts\n" +
                "JOIN transfers ON accounts.account_id = transfers.account_to\n" +
                "WHERE user_id = ?;";
        results = jdbcTemplate.queryForRowSet(sql, accountId);
        while(results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }


    public Transfer getTransferByTransferId(long transferId) {
        Transfer transfer = null;
        String sql = "SELECT transfer_id, transfer_type_id, "+
    "account_from, account_to, amount FROM transfers WHERE transfer_id = ? ";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,transferId);
        if(results.next()) {
            transfer = mapRowToTransfer(results);
        }

        return transfer;
    }


    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer t = new Transfer();
        t.setTransfer_id(rs.getLong("transfer_id"));
        t.setTransfer_type_id(rs.getLong("transfer_type_id"));
        t.setAccount_from(rs.getLong("account_from"));
        t.setAccount_to(rs.getLong("account_to"));
        t.setAmount(rs.getBigDecimal("amount"));

        return t;
    }
}
