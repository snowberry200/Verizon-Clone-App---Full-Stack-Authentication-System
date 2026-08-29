package com.verizon.verizon.userstatuses;

public class ActiveStatus implements UserStatus {
    //dependency injection (constructor injection)
    UserStatusContext userStatusContext;

    public ActiveStatus(UserStatusContext userStatusContext) {
        this.userStatusContext = userStatusContext;
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
        return "Welcome back!";
    }

    @Override
    public void activate() {
        System.out.println("user is already active");
    }

    @Override
    public void deActivate() {
        userStatusContext.setStatus(new NonActiveStatus(this.userStatusContext));

    }

}

