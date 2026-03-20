package com.verizon.verizon.userstatuses;

public class StatusFactory {
    private final UserStatusContext userStatusContext;
    public  StatusFactory(UserStatusContext userStatusContext){
        this.userStatusContext = userStatusContext;
    }

    // The KEY method that connects database to objects
    public  UserStatus getStatusByCode(String statusCode) {
        if (statusCode == null) {
            return new NonActiveStatus(userStatusContext);  // Default
        }
        return switch (statusCode.toUpperCase()) {
            case "ACTIVE" -> new ActiveStatus(userStatusContext);
            case "NONACTIVE", "INACTIVE" -> new NonActiveStatus(userStatusContext);
            default -> throw new IllegalArgumentException("Unknown status code: " + statusCode);
        };
    }

}
