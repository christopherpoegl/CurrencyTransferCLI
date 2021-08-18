package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

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
}
