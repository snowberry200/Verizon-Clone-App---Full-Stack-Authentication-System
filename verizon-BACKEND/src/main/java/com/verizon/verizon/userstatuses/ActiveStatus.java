package com.verizon.verizon.userstatuses;

public class ActiveStatus implements UserStatus{
    UserStatusObject userStatusObject;

    public ActiveStatus(UserStatusObject userStatusObject){
        this.userStatusObject = userStatusObject;
    }
    @Override
    public String getStatusCode() {
        return "ACTIVE";
    }

    @Override
    public String getStatusName() {
        return "Active";
    }

    @Override
    public boolean canLogin() {
        return true;
    }

    @Override
    public String getNextStepMessage() {
        return "Welcome";
    }
}
