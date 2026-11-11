package com.gym.dao;

import com.gym.model.Payment;
import com.gym.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for Payment entities.
 * Handles database operations for payments table.
 */
public class PaymentDAO {

    /**
     * Records a new payment in the database.
     * @param payment Payment object
     * @return true if payment recorded successfully, false otherwise
     */
    public boolean recordPayment(Payment payment) {
        String sql = "INSERT INTO payments (member_id, amount, payment_date, payment_status) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, payment.getMemberId());
            pstmt.setDouble(2, payment.getAmount());
            pstmt.setDate(3, Date.valueOf(payment.getPaymentDate()));
            pstmt.setString(4, payment.getPaymentStatus());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("[DAO Error] Failed to record payment: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all payments recorded in the system.
     * @return List of Payment objects
     */
    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT p.payment_id, p.member_id, m.name AS member_name, p.amount, p.payment_date, p.payment_status " +
                     "FROM payments p " +
                     "LEFT JOIN members m ON p.member_id = m.member_id " +
                     "ORDER BY p.payment_id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Payment payment = new Payment(
                        rs.getInt("payment_id"),
                        rs.getInt("member_id"),
                        rs.getString("member_name"),
                        rs.getDouble("amount"),
                        rs.getDate("payment_date").toLocalDate(),
                        rs.getString("payment_status")
                );
                payments.add(payment);
            }

        } catch (SQLException e) {
            System.err.println("[DAO Error] Failed to retrieve payments: " + e.getMessage());
        }

        return payments;
    }

    /**
     * Retrieves payment history for a specific member ID.
     * @param memberId ID of the member
     * @return List of Payment objects
     */
    public List<Payment> getPaymentsByMemberId(int memberId) {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT p.payment_id, p.member_id, m.name AS member_name, p.amount, p.payment_date, p.payment_status " +
                     "FROM payments p " +
                     "LEFT JOIN members m ON p.member_id = m.member_id " +
                     "WHERE p.member_id = ? " +
                     "ORDER BY p.payment_id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, memberId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Payment payment = new Payment(
                            rs.getInt("payment_id"),
                            rs.getInt("member_id"),
                            rs.getString("member_name"),
                            rs.getDouble("amount"),
                            rs.getDate("payment_date").toLocalDate(),
                            rs.getString("payment_status")
                    );
                    payments.add(payment);
                }
            }

        } catch (SQLException e) {
            System.err.println("[DAO Error] Failed to retrieve member payment history: " + e.getMessage());
        }

        return payments;
    }
}
