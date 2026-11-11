package com.gym.model;

/**
 * Model class representing a Membership Plan.
 */
public class MembershipPlan {
    private int planId;
    private String planName;
    private int durationMonths;
    private double price;

    // Default constructor
    public MembershipPlan() {
    }

    // Constructor without planId (for creating new plans before insertion)
    public MembershipPlan(String planName, int durationMonths, double price) {
        this.planName = planName;
        this.durationMonths = durationMonths;
        this.price = price;
    }

    // Full constructor (for reading from database)
    public MembershipPlan(int planId, String planName, int durationMonths, double price) {
        this.planId = planId;
        this.planName = planName;
        this.durationMonths = durationMonths;
        this.price = price;
    }

    // Getters and Setters
    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public int getDurationMonths() {
        return durationMonths;
    }

    public void setDurationMonths(int durationMonths) {
        this.durationMonths = durationMonths;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return String.format("ID: %-3d | Plan: %-18s | Duration: %2d month(s) | Price: ₹%.2f",
                planId, planName, durationMonths, price);
    }
}
