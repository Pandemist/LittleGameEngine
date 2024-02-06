package game.quests.Dialogs;

import game.quests.Condition;
import game.quests.DialogManager;
import game.quests.Event;
import helper.Option;
import org.json.JSONObject;

import java.util.ArrayList;

import static helper.Option.*;

public class DialogSegment {
	private DialogManager dm;

	private Option<String> c = None();
	private String rawText = "";
	private Option<String> e = None();

	public DialogSegment(DialogManager dm) {
		this.dm = dm;
	}

	public DialogSegment(DialogManager dm, JSONObject dialogSegment) {
		this.dm = dm;
		if(dialogSegment.has("text")) {
			rawText = dialogSegment.getString("text");
		}
		if(dialogSegment.has("event")) {
			e = Some(dialogSegment.getString("event"));
		}
		if(dialogSegment.has("condition")) {
			c = Some(dialogSegment.getString("condition"));
		}
	}

	public Option<String> getNextSegmentPointer(int variant) {
		return None();
	}

	public String getRawText() {
		return rawText;
	}

	public boolean conditionMatched() {
		if(c.isSome()) {
			Option<Condition> con = dm.getCondition(c.unwrap());
			if(con.isSome()) {
				con.unwrap().conditionMatched();
			}
		}
		return true;
	}

	public void execEvent() {
		if(e.isSome()) {
			Option<Event> eve = dm.getEvent(e.unwrap());
			if(eve.isSome()) {
				eve.unwrap().triggerEvent();
			}
		}
	}
}
