import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Panel extends JPanel {
    double cellSize = 42.5;
    int WIDTH;
    int HEIGHT;
    boolean[][] gameInfo;
    Random random = new Random();
    String[] typesOfPieces = {"Line", "Square", "Sblock", "RSblock", "Tblock"};

    Panel(int width, int height) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.gameInfo = new boolean[(int) (height / cellSize)][(int) (width / cellSize)];
        spawnPiece();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
        drawBlocks(g);
    }

    public void drawGrid(Graphics graphics) {
        int i = 0;
        while (i < WIDTH / cellSize) {
            graphics.drawLine((int) (i * cellSize), 0, (int) (i * cellSize), HEIGHT);
            i++;
        }
        i = 0;
        graphics.drawLine(WIDTH - 1, 0, WIDTH - 1, HEIGHT);
        while (i < HEIGHT / cellSize) {
            graphics.drawLine(0, (int) (i * cellSize), WIDTH, (int) (i * cellSize));
            i++;
        }
        graphics.drawLine(0, HEIGHT - 1, WIDTH, HEIGHT - 1);

    }

    private void drawBlocks(Graphics graphics) {
        for (int i = 0; i < gameInfo.length; i++) {
            for (int j = 0; j < gameInfo[i].length; j++) {
                if (gameInfo[i][j]) {
                    graphics.fillRect((int) (j * cellSize + 5), (int) (i * cellSize + 5), (int) (cellSize - 10), (int) (cellSize - 10));
                }
            }
        }
    }

    private void spawnPiece() {
        TetrisPiece piece = new TetrisPiece(typesOfPieces[random.nextInt(typesOfPieces.length)]);
        int k= checkEmptyRows(piece.tetrisPieceSpace);
        for (int i = 0; i + k < piece.tetrisPieceSpace.length; i++) {
            for (int j = 0; j < piece.tetrisPieceSpace.length; j++) {
                gameInfo[i][j + 3] = piece.tetrisPieceSpace[i + k][j];
            }
        }

    }

    private int checkEmptyRows(boolean[][] matrix) {
        int k = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j]) {
                    return k;
                }
            }
            k++;
        }
        return k;
    }

    public boolean[][] getGameInfo() {
        return gameInfo;
    }
}
