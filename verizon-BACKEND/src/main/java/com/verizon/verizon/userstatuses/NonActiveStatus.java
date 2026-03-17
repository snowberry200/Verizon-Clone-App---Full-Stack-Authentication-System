package com.verizon.verizon.userstatuses;

public class NonActiveStatus implements UserStatus {

    public NonActiveStatus() {
    }

    @Override
    public String getStatusCode() {
        return "NONACTIVE";
    }

    @Override
    public String getStatusName() {
        return "Non-Active";
    }

    @Override
    public boolean canLogin() {
        return false;
    }

    @Override
    public String getNextStepMessage() {
        return "Please complete your registration to activate your account";
    }


}