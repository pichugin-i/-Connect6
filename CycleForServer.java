import java.util.Scanner;

public class CycleForServer {
    private WindowGame gameWindow;
    private Scanner msgServer;

    public CycleForServer(WindowGame gameWindow, Scanner msgServer) {
        this.gameWindow = gameWindow;
        this.msgServer = msgServer;
    }

    public void start() {
        while (true) {
            if (msgServer.hasNext()) {
                String commandFromServer = msgServer.nextLine();
                switch (commandFromServer) {
                    case "Player1":
                        gameWindow.setPlayerFirst(true);
                        break;
                    case "Player2":
                        gameWindow.setPlayerFirst(false);
                        break;
                    case "start":
                        gameWindow.startGame();
                        break;
                    default:
                        String[] data = commandFromServer.split("/");
                        int x = Integer.parseInt(data[0]);
                        int y = Integer.parseInt(data[1]);
                        int playerNum = Integer.parseInt(data[2]);
                        gameWindow.makeMove(x, y, playerNum);
                        break;
                }
            }
        }
    }
}
