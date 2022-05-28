package com.casoncompany.engine.renderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class TerrainRenderer implements RendererInterface {
	
	private Window window;
	private Shader shader;
	private List<Terrain> terrains;
	
	public TerrainRenderer(Window window) throws Exception {
		this.window = window;
		terrains = new ArrayList<>();
		shader = new Shader();
	}

	@Override
	public void init() throws Exception {
		shader.createVertexShader(Utils.loadResource("/shaders/terrain_vertex.vs"));
		shader.createFragmentShader(Utils.loadResource("/shaders/terrain_fragment.fs"));
		shader.link();
		shader.createUniform("backgroundTexture");
		shader.createUniform("redTexture");
		shader.createUniform("greenTexture");
		shader.createUniform("blueTexture");
		shader.createUniform("blendMap");
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

	@Override
	public void render(Camera camera, PointLight[] pointLights, SpotLight[] spotLights, DirectionalLight directionalLight) {
		shader.bind();	
		shader.setUniform("projectionMatrix", window.updateProjectionMatrix());	
		Renderer.renderLights(pointLights, spotLights, directionalLight, shader);
		for(Terrain terrain : terrains) {
			bind(terrain.getModel());
			prepare(terrain, camera);
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			unbind();
		}
		
		terrains.clear();
		shader.unbind();
	}

	@Override
	public void bind(Model model) {
		GL30.glBindVertexArray(model.getId());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		
		shader.setUniform("backgroundTexture", 0);
		shader.setUniform("redTexture", 1);
		shader.setUniform("greenTexture", 2);
		shader.setUniform("blueTexture", 3);
		shader.setUniform("blendMap", 4);
		
		shader.setUniform("material", model.getMaterial());
	}

	@Override
	public void unbind() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

	@Override
	public void prepare(Object terrain, Camera camera) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, ((Terrain) terrain).getBlendMapTerrain().getBackground().getId());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, ((Terrain) terrain).getBlendMapTerrain().getRedTexture().getId());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, ((Terrain) terrain).getBlendMapTerrain().getGreenTexture().getId());
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, ((Terrain) terrain).getBlendMapTerrain().getBlueTexture().getId());
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, ((Terrain) terrain).getBlendMap().getId());
		
		shader.setUniform("transformationMatrix", Transformation.createTransformationMatrix((Terrain) terrain));
		shader.setUniform("viewMatrix", Transformation.getViewMatrix(camera));
	}

	@Override
	public void cleanup() {
		shader.cleanup();
	}
	
	public List<Terrain> getTerrain() {
		return terrains;
	}

}
