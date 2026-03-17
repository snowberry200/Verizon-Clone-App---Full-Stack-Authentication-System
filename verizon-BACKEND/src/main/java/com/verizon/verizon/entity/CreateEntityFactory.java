package com.verizon.verizon.entity;

import com.verizon.verizon.userstatuses.ActiveStatus;
import com.verizon.verizon.userstatuses.UserStatus;
import java.time.LocalDateTime;
import java.util.List;

public class CreateEntityFactory {

    // Private constructor to prevent instantiation
    private CreateEntityFactory() {
        throw new UnsupportedOperationException(
                "This is a utility class and cannot be instantiated"
        );
    }
    // create new Roles
    public static Roles createRole(String name, List<User> users){
        Roles newRole = new Roles.Builder(name)
                .users(users)
                .build();
        if(users!=null){
            for(User user:users){
                user.addSingleRole(newRole);
            }
        }
        return newRole;

    }

    //create new SecurityQuestion
    public static SecurityQuestion createSecurityQuestion(String name,String questionText){
        return  new SecurityQuestion(name,questionText);
    }

    // create new User
    public static User createUser(String name,
                                  String email,
                                  String password,
                                  String accessToken,
                                  LocalDateTime createdAt,
                                  LocalDateTime lastLogin,
                                  UserStatus status,
                                  UserSecurityQuestion userSecurityQuestion,
                                  List<Roles>roles
    )

    {
        User newUser = new User.Builder(name,email,password)
                .accessToken(accessToken)
                .createdAt(createdAt)
                .lastLogin(lastLogin)
                .status(new ActiveStatus())
                .userSecurityQuestion(userSecurityQuestion)
                .roles(roles)
                .build();

        // Maintain bidirectional relationships AFTER creation
        //Maintain bidirectional relationship for userSecurityQuestion
        if(userSecurityQuestion!=null){
            userSecurityQuestion.addSingleUser(newUser);
        }
        // //Maintain bidirectional relationship for roles
        if(roles!=null){
            for(Roles role : roles){
                role.addSingleUserInRole(newUser);
            }
        }

        return  newUser;
    }

    // create new UserSecurityQuestion
    public static UserSecurityQuestion createUserQuestion(
            String answer,
            SecurityQuestion question,
            List<User> users) {

        UserSecurityQuestion userSecurityQuestion = new UserSecurityQuestion.Builder(answer)
                .question(question)
                .users(users)
                .build();

        // Maintain bidirectional
        if (users != null){
            for(User user : users ){
               if(user.getUserSecurityQuestion()!= userSecurityQuestion){
                   user.setUserSecurityQuestion(userSecurityQuestion);
               }
            }
        }
        return userSecurityQuestion;
    }
}
