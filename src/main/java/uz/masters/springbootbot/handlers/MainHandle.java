package uz.masters.springbootbot.handlers;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class MainHandle {
    private final CallBackQueryHandle callBackQueryHandle;
    private final MessageHandle messageHandle;

    public MainHandle(@Lazy CallBackQueryHandle callBackQueryHandle, @Lazy MessageHandle messageHandle) {
        this.callBackQueryHandle = callBackQueryHandle;
        this.messageHandle = messageHandle;
    }

    public void handle(Update update) {
        if(update.hasMessage()){
            messageHandle.handle(update.getMessage());
        } else if(update.hasCallbackQuery()){
            callBackQueryHandle.handle(update.getCallbackQuery());
        }
    }
}
