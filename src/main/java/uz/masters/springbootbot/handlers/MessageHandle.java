package uz.masters.springbootbot.handlers;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.masters.springbootbot.bot.TugBot;
import uz.masters.springbootbot.entity.AuthUser;
import uz.masters.springbootbot.enums.State;
import uz.masters.springbootbot.services.AuthUserService;

@Service
public class MessageHandle {

    private final TugBot bot;
    private final AuthUserService authUserService;


    public MessageHandle(TugBot bot, AuthUserService authUserService) {
        this.bot = bot;
        this.authUserService = authUserService;
    }

    public void handle(Message message) {
        Long chatId = message.getChatId();
        if (authUserService.findAuthUserByChatId(chatId)) {
            String fullName = message.getChat().getFirstName() + " " + message.getChat().getLastName();
            authUserService.createUser(message, fullName);
            bot.sendMessage(chatId, "Assalomu alaykum. " + fullName + " 😊 \nSiz Ro`yhatdan o`tmagansiz. Iltimos, menga ismingizni yuboring 👇👇👇👇");
        } else if (message.getText().equals("/change_name")) {
            AuthUser authUser = authUserService.getAuthUser(chatId);
            authUser.setState(State.FULLNAME);
            authUserService.saveAuthUser(authUser);
            bot.sendMessage(chatId, "Yaxshi! menga ismingizni yuboring, men uni qayta saqlayman 😊");
        } else {
            AuthUser authUser = authUserService.getAuthUser(chatId);
            switch (authUser.getState()) {
                case FULLNAME -> {
                    if (!message.hasText()) {
                        bot.sendMessage(chatId, "Siz yuborgan habar ism emas! Iltimos ismningizni yuboring (Siz yuborgan ism bilan men sizni chaqiraman 😊)");
                    }
                    authUser.setFullName(message.getText());
                    authUser.setState(State.REGISTERED);
                    authUser.setRegistered(true);
                    authUserService.saveAuthUser(authUser);
                    bot.sendMessage(chatId, "Tabriklaymiz! 🎉. Sizni men " + message.getText() + " nom bilan saqladim.\nTugilgan kunni aniqlasam sizga darhol xabar beraman 😊");
                }
                case REGISTERED, OK -> {
                    bot.sendMessage(chatId, "Xozirda tug'ilgan kunlar mavjud emas 🎉.\nTugilgan kunni aniqlasam sizga darhol xabar beraman 😊");
                }
            }
        }
    }
}
