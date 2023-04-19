import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientConnection extends Thread {
    Socket s;
    PrintWriter msgServer;
    Scanner mgsClient;

    public ClientConnection(Socket aClientSocket) {
        try {
            s = aClientSocket;
            msgServer = new PrintWriter(s.getOutputStream());
            mgsClient = new Scanner(s.getInputStream());
        }
        catch (IOException e) {
            System.out.println("TCP ClientConnection:"+ e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (mgsClient.hasNext()) {
                    String clientMessage = mgsClient.nextLine();
                    Server.sendMessageToAllClients(clientMessage);
                }
                Thread.sleep(100);
            }
        }
        catch (InterruptedException e) {
            System.out.println("InterruptedException:"+ e.getMessage());
        }
    }

    public void sendMessageToClient(String msg) {
        try {
            msgServer.println(msg);
            msgServer.flush();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
