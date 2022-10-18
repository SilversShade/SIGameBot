package SiGameBot.Logic;

import SiGameBot.Bot;

import java.util.ArrayList;

public class Game {
    private final Bot bot;
    private Player leading;
    private final ArrayList<Player> players = new ArrayList<>();
    private final String gameName;
    private final String password;
    public Game(Bot bot, long leadingId, String gameName){
        this.gameName = gameName;
        this.bot = bot;
        this.password = "";
        this.leading = new Player("Ведущий", leadingId, this);
    }
    public Game(Bot bot, long leadingId, String gameName, String password){
        this.gameName = gameName;
        this.bot = bot;
        this.password = password;
        this.leading = new Player("Ведущий", leadingId, this);
    }
    public String getGameName(){ return gameName; }
    public String getPassword(){ return password; }
    public void addPlayer(String name, long chatId){
        Player player = new Player(name, chatId, this);
        players.add(player);
        bot.sendMessage(name + " подключился к игре", leading.id);
    }
    public boolean addPlayer(String name, String pass, long chatId){
        if(pass.equals(password)){
            addPlayer(name, chatId);
            return true;
        }
        return false;
    }
    public void startGame(){

    }
}
