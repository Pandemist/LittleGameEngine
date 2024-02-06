package UI.Boxes;

import java.awt.*;

import static helper.Utilities.getFontBox;

public class UiTextBox{
	private final Color textColor = new Color(255, 255, 255);
	private final Font font;
	private final int margin;
	private String text;
	private UiBox boxForText;

	private Point textPos;
	private Point fontBounds;

	public UiTextBox(UiBox boxForText, String text, Font font, int margin) {
		this.boxForText = boxForText;
		this.text = text;
		this.font = font;
		this.fontBounds = getFontBox(text, font);
		this.margin = margin;
		boxForText.setBounds(new Point(fontBounds.x + (margin*2), fontBounds.y + (margin*2)));
	}

	public UiTextBox(UiBox boxForText, String text, Font font, int margin, Color frameCol) {
		this(boxForText, text, font, margin);
	//	super.frame = frameCol;
	}

	public UiTextBox(UiBox boxForText, String text, Font font, int margin, int opacityFocused) {
		this(boxForText, text, font, margin);
	//	super.alphaFucused = opacityFocused;
	}

	public void update() {
		this.boxForText.update();
	}

	public void draw(Graphics2D g2) {
		boxForText.draw(g2);

		g2.setFont(font);
		g2.setColor(textColor);
		g2.drawString(text, textPos.x + margin, textPos.y + fontBounds.y - font.getSize()/5 + margin);
	}

	// Getter und Setter

	public void setText(String text) {
		this.text = text;
		this.fontBounds = getFontBox(text, font);
		this.boxForText.setBounds(new Point(fontBounds.x + (margin*2), fontBounds.y + (margin*2)));
	}

	public void setPosition(Point p) {
		textPos = p;
		boxForText.setPosition(p);
	}

	public Point getSize() {
		return this.boxForText.getSize();
	}

	public void setFocusedByMousePos(Point mouse) {
		this.boxForText.setFocusedByMousePos(mouse);
	}

	public boolean isFocused() {
		return this.boxForText.isFocused();
	}
}
