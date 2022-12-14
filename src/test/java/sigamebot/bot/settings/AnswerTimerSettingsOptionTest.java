package sigamebot.bot.settings;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Message;
import sigamebot.exceptions.IncorrectSettingsParameterException;
import sigamebot.user.ChatInfo;

public class AnswerTimerSettingsOptionTest{
    ChatInfo chatInfo;
    private static AnswerTimerSettingsOption answerTimerSettingsOption;
    @BeforeAll
    public static void setup() {
        answerTimerSettingsOption = new AnswerTimerSettingsOption();
    }


    private Message getUserMessage(String messageText) {
        var message = new Message();
        message.setText(messageText);
        return message;
    }


    @Test
    public void processUserResponseThrowsExceptionWhenReceivesNotAnInteger() {
        var userInput = getUserMessage("definitely not an integer");
        Assertions.assertThrows(IncorrectSettingsParameterException.class,
                () -> answerTimerSettingsOption.processUserResponse(userInput, chatInfo));
    }

    @Test
    public void processUserResponseThrowsExceptionWhenReceivesNegativeInteger() {
        var userInput = getUserMessage("-1");
        Assertions.assertThrows(IncorrectSettingsParameterException.class,
                () -> answerTimerSettingsOption.processUserResponse(userInput,
                chatInfo));
    }

    @Test
    public void processUserResponseThrowsExceptionWhenReceivesNumberLargerThanLongMaxValue() {
        var userInput = getUserMessage("18927398789127389712893789172389718343434293");
        Assertions.assertThrows(IncorrectSettingsParameterException.class,
                () -> answerTimerSettingsOption.processUserResponse(userInput,
                        chatInfo));
    }

    @Test
    public void processUserResponseThrowsExceptionWhenReceivesLongTypeNumber() {
        var userInput = getUserMessage("2147483648");
        Assertions.assertThrows(IncorrectSettingsParameterException.class,
                () -> answerTimerSettingsOption.processUserResponse(userInput,
                        chatInfo));
    }
}
