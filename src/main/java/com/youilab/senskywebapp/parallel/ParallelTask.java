package com.youilab.senskywebapp.parallel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ParallelTask implements Runnable{

    private String command, name;

    public ParallelTask(String command, String name) {
        this.command = command;
        this.name = name;
    }

    /**
     * Creates the command for the CMD and executes it.
     */
    @Override
    public void run() {
        System.out.println("[SenSky] Launching parallel task -> " + this.name);
        try {
            Process proc = new ProcessBuilder(this.command).start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

            String line = "";
            while((line = reader.readLine()) != null || (line = errorReader.readLine()) != null) {
                System.out.print(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("[SenSky] Cannot launch parallel task -> " + this.name);
        }

    }
}
