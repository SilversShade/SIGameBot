package sigamebot.bot.handlecallback;

import sigamebot.bot.core.ITelegramBot;
import sigamebot.logic.SoloGame;

public class SoloGameCallbackQueryHandler implements ICallbackQueryHandler{
    private ITelegramBot bot;
    public SoloGameCallbackQueryHandler(ITelegramBot bot){ this.bot = bot; }
    public void handleCallbackQuery(String callData, Integer messageId, Long chatId){
        var parsedData = callData.split(" ", 2);
        SoloGame.getOngoingSoloGames().get(chatId).nextQuestion(parsedData[1]);
    }
}
