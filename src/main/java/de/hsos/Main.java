package de.hsos;

import io.quarkus.runtime.annotations.QuarkusMain;
import io.quarkus.runtime.Quarkus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@QuarkusMain
public class Main {

    public static void main(String ... args) {
        // ki generiert
        try{
            ProcessBuilder processBuilder = new ProcessBuilder("docker-compose", "-f", "src/main/resources/keycloak/keycloak.yaml", "up");
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            Quarkus.run(args);

            // Read and print the output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        // ende ki generiert
    }
}