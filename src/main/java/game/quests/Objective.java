package game.quests;

import org.json.JSONObject;

public class Objective {

	public static Objective objectiveFabric(String name, JSONObject objectiveNode) {
		return new Objective();
	}

	public Objective() {

	}
}
