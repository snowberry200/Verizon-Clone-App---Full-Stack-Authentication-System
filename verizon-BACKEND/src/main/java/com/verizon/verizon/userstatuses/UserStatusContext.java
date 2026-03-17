package com.verizon.verizon.userstatuses;

public class UserStatusContext {

    UserStatus currentStatus;

    public UserStatusContext(UserStatus currentStatus) {
        this.currentStatus = currentStatus;
    }


    public String getStatusCode() {
        return currentStatus.getStatusCode();
    }

    public String getStatusName() {
        return currentStatus.getStatusName();
    }

    public boolean canLogin() {
        return currentStatus.canLogin();
    }

    public String getNextStepMessage() {
        return currentStatus.getNextStepMessage();
    }

    public UserStatusContext setStatus(UserStatus userStatus) {
        currentStatus = userStatus;
        return this;
    }

    public UserStatus getCurrentStatus() {
        return currentStatus;
    }

}




