package com.gym.model;

import java.time.LocalDate;

/**
 * Model class representing a Gym Member.
 * Demonstrates Encapsulation with private fields and public getters/setters.
 */
public class Member {
    private int memberId;
    private String name;
    private int age;
    private String gender;
    private String phone;
    private String email;
    private LocalDate joinDate;
    
    // Membership plan details assigned to member
    private Integer planId;
    private String planName; // Populated from membership_plans join for display
    private LocalDate membershipStartDate;
    private LocalDate membershipExpiryDate;

    // Default constructor
    public Member() {
    }

    // Constructor for registering a new member (before ID and Plan assignment)
    public Member(String name, int age, String gender, String phone, String email, LocalDate joinDate) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.joinDate = joinDate;
    }

    // Constructor for updating member profile
    public Member(int memberId, String name, int age, String gender, String phone, String email) {
        this.memberId = memberId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
    }

    // Full constructor (for reading from database)
    public Member(int memberId, String name, int age, String gender, String phone, String email,
                  LocalDate joinDate, Integer planId, String planName,
                  LocalDate membershipStartDate, LocalDate membershipExpiryDate) {
        this.memberId = memberId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.joinDate = joinDate;
        this.planId = planId;
        this.planName = planName;
        this.membershipStartDate = membershipStartDate;
        this.membershipExpiryDate = membershipExpiryDate;
    }

    // Getters and Setters
    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public LocalDate getMembershipStartDate() {
        return membershipStartDate;
    }

    public void setMembershipStartDate(LocalDate membershipStartDate) {
        this.membershipStartDate = membershipStartDate;
    }

    public LocalDate getMembershipExpiryDate() {
        return membershipExpiryDate;
    }

    public void setMembershipExpiryDate(LocalDate membershipExpiryDate) {
        this.membershipExpiryDate = membershipExpiryDate;
    }

    public boolean hasActivePlan() {
        return planId != null && membershipExpiryDate != null && !membershipExpiryDate.isBefore(LocalDate.now());
    }

    @Override
    public String toString() {
        return "Member [ID=" + memberId + ", Name=" + name + ", Age=" + age + 
               ", Gender=" + gender + ", Phone=" + phone + ", Email=" + email + 
               ", JoinDate=" + joinDate + ", Plan=" + (planName != null ? planName : "None") + 
               ", Expiry=" + (membershipExpiryDate != null ? membershipExpiryDate : "N/A") + "]";
    }
}
