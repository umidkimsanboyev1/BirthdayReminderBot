package uz.masters.springbootbot.handlers;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.masters.springbootbot.bot.TugBot;
import uz.masters.springbootbot.entity.AuthUser;
import uz.masters.springbootbot.entity.Birthdays;
import uz.masters.springbootbot.enums.State;
import uz.masters.springbootbot.repository.BirthDaysRepository;
import uz.masters.springbootbot.services.AuthUserService;

@Service
public class MessageHandle {

    private final TugBot bot;
    private final AuthUserService authUserService;
    private final BirthDaysRepository birthDaysRepository;


    public MessageHandle(TugBot bot, AuthUserService authUserService, BirthDaysRepository birthDaysRepository) {
        this.bot = bot;
        this.authUserService = authUserService;
        this.birthDaysRepository = birthDaysRepository;
    }

    public void handle(Message message) {
        Long chatId = message.getChatId();
        if (authUserService.findAuthUserByChatId(chatId)) {
            String fullName = message.getChat().getFirstName() + " " + message.getChat().getLastName();
            authUserService.createUser(message, fullName);
            bot.sendMessage(chatId, "Assalomu alaykum. " + fullName + " 😊 \nSiz Ro`yhatdan o`tmagansiz. Iltimos, menga ismingizni yuboring 👇👇👇👇");
        } else if (message.getText().equals("/addbirthday")) {
            AuthUser authUser = authUserService.getAuthUser(chatId);
            authUser.setState(State.INPUTNBIRTHDAY);
            authUserService.saveAuthUser(authUser);
            bot.sendMessage(chatId, "Yaxshi! Menga tug'ilgan haqida ma'lumotni bitta habarda quyidagi na'muna asosida yuboring" +
                    "\nMisol uchun: Aliyev Ali; kk.oo.yyyy");
        } else if (message.getText().equals("/changename")) {
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
                case INPUTNBIRTHDAY -> {
                    String text = message.getText();
                    String[] split = text.split(";");
                    Birthdays tempBirthday = new Birthdays();
                    tempBirthday.setBirthDayOwner(split[0].trim());
                    tempBirthday.setDate(split[1].trim());
                    System.out.println("Tugilgan kun qoshildi: " + tempBirthday);
                    birthDaysRepository.save(tempBirthday);
                    authUser.setState(State.REGISTERED);
                    authUserService.saveAuthUser(authUser);
                    bot.sendMessage(chatId, "Zo'r 🎉. Men " + tempBirthday.getBirthDayOwner() +
                            "ning tugilgan kunini " + tempBirthday.getDate() +
                            " sana bilan saqladim.\nTug'ilgan kuni kelganda xabar beraman");
                }
                case REGISTERED, OK -> {
                    bot.sendMessage(chatId, "Xozirda tug'ilgan kunlar mavjud emas 🎉.\nTug'ilgan kunni aniqlasam sizga darhol xabar beraman 😊");
                }
            }
        }
    }
}
