package me.monmcgt.code.client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class runClient {
    private static String localhost = "localhost";

    public static void main(String[] args) {
        try {
            System.out.print("Enter your username: ");
            String username = (new Scanner(System.in)).nextLine();

            Socket socket = new Socket(localhost, 1337);

            Client client = new Client(socket, username);
            client.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
