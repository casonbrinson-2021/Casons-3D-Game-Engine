package com.casoncompany.engine.renderer;

import com.casoncompany.engine.Camera;
import com.casoncompany.engine.entity.Model;
import com.casoncompany.engine.lighting.DirectionalLight;
import com.casoncompany.engine.lighting.PointLight;
import com.casoncompany.engine.lighting.SpotLight;

public interface RendererInterface<T> {
	
	public void init() throws Exception;
	
	public void render(Camera camera, PointLight[] pointLights, SpotLight[] spotLights, DirectionalLight directionalLight);

	abstract void bind(Model model);
	
	public void unbind();
	
	public void prepare(T t, Camera camera);
	
	public void cleanup();
}

