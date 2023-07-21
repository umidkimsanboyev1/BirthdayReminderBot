package uz.masters.springbootbot.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import uz.masters.springbootbot.entity.AuthUser;
import uz.masters.springbootbot.enums.State;

import java.util.List;

public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {
    List<AuthUser> findAuthUserByState(State state);


    AuthUser findAuthUserByChatId(String chatId);

}
