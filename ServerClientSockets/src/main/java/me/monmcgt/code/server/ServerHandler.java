package me.monmcgt.code.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerHandler {
    private ServerSocket serverSocket;

    public ServerHandler(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void runServer() {
        boolean run = true;
        try {
            do {
                Socket s = this.serverSocket.accept();
                System.out.println("A new client has been connected.");
                ClientHandler clientHandler = new ClientHandler(s);

                Thread t = new Thread(clientHandler);
                t.start();
            } while (run);
        } catch (IOException e) {
            System.out.println("exception at runServer.class");
            e.printStackTrace();
            run = false;
            this.shutdownServer();
        }
    }

    public void shutdownServer() {
        if (!this.serverSocket.isClosed()) {
            try {
                this.serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
