package settings;

import lang.Languages;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Settings {
	private final String optionFileName = "Settings.options";
	private InputStream file = null;
	private HashMap<String, String> options;
	private Languages gameLang;

	public Settings() {
		options = new HashMap<>();
		setDefaultOptions();

		ClassLoader classLoader = Settings.class.getClassLoader();
		URL resourceUrl = classLoader.getResource(optionFileName);

		if (resourceUrl == null) {
			throw new RuntimeException("Die Ressource '" + optionFileName + "' wurde nicht gefunden.");
		}

		try {
			file = resourceUrl.openStream();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		loadFromFile(file);

	}

	public void setOption(String key, String value) {
		options.put(key, value);
	}

	private void setDefaultOptions() {
		gameLang = Languages.GERMAN;
		setOption("LANGUAGE", "DE");
	}

	public void saveToFile(File file) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			for (Map.Entry<String, String> entry : options.entrySet()) {
				writer.write(entry.getKey() + "=" + entry.getValue());
				writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadFromFile(InputStream file) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(file))) {
			options.clear();
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split("=", 2);
				if (parts.length == 2) {
					String key = parts[0].trim();
					String value = parts[1].trim();
					options.put(key, value);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Languages getGameLang() {
		return Languages.getLangByValue(options.get("Languge"));
	}
}
