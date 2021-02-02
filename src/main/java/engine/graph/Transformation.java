package engine.graph;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformation {

  private final Matrix4f projectionMatrix;

  private final Matrix4f worldMatrix;

  public Transformation() {
    this.projectionMatrix = new Matrix4f();
    this.worldMatrix = new Matrix4f();
  }

  public Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
    return projectionMatrix.setPerspective(fov, width/height,zNear,zFar);
  }

  public Matrix4f getWorldMatrix(Vector3f offset, Vector3f rotation, float scale) {
    return worldMatrix.identity().translation(offset)
        .rotateX((float) Math.toRadians(rotation.x))
        .rotateY((float) Math.toRadians(rotation.y))
        .rotateZ((float) Math.toRadians(rotation.z))
        .scale(scale);
  }
}
