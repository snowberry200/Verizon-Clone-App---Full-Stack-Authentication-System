package com.verizon.verizon.entity;

import jakarta.persistence.*;

@Entity
@Table(name="security_question")
public class SecurityQuestion{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    //required params
    @Column(nullable = false)
    private String questionText;
    @Column(nullable = false)
    private String name;


    public SecurityQuestion(){
        //for Jpa
    }
  // create  security Question
    public SecurityQuestion(String questionText,String name){
        this.name = name ;
        this.questionText = questionText;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}