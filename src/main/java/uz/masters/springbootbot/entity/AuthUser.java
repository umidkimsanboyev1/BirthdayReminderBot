package uz.masters.springbootbot.entity;

import lombok.Data;
import uz.masters.springbootbot.enums.State;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class AuthUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String chatId;
    private String fullName;
    private String userName;
    private String bio;
    private String nickName;
    private boolean registered;
    private String organization;
    private State state;


}
