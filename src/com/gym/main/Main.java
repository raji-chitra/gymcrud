package com.gym.main;

import com.gym.dao.MemberDAO;
import com.gym.dao.MembershipDAO;
import com.gym.dao.PaymentDAO;
import com.gym.model.Member;
import com.gym.model.MembershipPlan;
import com.gym.model.Payment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * Main application class providing a console-based interface
 * for the Gym Membership Management System.
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final MemberDAO memberDAO = new MemberDAO();
    private static final MembershipDAO membershipDAO = new MembershipDAO();
    private static final PaymentDAO paymentDAO = new PaymentDAO();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        boolean running = true;

        System.out.println("=================================================");
        System.out.println("  Welcome to Gym Membership Management System    ");
        System.out.println("  Built with Core Java, JDBC & MySQL             ");
        System.out.println("=================================================");

        while (running) {
            displayMenu();
            int choice = readIntInput("Choose an option: ");

            switch (choice) {
                case 1 -> addMember();
                case 2 -> viewAllMembers();
                case 3 -> searchMember();
                case 4 -> updateMember();
                case 5 -> deleteMember();
                case 6 -> addMembershipPlan();
                case 7 -> viewMembershipPlans();
                case 8 -> assignMembershipPlan();
                case 9 -> recordPayment();
                case 10 -> viewPaymentHistory();
                case 11 -> {
                    System.out.println("\nThank you for using Gym Membership Management System. Goodbye!");
                    running = false;
                }
                default -> System.out.println("\n[!] Invalid option! Please choose a number between 1 and 11.");
            }

            if (running) {
                System.out.println("\nPress ENTER to continue...");
                scanner.nextLine();
            }
        }

        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\n===== GYM MEMBERSHIP MANAGEMENT SYSTEM =====");
        System.out.println(" 1. Add Member");
        System.out.println(" 2. View All Members");
        System.out.println(" 3. Search Member");
        System.out.println(" 4. Update Member");
        System.out.println(" 5. Delete Member");
        System.out.println(" 6. Add Membership Plan");
        System.out.println(" 7. View Membership Plans");
        System.out.println(" 8. Assign Membership Plan");
        System.out.println(" 9. Record Payment");
        System.out.println("10. View Payment History");
        System.out.println("11. Exit");
        System.out.println("============================================");
    }

    // ==========================================
    // 1. ADD MEMBER
    // ==========================================
    private static void addMember() {
        System.out.println("\n--- [1] Add New Member ---");
        String name = readNonEmptyString("Enter Full Name: ");
        int age = readPositiveInt("Enter Age: ");
        String gender = readGender("Enter Gender (Male/Female/Other): ");
        String phone = readNonEmptyString("Enter Phone Number (10 digits): ");
        String email = readNonEmptyString("Enter Email: ");
        LocalDate joinDate = LocalDate.now();

        Member member = new Member(name, age, gender, phone, email, joinDate);
        boolean success = memberDAO.addMember(member);

        if (success) {
            System.out.println(">> Success: Member registered successfully on " + joinDate + "!");
        } else {
            System.out.println(">> Failed: Could not register member. Check if phone/email already exists.");
        }
    }

    // ==========================================
    // 2. VIEW ALL MEMBERS
    // ==========================================
    private static void viewAllMembers() {
        System.out.println("\n--- [2] View All Members ---");
        List<Member> members = memberDAO.getAllMembers();

        if (members.isEmpty()) {
            System.out.println("No members found in the database.");
            return;
        }

        printMemberTableHeader();
        for (Member m : members) {
            String plan = (m.getPlanName() != null) ? m.getPlanName() : "None";
            String expiry = (m.getMembershipExpiryDate() != null) 
                    ? m.getMembershipExpiryDate().format(DATE_FORMATTER) 
                    : "N/A";
            System.out.printf("| %-4d | %-18s | %-3d | %-7s | %-12s | %-22s | %-16s | %-10s |\n",
                    m.getMemberId(),
                    truncate(m.getName(), 18),
                    m.getAge(),
                    m.getGender(),
                    m.getPhone(),
                    truncate(m.getEmail(), 22),
                    truncate(plan, 16),
                    expiry);
        }
        printMemberTableFooter();
        System.out.println("Total Members: " + members.size());
    }

    // ==========================================
    // 3. SEARCH MEMBER
    // ==========================================
    private static void searchMember() {
        System.out.println("\n--- [3] Search Member ---");
        int memberId = readIntInput("Enter Member ID to search: ");

        Member m = memberDAO.getMemberById(memberId);
        if (m == null) {
            System.out.println(">> No member found with ID: " + memberId);
            return;
        }

        System.out.println("\n---------------- MEMBER PROFILE ----------------");
        System.out.println(" Member ID        : " + m.getMemberId());
        System.out.println(" Full Name        : " + m.getName());
        System.out.println(" Age              : " + m.getAge());
        System.out.println(" Gender           : " + m.getGender());
        System.out.println(" Phone Number     : " + m.getPhone());
        System.out.println(" Email Address    : " + m.getEmail());
        System.out.println(" Date of Joining  : " + m.getJoinDate());
        System.out.println(" Active Plan      : " + (m.getPlanName() != null ? m.getPlanName() : "No active plan"));
        System.out.println(" Plan Start Date  : " + (m.getMembershipStartDate() != null ? m.getMembershipStartDate() : "N/A"));
        System.out.println(" Plan Expiry Date : " + (m.getMembershipExpiryDate() != null ? m.getMembershipExpiryDate() : "N/A"));
        System.out.println(" Status           : " + (m.hasActivePlan() ? "[ACTIVE]" : "[EXPIRED / INACTIVE]"));
        System.out.println("------------------------------------------------");
    }

    // ==========================================
    // 4. UPDATE MEMBER
    // ==========================================
    private static void updateMember() {
        System.out.println("\n--- [4] Update Member Details ---");
        int memberId = readIntInput("Enter Member ID to update: ");

        Member existing = memberDAO.getMemberById(memberId);
        if (existing == null) {
            System.out.println(">> Member not found with ID: " + memberId);
            return;
        }

        System.out.println("\nCurrent Details:");
        System.out.println("Name: " + existing.getName() + ", Age: " + existing.getAge() + 
                           ", Gender: " + existing.getGender() + ", Phone: " + existing.getPhone() + 
                           ", Email: " + existing.getEmail());
        System.out.println("(Press ENTER to keep existing value)");

        System.out.print("New Name [" + existing.getName() + "]: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) name = existing.getName();

        System.out.print("New Age [" + existing.getAge() + "]: ");
        String ageStr = scanner.nextLine().trim();
        int age = ageStr.isEmpty() ? existing.getAge() : Integer.parseInt(ageStr);

        System.out.print("New Gender [" + existing.getGender() + "]: ");
        String gender = scanner.nextLine().trim();
        if (gender.isEmpty()) gender = existing.getGender();

        System.out.print("New Phone [" + existing.getPhone() + "]: ");
        String phone = scanner.nextLine().trim();
        if (phone.isEmpty()) phone = existing.getPhone();

        System.out.print("New Email [" + existing.getEmail() + "]: ");
        String email = scanner.nextLine().trim();
        if (email.isEmpty()) email = existing.getEmail();

        Member updatedMember = new Member(memberId, name, age, gender, phone, email);
        boolean success = memberDAO.updateMember(updatedMember);

        if (success) {
            System.out.println(">> Success: Member ID " + memberId + " details updated successfully!");
        } else {
            System.out.println(">> Failed: Could not update member details.");
        }
    }

    // ==========================================
    // 5. DELETE MEMBER
    // ==========================================
    private static void deleteMember() {
        System.out.println("\n--- [5] Delete Member ---");
        int memberId = readIntInput("Enter Member ID to delete: ");

        Member existing = memberDAO.getMemberById(memberId);
        if (existing == null) {
            System.out.println(">> Member not found with ID: " + memberId);
            return;
        }

        System.out.print("Are you sure you want to delete '" + existing.getName() + "' (ID: " + memberId + ")? (Y/N): ");
        String confirm = scanner.nextLine().trim();

        if (confirm.equalsIgnoreCase("Y")) {
            boolean success = memberDAO.deleteMember(memberId);
            if (success) {
                System.out.println(">> Success: Member deleted successfully!");
            } else {
                System.out.println(">> Failed: Could not delete member.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    // ==========================================
    // 6. ADD MEMBERSHIP PLAN
    // ==========================================
    private static void addMembershipPlan() {
        System.out.println("\n--- [6] Add Membership Plan ---");
        String planName = readNonEmptyString("Enter Plan Name (e.g. Monthly, Quarterly, Yearly): ");
        int durationMonths = readPositiveInt("Enter Duration in Months (e.g. 1, 3, 6, 12): ");
        double price = readPositiveDouble("Enter Plan Price (₹): ");

        MembershipPlan plan = new MembershipPlan(planName, durationMonths, price);
        boolean success = membershipDAO.addPlan(plan);

        if (success) {
            System.out.println(">> Success: Membership plan '" + planName + "' added successfully!");
        } else {
            System.out.println(">> Failed: Could not add membership plan.");
        }
    }

    // ==========================================
    // 7. VIEW MEMBERSHIP PLANS
    // ==========================================
    private static void viewMembershipPlans() {
        System.out.println("\n--- [7] Available Membership Plans ---");
        List<MembershipPlan> plans = membershipDAO.getAllPlans();

        if (plans.isEmpty()) {
            System.out.println("No membership plans configured yet.");
            return;
        }

        System.out.println("+---------+----------------------+-------------------+--------------+");
        System.out.println("| Plan ID | Plan Name            | Duration (Months) | Price (₹)    |");
        System.out.println("+---------+----------------------+-------------------+--------------+");
        for (MembershipPlan p : plans) {
            System.out.printf("| %-7d | %-20s | %-17d | ₹%-11.2f |\n",
                    p.getPlanId(),
                    truncate(p.getPlanName(), 20),
                    p.getDurationMonths(),
                    p.getPrice());
        }
        System.out.println("+---------+----------------------+-------------------+--------------+");
    }

    // ==========================================
    // 8. ASSIGN MEMBERSHIP PLAN
    // ==========================================
    private static void assignMembershipPlan() {
        System.out.println("\n--- [8] Assign Membership Plan ---");
        int memberId = readIntInput("Enter Member ID: ");

        Member member = memberDAO.getMemberById(memberId);
        if (member == null) {
            System.out.println(">> Member not found with ID: " + memberId);
            return;
        }

        System.out.println("Selected Member: " + member.getName());
        viewMembershipPlans();

        int planId = readIntInput("Enter Plan ID to assign: ");
        MembershipPlan plan = membershipDAO.getPlanById(planId);
        if (plan == null) {
            System.out.println(">> Plan not found with ID: " + planId);
            return;
        }

        LocalDate startDate = LocalDate.now();
        LocalDate expiryDate = startDate.plusMonths(plan.getDurationMonths());

        boolean success = memberDAO.assignPlan(memberId, planId, startDate, expiryDate);

        if (success) {
            System.out.println(">> Success: Assigned '" + plan.getPlanName() + "' to " + member.getName() + "!");
            System.out.println("   Start Date  : " + startDate);
            System.out.println("   Expiry Date : " + expiryDate);

            System.out.print("\nWould you like to record payment of ₹" + plan.getPrice() + " now? (Y/N): ");
            String recordPay = scanner.nextLine().trim();
            if (recordPay.equalsIgnoreCase("Y")) {
                Payment payment = new Payment(memberId, plan.getPrice(), startDate, "Paid");
                boolean paySuccess = paymentDAO.recordPayment(payment);
                if (paySuccess) {
                    System.out.println(">> Payment of ₹" + plan.getPrice() + " recorded successfully!");
                } else {
                    System.out.println(">> Warning: Could not auto-record payment. Please use option 9.");
                }
            }
        } else {
            System.out.println(">> Failed: Could not assign membership plan.");
        }
    }

    // ==========================================
    // 9. RECORD PAYMENT
    // ==========================================
    private static void recordPayment() {
        System.out.println("\n--- [9] Record Payment ---");
        int memberId = readIntInput("Enter Member ID: ");

        Member member = memberDAO.getMemberById(memberId);
        if (member == null) {
            System.out.println(">> Member not found with ID: " + memberId);
            return;
        }

        System.out.println("Member: " + member.getName());
        double amount = readPositiveDouble("Enter Payment Amount (₹): ");
        System.out.print("Enter Payment Status [Paid / Pending / Failed] (Default: Paid): ");
        String status = scanner.nextLine().trim();
        if (status.isEmpty()) {
            status = "Paid";
        }

        LocalDate paymentDate = LocalDate.now();
        Payment payment = new Payment(memberId, amount, paymentDate, status);

        boolean success = paymentDAO.recordPayment(payment);
        if (success) {
            System.out.println(">> Success: Payment of ₹" + amount + " recorded on " + paymentDate + " [Status: " + status + "]!");
        } else {
            System.out.println(">> Failed: Could not record payment.");
        }
    }

    // ==========================================
    // 10. VIEW PAYMENT HISTORY
    // ==========================================
    private static void viewPaymentHistory() {
        System.out.println("\n--- [10] View Payment History ---");
        System.out.println(" 1. View All Payments");
        System.out.println(" 2. View Payments by Member ID");
        int choice = readIntInput("Choose option: ");

        List<Payment> payments;
        if (choice == 1) {
            payments = paymentDAO.getAllPayments();
        } else if (choice == 2) {
            int memberId = readIntInput("Enter Member ID: ");
            payments = paymentDAO.getPaymentsByMemberId(memberId);
        } else {
            System.out.println("Invalid option.");
            return;
        }

        if (payments.isEmpty()) {
            System.out.println("No payment records found.");
            return;
        }

        System.out.println("+------------+-----------+----------------------+--------------+------------+------------+");
        System.out.println("| Payment ID | Member ID | Member Name          | Amount (₹)   | Date       | Status     |");
        System.out.println("+------------+-----------+----------------------+--------------+------------+------------+");
        for (Payment p : payments) {
            System.out.printf("| %-10d | %-9d | %-20s | ₹%-11.2f | %-10s | %-10s |\n",
                    p.getPaymentId(),
                    p.getMemberId(),
                    truncate(p.getMemberName() != null ? p.getMemberName() : "N/A", 20),
                    p.getAmount(),
                    p.getPaymentDate(),
                    p.getPaymentStatus());
        }
        System.out.println("+------------+-----------+----------------------+--------------+------------+------------+");
        System.out.println("Total Records: " + payments.size());
    }

    // ==========================================
    // HELPER METHODS FOR INPUT & FORMATTING
    // ==========================================
    private static int readIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("[!] Invalid number. Please enter an integer.");
            }
        }
    }

    private static int readPositiveInt(String prompt) {
        while (true) {
            int val = readIntInput(prompt);
            if (val > 0) return val;
            System.out.println("[!] Value must be greater than 0.");
        }
    }

    private static double readPositiveDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                double val = Double.parseDouble(input);
                if (val >= 0) return val;
                System.out.println("[!] Amount cannot be negative.");
            } catch (NumberFormatException e) {
                System.out.println("[!] Invalid decimal amount. Please enter a valid number.");
            }
        }
    }

    private static String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("[!] This field cannot be empty. Please enter a value.");
        }
    }

    private static String readGender(String prompt) {
        while (true) {
            String input = readNonEmptyString(prompt);
            if (input.equalsIgnoreCase("Male") || input.equalsIgnoreCase("Female") || input.equalsIgnoreCase("Other")) {
                return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
            }
            System.out.println("[!] Please enter Male, Female, or Other.");
        }
    }

    private static String truncate(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 3) + "...";
    }

    private static void printMemberTableHeader() {
        System.out.println("+------+--------------------+-----+---------+--------------+------------------------+------------------+------------+");
        System.out.println("| ID   | Name               | Age | Gender  | Phone        | Email                  | Active Plan      | Expiry     |");
        System.out.println("+------+--------------------+-----+---------+--------------+------------------------+------------------+------------+");
    }

    private static void printMemberTableFooter() {
        System.out.println("+------+--------------------+-----+---------+--------------+------------------------+------------------+------------+");
    }
}
