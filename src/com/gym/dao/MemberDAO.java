package com.gym.dao;

import com.gym.model.Member;
import com.gym.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for Member entities.
 * Encapsulates all SQL CRUD operations for the members table.
 */
public class MemberDAO {

    /**
     * Adds a new member to the database.
     * @param member Member object with registration details
     * @return true if inserted successfully, false otherwise
     */
    public boolean addMember(Member member) {
        String sql = "INSERT INTO members (name, age, gender, phone, email, join_date) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, member.getName());
            pstmt.setInt(2, member.getAge());
            pstmt.setString(3, member.getGender());
            pstmt.setString(4, member.getPhone());
            pstmt.setString(5, member.getEmail());
            pstmt.setDate(6, Date.valueOf(member.getJoinDate()));

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("[DAO Error] Failed to add member: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all registered members with their membership plan details.
     * @return List of Member objects
     */
    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT m.member_id, m.name, m.age, m.gender, m.phone, m.email, m.join_date, " +
                     "m.plan_id, p.plan_name, m.membership_start_date, m.membership_expiry_date " +
                     "FROM members m " +
                     "LEFT JOIN membership_plans p ON m.plan_id = p.plan_id " +
                     "ORDER BY m.member_id ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Member member = extractMemberFromResultSet(rs);
                members.add(member);
            }

        } catch (SQLException e) {
            System.err.println("[DAO Error] Failed to retrieve members: " + e.getMessage());
        }

        return members;
    }

    /**
     * Finds a member by Member ID.
     * @param memberId ID of the member
     * @return Member object if found, null otherwise
     */
    public Member getMemberById(int memberId) {
        String sql = "SELECT m.member_id, m.name, m.age, m.gender, m.phone, m.email, m.join_date, " +
                     "m.plan_id, p.plan_name, m.membership_start_date, m.membership_expiry_date " +
                     "FROM members m " +
                     "LEFT JOIN membership_plans p ON m.plan_id = p.plan_id " +
                     "WHERE m.member_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, memberId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractMemberFromResultSet(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("[DAO Error] Failed to search member by ID: " + e.getMessage());
        }

        return null;
    }

    /**
     * Updates an existing member's personal information.
     * @param member Member object containing updated values
     * @return true if updated successfully, false otherwise
     */
    public boolean updateMember(Member member) {
        String sql = "UPDATE members SET name = ?, age = ?, gender = ?, phone = ?, email = ? WHERE member_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, member.getName());
            pstmt.setInt(2, member.getAge());
            pstmt.setString(3, member.getGender());
            pstmt.setString(4, member.getPhone());
            pstmt.setString(5, member.getEmail());
            pstmt.setInt(6, member.getMemberId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("[DAO Error] Failed to update member: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a member from the database by ID.
     * @param memberId ID of the member to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteMember(int memberId) {
        String sql = "DELETE FROM members WHERE member_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, memberId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("[DAO Error] Failed to delete member: " + e.getMessage());
            return false;
        }
    }

    /**
     * Assigns a membership plan to a member and updates their active dates.
     * @param memberId Member ID
     * @param planId Membership Plan ID
     * @param startDate Plan start date
     * @param expiryDate Plan expiry date
     * @return true if assigned successfully, false otherwise
     */
    public boolean assignPlan(int memberId, int planId, LocalDate startDate, LocalDate expiryDate) {
        String sql = "UPDATE members SET plan_id = ?, membership_start_date = ?, membership_expiry_date = ? " +
                     "WHERE member_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, planId);
            pstmt.setDate(2, Date.valueOf(startDate));
            pstmt.setDate(3, Date.valueOf(expiryDate));
            pstmt.setInt(4, memberId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("[DAO Error] Failed to assign plan: " + e.getMessage());
            return false;
        }
    }

    /**
     * Checks if a member exists with the given ID.
     * @param memberId Member ID
     * @return true if exists, false otherwise
     */
    public boolean memberExists(int memberId) {
        String sql = "SELECT 1 FROM members WHERE member_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.err.println("[DAO Error] Failed to check member existence: " + e.getMessage());
            return false;
        }
    }

    /**
     * Helper method to map a ResultSet row to a Member object.
     */
    private Member extractMemberFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("member_id");
        String name = rs.getString("name");
        int age = rs.getInt("age");
        String gender = rs.getString("gender");
        String phone = rs.getString("phone");
        String email = rs.getString("email");
        LocalDate joinDate = rs.getDate("join_date").toLocalDate();

        int planIdRaw = rs.getInt("plan_id");
        Integer planId = rs.wasNull() ? null : planIdRaw;
        String planName = rs.getString("plan_name");

        Date sqlStartDate = rs.getDate("membership_start_date");
        LocalDate startDate = (sqlStartDate != null) ? sqlStartDate.toLocalDate() : null;

        Date sqlExpiryDate = rs.getDate("membership_expiry_date");
        LocalDate expiryDate = (sqlExpiryDate != null) ? sqlExpiryDate.toLocalDate() : null;

        return new Member(id, name, age, gender, phone, email, joinDate,
                planId, planName, startDate, expiryDate);
    }
}
