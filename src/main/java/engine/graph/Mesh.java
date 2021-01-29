package engine.graph;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {

  private final int vaoId;

  private final int posVboId;

  private final int idxVboId;

  private final int colorVboId;

  private final int vertexCount;

  public Mesh(float[] positions, int[] indices, float[] colors) {
    FloatBuffer posBuffer = null;
    IntBuffer indicesBuffer = null;
    try {
      vertexCount = indices.length;

      vaoId = glGenVertexArrays();
      glBindVertexArray(vaoId);

      // Position VBO
      posVboId = glGenBuffers();
      posBuffer = MemoryUtil.memAllocFloat(positions.length);
      posBuffer.put(positions).flip();
      glBindBuffer(GL_ARRAY_BUFFER, posVboId);
      glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
      glEnableVertexAttribArray(0);
      glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

      // Index VBO
      idxVboId = glGenBuffers();
      indicesBuffer = MemoryUtil.memAllocInt(indices.length);
      indicesBuffer.put(indices).flip();
      glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId);
      glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

      //Color VBO
      colorVboId = glGenBuffers();
      FloatBuffer colorBuffer = MemoryUtil.memAllocFloat(colors.length);
      colorBuffer.put(colors).flip();
      glBindBuffer(GL_ARRAY_BUFFER,colorVboId);
      glBufferData(GL_ARRAY_BUFFER,colorBuffer,GL_STATIC_DRAW);
      glEnableVertexAttribArray(1);
      glVertexAttribPointer(1,3,GL_FLOAT,false,0,0);

      glBindBuffer(GL_ARRAY_BUFFER, 0);
      glBindVertexArray(0);



    } finally {
      if (posBuffer != null)
        MemoryUtil.memFree(posBuffer);
      if(indicesBuffer != null)
        MemoryUtil.memFree(indicesBuffer);
    }
  }

  public int getVaoId() {
    return vaoId;
  }

  public int getVertexCount() {
    return vertexCount;
  }

  public void cleanUp() {
    glDisableVertexAttribArray(0);

    //Delete VBOs
    glBindBuffer(GL_ARRAY_BUFFER, 0);
    glDeleteBuffers(idxVboId);
    glDeleteBuffers(posVboId);

    //Delete VAO
    glBindVertexArray(0);
    glDeleteVertexArrays(vaoId);
  }
}
