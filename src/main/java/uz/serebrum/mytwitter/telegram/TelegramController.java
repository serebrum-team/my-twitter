package uz.serebrum.mytwitter.telegram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.serebrum.mytwitter.entity.User;
import uz.serebrum.mytwitter.service.RegisterWithTelegramService;
import uz.serebrum.mytwitter.service.UserService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class TelegramController extends TelegramLongPollingBot {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RegisterWithTelegramService register;
    @Autowired
    private UserService userService;


    private static HashMap<Long, User> telegramRegisterUser = new HashMap<>();



    @PostConstruct
    public void registerBot(){
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(this);
        } catch (TelegramApiException e) {
            logger.error(e.toString());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {

        logger.info(update.getMessage().getText());
        if (update.getMessage().hasText() && update.getMessage().getText().equals("/start") && !register.existsUser(update.getMessage().getChatId())) {
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId());
            message.setText("Telegram bot orqali ro'yxatdan o'tasizmi ?");


            // Create ReplyKeyboardMarkup object
            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
            // Create the keyboard (list of keyboard rows)
            List<KeyboardRow> keyboard = new ArrayList<>();
            // Create a keyboard row
            KeyboardRow row = new KeyboardRow();

            // Set each button, you can also use KeyboardButton objects if you need something else than text
            row.add("Ha roziman");


            // Add the first row to the keyboard
            keyboard.add(row);
            // Create another keyboard row
            row = new KeyboardRow();
            // Set each button for the second line
            row.add("Yoq rozi emasman");

            // Add the second row to the keyboard
            keyboard.add(row);
            // Set the keyboard to the markup
            keyboardMarkup.setKeyboard(keyboard);
            // Add it to the message
            message.setReplyMarkup(keyboardMarkup);

            try {
                // Send the message
                execute(message);
                return;
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        logger.info(String.valueOf(update.getMessage().getText().equals("Ha roziman")));

        if (update.getMessage().getText().equals("Ha roziman")) {
            System.out.println("TelegramController.onUpdateReceived");
            User user = new User();
            user.getOtherUserDetails().setFirstName((update.getMessage().getChat().getFirstName()));
            user.getOtherUserDetails().setLastName((update.getMessage().getChat().getLastName()));
            user.getOtherUserDetails().setBio((update.getMessage().getChat().getDescription()));
            telegramRegisterUser.put(update.getMessage().getChatId(), user);
            SendMessage namesMessage = new SendMessage().setChatId(update.getMessage().getChatId())
                    .setText("Sizning ismingiz sifatida - " + update.getMessage().getChat().getFirstName() + " olinadi \n" +
                            "Sizning familyangiz sifatida - " + update.getMessage().getChat().getLastName() + " olinadi \n") ;
//                            "Sizning bio qilib - " + update.getMessage().getChat().getDescription() + " olinadi.");

            SendMessage username = new SendMessage().setChatId(update.getMessage().getChatId())
                    .setText("Accountingiz uchun username tanlashingizkerak . " +
                            "Username kamida 6 ta harf bolishi kerak. " +
                            "Misol uchun behruz123. Yuborish paytida username so'zidan keyin tanlagan usernameingizni yuboring. Misol uchun" +
                            "\nusername behruz123");

            try {
                execute(namesMessage);
                execute(username);
                return;
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }


        }

        if (update.getMessage().getText().equals("Yoq rozi emasman")) {
            try {
                execute(new SendMessage().setChatId(update.getMessage().getChatId()).setText("U holda tizimdan email orqali royxatdan o'ting"));

                return;
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        if (update.getMessage().getText().startsWith("username") &&
                telegramRegisterUser.containsKey(update.getMessage().getChatId())) {
            String getUserName = update.getMessage().getText().replace("username", "").trim();
            if (register.existsUserByUserName(getUserName)) {
                try {
                    execute(new SendMessage().setChatId(update.getMessage().getChatId()).setText("Bu username allaqachon tanlangan iltimos qaytadan yuboring."));
                    return;
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            telegramRegisterUser.get(update.getMessage().getChatId()).setUserName(getUserName);

            try {
                execute(new SendMessage().setChatId(update.getMessage().getChatId()).setText("Endi password tanlashingiz kerak. Password sifatida" +
                        "8 ta belgidan ko'p bo'lgan password tanlashingiz kerak. Misol uchun 123" +
                        "456789. Tanlagan passwordingizni password so'zidan keyin yubirishingiz mumkin"));
                return;
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        if (update.getMessage().getText().startsWith("password") &&
                telegramRegisterUser.containsKey(update.getMessage().getChatId())) {
            String getPassword = update.getMessage().getText().replace("password", "").trim();
            telegramRegisterUser.get(update.getMessage().getChatId()).setPassword(getPassword);

            try {

                userService.saveUser(telegramRegisterUser.get(update.getMessage().getChatId()));
                telegramRegisterUser.remove(update.getMessage().getChatId());
                execute(new SendMessage().setChatId(update.getMessage().getChatId()).setText("Username va password " +
                        "muvaffaqqiyatli ornatildi . Tizimga shu username va parol bilan kirishingiz mumkin ."));
                return;
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        try {
            execute(new SendMessage().setChatId(update.getMessage().getChatId()).setText("Yana bir bor harakat qilib ko'ring."));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getBotUsername() {
        return "@foooooooooooooooo_bot";
    }

    @Override
    public String getBotToken() {
        return "914627839:AAEXGzuOY8AS_5kRentnV2pGYWAZ17shbKo";
    }
}
