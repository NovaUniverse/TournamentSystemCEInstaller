package net.novauniverse.tournamentsystem.installer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.JSONArray;

import net.novauniverse.tournamentsystem.installer.config.InstallerConfig;
import net.novauniverse.tournamentsystem.installer.utils.DockerUtils;
import net.novauniverse.tournamentsystem.installer.utils.DownloadUtils;
import net.novauniverse.tournamentsystem.installer.utils.FileUtils;
import net.novauniverse.tournamentsystem.installer.utils.HttpUtils;
import net.novauniverse.tournamentsystem.installer.utils.RandomUtils;

public class TournamentInstaller {
	public static void main(String[] args) {
		System.out.println("Checking requirements...");
		if (!DockerUtils.isDockerInstalled()) {
			System.err.println("Docker is not installed on this system. Please install it from https://www.docker.com/get-started/");
			System.exit(1);
		}

		if (!DockerUtils.isDockerRunning()) {
			System.err.println("Docker is not running. Please start docker before trying to install");
			System.exit(1);
		}

		System.out.println("Fetching version list...");
		JSONArray versions = null;
		try {
			String versionDataString = HttpUtils.get(InstallerConfig.VERSION_MANIFEST_URL);
			if (versionDataString == null) {
				System.exit(1);
			}
			versions = new JSONArray(versionDataString);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("An error occued while fetching version info");
			System.exit(1);
		}

		String[] versionStringArray = new String[versions.length()];
		for (int i = 0; i < versions.length(); i++) {
			versionStringArray[i] = versions.getString(i);
		}

		if (args.length == 0) {
			System.err.println("Please provide what version you want to download. Valid versions are: " + String.join(", ", versionStringArray));
			System.exit(1);
		}

		String selectedVersion = args[0];
		boolean isValid = false;
		for (int i = 0; i < versions.length(); i++) {
			if (selectedVersion.equals(versions.get(i))) {
				isValid = true;
				break;
			}
		}

		if (!isValid) {
			System.err.println("Invalid version: " + selectedVersion + ". Valid ones are: " + String.join(", ", versionStringArray));
			System.exit(1);
		}

		File dockerFile = new File("docker-compose.yml");
		String versionUrl = InstallerConfig.DOCKER_COMPOSE_URL.replace("%version%", selectedVersion);
		if (!dockerFile.exists()) {
			System.out.println("Trying to download docker-conpose.yml from " + versionUrl);
			try {
				DownloadUtils.downloadFile(versionUrl, dockerFile.getAbsolutePath());
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Download failed");
				System.exit(1);
			}
		}

		File dataFolder = new File("data");
		if (!dataFolder.exists()) {
			System.out.println("Creating data folder");
			dataFolder.mkdir();
		}

		File envFile = new File(".env");
		if (!envFile.exists()) {
			String envData = "";
			String dbPassword = RandomUtils.generateHexString(64);

			envData += "DATA_DIRECTORY=\"" + dataFolder.getAbsolutePath().replace("\\", "/") + "\"\n";
			envData += "DB_PASSWORD=\"" + dbPassword + "\"\n";
			envData += "DB_DATABASE=\"minecraft_tournament\"\n";
			envData += "OFFLINE_MODE=\"false\"";

			try {
				FileUtils.writeToFile(envFile, envData);
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("Failed to create .env file");
				System.exit(1);
			}
		}

		File runFile = new File("./run.bat");
		if (!runFile.exists()) {
			try {
				FileUtils.writeToFile(runFile, "@echo off\ndocker compose up\npause");
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("Failed to create run.bat");
				System.exit(1);
			}
		}

		File stopFile = new File("./stop.bat");
		if (!stopFile.exists()) {
			try {
				FileUtils.writeToFile(stopFile, "@echo off\ndocker compose down");
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("Failed to create stop.bat");
				System.exit(1);
			}
		}

		File updateFile = new File("./update.bat");
		if (!updateFile.exists()) {
			try {
				FileUtils.writeToFile(updateFile, "@echo off\ndocker compose pulkl\npause");
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("Failed to create update.bat");
				System.exit(1);
			}
		}

		System.out.println("Downloading tournament data. This might take several minutes");
		try {
			String[] command = { "docker", "compose", "pull" };
			Process process = new ProcessBuilder(command)
					.redirectErrorStream(true)
					.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
			int exitCode = process.waitFor();
			System.out.println("Docker Compose pull exited with code " + exitCode);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Installation complete. Start run.bat to start the tournament server");
	}
}
