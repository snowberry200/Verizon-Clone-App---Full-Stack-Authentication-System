package com.verizon.verizon.userstatuses;

public class NonActiveStatus implements UserStatus{
    @Override
    public String getStatusCode() {
        return "NONACTIVE";
    }

    @Override
    public String getStatusName() {
        return "NonActive";
    }

    @Override
    public boolean canLogin() {
        return false;
    }

    @Override
    public String getNextStepMessage() {
        return "sorry you are not a member yet";
    }
}
