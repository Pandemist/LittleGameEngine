package game.quests;

import game.GameState;
import game.quests.Dialogs.Dialog;
import game.quests.Dialogs.DialogSegment;
import game.quests.Dialogs.NPCDialigSegment;
import helper.Option;
import lang.LangManager;
import main.GameController;
import org.json.JSONObject;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

import static game.GameState.*;
import static helper.Option.*;
import static helper.ReadFromFile.getFiles;
import static helper.ReadFromFile.getJsonObject;

public class DialogManager {
	private GameController gc;

	public static final String DIALOG_MAIN_PATH = "interaction";
	public static final String DIALOG_EVENTS_PATH = "events";
	public static final String DIALOG_CONDITION_PATH = "conditions";
	public static final String DIALOG_DIALOGS_PATH = "dialogs";
	public static final String DIALOG_OBJECTIVES_PATH = "objectives";

	private Option<Dialog> currentDialog = None();
	private Option<DialogSegment> currentDialogSegment = None();

	private HashMap<String, Dialog> dialogListe = new HashMap<>();
	private HashMap<String, Event> eventList = new HashMap<>();
	private HashMap<String, Condition> conditionList = new HashMap<>();
	private HashMap<String, Objective> objectivList = new HashMap<>();

	public DialogManager(GameController gc) {
		this.gc = gc;
		loadEvents();
		loadConditions();
		loadObjectives();
		loadDialogs();
	}

	public void update() {
		boolean isInDialog = currentDialog.isSome() && currentDialogSegment.isSome();

		if(isInDialog) {
			Option<String> currentText = getTextOfCurrentSegment();
			if(currentText.isSome()) {
				gc.ui.dialogMenu.setDialogText(currentText.unwrap());
			}
			gc.ui.dialogMenu.setAnswerString(getAnswerStrings());
			gc.ui.dialogMenu.setTalker(getNPCNameTalkingTo());
		}

		if ((gc.getGs() == RUNNING_GAME) && isInDialog) {
			gc.setGs(IN_DIALOG);
		}
		if ((gc.getGs() == IN_DIALOG) && (!isInDialog)) {
			currentDialog = None();
			currentDialogSegment = None();
			gc.setGs(RUNNING_GAME);
		}
	}

	private void loadEvents() {
		try {
			URL url = LangManager.class.getResource("/" + DIALOG_MAIN_PATH + "/" + DIALOG_EVENTS_PATH);

			if (url != null) {
				String folderPath = Paths.get(url.toURI()).toString();
				File folder = new File(folderPath);

				if (folder.isDirectory()) {

					File[] files = folder.listFiles();

					if (files != null) {
						for (File f : files) {
							JSONObject node = getJsonObject(f);

							for (Iterator<String> it = node.keys(); it.hasNext(); ) {
								String eventName = it.next();
								JSONObject event = node.getJSONObject(eventName);
								eventList.put(eventName, Event.eventFabric(eventName, event));
							}
						}
					}
				}


			} else {
				// Wenn der Ordner nicht gefunden wird, gib eine Fehlermeldung aus
				throw new RuntimeException("Ordner nicht gefunden: " + DIALOG_MAIN_PATH + "/" + DIALOG_EVENTS_PATH);
			}
		} catch (Exception e) {
			throw new RuntimeException();
		}


		/*	ArrayList<File> files = getFiles(DialogManager.class, DIALOG_MAIN_PATH + "/" + DIALOG_EVENTS_PATH);
		for(File f : files) {
			JSONObject node = getJsonObject(f);

			for (Iterator<String> it = node.keys(); it.hasNext(); ) {
				String eventName = it.next();
				JSONObject event = node.getJSONObject(eventName);
				eventList.put(eventName, Event.eventFabric(eventName, event));
			}
		}*/
	}

	private void loadConditions() {
		try {
			URL url = LangManager.class.getResource("/" + DIALOG_MAIN_PATH + "/" + DIALOG_CONDITION_PATH);

			if (url != null) {
				String folderPath = Paths.get(url.toURI()).toString();
				File folder = new File(folderPath);

				if (folder.isDirectory()) {

					File[] files = folder.listFiles();

					if (files != null) {
						for (File f : files) {
							JSONObject node = getJsonObject(f);

							for (Iterator<String> it = node.keys(); it.hasNext(); ) {
								String conditionName = it.next();
								JSONObject condition = node.getJSONObject(conditionName);
								conditionList.put(conditionName, Condition.conditionFabric(conditionName, condition));
							}
						}
					}
				}


			} else {
				// Wenn der Ordner nicht gefunden wird, gib eine Fehlermeldung aus
				throw new RuntimeException("Ordner nicht gefunden: " + DIALOG_MAIN_PATH + "/" + DIALOG_CONDITION_PATH);
			}
		} catch (Exception e) {
			throw new RuntimeException();
		}

		/*ArrayList<File> files = getFiles(DialogManager.class, DIALOG_MAIN_PATH + "/" + DIALOG_CONDITION_PATH);
		for(File f : files) {
			JSONObject node = getJsonObject(f);

			for (Iterator<String> it = node.keys(); it.hasNext(); ) {
				String conditionName = it.next();
				JSONObject condition = node.getJSONObject(conditionName);
				conditionList.put(conditionName, Condition.conditionFabric(conditionName, condition));
			}
		}*/
	}

	private void loadObjectives() {
		try {
			URL url = LangManager.class.getResource("/" + DIALOG_MAIN_PATH + "/" + DIALOG_OBJECTIVES_PATH);

			if (url != null) {
				String folderPath = Paths.get(url.toURI()).toString();
				File folder = new File(folderPath);

				if (folder.isDirectory()) {

					File[] files = folder.listFiles();

					if (files != null) {
						for (File f : files) {
							JSONObject node = getJsonObject(f);

							for (Iterator<String> it = node.keys(); it.hasNext(); ) {
								String objectiveName = it.next();
								JSONObject objective = node.getJSONObject(objectiveName);
								objectivList.put(objectiveName, Objective.objectiveFabric(objectiveName, objective));
							}
						}
					}
				}


			} else {
				// Wenn der Ordner nicht gefunden wird, gib eine Fehlermeldung aus
				throw new RuntimeException("Ordner nicht gefunden: " + DIALOG_MAIN_PATH + "/" + DIALOG_OBJECTIVES_PATH);
			}
		} catch (Exception e) {
			throw new RuntimeException();
		}

	/*	ArrayList<File> files = getFiles(DialogManager.class, DIALOG_MAIN_PATH + "/" + DIALOG_OBJECTIVES_PATH);
		for(File f : files) {
			JSONObject node = getJsonObject(f);

			for (Iterator<String> it = node.keys(); it.hasNext(); ) {
				String objectiveName = it.next();
				JSONObject objective = node.getJSONObject(objectiveName);
				objectivList.put(objectiveName, Objective.objectiveFabric(objectiveName, objective));
			}
		}*/
	}

	private void loadDialogs() {
		try {
			URL url = LangManager.class.getResource("/" + DIALOG_MAIN_PATH + "/" + DIALOG_DIALOGS_PATH);

			if (url != null) {
				String folderPath = Paths.get(url.toURI()).toString();
				File folder = new File(folderPath);

				if (folder.isDirectory()) {

					File[] files = folder.listFiles();

					if (files != null) {
						for (File f : files) {
							JSONObject node = getJsonObject(f);

							String npcId = node.getString("npc_id");

							dialogListe.put(npcId, new Dialog(this, npcId, node));
						}
					}
				}


			} else {
				// Wenn der Ordner nicht gefunden wird, gib eine Fehlermeldung aus
				throw new RuntimeException("Ordner nicht gefunden: " + DIALOG_MAIN_PATH + "/" + DIALOG_DIALOGS_PATH);
			}
		} catch (Exception e) {
			throw new RuntimeException();
		}


	/*	ArrayList<File> files = getFiles(DialogManager.class, DIALOG_MAIN_PATH + "/" + DIALOG_DIALOGS_PATH);
		for(File f : files) {
			JSONObject node = getJsonObject(f);

			String npcId = node.getString("npc_id");

			dialogListe.put(npcId, new Dialog(this, npcId, node));
		}*/
	}

	public Option<String> getTextOfCurrentSegment() {
		if(currentDialogSegment.isSome()) {
			return Some(currentDialogSegment.unwrap().getRawText());
		}
		return None();
	}

	public ArrayList<String> getAnswerStrings() {
		if(currentDialogSegment.isSome() && currentDialog.isSome()) {
			return currentDialog.unwrap().getAnswerStrings(currentDialogSegment.unwrap());
		}
		return null;
	}

	public void startConversation(String npcName) {
		if(currentDialogSegment.isNone()) {
			Option<Dialog> dialogOpt = getDialigByName(npcName);
			if(dialogOpt.isSome()) {
				currentDialog = dialogOpt;
				currentDialogSegment = currentDialog.unwrap().getStartSegment();
			}
		}
	}

	public void endConversation() {
		currentDialog = None();
		currentDialogSegment = None();
	}

	public void forwardDialog(int variant) {
		if(currentDialogSegment.isSome() && currentDialog.isSome()) {
			DialogSegment dSeg = currentDialogSegment.unwrap();
			dSeg.execEvent();
			Option<String> nextSegment = dSeg.getNextSegmentPointer(variant);
			if(nextSegment.isSome()) {
				currentDialogSegment = currentDialog.unwrap().getDialogByName(dSeg, nextSegment.unwrap());
			}
		}
	}

	public String getNPCNameTalkingTo() {
		if(currentDialog.isSome() && currentDialogSegment.isSome()) {
			if(currentDialogSegment.unwrap() instanceof NPCDialigSegment) {
				return currentDialog.unwrap().getNpcName();
			}else{
				return gc.world.entityManager.player.getName();
			}

		}
		return "";
	}

	private Option<Dialog> getDialigByName(String npcName) {
		if(dialogListe.containsKey(npcName)) {
			return Some(dialogListe.get(npcName));
		}
		return None();
	}

	public Option<Event> getEvent(String rawEvent) {
		if(eventList.containsKey(rawEvent)) {
			return Some(eventList.get(rawEvent));
		}
		return None();
	}

	public Option<Condition> getCondition(String rawCon) {
		if(conditionList.containsKey(rawCon)) {
			return Some(conditionList.get(rawCon));
		}
		return None();
	}
}
