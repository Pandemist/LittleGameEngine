package helper;

import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;

public class ReadFromFile {
	public static InputStream getFile(Class<?> clazz, String path) {
		try {
			ClassLoader classLoader = clazz.getClassLoader();
			URL resourceUrl = classLoader.getResource(path);

			if (resourceUrl == null) {
				throw new RuntimeException("Die Ressource '" + path + "' wurde nicht gefunden.");
			}

			return resourceUrl.openStream();
		}catch (Exception e) {
			throw new RuntimeException("Die Resource " + path + " wurde nicht gefunden!");
		}
	}

	public static ArrayList<File> getFiles(Class<?> clazz, String path) {
		ArrayList<File> files = new ArrayList<>();
		try {
			ClassLoader classLoader = clazz.getClassLoader();
			Enumeration<URL> resources = classLoader.getResources(path);

			if (resources == null) {
				throw new RuntimeException("Die Ressource '" + path + "' wurde nicht gefunden.");
			}

			while (resources.hasMoreElements()) {
				URL folderUrl = resources.nextElement();
				File folder = new File(folderUrl.toExternalForm());

				if (folder.exists() && folder.isDirectory()) {

					File[] localFiles = folder.listFiles((dir, name) -> name.endsWith(".json"));

					files.addAll(Arrays.asList(localFiles));
				}
			}
		}catch (Exception e) {
			throw new RuntimeException("Die Ressource '" + path + "' wurde nicht gefunden.");
		}
		return files;
	}

	public static JSONObject getJsonObject(Class<?> clazz, String path) {
		try {
			InputStream file = getFile(clazz, path);

			assert file != null;
			BufferedReader reader = new BufferedReader(new InputStreamReader(file));

			// Lese den gesamten Inhalt der JSON-Datei
			StringBuilder contentBuilder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				contentBuilder.append(line);
			}
			reader.close();
			String content = contentBuilder.toString();

			return new JSONObject(content);
		}catch (Exception  e) {
			throw new RuntimeException("Die Ressource '" + path + "' wurde nicht gefunden.");
		}
	}

	public static JSONObject getJsonObject(File file) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));

			// Lese den gesamten Inhalt der JSON-Datei
			StringBuilder contentBuilder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				contentBuilder.append(line);
			}
			reader.close();
			String content = contentBuilder.toString();

			return new JSONObject(content);
		}catch (Exception  e) {
			throw new RuntimeException("Lesen von " + file.getName() + " schlug fehl.");
		}
	}

	public static InputStream getInputStream(Class<?> clazz, String path) {
		try {
			InputStream file = getFile(clazz, path);
			assert file != null;
			return file;
		}catch (Exception e) {
			throw new RuntimeException("Die Ressource '" + path + "' wurde nicht gefunden.");
		}
	}
}
