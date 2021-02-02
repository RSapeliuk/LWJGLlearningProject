package game;

import engine.GameItem;
import engine.IGameLogic;
import engine.Window;
import engine.graph.Mesh;
import engine.graph.Texture;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;

public class DummyGame implements IGameLogic {

    private int displxInc = 0;

    private int displyInc = 0;

    private int displzInc = 0;

    private int scaleInc = 0;

    private final Renderer renderer;

    private GameItem[] gameItems;

    public DummyGame() {
        renderer = new Renderer();
    }


    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);
        float[] positions = new float[]{
            // V0
            -0.5f, 0.5f, 0.5f,
            // V1
            -0.5f, -0.5f, 0.5f,
            // V2
            0.5f, -0.5f, 0.5f,
            // V3
            0.5f, 0.5f, 0.5f,
            // V4
            -0.5f, 0.5f, -0.5f,
            // V5
            0.5f, 0.5f, -0.5f,
            // V6
            -0.5f, -0.5f, -0.5f,
            // V7
            0.5f, -0.5f, -0.5f,

            // For text coords in top face
            // V8: V4 repeated
            -0.5f, 0.5f, -0.5f,
            // V9: V5 repeated
            0.5f, 0.5f, -0.5f,
            // V10: V0 repeated
            -0.5f, 0.5f, 0.5f,
            // V11: V3 repeated
            0.5f, 0.5f, 0.5f,

            // For text coords in right face
            // V12: V3 repeated
            0.5f, 0.5f, 0.5f,
            // V13: V2 repeated
            0.5f, -0.5f, 0.5f,

            // For text coords in left face
            // V14: V0 repeated
            -0.5f, 0.5f, 0.5f,
            // V15: V1 repeated
            -0.5f, -0.5f, 0.5f,

            // For text coords in bottom face
            // V16: V6 repeated
            -0.5f, -0.5f, -0.5f,
            // V17: V7 repeated
            0.5f, -0.5f, -0.5f,
            // V18: V1 repeated
            -0.5f, -0.5f, 0.5f,
            // V19: V2 repeated
            0.5f, -0.5f, 0.5f,
        };

        int[] indices = new int[]{
            // Front face
            0, 1, 3, 3, 1, 2,
            // Top Face
            4, 0, 3, 5, 4, 3,
            // Right face
            3, 2, 7, 5, 3, 7,
            // Left face
            6, 1, 0, 6, 0, 4,
            // Bottom face
            2, 1, 6, 2, 6, 7,
            // Back face
            7, 6, 4, 7, 4, 5,
        };

        float[] textCoords = new float[]{
            0.0f, 0.0f,
            0.0f, 0.5f,
            0.5f, 0.5f,
            0.5f, 0.0f,

            0.0f, 0.0f,
            0.5f, 0.0f,
            0.0f, 0.5f,
            0.5f, 0.5f,

            // For text coords in top face
            0.0f, 0.5f,
            0.5f, 0.5f,
            0.0f, 1.0f,
            0.5f, 1.0f,

            // For text coords in right face
            0.0f, 0.0f,
            0.0f, 0.5f,

            // For text coords in left face
            0.5f, 0.0f,
            0.5f, 0.5f,

            // For text coords in bottom face
            0.5f, 0.0f,
            1.0f, 0.0f,
            0.5f, 0.5f,
            1.0f, 0.5f,
        };
        Texture texture = new Texture("textures/grassblock.png");
       Mesh mesh = new Mesh(positions, textCoords, indices, texture);
       GameItem gameItem = new GameItem(mesh);
       gameItem.setPosition(0,0,-2);
       gameItems = new GameItem[]{gameItem};
    }

    @Override
    public void input(Window window) {
        displxInc = 0;
        displyInc = 0;
        displzInc = 0;
        scaleInc = 0;
        if (window.isKeyPressed(GLFW_KEY_UP)) {
            displyInc = 1;
        } else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            displyInc = -1;
        } else if(window.isKeyPressed(GLFW_KEY_LEFT)) {
            displxInc = -1;
        } else if(window.isKeyPressed(GLFW_KEY_RIGHT)) {
            displxInc = 1;
        } else if(window.isKeyPressed(GLFW_KEY_A)) {
            displzInc = -1;
        } else if(window.isKeyPressed(GLFW_KEY_Q)) {
            displzInc = 1;
        } else if(window.isKeyPressed(GLFW_KEY_Z)) {
            scaleInc = -1;
        } else if(window.isKeyPressed(GLFW_KEY_X)) {
            scaleInc = 1;
        }
    }

    @Override
    public void update(float interval) {
        for (GameItem gameItem : gameItems) {
            //Update positions
            Vector3f itemPos = gameItem.getPosition();
            float posX = itemPos.x + displxInc * 0.01f;
            float posY = itemPos.y + displyInc * 0.01f;
            float posZ = itemPos.z + displzInc * 0.01f;
            gameItem.setPosition(posX,posY,posZ);

            //Update scale
            float scale = gameItem.getScale();
            scale += scaleInc * 0.05f;
            if(scale < 0)
                scale = 0;
            gameItem.setScale(scale);

            //Update rotation
            float rotation = gameItem.getRotation().x + 1.5f;
            if(rotation > 360)
                rotation = 0;
            gameItem.setRotation(rotation,rotation,rotation);
        }
    }

    @Override
    public void render(Window window) {
        renderer.render(window,gameItems);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        for (GameItem gameItem : gameItems) {
            gameItem.getMesh().cleanUp();
        }
    }
}
