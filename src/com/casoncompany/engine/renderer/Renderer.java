package com.casoncompany.engine.renderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.casoncompany.engine.Camera;
import com.casoncompany.engine.Window;
import com.casoncompany.engine.entity.Entity;
import com.casoncompany.engine.entity.Model;
import com.casoncompany.engine.lighting.DirectionalLight;
import com.casoncompany.engine.lighting.PointLight;
import com.casoncompany.engine.lighting.SpotLight;
import com.casoncompany.engine.utils.Transformation;
import com.casoncompany.engine.utils.Utils;

public class Renderer {
	
	public static final Vector3f AMBIENT_LIGHT = new Vector3f(0.5f, 0.5f, 0.5f);
	public static final float SPECULAR_POWER = 10.0f;
	
	private final Window window;
	private Shader shader;
	
	private Map<Model, List<Entity>> entities = new HashMap<>();
		
	public Renderer(Window window) {
		this.window = window;
	}
	
	public void init() throws Exception {
		shader = new Shader();
		shader.createVertexShader(Utils.loadResource("/shaders/vertex.vs"));
		shader.createFragmentShader(Utils.loadResource("/shaders/fragment.fs"));
		shader.link();
		shader.createUniform("textureSampler");
		shader.createUniform("transformationMatrix");
		shader.createUniform("projectionMatrix");
		shader.createUniform("viewMatrix");
		shader.createUniform("ambientLight");
		shader.createMaterialUniform("material");
		shader.createUniform("specularPower");
		shader.createDirectionalLightUniform("directionalLight");
		shader.createPointLightListUniform("pointLights", 5);
		shader.createSpotLightListUniform("spotLights", 5);
	}
	
	public void update() {
	}
	
	public void bind(Model model) {
		GL30.glBindVertexArray(model.getId());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		shader.setUniform("material", model.getMaterial());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getId());
	}
	
	public void unbind() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	public void prepare(Entity entity, Camera camera) {
		shader.setUniform("textureSampler", 0);
		shader.setUniform("transformationMatrix", Transformation.createTransformationMatrix(entity));
		shader.setUniform("viewMatrix", Transformation.getViewMatrix(camera));
	}
	
	public void renderLights(Camera camera, PointLight[] pointLights, SpotLight[] spotLights, DirectionalLight directionalLight) {
		shader.setUniform("ambientLight", AMBIENT_LIGHT);
		shader.setUniform("specularPower", SPECULAR_POWER);
		
		int numLights = spotLights != null ? spotLights.length : 0;
		for(int i = 0; i < numLights; i++)
			shader.setUniform("spotLights", spotLights[i], i);
		
		numLights = pointLights != null ? pointLights.length : 0;
		for(int i = 0; i < numLights; i++)
			shader.setUniform("pointLights", pointLights[i], i);
		
		shader.setUniform("directionalLight", directionalLight);
	}
	
	public void render(Camera camera, DirectionalLight directionalLight, PointLight[] pointLights, SpotLight[] spotLights) {
		clear();
		
		if(window.isResize()) {
			GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
			window.setResize(true);
		}
		
		shader.bind();	
		shader.setUniform("projectionMatrix", window.updateProjectionMatrix());	
		renderLights(camera, pointLights, spotLights, directionalLight);
		for(Model model : entities.keySet()) {
			bind(model);
			List<Entity> entityList = entities.get(model);
			for(Entity entity : entityList) {
				prepare(entity, camera);
				GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbind();
		}
		
		entities.clear();
		shader.unbind();
	}
	
	public void processEntities(Entity entity) {
		List<Entity> entityList = entities.get(entity.getModel());
		
		if(entityList != null)
			entityList.add(entity);
		else {
			List<Entity> newEntityList = new ArrayList<>();
			newEntityList.add(entity);
			entities.put(entity.getModel(), newEntityList);
		}
	}
	
	public void clear() {
		window.setClearColor(0.0f,  0.0f, 0.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public void cleanup() {
		shader.cleanup();
	}

}
