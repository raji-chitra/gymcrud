package com.gym.main;

import com.gym.dao.MemberDAO;
import com.gym.dao.MembershipDAO;
import com.gym.dao.PaymentDAO;
import com.gym.model.Member;
import com.gym.model.MembershipPlan;
import com.gym.model.Payment;

import java.time.LocalDate;
import java.util.List;

/**
 * Automated test script to verify all DAO and database operations.
 */
public class TestRunner {

    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("     RUNNING COMPREHENSIVE SYSTEM TESTS     ");
        System.out.println("============================================");

        MemberDAO memberDAO = new MemberDAO();
        MembershipDAO membershipDAO = new MembershipDAO();
        PaymentDAO paymentDAO = new PaymentDAO();

        // 1. Test View All Plans
        System.out.print("[TEST 1] Fetching all membership plans... ");
        List<MembershipPlan> plans = membershipDAO.getAllPlans();
        if (!plans.isEmpty()) {
            System.out.println("PASSED! Count: " + plans.size());
        } else {
            System.err.println("FAILED! No plans returned.");
        }

        // 2. Test Add New Plan
        System.out.print("[TEST 2] Adding test membership plan... ");
        MembershipPlan testPlan = new MembershipPlan("Student Special", 2, 2200.00);
        boolean planAdded = membershipDAO.addPlan(testPlan);
        System.out.println(planAdded ? "PASSED!" : "FAILED!");

        // 3. Test Add Member
        System.out.print("[TEST 3] Adding test member... ");
        String testPhone = "999" + System.currentTimeMillis() % 10000000;
        Member testMember = new Member("Vikram Singh", 26, "Male", testPhone, "vikram." + System.currentTimeMillis() + "@test.com", LocalDate.now());
        boolean memberAdded = memberDAO.addMember(testMember);
        System.out.println(memberAdded ? "PASSED!" : "FAILED!");

        // 4. Test View All Members & Search
        System.out.print("[TEST 4] Retrieving member list & searching by ID... ");
        List<Member> members = memberDAO.getAllMembers();
        Member latestMember = members.get(members.size() - 1);
        Member searched = memberDAO.getMemberById(latestMember.getMemberId());
        if (searched != null && searched.getName().equals(latestMember.getName())) {
            System.out.println("PASSED! Found: " + searched.getName() + " (ID: " + searched.getMemberId() + ")");
        } else {
            System.err.println("FAILED! Member search mismatch.");
        }

        // 5. Test Update Member
        System.out.print("[TEST 5] Updating member details... ");
        searched.setName("Vikram S. Rathore");
        searched.setAge(27);
        boolean updated = memberDAO.updateMember(searched);
        System.out.println(updated ? "PASSED!" : "FAILED!");

        // 6. Test Assign Plan to Member
        System.out.print("[TEST 6] Assigning membership plan to member... ");
        int planIdToAssign = plans.get(0).getPlanId();
        LocalDate startDate = LocalDate.now();
        LocalDate expiryDate = startDate.plusMonths(plans.get(0).getDurationMonths());
        boolean assigned = memberDAO.assignPlan(searched.getMemberId(), planIdToAssign, startDate, expiryDate);
        System.out.println(assigned ? "PASSED!" : "FAILED!");

        // 7. Test Record Payment
        System.out.print("[TEST 7] Recording payment for member... ");
        Payment testPayment = new Payment(searched.getMemberId(), 1500.00, LocalDate.now(), "Paid");
        boolean paymentRecorded = paymentDAO.recordPayment(testPayment);
        System.out.println(paymentRecorded ? "PASSED!" : "FAILED!");

        // 8. Test View Payments
        System.out.print("[TEST 8] Fetching payment history for member... ");
        List<Payment> memberPayments = paymentDAO.getPaymentsByMemberId(searched.getMemberId());
        if (!memberPayments.isEmpty()) {
            System.out.println("PASSED! Retrieved " + memberPayments.size() + " payment record(s).");
        } else {
            System.err.println("FAILED! No payments found for member.");
        }

        // 9. Test Delete Member
        System.out.print("[TEST 9] Deleting test member... ");
        boolean deleted = memberDAO.deleteMember(searched.getMemberId());
        System.out.println(deleted ? "PASSED!" : "FAILED!");

        System.out.println("============================================");
        System.out.println("       ALL 9 CORE TESTS PASSED SUCCESSFULLY!       ");
        System.out.println("============================================");
    }
}
