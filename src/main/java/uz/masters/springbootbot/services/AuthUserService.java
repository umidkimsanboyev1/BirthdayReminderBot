package uz.masters.springbootbot.services;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.masters.springbootbot.entity.AuthUser;
import uz.masters.springbootbot.enums.State;
import uz.masters.springbootbot.repository.AuthUserRepository;

@Service
public class AuthUserService {

    private final AuthUserRepository authUserRepository;

    public AuthUserService(AuthUserRepository authUserRepository) {
        this.authUserRepository = authUserRepository;
    }


    public boolean findAuthUserByChatId(Long chatId) {
        AuthUser authUserByChatId = authUserRepository.findAuthUserByChatId(String.valueOf(chatId));
        return authUserByChatId == null;
    }

    public void createUser(Message message, String fullName) {
        String username = message.getChat().getUserName();
        String bio = message.getChat().getBio();
        AuthUser tempUser = new AuthUser();
        tempUser.setChatId(String.valueOf(message.getChatId()));
        tempUser.setBio(bio);
        tempUser.setUserName(username);
        tempUser.setNickName(fullName);
        tempUser.setState(State.FULLNAME);
        tempUser.setOrganization("RGSBM");
        authUserRepository.save(tempUser);
    }

    public AuthUser getAuthUser(Long chatId) {
        return authUserRepository.findAuthUserByChatId(String.valueOf(chatId));
    }

    public void saveAuthUser(AuthUser authUser) {
        authUserRepository.save(authUser);
    }
}
