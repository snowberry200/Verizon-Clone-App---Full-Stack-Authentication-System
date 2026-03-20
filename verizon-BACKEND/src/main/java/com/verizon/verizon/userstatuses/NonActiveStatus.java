package com.verizon.verizon.userstatuses;

public class NonActiveStatus implements UserStatus {
    UserStatusContext userStatusContext;

    public NonActiveStatus(UserStatusContext userStatusContext) {
        this.userStatusContext = userStatusContext;
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

    public void activate() {
        this.userStatusContext.setStatus(new ActiveStatus(this.userStatusContext));

    }

    public void deActivate() {
        System.out.println("you do not have authorized access");

    }
}

