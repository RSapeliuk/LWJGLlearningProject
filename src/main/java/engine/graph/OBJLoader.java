package engine.graph;

import engine.Utils;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class OBJLoader {

  public static Mesh loadMesh(String fileName) throws Exception {
    List<String> lines = Utils.readAllLines(fileName);

    List<Vector3f> vertices = new ArrayList<>();
    List<Vector2f> textures = new ArrayList<>();
    List<Vector3f> normals = new ArrayList<>();
    List<Face> faces = new ArrayList<>();

    for (String line : lines) {
      String[] tokens = line.split("\\s+");
      switch (tokens[0]) {
        case "v":
          //Vertex
          Vector3f vector3f = new Vector3f(
              Float.parseFloat(tokens[1]),
              Float.parseFloat(tokens[2]),
              Float.parseFloat(tokens[3]));
          vertices.add(vector3f);
          break;
        case "vt":
          //Texture
          Vector2f vector2f = new Vector2f(
              Float.parseFloat(tokens[1]),
              Float.parseFloat(tokens[2]));
          textures.add(vector2f);
          break;
        case "vn":
          //Normals
          Vector3f vector3fNorm = new Vector3f(
              Float.parseFloat(tokens[1]),
              Float.parseFloat(tokens[2]),
              Float.parseFloat(tokens[3]));
          normals.add(vector3fNorm);
          break;
        case "f":
          Face face = new Face(tokens[1], tokens[2], tokens[3]);
          ;
          faces.add(face);
          break;
        default:
          //Ignore all
          break;
      }

    }
    return reorderedLists(vertices, textures, normals, faces);
  }

  private static Mesh reorderedLists(List<Vector3f> posList, List<Vector2f> textureList, List<Vector3f> normalsList, List<Face> faces) {

    List<Integer> indices = new ArrayList<>();

    float[] posArr = new float[posList.size() * 3];
    int i = 0;
    for (Vector3f pos : posList) {
      posArr[i * 3] = pos.x;
      posArr[i * 3 + 1] = pos.y;
      posArr[i * 3 + 2] = pos.z;
      i++;
    }
    float[] textCoordArr = new float[posList.size() * 2];
    float[] normArr = new float[posList.size() * 3];

    for (Face face : faces) {
      IdxGroup[] faceVertexIndices = face.getFaceVertexIndices();
      for (IdxGroup faceVertexIndex : faceVertexIndices) {
        processFaceVertex(faceVertexIndex, textureList, normalsList, indices, textCoordArr, normArr);
      }
    }
    int[] indicesArr = new int[indices.size()];
    indicesArr = indices.stream().mapToInt((Integer v) -> v).toArray();
    Mesh mesh = new Mesh(posArr, textCoordArr, normArr, indicesArr);
    return mesh;
  }

  private static void processFaceVertex(IdxGroup indices, List<Vector2f> textCoordList,
                                        List<Vector3f> normList, List<Integer> indicesList, float[] textCoordArr,
                                        float[] normArr) {
    int posIndex = indices.idxPos;
    indicesList.add(posIndex);

    if (indices.idxTextCoord >= 0) {
      Vector2f textCoord = textCoordList.get(indices.idxTextCoord);
      textCoordArr[posIndex * 2] = textCoord.x;
      textCoordArr[posIndex * 2 + 1] = 1 - textCoord.y;
    }
    if (indices.idxVecNormal >= 0) {
      Vector3f vectorNorm = normList.get(indices.idxVecNormal);
      normArr[posIndex * 3] = vectorNorm.x;
      normArr[posIndex * 3 + 1] = vectorNorm.y;
      normArr[posIndex * 3 + 2] = vectorNorm.z;
    }
  }

  protected static class Face {

    private IdxGroup[] idxGroups = new IdxGroup[3];

    public Face(String v1, String v2, String v3) {
      idxGroups = new IdxGroup[3];
      idxGroups[0] = parseLine(v1);
      idxGroups[1] = parseLine(v2);
      idxGroups[2] = parseLine(v3);
    }

    private IdxGroup parseLine(String line) {
      IdxGroup idxGroup = new IdxGroup();

      String[] lineTokens = line.split("/");
      int length = lineTokens.length;
      idxGroup.idxPos = Integer.parseInt(lineTokens[0]) - 1;
      if (length > 1) {
        String textCoords = lineTokens[1];
        idxGroup.idxTextCoord = textCoords.length() > 0 ? Integer.parseInt(textCoords) - 1 : IdxGroup.NO_VALUE;
        if (length > 2) {
          idxGroup.idxVecNormal = Integer.parseInt(lineTokens[2]) - 1;
        }
      }
      return idxGroup;
    }

    public IdxGroup[] getFaceVertexIndices() {
      return idxGroups;
    }

  }

  protected static class IdxGroup {
    public static final int NO_VALUE = -1;

    public int idxPos;
    public int idxTextCoord;
    public int idxVecNormal;

    public IdxGroup() {
      this.idxPos = NO_VALUE;
      this.idxTextCoord = NO_VALUE;
      this.idxVecNormal = NO_VALUE;
    }
  }
}
