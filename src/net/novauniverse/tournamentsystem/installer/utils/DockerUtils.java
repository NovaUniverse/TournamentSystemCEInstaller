package net.novauniverse.tournamentsystem.installer.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DockerUtils {
    public static boolean isDockerInstalled() {
        try {
            Process process = Runtime.getRuntime().exec("docker --version");
            process.waitFor();
            return process.exitValue() == 0;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }

    public static boolean isDockerRunning() {
        try {
            Process process = Runtime.getRuntime().exec("docker info");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Server Version:")) {
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            return false;
        }
    }
}