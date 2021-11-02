package me.monmcgt.code.server;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CreateFile {
    public static void init() {
        try {
            File file = new File("./run.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            if (!file.exists()) file.createNewFile();
            String ip = getIp();
            bufferedWriter.write(ip);
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getIp() {
        try {
            URL url = new URL("https://api.ipify.org/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String ip = bufferedReader.readLine();
            bufferedReader.close();
            connection.disconnect();
            return ip;
        } catch (MalformedURLException e) {
            return "error";
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }
}
