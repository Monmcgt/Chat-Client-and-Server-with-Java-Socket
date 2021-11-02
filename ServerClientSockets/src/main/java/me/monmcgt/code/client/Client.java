package me.monmcgt.code.client;

import me.monmcgt.code.exceptions.NameException;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private Scanner scanner;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private String username;

    public Client(Socket socket, String username) {
        try {
            if (this.checkName(username)) {
                throw new NameException();
            }
            this.socket = socket;
            this.scanner = new Scanner(System.in);
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.username = username;
        } catch (IOException e) {
            e.printStackTrace();
            this.close();
        } catch (NameException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void init() {
        this.listening();

        this.sendMessage(this.username, "has been connected.");

        while (this.socket.isConnected()) {
            String message = this.scanner.nextLine();
            this.sendMessage(this.username, message);
        }
    }

    private void listening() {
        Runnable runnable = () -> {
            try {
                String message;

                while (this.socket.isConnected()) {
                    message = this.bufferedReader.readLine();
                    System.out.println(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
                this.close();
            }
        };

        (new Thread(runnable)).start();
    }

    private void sendMessage(String message) {
        try {
            this.bufferedWriter.write(message);
            this.bufferedWriter.newLine();
            this.bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            this.close();
        }
    }

    private void sendMessage(String prefix, String message) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('[').append(prefix).append(']').append(" ").append(message);
        this.sendMessage(stringBuilder.toString());
    }

    private boolean checkName(String name) {
        String validCharacter = "abcdefchijklmnopqrstuvwxyz12345567890._-";

        for (int i = 0; i < name.length(); i++) {
            boolean var1 = false;
            for (int v = 0; v < validCharacter.length(); v++) {
                String cName = String.valueOf(name.charAt(i));
                if (cName.equalsIgnoreCase(String.valueOf(validCharacter.charAt(v)))) {
                    var1 = true;
                }
                if (var1) break;
            }
            if (var1) return false;
        }

        return true;
    }

    private void close() {
        try {
            if (this.socket != null) this.socket.close();
            if (this.bufferedWriter != null) this.bufferedWriter.close();
            if (this.bufferedReader != null) this.bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
