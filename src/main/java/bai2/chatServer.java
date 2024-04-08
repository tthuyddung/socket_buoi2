package bai2;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class chatServer {
    private static List<PrintWriter> clients = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Server is running and waiting for connections...");

            Thread serverThread = new Thread(() -> {
                try {
                    while (true) {
                        Socket clientSocket = serverSocket.accept();
                        System.out.println("A new client has connected.");

                        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                        clients.add(writer);

                        Thread clientThread = new Thread(new ClientHandler(clientSocket, writer));
                        clientThread.start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            serverThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private PrintWriter writer;

        public ClientHandler(Socket clientSocket, PrintWriter writer) {
            this.clientSocket = clientSocket;
            this.writer = writer;
        }

        @Override
        public void run() {
            try {
                Scanner scanner = new Scanner(clientSocket.getInputStream());
                String username = scanner.nextLine();
                broadcast(username + " has joined the chat.");

                String clientMessage;
                do {
                    clientMessage = scanner.nextLine();
                    broadcast(username + ": " + clientMessage);
                } while (!clientMessage.equals("bye"));

                clients.remove(writer);
                broadcast(username + " has left the chat.");
                writer.close();
                scanner.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private static void broadcast(String message) {
        for (PrintWriter client : clients) {
            client.println(message);
            client.flush();
        }
        System.out.println(message); // Hiển thị tin nhắn trên phía máy chủ
    }



}
