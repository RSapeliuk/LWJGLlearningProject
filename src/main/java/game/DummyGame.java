package game;

import engine.GameItem;
import engine.IGameLogic;
import engine.MouseInput;
import engine.Window;
import engine.graph.Camera;
import engine.graph.Mesh;
import engine.graph.OBJLoader;
import engine.graph.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;

public class DummyGame implements IGameLogic {

    private static final float MOUSE_SENSITIVITY = 0.2f;
    private static final float CAMERA_POS_STEP = 0.05f;

    private final Camera camera;

    private final Vector3f cameraInc;

    private final Renderer renderer;

    private GameItem[] gameItems;

    public DummyGame() {
        this.renderer = new Renderer();
        this.camera = new Camera();
        this.cameraInc = new Vector3f();

    }


    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);
       Mesh mesh = OBJLoader.loadMesh("/models/bunny.obj");
       Texture texture = new Texture("textures/grassBlock.png");
       mesh.setTexture(texture);
       GameItem gameItem = new GameItem(mesh);
       gameItem.setScale(0.5f);
       gameItem.setPosition(0,0,-2);
       gameItems = new GameItem[]{gameItem};
    }

    @Override
    public void input(Window window, MouseInput mouseInput) {
        cameraInc.set(0,0,0);
        if (window.isKeyPressed(GLFW_KEY_W)) {
            cameraInc.z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            cameraInc.z = 1;
        }
        if(window.isKeyPressed(GLFW_KEY_A)) {
            cameraInc.x = -1;
        } else if(window.isKeyPressed(GLFW_KEY_D)) {
            cameraInc.x = 1;
        }
         if(window.isKeyPressed(GLFW_KEY_Z)) {
            cameraInc.y = -1;
        } else if(window.isKeyPressed(GLFW_KEY_X)) {
            cameraInc.y = 1;
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        //Update camera pos
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);

        //Update mouse camera
        if(mouseInput.isRightButtonPressed()){
            Vector2f rotationVec = mouseInput.getDisplVec();
            camera.moveRotation(rotationVec.x * MOUSE_SENSITIVITY, rotationVec.y * MOUSE_SENSITIVITY, 0);
        }

    }

    @Override
    public void render(Window window) {
        renderer.render(window,camera,gameItems);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        for (GameItem gameItem : gameItems) {
            gameItem.getMesh().cleanUp();
        }
    }
}
