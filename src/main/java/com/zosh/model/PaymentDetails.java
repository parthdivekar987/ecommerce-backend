package com.zosh.model;

import jakarta.persistence.Embeddable;

// CORRECTED: Added @Embeddable annotation. This is required when using @Embedded in another entity.
@Embeddable
public class PaymentDetails {

    private String paymentMethod;
    private String status;
    private String paymentId; // Renamed for clarity from the generic "payment"

    // CORRECTED: Spelling "razorPaymentLinkId" to "razorpayPaymentLinkId" for consistency
    private String razorpayPaymentLinkId;

    // CORRECTED: Field name was incorrectly named like a getter method.
    private String razorpayPaymentLinkReferenceId;

    private String razorpayPaymentLinkStatus;
    private String razorpayPaymentId;

    public PaymentDetails() {

    }

    // Constructor updated with corrected field names
    public PaymentDetails(String paymentMethod, String status, String paymentId, String razorpayPaymentLinkId,
                          String razorpayPaymentLinkReferenceId, String razorpayPaymentLinkStatus, String razorpayPaymentId) {
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.paymentId = paymentId;
        this.razorpayPaymentLinkId = razorpayPaymentLinkId;
        this.razorpayPaymentLinkReferenceId = razorpayPaymentLinkReferenceId;
        this.razorpayPaymentLinkStatus = razorpayPaymentLinkStatus;
        this.razorpayPaymentId = razorpayPaymentId;
    }

    // Getters and Setters

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getRazorpayPaymentLinkId() {
        return razorpayPaymentLinkId;
    }

    public void setRazorpayPaymentLinkId(String razorpayPaymentLinkId) {
        this.razorpayPaymentLinkId = razorpayPaymentLinkId;
    }

    // CORRECTED: Getter name fixed to match the corrected field name.
    public String getRazorpayPaymentLinkReferenceId() {
        return razorpayPaymentLinkReferenceId;
    }

    // CORRECTED: Setter name fixed to match the corrected field name.
    public void setRazorpayPaymentLinkReferenceId(String razorpayPaymentLinkReferenceId) {
        this.razorpayPaymentLinkReferenceId = razorpayPaymentLinkReferenceId;
    }

    public String getRazorpayPaymentLinkStatus() {
        return razorpayPaymentLinkStatus;
    }

    public void setRazorpayPaymentLinkStatus(String razorpayPaymentLinkStatus) {
        this.razorpayPaymentLinkStatus = razorpayPaymentLinkStatus;
    }

    public String getRazorpayPaymentId() {
        return razorpayPaymentId;
    }

    public void setRazorpayPaymentId(String razorpayPaymentId) {
        this.razorpayPaymentId = razorpayPaymentId;
    }
}
