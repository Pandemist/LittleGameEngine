package game.quests;

import org.json.JSONObject;

public class Event {

	public static Event eventFabric(String name, JSONObject eventNode) {
		return new Event();
	}

	public Event() {

	}

	public void triggerEvent() {

	}
}
