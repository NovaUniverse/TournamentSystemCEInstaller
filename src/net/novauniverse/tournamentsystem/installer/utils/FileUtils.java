package net.novauniverse.tournamentsystem.installer.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {
	public static void writeToFile(File file, String data) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			writer.write(data);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}
}