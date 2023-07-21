package uz.masters.springbootbot.bot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.masters.springbootbot.config.BotConfig;
import uz.masters.springbootbot.handlers.MainHandle;

@Component
public class TugBot extends TelegramLongPollingBot {
    private final BotConfig botConfig;
    private final MainHandle mainHandle;

    public TugBot(BotConfig botConfig, MainHandle mainHandle) {
        this.botConfig = botConfig;
        this.mainHandle = mainHandle;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }



    @Override
    public void onUpdateReceived(Update update) {
        mainHandle.handle(update);
    }


    public void sendMessage(Long chatId, String message)  {
        SendMessage tempMessage = new SendMessage(String.valueOf(chatId), message);
        try {
            execute(tempMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }
}
