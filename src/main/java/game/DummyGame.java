package game;

import engine.GameItem;
import engine.IGameLogic;
import engine.MouseInput;
import engine.Window;
import engine.graph.*;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.system.CallbackI;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glArrayElement;
import static org.lwjgl.opengl.GL11.glViewport;

public class DummyGame implements IGameLogic {

  private static final float MOUSE_SENSITIVITY = 0.2f;
  private static final float CAMERA_POS_STEP = 0.05f;

  private final Camera camera;

  private final Vector3f cameraInc;

  private final Renderer renderer;

  private GameItem[] gameItems;

  private Vector3f ambientLight;

  private PointLight pointLight;

  public DummyGame() {
    this.renderer = new Renderer();
    this.camera = new Camera();
    this.cameraInc = new Vector3f();

  }


  @Override
  public void init(Window window) throws Exception {
    renderer.init(window);

    float reflectance = 1f;

    Mesh mesh = OBJLoader.loadMesh("/models/cube.obj");
    Texture texture = new Texture("textures/grassBlock.png");
    Material material = new Material(texture, reflectance);
    mesh.setMaterial(material);
    GameItem gameItem = new GameItem(mesh);
    gameItem.setScale(0.5f);
    gameItem.setPosition(0, 0, -2);
    gameItems = new GameItem[]{gameItem};

    ambientLight = new Vector3f(0.3f, 0.3f, 0.3f);
    Vector3f lightColor = new Vector3f(1, 1, 1);
    Vector3f lightPosition = new Vector3f(0, 0, 1);
    float lightIntensity = 1.0f;
    pointLight = new PointLight(lightColor, lightPosition, lightIntensity);
    PointLight.Attenuation att = new PointLight.Attenuation(0.0f, 0.0f, 1.0f);
    pointLight.setAttenuation(att);
  }

  @Override
  public void input(Window window, MouseInput mouseInput) {
    cameraInc.set(0, 0, 0);
    if (window.isKeyPressed(GLFW_KEY_W)) {
      cameraInc.z = -1;
    } else if (window.isKeyPressed(GLFW_KEY_S)) {
      cameraInc.z = 1;
    }
    if (window.isKeyPressed(GLFW_KEY_A)) {
      cameraInc.x = -1;
    } else if (window.isKeyPressed(GLFW_KEY_D)) {
      cameraInc.x = 1;
    }
    if (window.isKeyPressed(GLFW_KEY_Z)) {
      cameraInc.y = -1;
    } else if (window.isKeyPressed(GLFW_KEY_X)) {
      cameraInc.y = 1;
    }
    float lightPos = pointLight.getPosition().z;
    if (window.isKeyPressed(GLFW_KEY_N))
      this.pointLight.getPosition().z = lightPos + 0.1f;
    if (window.isKeyPressed(GLFW_KEY_M))
      this.pointLight.getPosition().z = lightPos - 0.1f;
  }

  @Override
  public void update(float interval, MouseInput mouseInput) {
    //Update camera pos
    camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);

    //Update mouse camera
    if (mouseInput.isRightButtonPressed()) {
      Vector2f rotationVec = mouseInput.getDisplVec();
      camera.moveRotation(rotationVec.x * MOUSE_SENSITIVITY, rotationVec.y * MOUSE_SENSITIVITY, 0);
    }

  }

  @Override
  public void render(Window window) {
    renderer.render(window, camera, gameItems, ambientLight, pointLight);
  }

  @Override
  public void cleanup() {
    renderer.cleanup();
    for (GameItem gameItem : gameItems) {
      gameItem.getMesh().cleanUp();
    }
  }
}
