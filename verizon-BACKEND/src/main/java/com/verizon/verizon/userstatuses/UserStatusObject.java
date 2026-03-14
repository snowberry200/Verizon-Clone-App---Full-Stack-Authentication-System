package com.verizon.verizon.userstatuses;

public class UserStatusObject implements UserStatus{

    // current status
    UserStatus currentStatus;
    // POSSIBLE STATUSES
    UserStatus active;


    UserStatus nonActive;

        // a class representation of all Possible statuses
    public UserStatusObject(UserStatus active,UserStatus nonActive){
        this.active = active;
        this.nonActive = nonActive;
        this.currentStatus = active; // start off
    }

    @Override
    public String getStatusCode() {
        return currentStatus.getStatusCode();
    }

    @Override
    public String getStatusName() {
        return currentStatus.getStatusName();
    }

    @Override
    public boolean canLogin() {
        return currentStatus.canLogin();
    }

    @Override
    public String getNextStepMessage() {
        return currentStatus.getNextStepMessage();
    }

    // Helper methods for statuses to use
    public void setStatus(UserStatus status){
        this.currentStatus = status;
    }

    public UserStatus getActive() {
        return active;
    }

    public UserStatus getNonActive() {
        return nonActive;
    }
}


