package engine.graph;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {

  private final int vaoId;

  private final List<Integer> vboIdList;

  private final int vertexCount;

  private Material material;

  public Mesh(float[] positions, float[] textCoords, float[] normals, int[] indices) {
    FloatBuffer posBuffer = null;
    IntBuffer indicesBuffer = null;
    FloatBuffer textCoordsBuffer = null;
    FloatBuffer vecNormalBuffer = null;
    try {
      vertexCount = indices.length;
      this.vboIdList = new ArrayList<>();

      vaoId = glGenVertexArrays();
      glBindVertexArray(vaoId);

      // Position VBO
      int vboId = glGenBuffers();
      vboIdList.add(vboId);
      posBuffer = MemoryUtil.memAllocFloat(positions.length);
      posBuffer.put(positions).flip();
      glBindBuffer(GL_ARRAY_BUFFER, vboId);
      glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
      glEnableVertexAttribArray(0);
      glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

      // Index VBO
      vboId  = glGenBuffers();
      vboIdList.add(vboId);
      indicesBuffer = MemoryUtil.memAllocInt(indices.length);
      indicesBuffer.put(indices).flip();
      glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
      glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

      //Texture VBO
      vboId = glGenBuffers();
      vboIdList.add(vboId);
      textCoordsBuffer = MemoryUtil.memAllocFloat(textCoords.length);
      textCoordsBuffer.put(textCoords).flip();
      glBindBuffer(GL_ARRAY_BUFFER,vboId);
      glBufferData(GL_ARRAY_BUFFER,textCoordsBuffer,GL_STATIC_DRAW);
      glEnableVertexAttribArray(1);
      glVertexAttribPointer(1,2,GL_FLOAT,false,0,0);

      //Vertex normals VBO
      vboId = glGenBuffers();
      vboIdList.add(vboId);
      vecNormalBuffer = MemoryUtil.memAllocFloat(normals.length);
      vecNormalBuffer.put(normals).flip();
      glBindBuffer(GL_ARRAY_BUFFER, vboId);
      glBufferData(GL_ARRAY_BUFFER, vecNormalBuffer, GL_STATIC_DRAW);
      glEnableVertexAttribArray(2);
      glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);

      glBindBuffer(GL_ARRAY_BUFFER, 0);
      glBindVertexArray(0);

    } finally {
      if (posBuffer != null)
        MemoryUtil.memFree(posBuffer);
      if (indicesBuffer != null)
        MemoryUtil.memFree(indicesBuffer);
      if (textCoordsBuffer != null)
        MemoryUtil.memFree(textCoordsBuffer);
      if (vecNormalBuffer != null)
        MemoryUtil.memFree(vecNormalBuffer);
    }
  }

  public Material getMaterial() {
    return material;
  }

  public void setMaterial(Material material) {
    this.material = material;
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
    for (Integer vboId : vboIdList) {
      glDeleteBuffers(vboId);
    }

    //Delete the texture
    Texture texture = material.getTexture();
    if(texture != null)
      texture.cleanUp();

    //Delete VAO
    glBindVertexArray(0);
    glDeleteVertexArrays(vaoId);
  }

  public void render(){
    Texture texture = material.getTexture();
    if(texture != null) {
      glActiveTexture(GL_TEXTURE0);

      glBindTexture(GL_TEXTURE_2D, texture.getId());
    }

    glBindVertexArray(getVaoId());

    glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

    //Restore state
    glBindVertexArray(0);
    glBindTexture(GL_TEXTURE_2D,0);
  }
}
