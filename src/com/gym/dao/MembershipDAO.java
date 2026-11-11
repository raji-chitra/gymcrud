package com.gym.dao;

import com.gym.model.MembershipPlan;
import com.gym.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for Membership Plan entities.
 * Handles database CRUD operations for membership_plans table.
 */
public class MembershipDAO {

    /**
     * Inserts a new membership plan into the database.
     * @param plan MembershipPlan object
     * @return true if added successfully, false otherwise
     */
    public boolean addPlan(MembershipPlan plan) {
        String sql = "INSERT INTO membership_plans (plan_name, duration_months, price) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, plan.getPlanName());
            pstmt.setInt(2, plan.getDurationMonths());
            pstmt.setDouble(3, plan.getPrice());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("[DAO Error] Failed to add membership plan: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all membership plans from the database.
     * @return List of MembershipPlan objects
     */
    public List<MembershipPlan> getAllPlans() {
        List<MembershipPlan> plans = new ArrayList<>();
        String sql = "SELECT plan_id, plan_name, duration_months, price FROM membership_plans ORDER BY plan_id ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                MembershipPlan plan = new MembershipPlan(
                        rs.getInt("plan_id"),
                        rs.getString("plan_name"),
                        rs.getInt("duration_months"),
                        rs.getDouble("price")
                );
                plans.add(plan);
            }

        } catch (SQLException e) {
            System.err.println("[DAO Error] Failed to retrieve plans: " + e.getMessage());
        }

        return plans;
    }

    /**
     * Finds a membership plan by its ID.
     * @param planId Plan ID
     * @return MembershipPlan if found, null otherwise
     */
    public MembershipPlan getPlanById(int planId) {
        String sql = "SELECT plan_id, plan_name, duration_months, price FROM membership_plans WHERE plan_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, planId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new MembershipPlan(
                            rs.getInt("plan_id"),
                            rs.getString("plan_name"),
                            rs.getInt("duration_months"),
                            rs.getDouble("price")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("[DAO Error] Failed to search plan by ID: " + e.getMessage());
        }

        return null;
    }

    /**
     * Checks if a membership plan exists with the given ID.
     * @param planId Plan ID
     * @return true if exists, false otherwise
     */
    public boolean planExists(int planId) {
        return getPlanById(planId) != null;
    }
}
