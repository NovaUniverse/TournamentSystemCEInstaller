package net.novauniverse.tournamentsystem.installer.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class DownloadUtils {
	public static void downloadFile(String fileUrl, String saveFilePath) throws IOException {
		URL url = new URL(fileUrl);
		ReadableByteChannel channel = Channels.newChannel(url.openStream());
		FileOutputStream outputStream = new FileOutputStream(saveFilePath);
		outputStream.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);
		outputStream.close();
		channel.close();
	}
}