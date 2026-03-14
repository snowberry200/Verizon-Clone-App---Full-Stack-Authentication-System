package com.verizon.verizon.userstatuses;

public interface UserStatus {
    String getStatusCode();     // "ACTIVE", "NOT_ACTIVE" - fixed for instance
    String getStatusName();
    boolean canLogin();
    String getNextStepMessage();
}
