package game.quests.Dialogs;

import game.quests.DialogManager;
import helper.Option;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;

import static helper.Option.*;

public class PlayerDialogSegment extends DialogSegment{
	private Option<String> pointer = None();

	public PlayerDialogSegment(DialogManager dm, JSONObject dialogSegment) {
		super(dm, dialogSegment);
		if(dialogSegment.has("pointer")) {
			pointer = Some(dialogSegment.getString("pointer"));
		}
	}

	@Override
	public Option<String> getNextSegmentPointer(int variant) {
		return pointer;
	}
}
