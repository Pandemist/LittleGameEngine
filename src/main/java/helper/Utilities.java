package helper;

import game.Rendering.RenderEntry;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Base64;
import java.util.zip.DataFormatException;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;

public class Utilities {
	public static byte[] base64Decoder(String text) {
		return Base64.getDecoder().decode(text);
	}

	public static byte[] csvDecoder(String encoded) {
		String[] byteStrings = encoded.split(",");
		byte[] bytes = new byte[byteStrings.length];

		for (int i = 0; i < byteStrings.length; i++) {
			bytes[i] = Byte.parseByte(byteStrings[i]);
		}

		return bytes;
	}

	public static byte[] gzipDecompress(byte[] compressedData) {
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(compressedData);
			GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = gzipInputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}

			gzipInputStream.close();
			outputStream.close();

			return outputStream.toByteArray();
		} catch (IOException e) {
			// Fehler beim Dekomprimieren
			e.printStackTrace();
			return compressedData; // oder eine andere angemessene Behandlung des Fehlers
		}
	}

	public static byte[] lzipDecompress(byte[] compressedData) {
		try {
			Inflater inflater = new Inflater();
			inflater.setInput(compressedData);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];

			while (!inflater.finished()) {
				int count = inflater.inflate(buffer);
				outputStream.write(buffer, 0, count);
			}

			outputStream.close();
			inflater.end();

			return outputStream.toByteArray();
		} catch (DataFormatException | IOException e) {
			// Fehler beim Dekomprimieren
			e.printStackTrace();
			return compressedData; // oder eine andere angemessene Behandlung des Fehlers
		}
	}

	public static String getPathOfSource(String source) {
		String path = "";
		int lastSlashIndex = source.lastIndexOf('/');
		if (lastSlashIndex >= 0) {
			path = source.substring(0, lastSlashIndex);
		}else {
			path = source;
		}
		return path;
	}

	public static String getFileOfSource(String source) {
		return source.split("/")[source.split("/").length-1];
	}

	public static double divideSafely(double dividend, double divisor, double alternate) {
		if (divisor == 0) {
			return alternate;
		}

		return dividend / divisor;
	}

	public static Point getFontBox(String text, Font f) {
		BufferedImage tempImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = tempImage.createGraphics();

		// Set the font to the desired font
		g2d.setFont(f);

		// Get FontMetrics to measure text dimensions
		FontMetrics fontMetrics = g2d.getFontMetrics();

		// Calculate the width and height of the text
		int width = fontMetrics.stringWidth(text);
		int height = fontMetrics.getHeight();

		g2d.dispose();

		return new Point(width, height);

	//	int x = (screen.x - width) / 2;
	//	int y = (screen.y - height) / 2 + fontMetrics.getAscent();

	//	return new Point(x, y);
	}

	public static List<String> splitLineByWidth(String paragraph, Font font, int maxWidth) {
		List<String> result = new ArrayList<>();
		String[] words = paragraph.split(" ");

		StringBuilder currentLine = new StringBuilder();
		FontMetrics fontMetrics = new Canvas().getFontMetrics(font);

		for (String word : words) {
			if (fontMetrics.stringWidth(currentLine.toString() + " " + word) <= maxWidth) {
				if (currentLine.length() > 0) {
					currentLine.append(" ");
				}
				currentLine.append(word);
			} else {
				if (currentLine.length() > 0) {
					result.add(currentLine.toString());
					currentLine = new StringBuilder(word);
				} else {
					// Handle the case where a single word is longer than maxWidth
					int startIndex = 0;
					int endIndex = 0;
					while (endIndex < word.length()) {
						if (fontMetrics.stringWidth(word.substring(startIndex, endIndex + 1)) <= maxWidth) {
							endIndex++;
						} else {
							result.add(word.substring(startIndex, endIndex));
							startIndex = endIndex;
						}
					}
					if (startIndex < word.length()) {
						result.add(word.substring(startIndex));
					}
				}
			}
		}

		if (currentLine.length() > 0) {
			result.add(currentLine.toString());
		}

		return result;
	}

	public static String fillStringleftWithChar(String input, char c, int length) {
		if(input.length() >= length) {
			return input;
		}

		StringBuilder inputBuilder = new StringBuilder(input);
		while(inputBuilder.length() < length) {
			inputBuilder.insert(0, c);
		}
		return inputBuilder.toString();
	}

	public static void drawLightsourceOnLightmap(BufferedImage map, BufferedImage lightSource) {
		int lightX = map.getWidth();
		int lightY = map.getHeight();

		for (int x = 0; x < lightX; x++) {
			for (int y = 0; y < lightY; y++) {
				int pixelOnLight = lightSource.getRGB(x, y);
				int pixelOnMap = map.getRGB(x, y);

				if(pixelOnLight != 0) {
					int mapRed = (pixelOnMap >> 16) & 0xFF;
					int mapGreen = (pixelOnMap >> 8) & 0xFF;
					int mapBlue = (pixelOnMap) & 0xFF;
					int mapAlpha = (pixelOnMap >> 24) & 0xFF;

					int lightRed = (pixelOnLight >> 16) & 0xFF;
					int lightGreen = (pixelOnLight >> 8) & 0xFF;
					int lightBlue = (pixelOnLight) & 0xFF;
					int lightAlpha = (pixelOnLight >> 24) & 0xFF;

				//	if((x == 0) && (y == 0)) {
				//		System.out.println("Red: " + lightRed + " Green: " + lightGreen + " Blue: " + lightBlue + " Alpha: " + lightAlpha);
				//	}

					int newRed = Math.max(mapRed, lightRed);
					int newGreen = Math.max(mapGreen, lightGreen);
					int newBlue = Math.max(mapBlue, lightBlue);
				//	int newAlpha = Math.min(mapAlpha, lightAlpha);
				//	int newAlpha = lightAlpha;

				//	int newRed = Math.min(255, mapRed + lightRed);
				//	int newGreen = Math.min(255, mapGreen + lightGreen);
				//	int newBlue = Math.min(255, mapBlue + lightBlue);
					int newAlpha = Math.max(0, mapAlpha + (lightAlpha));


					newRed = (mapRed * (1-(lightAlpha/255))) + (lightRed * (lightAlpha / 255));
					newGreen = (mapGreen * (1-(lightAlpha/255))) + (lightGreen * (lightAlpha / 255));
					newBlue = (mapBlue * (1-(lightAlpha/255))) + (lightBlue * (lightAlpha / 255));

					newRed = Math.min(255, lightRed + (mapRed * (lightAlpha/255)));
					newGreen = Math.min(255, lightGreen + (mapGreen * (lightAlpha/255)));
					newBlue = Math.min(255, lightBlue + (mapBlue * (lightAlpha/255)));

					newAlpha = Math.min(255, Math.max(0, ((255 - mapAlpha) * (lightAlpha)) + (mapAlpha * (255 - lightAlpha))));

					newRed = mapRed;
					newGreen = mapGreen;
					newBlue = mapBlue;

				//	newRed = lightRed;
				//	newGreen = lightGreen;
				//	newBlue = lightBlue;

					newAlpha = Math.min(mapAlpha, Math.max(0, 255 - lightAlpha));
				//			Math.max(mapAlpha * (255 - lightAlpha), lightAlpha)));

					newRed = Math.min(255, Math.max(0, (mapRed * (newAlpha/255)) + (lightRed * (lightAlpha/255))));
					newGreen = Math.min(255, Math.max(0, (mapGreen * (newAlpha/255)) + (lightGreen * (lightAlpha/255))));
					newBlue = Math.min(255, Math.max(0, (mapBlue * (newAlpha/255)) + (lightBlue * (lightAlpha/255))));

					newRed = lightRed * (lightAlpha/255);
					newGreen = lightGreen * (lightAlpha/255);
					newBlue = lightBlue * (lightAlpha/255);

					int newPixel = (newAlpha << 24) | (newRed << 16) | (newGreen << 8) | newBlue;

				//	newPixel = (newAlpha << 24) | (newRed << 16) | (newGreen << 8) | newBlue;
				//	newPixel = (newAlpha << 24) | (newAlpha << 16) | (newAlpha << 8) | newAlpha;

				//	System.out.println("Alpha: " + newAlpha + " Pixel: " + Integer.toHexString(newPixel));

					map.setRGB(x, y, newPixel);
				}
			}
		}
	}

	public static BufferedImage multiplyImages(BufferedImage image1, BufferedImage image2) {
		int width = image1.getWidth();
		int height = image1.getHeight();

		BufferedImage resultImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int pixel1 = image1.getRGB(x, y);
				int pixel2 = image2.getRGB(x, y);

				int alpha1 = (pixel1 >> 24) & 0xFF;
				int red1 = (pixel1 >> 16) & 0xFF;
				int green1 = (pixel1 >> 8) & 0xFF;
				int blue1 = pixel1 & 0xFF;

				int alpha2 = (pixel2 >> 24) & 0xFF;
				int red2 = (pixel2 >> 16) & 0xFF;
				int green2 = (pixel2 >> 8) & 0xFF;
				int blue2 = pixel2 & 0xFF;

				int newAlpha = alpha1 * alpha2 / 255;
				int newRed = (red1 * red2) / 255;
				int newGreen = (green1 * green2) / 255;
				int newBlue = (blue1 * blue2) / 255;

				int newPixel = (newAlpha << 24) | (newRed << 16) | (newGreen << 8) | newBlue;

				resultImage.setRGB(x, y, newPixel);
			}
		}

		return resultImage;
	}

	public static String getIngameHours(double time) {
		return fillStringleftWithChar(String.valueOf((int) (time * 24)), '0', 2);
	}

	public static String getIngameMinutes(double time) {
		return fillStringleftWithChar(String.valueOf((int) (time * 24 * 60) % 60), '0', 2);
	}

	public static String getIngameSeconds(double time) {
		return fillStringleftWithChar(String.valueOf((int) (time * 24 * 60 * 60) % 60), '0', 2);
	}

	public static String getWeekDayShort(int day) {
		switch (day) {
			case 0: {
				return "Montag";
			}
			case 1: {
				return "Dienstag";
			}
			case 2: {
				return "Mittwoch";
			}
			case 3: {
				return "Donnerstag";
			}
			case 4: {
				return "Freitag";
			}
			case 5: {
				return "Samstag";
			}
			case 6: {
				return "Sonntag";
			}
		}
		return "Montag";
	}
}

