package engine.graph;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {

  private final int vaoId;

  private final int vboId;

  private final int vertexCount;

  public Mesh(float[] positions) {
    FloatBuffer verticesBuffer = null;
    try {
      verticesBuffer = MemoryUtil.memAllocFloat(positions.length);
      vertexCount = positions.length / 3;
      verticesBuffer.put(vertexCount).flip();

      vaoId = glGenVertexArrays();
      glBindVertexArray(vaoId);

      vboId = glGenBuffers();
      glBindBuffer(GL_ARRAY_BUFFER, vboId);
      glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
      glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
      glBindBuffer(GL_ARRAY_BUFFER, 0);

      glBindVertexArray(0);

    }finally{
      if(verticesBuffer != null)
        MemoryUtil.memFree(verticesBuffer);
    }
  }

  public int getVaoId() {
    return vaoId;
  }

  public int getVertexCount() {
    return vertexCount;
  }

  public void cleanUp(){
    glDisableVertexAttribArray(0);

    glBindBuffer(GL_ARRAY_BUFFER,0);
    glDeleteBuffers(vboId);

    glBindVertexArray(0);
    glDeleteVertexArrays(vaoId);
  }
}
