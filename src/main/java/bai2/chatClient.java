package bai2;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class chatClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 5000);
            System.out.println("Connected to the chat server.");

            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            Scanner scanner = new Scanner(socket.getInputStream());

            System.out.print("Enter your username: ");
            String username = new Scanner(System.in).nextLine();
            writer.println(username);
            writer.flush();

            Thread serverThread = new Thread(new ServerHandler(scanner));
            serverThread.start();

            String message;
            do {
                message = new Scanner(System.in).nextLine();
                writer.println(message);
                writer.flush();
            } while (!message.equals("bye"));

            writer.close();
            scanner.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ServerHandler implements Runnable {
        private Scanner scanner;

        public ServerHandler(Scanner scanner) {
            this.scanner = scanner;
        }

        @Override
        public void run() {
            while (scanner.hasNextLine()) {
                String message = scanner.nextLine();
                System.out.println(message);
            }
        }
    }
}
