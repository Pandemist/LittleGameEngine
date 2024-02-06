package game.environment;

import main.GameController;
import game.Rendering.Renderer;

import java.awt.*;
import java.util.ArrayList;

public class EnviManager {
	private final GameController gc;
	private ArrayList<Light> lights;

	public EnviManager(GameController gc) {
		this.gc = gc;
		lights = new ArrayList<>();

		//	LightSource ls = new LightSource(gc, 100, new Color(0, 255, 0), 5, 2.5);

	//	LightSource ls = new LightSource(gc, 80, new Color(244, 219, 83, 100), 10, 4);
	//	ls.setCenter(new Point(gc.screenWidth/2, gc.screenHeight/2));
	//	lights.add(ls);

	//	LightBlock lb = new LightBlock(gc, 50, new Color(244, 219, 83, 100));
	//	lb.setCenter(new Point(gc.screenWidth/2, gc.screenHeight/2));
	//	lights.add(lb);
	}

	public void update(double timeGap) {
		Point mousePos = new Point(gc.inputHandler.getCurrentMouseX(), gc.inputHandler.getCurrentMouseY());
		for(Light light : lights) {
			light.setCenter(mousePos);
			light.update(timeGap);
		}
	}

	public void draw(Renderer r) {
		for(Light ls : lights) {
			ls.draw(r);
		}
	}
}
