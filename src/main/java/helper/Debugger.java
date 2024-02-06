package helper;

import main.GameController;

import java.awt.*;

public class Debugger {
	private boolean DebugMode = false;
	private GameController gp;
	private int FPS;

	public void DebugPrinter(String s) {
		if(DebugMode) {
			System.out.println(s);
		}
	}

	public void debuggerDraw(Graphics2D g2) {
		Font font = new Font("Arial", Font.PLAIN, 15);

		g2.setFont(font);
		g2.setColor(Color.GREEN);

		String text = FPS+" FPS";

		FontMetrics fontMetrics = g2.getFontMetrics();
		int textWidth = fontMetrics.stringWidth(text);

		int x = (gp.getWidth() - textWidth) - 5;
		int y = 5 + fontMetrics.getAscent();

		g2.drawString(text, x, y);
	}

	public void setDebugMode(boolean debugModeb) {
		DebugMode = debugModeb;
	}

	public void setGp(GameController gp) {
		this.gp = gp;
	}

	public void setFPS(int FPS) {
		this.FPS = FPS;
	}
}

