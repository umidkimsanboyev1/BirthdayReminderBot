package uz.masters.springbootbot.services;

import org.springframework.stereotype.Component;
import uz.masters.springbootbot.bot.TugBot;
import uz.masters.springbootbot.entity.AuthUser;
import uz.masters.springbootbot.entity.Birthdays;
import uz.masters.springbootbot.enums.State;
import uz.masters.springbootbot.repository.AuthUserRepository;
import uz.masters.springbootbot.repository.BirthDaysRepository;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class TimeService {

    private final TugBot bot;

    public TimeService(BirthDaysRepository birthDaysRepository, AuthUserRepository authUserRepository, TugBot bot) {
        this.bot = bot;
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                List<Birthdays> birthdaysList = birthDaysRepository.findBirthdaysByCongratulatedFalse();
                List<AuthUser> users = authUserRepository.findAuthUserByState(State.REGISTERED);
                checkBirthdays(birthdaysList, users);
            }
        }, getNineAM(), 24 * 60 * 60 * 1000);

    }

    private Date getNineAM() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private void checkBirthdays(List<Birthdays> birthdaysList, List<AuthUser> users) {
        System.out.println("vazifaga kirishildi");
        System.out.println(birthdaysList);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String today = sdf.format(new Date());
        System.out.println(today);
        for (Birthdays birthday : birthdaysList) {
            if (birthday.getDate().equals(today)) {
                System.out.println(birthday);
                for (AuthUser user : users) {
                    bot.sendMessage(Long.valueOf(user.getChatId()),
                            "Salom " + user.getFullName() +
                            ".\nBugun " + today + ". " + birthday.getBirthDayOwner() +
                            "ning tavallud topgan kuni 🎉🎉🎉\n" +
                            "Tabriklash esdan chiqmasin ‼. \n" +
                            "Yana tavallud kun bo'lsa xabar beraman 😊");
                    System.out.println("yuborildi: " + user.getFullName());

                }
            }
        }
    }
}
