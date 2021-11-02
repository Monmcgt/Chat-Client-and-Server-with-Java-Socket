package me.monmcgt.code.server;

import me.monmcgt.code.storage.managers.ClientHandlersManager;
import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private String username;

    public ClientHandler(Socket socket) throws IOException {
        this.init(socket);
    }

    @Override
    public void run() {
        String message;

        while (socket.isConnected()) {
            try {
                message = this.bufferedReader.readLine();
                this.print(message);
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void init(Socket socket) throws IOException {
        this.socket = socket;
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.addClientHandlers();

        this.method1();
    }

    private void method1() throws IOException {
        String message = this.bufferedReader.readLine();
        StringBuilder author = new StringBuilder();
        StringBuilder content = new StringBuilder();
        boolean var1 = true;
        for (int i = 0; i < message.length(); i++) {
            if (var1) {
                if (message.charAt(i) == '[') continue;
                if (message.charAt(i) == ']') {
                    var1 = false;
                    continue;
                }
                author.append(message.charAt(i));
            } else {
                content.append(message.charAt(i));
            }
        }

        this.username = author.toString();
    }

    private void print(String message) {
        try {
            for (ClientHandler c: ClientHandlersManager.clientHandlers) {
                if (c.username.equals(this.username)) continue;
                c.bufferedWriter.write(message);
                c.bufferedWriter.newLine();
                c.bufferedWriter.flush();
            }
        } catch (IOException e) { }
    }

    private void print(String username, String message) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('[').append(username).append(']').append(" ").append(message);
        this.print(stringBuilder.toString());
    }

    private void addClientHandlers() { ClientHandlersManager.clientHandlers.add(this); }

    private void removeClientHandlers() {
        ClientHandlersManager.clientHandlers.remove(this);
        this.print("SERVER", this.username + " " + "has left the chat.");
    }

    private void close() {
        try {
            this.removeClientHandlers();
            if (this.socket != null) this.socket.close();
            if (this.bufferedWriter != null) this.bufferedWriter.close();
            if (this.bufferedReader != null) this.bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
