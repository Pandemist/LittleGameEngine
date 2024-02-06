package game.level;

import helper.PairwiseLinearFunction;

import java.awt.*;

public class DayNightCycle {
	private final PairwiseLinearFunction sunLight;

	public DayNightCycle() {
		sunLight = new PairwiseLinearFunction();
		sunLight.addSegment(0,0.1);
		sunLight.addSegment((4.0 / 24), 0.1);
		sunLight.addSegment((7.0 / 24), 0.95);
		sunLight.addSegment((8.0 / 24), 1);
		sunLight.addSegment((18.5 / 24), 1);
		sunLight.addSegment((21.0 / 24), 0.1);
		sunLight.addSegment(1, 0.1);
	}

	public Color getNightValue(double time) {
		int alpha = (int) ((1-(sunLight.evaluate(time))) * 255);

		int nightLightRed = 0;
		int nightLightGreen = 0;
		int nightLightBlue = 0;

		return new Color(nightLightRed, nightLightGreen, nightLightBlue, alpha);
	}
}
