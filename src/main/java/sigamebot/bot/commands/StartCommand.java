package sigamebot.bot.commands;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import sigamebot.bot.botstate.FileRequestStage;
import sigamebot.bot.core.ITelegramBot;
import sigamebot.bot.core.SigameBot;
import sigamebot.utilities.properties.CallbackPrefix;
import sigamebot.utilities.properties.CommandNames;
import sigamebot.utilities.properties.FilePaths;
import sigamebot.utilities.StreamReader;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class StartCommand extends SigameBotCommand{
    public StartCommand(String command, String description, SigameBot bot) {
        super(command, description, bot);
    }

    @Override
    public void executeCommand(long chatId) {
        var display = SigameBot.displays.get(chatId);
        if (SigameBot.displays.get(chatId).stageFileRequest.getStage() != FileRequestStage.DEFAULT_STATE)
            return;
        try {
            List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
            buttons.add(List.of(ITelegramBot.createInlineKeyboardButton("Меню",
                    CallbackPrefix.MENU + " " + CommandNames.MENU_COMMAND_NAME)));
            display.updateMenuMessage(StreamReader.readFromInputStream(
                            FilePaths.START_COMMAND_MESSAGE),buttons);
        } catch (IOException e) {
            this.bot.sendMessage("Произошла ошибка при исполнении команды.", chatId);
        }
    }
}
