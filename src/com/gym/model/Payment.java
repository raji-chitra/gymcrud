package com.gym.model;

import java.time.LocalDate;

/**
 * Model class representing a Payment.
 */
public class Payment {
    private int paymentId;
    private int memberId;
    private String memberName; // Populated from members join for display
    private double amount;
    private LocalDate paymentDate;
    private String paymentStatus;

    // Default constructor
    public Payment() {
    }

    // Constructor for recording a new payment (without paymentId)
    public Payment(int memberId, double amount, LocalDate paymentDate, String paymentStatus) {
        this.memberId = memberId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentStatus = paymentStatus;
    }

    // Full constructor (for reading from database)
    public Payment(int paymentId, int memberId, String memberName, double amount, 
                   LocalDate paymentDate, String paymentStatus) {
        this.paymentId = paymentId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentStatus = paymentStatus;
    }

    // Getters and Setters
    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Override
    public String toString() {
        return String.format("Payment ID: %-4d | Member ID: %-4d | Member: %-18s | Amount: ₹%-8.2f | Date: %s | Status: %s",
                paymentId, memberId, (memberName != null ? memberName : "N/A"), amount, paymentDate, paymentStatus);
    }
}
