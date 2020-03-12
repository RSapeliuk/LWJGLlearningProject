import engine.GameEngine;
import engine.IGameLogic;
import game.DummyGame;

public class Main {

    public static void main(String[] args) {

        try{
            boolean vSync = true;
            IGameLogic gameLogic = new DummyGame();
            GameEngine gameEngine = new GameEngine("GAME", 600, 480, vSync, gameLogic);
            gameEngine.run();
        } catch (Exception e){
            e.printStackTrace();
            System.exit(-1);
        }

    }
}
