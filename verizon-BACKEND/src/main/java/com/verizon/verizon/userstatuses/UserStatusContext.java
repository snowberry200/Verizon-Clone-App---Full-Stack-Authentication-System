package com.verizon.verizon.userstatuses;

public class UserStatusContext {

    UserStatus currentStatus;

    public UserStatusContext() {
        // assign current status to initial status
        this.currentStatus = new NonActiveStatus(this);
    }

    // delegate interface methods to context class
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

    public void activate() {
        currentStatus.activate();
    }

    public void deActivate() {
        currentStatus.deActivate();

    }

    // helper method to set to new status
    public void setStatus(UserStatus userStatus) {
        currentStatus = userStatus;
    }

}




