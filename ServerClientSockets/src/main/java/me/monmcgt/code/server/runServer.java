package me.monmcgt.code.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Arrays;

public class runServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1337);
        ServerHandler serverHandler = new ServerHandler(serverSocket);

        CreateFile.init();

        serverHandler.runServer();
    }
}
