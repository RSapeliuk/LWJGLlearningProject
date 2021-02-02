package game;

import engine.GameItem;
import engine.Utils;
import engine.Window;
import engine.graph.Mesh;
import engine.graph.ShaderProgram;
import engine.graph.Transformation;
import lombok.NoArgsConstructor;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Renderer {

    private ShaderProgram shaderProgram;

    private static final float FOV = (float) Math.toRadians(60.0f);

    private static final float Z_NEAR = 0.01f;

    private static final float Z_FAR = 1000.0f;

    private final Transformation transformation;

    public Renderer() {
        transformation = new Transformation();
    }

    public void init(Window window) throws Exception {
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource("/vertex.vs"));
        shaderProgram.createFragmentShader(Utils.loadResource("/fragment.fs"));
        shaderProgram.link();

        //Create uniforms for matrices
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("worldMatrix");
        shaderProgram.createUniform("texture_sampler");

    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Window window, GameItem[] gameItems) {
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();

        //Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(),window.getHeight(),Z_NEAR,Z_FAR);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);

        shaderProgram.setUniform("texture_sampler", 0);

        //Render each gameItem
        for (GameItem gameItem : gameItems) {
            Matrix4f worldMatrix = transformation.getWorldMatrix(
                gameItem.getPosition(),
                gameItem.getRotation(),
                gameItem.getScale());
            shaderProgram.setUniform("worldMatrix", worldMatrix);
            gameItem.getMesh().render();
        }

        shaderProgram.unbind();
    }

    public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }
    }
}
