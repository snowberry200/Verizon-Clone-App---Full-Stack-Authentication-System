package com.verizon.verizon.userstatuses;

public class StatusFactory {
    private  StatusFactory(){}

    // The KEY method that connects database to objects
    public static UserStatus getStatusByCode(String statusCode) {
        if (statusCode == null) {
            return new NonActiveStatus();  // Default
        }
        return switch (statusCode.toUpperCase()) {
            case "ACTIVE" -> new ActiveStatus();
            case "NONACTIVE", "INACTIVE" -> new NonActiveStatus();
            default -> throw new IllegalArgumentException("Unknown status code: " + statusCode);
        };
    }

    public static NonActiveStatus createNonActiveStatusObject(){
        return new NonActiveStatus();
    }

    public static ActiveStatus createActiveStatusObject(){
        return new ActiveStatus();
    }
}
