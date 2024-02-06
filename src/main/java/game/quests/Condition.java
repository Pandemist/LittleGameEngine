package game.quests;

import org.json.JSONObject;

public class Condition {

	public static Condition conditionFabric(String name, JSONObject conditionNode) {
		return new Condition();
	}

	public Condition() {

	}

	public boolean conditionMatched() {
		return true;
	}
}
