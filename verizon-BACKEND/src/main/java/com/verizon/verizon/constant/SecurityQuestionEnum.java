package com.verizon.verizon.constant;

public enum SecurityQuestionEnum {
    FIRST_CONCERT("What was the first live concert you attended?"),
    FIRST_MEET("Where did you and your spouse first meet?"),
    FAVORITE_CHILDHOOD_PLACE("What was your favorite place to visit as a child?"),
    FIRST_ROOMMATE("What was the first name of your first roommate?"),
    MEMORABLE_PLACE("What is the name of a memorable place you visited?"),
    COLLEGE_RESTAURANT("What was your favorite restaurant in college?");

    private final String question;

    SecurityQuestionEnum(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }
}