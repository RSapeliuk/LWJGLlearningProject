package engine;

import lombok.AllArgsConstructor;


public class GameEngine implements Runnable {

    public static final int TARGET_FPS = 60;

    public static final int TARGET_UPS = 30;

    private final Window window;

    private final Timer timer;

    private final IGameLogic gameLogic;

    public GameEngine(String windowTitle, int width, int height, boolean vSync, IGameLogic gameLogic) {
        this.window = new Window(windowTitle,width,height,vSync);
        this.timer = new Timer();
        this.gameLogic = gameLogic;
    }

    @Override
    public void run() {
        try {
            init();
            gameLoop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cleanup();
        }
    }

    protected void init() throws Exception {
        window.init();
        timer.init();
        gameLogic.init(window);
    }

    protected void gameLoop() {
        float elapsedTime;
        float accumulator = 0f;
        float interval = 1f/TARGET_UPS;

        boolean running = true;
        while(running && !window.windowShouldClose()){
            elapsedTime = timer.getElapsedTime();
            accumulator += elapsedTime;

            input();

            while(accumulator >= interval){
                update(interval);
                accumulator -= interval;
            }

            render();

            if(!window.isVSync()){
                sync();
            }
        }
    }

    protected void cleanup(){
        gameLogic.cleanup();
    }

    protected void sync(){
        float loopSlot = 1f/TARGET_FPS;
        double endTime = timer.getLastLoopTime() + loopSlot;
        while(timer.getTime() < endTime){
            try{
                Thread.sleep(1);
            } catch (InterruptedException ie){

            }
        }
    }
    protected  void input(){
        gameLogic.input(window);
    }

    protected void update(float interval){
        gameLogic.update(interval);
    }

    protected  void render(){
        gameLogic.render(window);
        window.update();
    }

}
