import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintWriter;

public class Field extends JPanel {
    private final GameLogic game;
    private final PrintWriter msgClient;
    private final int FIELD_WIDTH = 480;
    private final int cellSize = FIELD_WIDTH / 19;

    private void sendMessageToServer(String msg) {
        try {
            msgClient.println(msg);
            msgClient.flush();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Field(GameLogic game, PrintWriter msgClient) {
        setVisible(false); // устанавливает видимость поля
        this.game = game;
        this.msgClient = msgClient;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int x, y;

                x = (e.getX() - 10 + cellSize / 2) / cellSize;
                y = (e.getY() - 10 + cellSize / 2) / cellSize;

                int cx = e.getX() - 10 - x * cellSize;
                int cy = e.getY() - 10 - y * cellSize;

                int rx = Math.round((float)cx / (float)cellSize);
                int ry = Math.round((float)cy / (float)cellSize);

                if (cx - rx * cellSize > cellSize / 2) {x = x + 1;}
                if (cy - ry * cellSize > cellSize / 2) {y = y + 1;}

                sendMessageToServer(x + "/" + y + "/" + game.getPlayerNum());
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(new Color(255, 255, 255));
        g.fillRect(10, 10, FIELD_WIDTH, FIELD_WIDTH); // заполняем фон белым

        // Рисование разделительных линий
        for (int i = 0; i < 19; i++) {
            g.setColor(Color.BLACK);
            g.drawLine(10,
                    10 + i * cellSize,
                    10 + FIELD_WIDTH,
                    10 + i * cellSize);
            g.drawLine(10 + i * cellSize,
                    10,
                    10 + i * cellSize,
                    10 + FIELD_WIDTH);
        }

        // Отображение ходов
        for (int x = 0; x < 19; x++) {
            for (int y = 0; y < 19; y++) {
                if (!game.getCellMarker(x,y).equals(GameLogic.CurrentStatusCells.free)) {
                    int cellX = 10 + x * cellSize;
                    int cellY = 10 + y * cellSize;

                    if (game.getCellMarker(x,y).equals(GameLogic.CurrentStatusCells.player1)) {
                        g.setColor(Color.BLACK);
                        g.fillOval(cellX - cellSize / 2, cellY - cellSize / 2,
                                cellSize, cellSize);
                    }

                    if (game.getCellMarker(x,y).equals(GameLogic.CurrentStatusCells.player2)) {
                        g.setColor(new Color(211, 211, 211));
                        g.fillOval(cellX - cellSize / 2, cellY - cellSize / 2,
                                cellSize, cellSize);
                    }
                }
            }
        }

// Вывеска победы
        if (game.getGameOver()) {
            g.setColor(new Color(14, 131, 66));
            int lineHeight = FIELD_WIDTH / 5;
            g.fillRect(10, 10 + FIELD_WIDTH / 2 - lineHeight / 2, FIELD_WIDTH, lineHeight);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Times New Roman", Font.BOLD, 12));
            String gom = game.getGameOverMessage();
            g.drawString(gom, 10 + FIELD_WIDTH / 4 + 30, 10 + FIELD_WIDTH / 2 + gom.length() / 2 + 10);

            // цвет кружка в зависимости от победителя
            if (game.checkWin(GameLogic.CurrentStatusCells.player1)) {
                g.setColor(Color.BLACK);
            } else if (game.checkWin(GameLogic.CurrentStatusCells.player2)) {
                g.setColor(new Color(211, 211, 211));
            }

            // Рисование кружка
            int circleX = 10 + FIELD_WIDTH / 2 - cellSize / 2;
            int circleY = 10 + FIELD_WIDTH / 2 - lineHeight / 2 + lineHeight / 4 - cellSize / 2;
            g.fillOval(circleX, circleY, cellSize, cellSize);
        }
    }
}
