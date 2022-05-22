package com.casoncompany.engine.entity;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import com.casoncompany.engine.utils.*;

public class ObjectLoader {
	
	private List<Integer> vaos = new ArrayList<>();
	private List<Integer> vbos = new ArrayList<>();
	private List<Integer> textures = new ArrayList<>();
	
	public Model loadOBJModel(String filename) {
		List<String> lines = Utils.readAllLines(filename);
		
		List<Vector3f> vertices = new ArrayList<>();
		List<Vector3f> normals = new ArrayList<>();
		List<Vector2f> textures = new ArrayList<>();
		List<Vector3i> faces = new ArrayList<>();
		
		for(String line : lines) {
			String tokens[] = line.split("\\s+");
			
			switch(tokens[0]) {
			//vertices
			case "v":
				Vector3f verticesVec = new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]));
				vertices.add(verticesVec);
				break;
				
			//vertex textures
			case "vt":
				Vector2f texturesVec = new Vector2f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[1]));
				textures.add(texturesVec);
				break;
				
			//vertex normals
			case "vn":
				Vector3f normalsVec = new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]));
				normals.add(normalsVec);
				break;
				
			//faces
			case "f":
				processFace(tokens[1], faces);
				processFace(tokens[2], faces);
				processFace(tokens[3], faces);
				break;
			
			default:
				break;
			}
		}
		
		List<Integer> indices = new ArrayList<>();
		float[] verticesArray = new float[vertices.size() * 3];
		
		for(int i = 0; i < vertices.size(); i++) {
			verticesArray[i * 3] = vertices.get(i).x;
			verticesArray[i * 3 + 1] = vertices.get(i).y;
			verticesArray[i * 3 + 2] = vertices.get(i).z;
		}
		
		float[] textureCoordArray = new float[vertices.size() * 2];
		float[] normalsArray = new float[vertices.size() * 3];
		
		//will come back to later
		for(Vector3i face : faces) {
			processVertex(face.x, face.y, face.z, textures, normals, indices, textureCoordArray, normalsArray);
		}
		
		int[] indicesArray = indices.stream().mapToInt((Integer v) -> v).toArray();
		
		return loadModel(verticesArray, textureCoordArray, indicesArray);
	}
	
	private static void processVertex(int pos, int textureCoord, int normal, List<Vector2f> textureCoordList, List<Vector3f> normalList, List<Integer> indicesList, float[] textureCoordArray, float[] normalArray) {
		indicesList.add(pos);
		
		if(textureCoord >= 0) {
			Vector2f textureCoordVec = textureCoordList.get(textureCoord);
			textureCoordArray[pos * 2] = textureCoordVec.x;
			textureCoordArray[pos * 2 + 1] = 1 - textureCoordVec.y;
		}
		
		if(normal >= 0) {
			Vector3f normalVec = normalList.get(normal);
			normalArray[pos * 3] = normalVec.x;
			normalArray[pos * 3 + 1] = normalVec.y;
			normalArray[pos * 3 + 2] = normalVec.z;
		}
		
	}
	
	private static void processFace(String token, List<Vector3i> faces) {
		String[] lineToken = token.split("/");
		int length = lineToken.length;
		
		int pos = -1;
		int coords = -1;
		int normal = -1;
		
		pos = Integer.parseInt(lineToken[0]) - 1;
		
		if(length > 1) {
			String textCoord = lineToken[1];
			coords = textCoord.length() > 0 ? Integer.parseInt(textCoord)-1 : -1;
			
			if(length > 2) {
				normal = Integer.parseInt(lineToken[2]) - 1;
			}
		}
		
		Vector3i facesVec = new Vector3i(pos, coords, normal);
		
		faces.add(facesVec);
	}
	
	public Model loadModel(float[] vertices, float[] textureCoords, int[] indices) {
		int id = createVAO();
		
		storeIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, vertices);
		storeDataInAttributeList(1, 2, textureCoords);
		
		unbind();
		
		return new Model(id, indices.length);
	}
	
	public int loadTexture(String filename) throws Exception {
		int width, height;
		ByteBuffer buffer;
		
		try(MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer c = stack.mallocInt(1);
			
			buffer = STBImage.stbi_load(filename, w, h, c, 4);
			if(buffer == null)
				throw new Exception("Image file " + filename + " not loaded " + STBImage.stbi_failure_reason());
			
			width = w.get();
			height = h.get();
		}
		
		int id = GL11.glGenTextures();
		textures.add(id);
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		STBImage.stbi_image_free(buffer);
		
		return id;
		
	}
	
	private int createVAO() {
		int id = GL30.glGenVertexArrays();
		vaos.add(id);
		GL30.glBindVertexArray(id);
		return id;
	}
	
	private void storeDataInAttributeList(int attribNo, int vertexCount, float[] data) {
		int vbo = GL15.glGenBuffers();
		vbos.add(vbo);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		FloatBuffer buffer = Utils.storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attribNo, vertexCount, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	private void storeIndicesBuffer(int[] indices) {
		int vbo = GL15.glGenBuffers();
		vbos.add(vbo);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
		IntBuffer buffer = Utils.storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	private void unbind() {
		GL30.glBindVertexArray(0);
	}
	
	public void cleanup() {
		for(int vao : vaos)
			GL30.glDeleteVertexArrays(vao);
		for(int vbo : vbos)
			GL30.glDeleteBuffers(vbo);
		for(int texture : textures)
			GL30.glDeleteTextures(texture);
	}

}
