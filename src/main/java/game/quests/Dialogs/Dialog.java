package game.quests.Dialogs;

import game.quests.Condition;
import game.quests.DialogManager;
import helper.Option;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import static helper.Option.*;

public class Dialog {
	private DialogManager dm;

	private String npcId;
	private String npcName;

	private ArrayList<String> startSegments = new ArrayList<>();

	private HashMap<String, NPCDialigSegment> npcOptions = new HashMap<>();
	private HashMap<String, PlayerDialogSegment> playerOptions = new HashMap<>();

	public Dialog(DialogManager dm, String id, JSONObject node) {
		this.dm = dm;

		this.npcId = id;
		this.npcName = node.getString("npc_name");

		String[] startSegmentString = node.getString("startRules").replaceAll("[\r\t\n) ]", "").split(",");
		startSegments.addAll(Arrays.asList(startSegmentString));

		JSONObject npcDialog = node.getJSONObject("npcDialogs");
		for (Iterator<String> it = npcDialog.keys(); it.hasNext(); ) {
			String dialogName = it.next();
			JSONObject dialogSegment = npcDialog.getJSONObject(dialogName);
			npcOptions.put(dialogName, new NPCDialigSegment(dm, dialogSegment));
		}

		JSONObject playerDialog = node.getJSONObject("playerDialogs");
		for (Iterator<String> it = playerDialog.keys(); it.hasNext(); ) {
			String dialogName = it.next();
			JSONObject dialogSegment = playerDialog.getJSONObject(dialogName);
			playerOptions.put(dialogName, new PlayerDialogSegment(dm, dialogSegment));
		}
	}

	public Option<DialogSegment> getStartSegment() {
		for(String s : startSegments) {
			DialogSegment startSegmentOpt = npcOptions.get(s);
			if(startSegmentOpt != null) {
				if(startSegmentOpt.conditionMatched()) {
					return Some(startSegmentOpt);
				}
			}
		}
		return None();
	}

	public Option<DialogSegment> getNPCDialogByName(String pointer) {
		DialogSegment d = npcOptions.get(pointer);
		if(d != null) {
			if(d.conditionMatched()) {
				return Some(d);
			}
		}
		return None();
	}

	public Option<DialogSegment> getPlayerDialogByName(String pointer) {
		DialogSegment d = playerOptions.get(pointer);
		if(d != null) {
			if(d.conditionMatched()) {
				return Some(d);
			}
		}
		return None();
	}

	public Option<DialogSegment> getDialogByName(DialogSegment dSeg, String pointer) {
		if(dSeg instanceof NPCDialigSegment) {
			return getPlayerDialogByName(pointer);
		}else if(dSeg instanceof PlayerDialogSegment) {
			return getNPCDialogByName(pointer);
		}
		return None();
	}

	public ArrayList<String> getAnswerStrings(DialogSegment segment) {
		ArrayList<String> answerStrings = new ArrayList<>();
		if(segment instanceof NPCDialigSegment) {
			for (String s : ((NPCDialigSegment) segment).getPointer()) {
				Option<DialogSegment> dOpt = getPlayerDialogByName(s);
				if(dOpt.isSome()) {
					DialogSegment d = dOpt.unwrap();
					if(d.conditionMatched()) {
						answerStrings.add(d.getRawText());
					}
				}
			}
		}
		return answerStrings;
	}

	public String getNpcName() {
		return npcName;
	}
}
