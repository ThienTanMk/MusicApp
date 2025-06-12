package com.app.musicapp.model.request;

public class ConfirmOtpRequest {
    String email;
    String otp;
    String newPassword;

    public ConfirmOtpRequest(String email, String otp,String password) {
        this.email = email;
        this.otp = otp;
        this.newPassword = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
    public String getPassword() {
        return newPassword;
    }
    public void setPassword(String password) {
        this.newPassword = password;
    }
}
