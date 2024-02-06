package UI;

import UI.Boxes.*;
import game.GameState;
import main.GameController;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static helper.Utilities.splitLineByWidth;

public class DialogMenuUi {
	GameController gc;
	Graphics2D g2;
	private final int boxMargin = 15;
	private final Color textColor;
	private final int dialogBoxHeight = 100;

	int displayedChar = 0;
	double displayedCharRaw = 0;
	String talker = "";
	String dialogText = "";
	boolean answersLocked = false;
	ArrayList<String> answerString = new ArrayList<>();
	ArrayList<String> dialogLines = new ArrayList<>();
	int currentLine = 0;
	double blinkPointTimer = 0;
	boolean blinkPoints = false;

	Font dialogFont;
	int dialogFontHeight = -1;

	private UiBoxOutline dialogBox;
	private UiTextBox nameBox;
	private ArrayList<UiTextBox> answerBoxes;

	public DialogMenuUi(GameController gc) {
		this.gc = gc;

		textColor = new Color(255, 255, 255);
		dialogFont = new Font("Arial", Font.PLAIN, 18);

		dialogBox = new UiBoxOutline();
		nameBox = new UiTextBox(new UiBoxOutline(), talker, dialogFont, 15);
		answerBoxes = new ArrayList<>();
	}

	public void update(double timeGap) {
		if(gc.getGs() != GameState.IN_DIALOG) {
			displayedChar = 0;
			displayedCharRaw = 0;
		}

		if(displayedChar < getCharsOnVisibleLines()) {
			displayedCharRaw += timeGap * 20;
		}
		displayedChar = (int) displayedCharRaw;

		if(dialogLines.size() > currentLine+2) {
			blinkPointTimer += timeGap;
			if(blinkPointTimer > 1.5) {
				blinkPointTimer -= 1.5;
			}
			blinkPoints = blinkPointTimer > 0.75;
		}else{
			blinkPointTimer = 0;
			blinkPoints = false;
		}
	}

	private ArrayList<String> prepareText(String s) {
		ArrayList<String> result = new ArrayList<>();
		String[] paragraphs = s.split("\\r?\\n");

		for (String paragraph : paragraphs) {
			result.addAll(splitLineByWidth(paragraph, dialogFont, gc.screenWidth - 150));
		}

		return result;
	}

	public void resize() {

	}

	private int getCharsOnVisibleLines() {
		int result = 0;
		if(dialogLines.size() > currentLine) {
			result += dialogLines.get(currentLine).length();
		}
		if(dialogLines.size() > currentLine+1) {
			result += dialogLines.get(currentLine+1).length();
		}
		return result;
	}

	public void draw(Graphics2D g2) {
		this.g2 = g2;

		if(dialogFontHeight < 0) {
			FontMetrics fm = g2.getFontMetrics(dialogFont);
			dialogFontHeight = fm.getHeight();
		}

		Point screenCenter = new Point(gc.screenWidth, gc.screenHeight);

		if(gc.world.isMovedToLowerHalf()) {
			// oben hin
			Point dialogBoxCorner = new Point(boxMargin, boxMargin);
			dialogBox.setPosition(dialogBoxCorner);
			dialogBox.setBounds(new Point(screenCenter.x - (boxMargin * 2), dialogBoxHeight));
			dialogBox.draw(g2);

			g2.setFont(dialogFont);
			g2.setColor(textColor);

			if(dialogLines.size() > currentLine) {
				g2.drawString(talker + ": " + dialogLines.get(currentLine).substring(0, Math.min(Math.max(0, displayedChar), dialogLines.get(currentLine).length())),
						boxMargin, boxMargin);
			}
			if(dialogLines.size() > currentLine+1) {
				g2.drawString(talker + ": " + dialogLines.get(currentLine+1).substring(0, Math.min(Math.max(0, (displayedChar - dialogLines.get(currentLine).length())), dialogLines.get(currentLine+1).length())),
						boxMargin, boxMargin + dialogFontHeight);
			}

			if((dialogLines.size() > currentLine+2) && blinkPoints) {
				g2.drawString("▼", screenCenter.x - (boxMargin * 3) - 10, boxMargin + dialogFontHeight);
			}

			if(((currentLine+2) >= dialogLines.size()) && (displayedChar >= getCharsOnVisibleLines())) {
				int answerBoxShift = dialogBoxHeight + 5 + boxMargin;
				if(displayedChar >= dialogText.length()) {
					for(UiTextBox answer : answerBoxes) {
						answer.setPosition(new Point(10, answerBoxShift));
						answer.draw(g2);
						answerBoxShift += answer.getSize().y + 5;
					}
				}
			}
		}else{
			// Unten hin

			Point dialogBoxCorner = new Point(boxMargin, screenCenter.y - boxMargin - dialogBox.getSize().y);
			dialogBox.setPosition(dialogBoxCorner);
			dialogBox.setBounds(new Point(screenCenter.x - (boxMargin * 2), dialogBoxHeight));
			dialogBox.draw(g2);

			g2.setFont(dialogFont);
			g2.setColor(textColor);

			if(dialogLines.size() > currentLine) {
				g2.drawString(talker + ": " + dialogLines.get(currentLine).substring(0, Math.min(Math.max(0, displayedChar), dialogLines.get(currentLine).length())),
						dialogBoxCorner.x + boxMargin, dialogBoxCorner.y + (2*boxMargin));
			}
			if(dialogLines.size() > currentLine+1) {
				g2.drawString(talker + ": " + dialogLines.get(currentLine+1).substring(0, Math.min(Math.max(0, (displayedChar - dialogLines.get(currentLine).length())), dialogLines.get(currentLine+1).length())),
						dialogBoxCorner.x + boxMargin, dialogBoxCorner.y + (2*boxMargin) + dialogFontHeight);
			}

			if((dialogLines.size() > currentLine+2) && blinkPoints) {
				g2.drawString("▼", screenCenter.x - (boxMargin * 3) - 10, dialogBoxCorner.y + (2*boxMargin) + dialogFontHeight);
			}

			if(((currentLine+2) >= dialogLines.size()) && (displayedChar >= getCharsOnVisibleLines())) {
				int answerBoxShift = dialogBoxCorner.y;
				for (UiTextBox answer : answerBoxes) {
					answerBoxShift -= answer.getSize().y + 5;
					answer.setPosition(new Point(10, answerBoxShift));
					answer.draw(g2);
				}
			}
		}
	}

	public void setTalker(String s) {
		talker = s;
		nameBox.setText(talker);
	}

	public void setDialogText(String s) {
		if(!dialogText.equals(s)) {
			this.dialogText = s;
			dialogLines = prepareText(s);
		}
	}

	public void setAnswerString(ArrayList<String> answers) {
		if(!answerString.equals(answers)) {
			answerString = answers;
			answersLocked = true;
			answerBoxes.clear();
			for(String s : answers) {
				UiTextBox nb = new UiTextBox(new UiBoxOutlineWithFocus(), s, dialogFont, boxMargin);
				answerBoxes.add(nb);
			}

			answersLocked = false;
		}
	}

	public void mouseOver(Point mouse) {
		if(!answersLocked) {
			for (UiTextBox answer : answerBoxes) {
				answer.setFocusedByMousePos(mouse);

			}
		}
	}

	public void clicked(MouseEvent e) {
		if(displayedChar < getCharsOnVisibleLines()) {
			displayedCharRaw = getCharsOnVisibleLines();
		}else{
			if(dialogLines.size() > currentLine+2) {
				currentLine += 2;
				displayedCharRaw = 0;
			}
			if(!answersLocked) {
				if(answerBoxes.isEmpty()) {
					gc.dm.forwardDialog(-1);
					currentLine = 0;
					displayedCharRaw = 0;
				}else{
					for(UiTextBox answer : answerBoxes) {
						if(answer.isFocused()) {
							gc.dm.forwardDialog(answerBoxes.indexOf(answer));
							currentLine = 0;
							displayedCharRaw = 0;
						}
					}
				}
			}
		}
	}
}
