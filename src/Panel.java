import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.Arrays;
import java.util.Random;

public class Panel extends JPanel implements KeyListener {
    double cellSize = 42.5;
    int WIDTH;
    int HEIGHT;
    boolean[][] gameInfo;
    boolean[][] ghostGameInfo;
    Random random = new Random();
    String[] typesOfPieces = {"Line", "Square", "Sblock", "RSblock", "Tblock"};

    Panel(int width, int height) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.gameInfo = new boolean[(int) (height / cellSize)][(int) (width / cellSize)];
        this.ghostGameInfo = new boolean[(int) (height / cellSize)][(int) (width / cellSize)];
        spawnPiece();

        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);
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
                if (gameInfo[i][j] || ghostGameInfo[i][j]) {
                    graphics.fillRect((int) (j * cellSize + 5), (int) (i * cellSize + 5), (int) (cellSize - 10), (int) (cellSize - 10));
                }
            }
        }
    }

    private void spawnPiece() {
        TetrisPiece piece = new TetrisPiece(typesOfPieces[random.nextInt(typesOfPieces.length)]);
        int k = checkEmptyRows(piece.tetrisPieceSpace);
        for (int i = 0; i + k < piece.tetrisPieceSpace.length; i++) {
            for (int j = 0; j < piece.tetrisPieceSpace.length; j++) {
                ghostGameInfo[i][j + 3] = piece.tetrisPieceSpace[i + k][j];
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

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        // Handle key press events
        if (keyCode == KeyEvent.VK_LEFT) {
            if (checkEmptyColumn(0)) {
                for (int i = 0; i < ghostGameInfo.length; i++) {
                    for (int j = 0; j < ghostGameInfo[i].length - 1; j++) {
                        ghostGameInfo[i][j] = ghostGameInfo[i][j + 1];
                    }
                    ghostGameInfo[i][ghostGameInfo[i].length - 1] = false;
                }
            }
            repaint();

        } else if (keyCode == KeyEvent.VK_RIGHT) {
            if (checkEmptyColumn(ghostGameInfo[0].length - 1)) {
                for (int i = ghostGameInfo.length - 1; i >= 0; i--) {
                    for (int j = ghostGameInfo[i].length - 1; j >= 1; j--) {
                        ghostGameInfo[i][j] = ghostGameInfo[i][j - 1];
                    }
                    ghostGameInfo[i][0] = false;
                }
            }
            repaint();
        } else if (keyCode == KeyEvent.VK_DOWN) {
            for (int i = ghostGameInfo.length - 1; i >= 1; i--) {
                for (int j = 0; j <ghostGameInfo[i].length; j++) {
                    ghostGameInfo[i][j] = ghostGameInfo[i-1][j];
                }

            }
            Arrays.fill(ghostGameInfo[0], false);
            repaint();
            // Move the current piece down
        } else if (keyCode == KeyEvent.VK_UP) {
            for (int i = 0; i < ghostGameInfo.length; i++) {
                for (int j = i + 1; j < ghostGameInfo[i].length; j++) {
                    boolean holder = ghostGameInfo[i][j];
                    ghostGameInfo[i][j] = ghostGameInfo[j][i];
                    ghostGameInfo[j][i] = holder;
                }
            }
            for (int i = 0; i < ghostGameInfo.length; i++) {
                for (int j = 0; j < ghostGameInfo[i].length / 2; j++) {
                    boolean holder = ghostGameInfo[i][j];
                    ghostGameInfo[i][j] = ghostGameInfo[i][ghostGameInfo[i].length - j - 1];
                    ghostGameInfo[i][ghostGameInfo[i].length - j - 1] = holder;
                }
            }
            repaint();
        }
    }

    private boolean checkEmptyColumn(int column) {
        for (int i = 0; i < ghostGameInfo.length; i++) {
            if (ghostGameInfo[i][column]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
