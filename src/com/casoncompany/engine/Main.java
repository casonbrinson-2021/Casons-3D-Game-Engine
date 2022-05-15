package com.casoncompany.engine;

public class Main implements Runnable {
	
	private static final int UPDATES_PER_SECOND = 60;
	private static final double NANOSECONDS_PER_UPDATE = 1000000000.0 / UPDATES_PER_SECOND;
	
	private Thread thread;
	private boolean running = false;
	
	public void start() {
		if(running)return;
		
		running = true;
		
		thread = new Thread(this, "Main Thread");
		thread.start();
	}
	
	public void stop() {
		if(!running)return;
		
		running = false;
		
		try {
			thread.join();
		} catch(Exception e) {
			e.printStackTrace();
			System.err.println("Error joining thread...");
			System.exit(1);
		}
	}
	
	public void init() {
		System.out.println("Initializing game...");
	}
	
	//draws the game to the screen
	public void render() {
		
	}
	
	//updates the state of things in the game at a consistent rate of UPDATES_PER_SECOND
	public void update() {
		
	}
	
	public void loop() {
		System.out.println("Starting game loop");
		long prevTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		int frames = 0;
		double delta = 0.0;	//when delta hits a value of 1 (100%) then we should do an update
		
		
		while(running) {
			long currTime = System.nanoTime();
			delta += (currTime - prevTime) / NANOSECONDS_PER_UPDATE;
			prevTime = currTime;
			
			while(delta >= 1) {
				update();
				delta-=1.0;
			}
			
			render();
			frames++;
			
			//if a second has passed
			if((System.currentTimeMillis() - timer) > 1000) {
				timer+=1000;
				//gameWindow.changeTitle(TITLE + " - " + frames + " fps");
				frames = 0;
			}
		}
	}

	@Override
	public void run() {
		init();
		loop();
	}
	

	public static void main(String[] args) {
		new Main().start();
	}
}
