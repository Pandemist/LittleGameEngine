package main;

import UI.UI;
import game.GameState;
import game.Rendering.Renderer;
import game.World;
import game.quests.DialogManager;
import input.InputHandler;
import lang.LangManager;
import settings.Settings;

import javax.swing.*;
import java.awt.*;

public class GameController extends JPanel implements Runnable {
	// Screen Settings
	public final int tileSize = 16;
	public final int rawScreenWidth = 16 * tileSize;
	public final int rawScreenHeight = 12 * tileSize;
	public final int scale = 4;
	public final int screenWidth = rawScreenWidth * (scale-1);
	public final int screenHeight = rawScreenHeight * (scale-1);

	private boolean isRunning = true;
	private final int targetFPS = 60;
	private final long drawInterval = 1000000000 / targetFPS;

	private GameState gs = GameState.START_SCREEN;
	private GameState gsLast = GameState.START_SCREEN;

	public World world = new World(this);
	public DialogManager dm = new DialogManager(this);
	Settings settings = new Settings();
	public LangManager langManager = new LangManager();
	public InputHandler inputHandler = new InputHandler(this);
	public UI ui = new UI(this);
	public game.Rendering.Renderer renderer = new Renderer(this);
	Thread gameThread;

	public GameController() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.BLACK);
		this.setDoubleBuffered(true);
		this.addKeyListener(inputHandler);
		this.addMouseListener(inputHandler);
		this.addMouseMotionListener(inputHandler);
		this.addMouseWheelListener(inputHandler);
		this.setFocusable(true);
	}

	public void prepareGame() {
		langManager.PrepareLanguageDirectory(settings.getGameLang());
	}

	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	@Override
	public void run() {
		double currentTime;
		double elapsedTime;
		double lastTime = System.nanoTime();

		double delta = 0;
		double timeGap = 0;

		long frameTimer = 0;
		int frames = 0;

		while(isRunning) {
			currentTime = System.nanoTime();
			elapsedTime = currentTime - lastTime;
			lastTime = currentTime;

			delta += elapsedTime / drawInterval;
			frameTimer += elapsedTime;
			timeGap += elapsedTime;

			if(delta >= 1) {
				update(timeGap / 1000000000.0);
				repaint();
				delta--;
				frames++;
				timeGap = 0;
			}

			// FPS anzeigen
			if(frameTimer >= 1000000000) {
				Main.debugger.setFPS(frames);
				frames = 0;
				frameTimer = 0;
			}
		}
		System.exit(0);
	}

	private void update(double timeGap) {
		world.update(timeGap);
		dm.update();
		ui.update(timeGap);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D)g;

		renderer.render(g2);

		Main.debugger.debuggerDraw(g2);
	}

	// Getter und Setter

	public void stopGame() {
		isRunning = false;
	}

	public GameState getGs() {
		return gs;
	}

	public void setGs(GameState gs) {
		this.gsLast = this.gs;
		this.gs = gs;
	}

	public GameState getGsLast() {
		return gsLast;
	}
}
