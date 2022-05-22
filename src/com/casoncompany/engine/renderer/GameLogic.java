package com.casoncompany.engine.renderer;

public interface GameLogic {
	
	public void init() throws Exception;
		
	public void render();
	
	public void update();
		
	public void cleanup();

}
