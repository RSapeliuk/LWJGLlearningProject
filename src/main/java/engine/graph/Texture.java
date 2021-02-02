package engine.graph;

import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;


public class Texture {

  private final int id;

  public Texture(int id) {
    this.id = id;
  }

  public Texture(String fileName) throws Exception {
    this(loadTexture(fileName));
  }

  private static int loadTexture(String fileName) throws Exception {
    int width;
    int height;
    ByteBuffer buffer;

    try(MemoryStack stack = MemoryStack.stackPush())  {
      IntBuffer w = stack.mallocInt(1);
      IntBuffer h = stack.mallocInt(1);
      IntBuffer channels = stack.mallocInt(1);

      buffer = stbi_load(fileName, w, h, channels, 4);
      if (buffer == null)
        throw new Exception("Image file [" + fileName + "] not loaded" + stbi_failure_reason());

      //Get width and height of image
      width = w.get();
      height = h.get();
    }

      //Create new OpenGL texture
      int textureId = glGenTextures();

      //Bind the texture
      glBindTexture(GL_TEXTURE_2D, textureId);

      //Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
      glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

      // Generate a texture
      glTexImage2D(GL_TEXTURE_2D,0,GL_RGBA,width,height,0,GL_RGBA,GL_UNSIGNED_BYTE, buffer);

      glGenerateMipmap(GL_TEXTURE_2D);

      //Free memory for the image
      stbi_image_free(buffer);

      return textureId;
  }

  public void cleanUp(){
    glDeleteTextures(id);
  }

  public int getId() {
    return id;
  }
}
