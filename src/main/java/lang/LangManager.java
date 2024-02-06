package lang;


import game.entities.EntityManager;
import game.level.GameMap;
import helper.ReadFromFile;

import java.io.*;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.nio.file.Paths;

public class LangManager {
	public static final String LANG_PATH = "lang";
	public static final String LANG_EXT = "lang";


	private HashMap<String, String> langDirectory = new HashMap<String, String>();

	private void ReadFileOfLanguage(InputStream file) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split("=");
				if (parts.length == 2) {
					String key = parts[0].trim();
					String value = parts[1].trim();
					langDirectory.put(key, value);
				}
			}
		}catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void PrepareLanguageDirectory(Languages lang) {
		langDirectory.clear();

		try {
			URL url = LangManager.class.getResource("/" + LANG_PATH);

			if (url != null) {
				// Konvertiere die URL in einen Pfad
				String folderPath = Paths.get(url.toURI()).toString();

				File folder = new File(folderPath);

				if (folder.isDirectory()) {
					// Auflisten aller Dateien im Ordner
					File[] dateien = folder.listFiles();

					// Iterieren über die Dateien und sie ausgeben (oder weiterverarbeiten)
					if (dateien != null) {
						for (File datei : dateien) {
							if (datei.isFile() && datei.getName().contains(lang.toString()) && datei.getName().endsWith("."+LANG_EXT)) {

								ReadFileOfLanguage(new FileInputStream(datei));
							}
						}
					}
				}
			} else {
				// Wenn der Ordner nicht gefunden wird, gib eine Fehlermeldung aus
				throw new RuntimeException("Ordner nicht gefunden: " + LANG_PATH);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getDictEntryByName(String langEntry) {
		if(langDirectory.containsKey(langEntry)) {
			return langDirectory.get(langEntry);
		}else{
			return "<" + langEntry + ">";
		}
	}
}
