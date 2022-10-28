package sigamebot.bot.core;

import org.telegram.telegrambots.meta.api.objects.Message;
import sigamebot.bot.botstate.ITelegramBotState;
import sigamebot.bot.botstate.SigameBotState;
import sigamebot.bot.commands.CancelCommand;
import sigamebot.bot.userinteraction.ICallbackQueryHandler;
import sigamebot.bot.userinteraction.UpdateProcessor;
import sigamebot.bot.commands.MenuCommand;
import sigamebot.bot.commands.SigameBotCommand;
import sigamebot.bot.commands.StartCommand;
import sigamebot.bot.userinteraction.filehandlers.UserFileHandler;
import sigamebot.ui.gamedisplaying.TelegramGameDisplay;
import sigamebot.utilities.CallbackPrefix;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class SigameBot extends TelegramLongPollingBot implements ITelegramBot{
    private static final String TOKEN = System.getenv("botToken");
    private static final String NAME = "SIGame Bot";
    public static Map<String, SigameBotCommand> commandMap;
    private static Map<String, Class<? extends ICallbackQueryHandler>> queryHandlerMap;
    public static Map<Long, ITelegramBotState> chatToBotState;
    public SigameBot() {
        commandMap = Map.of("/start",
                new StartCommand("/start", "Краткое описание бота и список доступных команд", this),
                "/menu",
                new MenuCommand("/begin", "Начало игры", this),
                "/cancel",
                new CancelCommand("/cancel", "Выход из режима ожидания отправки пака", this));

        queryHandlerMap = Map.of(CallbackPrefix.MENU,
                MenuCommand.class,
                TelegramGameDisplay.SOLO_GAME_CALLBACK_PREFIX,
                TelegramGameDisplay.class);

        chatToBotState = new HashMap<>();
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = null;

        if (update.hasMessage()) {
            message = update.getMessage();
            if (!chatToBotState.containsKey(message.getChatId()))
                chatToBotState.put(message.getChatId(), SigameBotState.DEFAULT_STATE);
        }
        UpdateProcessor.processCommands(message, commandMap);

        UserFileHandler.handleUserFiles(this, message);

        if (update.hasCallbackQuery())
            UpdateProcessor.processCallbackQuery(this, update, queryHandlerMap);
    }

    @Override
    public String getBotUsername() {
        return NAME;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }

    // Отправка сообщений
    public int sendMessage(String text, long chatId){
        SendMessage message = createSendMessageObject(text, chatId);
        try {
            return execute(message).getMessageId();
        } catch (TelegramApiException e) {
            return -1;
        }

    }
    public int sendMessage(String text, long chatId, List<List<InlineKeyboardButton>> buttons){
        SendMessage message = createSendMessageObject(text, chatId);
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        keyboard.setKeyboard(buttons);
        message.setReplyMarkup(keyboard);
        try {
            return execute(message).getMessageId();
        } catch (TelegramApiException e) {
            return -1;
        }
    }
    //public int sendMessage(String text, long chatId, InlineKeyboardMarkup keyboard){
    //    SendMessage message = createSendMessageObject(text, chatId);
    //    message.setReplyMarkup(keyboard);
    //    try {
    //        return execute(message).getMessageId();
    //    } catch (TelegramApiException e) {
    //        return -1;
    //    }
//
    //}

    // Редактирование сообщения
    public int editMessage(String text, long chatId, int messageId){
        EditMessageText message = createEditMessageObject(text, chatId, messageId);
        try {
            execute(message);
            return 0;
        } catch (TelegramApiException e) {
            return -1;
        }

    }
    public int editMessage(String text, long chatId, int messageId, List<List<InlineKeyboardButton>> buttons){
        EditMessageText message = createEditMessageObject(text, chatId, messageId);
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        keyboard.setKeyboard(buttons);
        message.setReplyMarkup(keyboard);
        try {
            execute(message);
            return 0;
        } catch (TelegramApiException e) {
            return -1;
        }

    }
    //public int editMessage(String text, long chatId, int messageId, InlineKeyboardMarkup keyboard){
    //    EditMessageText message = createEditMessageObject(text, chatId, messageId);
    //    message.setReplyMarkup(keyboard);
    //    try {
    //        execute(message);
    //        return 0;
    //    } catch (TelegramApiException e) {
    //        return -1;
    //    }
    //}

    // Удаление сообщения
    public int deleteMessage(long chatId, int messageId){
        DeleteMessage message = new DeleteMessage();
        message.setChatId(chatId);
        message.setMessageId(messageId);
        try {
            execute(message);
            return 0;
        } catch (TelegramApiException e) {
            return -1;
        }

    }

    // Дополнительные методы
    private SendMessage createSendMessageObject(String text, long chatId){
        SendMessage message = new SendMessage();
        message.setText(text);
        message.setChatId(chatId);
        message.enableHtml(true);
        return message;
    }
    private EditMessageText createEditMessageObject(String text, long chatId, int messageId){
        EditMessageText message = new EditMessageText();
        message.setChatId(chatId);
        message.setText(text);
        message.setMessageId(messageId);
        return message;
    }
}