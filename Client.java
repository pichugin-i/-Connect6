import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Scanner mgsServer;
        PrintWriter msgClient;

        int serverPort = 8080;
        String serverHost = "localhost";

        try (Socket s = new Socket(serverHost, serverPort)) {
            System.out.println("Подключение к серверу установлено!");

            mgsServer = new Scanner(s.getInputStream());
            msgClient = new PrintWriter(s.getOutputStream());

            new CycleForServer(new WindowGame(msgClient), mgsServer).start();

        } catch (SocketException e) {
            System.out.println("SocketException: " + e.getMessage()); // Проблема с сокетом
        }
          catch (IOException e) {
            System.out.println("IOException: " + e.getMessage()); // Проблема с вводом/выводом
        }
    }
}
