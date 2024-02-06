package game.quests.Dialogs;

import game.quests.DialogManager;
import helper.Option;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import static helper.Option.*;

public class NPCDialigSegment extends DialogSegment{
	private ArrayList<String> pointer;

	public NPCDialigSegment(DialogManager dm, JSONObject dialogSegment) {
		super(dm, dialogSegment);
		pointer = new ArrayList<>();
		if(dialogSegment.has("pointer")) {
			String[] pointers = dialogSegment.getString("pointer").replaceAll("[\r\t\n) ]", "").split(",");
			pointer.addAll(Arrays.asList(pointers));
		}
	}

	@Override
	public Option<String> getNextSegmentPointer(int variant) {
		if((variant >= 0) && (variant < pointer.size())) {
			return Some(pointer.get(variant));
		}
		return None();
	}

	public ArrayList<String> getPointer() {
		return pointer;
	}
}
