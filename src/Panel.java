import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class Panel extends JPanel implements KeyListener {
    double cellSize = 42.5;
    int WIDTH;
    int HEIGHT;
    boolean[][] gameInfo;
    boolean[][] ghostGameInfo;
    Random random = new Random();
    int count = 0;
    TetrisPiece piece;
    MoveDownController moveDownController;
    ArrayList<Integer> fullRows;
    String[] typesOfPieces = {"Line", "Square", "Sblock", "RSblock", "Tblock", "Lblock", "RLblock"};

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

    private ArrayList<Integer> checkFullRows() {
        ArrayList<Integer> clearableRows = new ArrayList<>();
        for (int i = 0; i < gameInfo.length; i++) {
            for (int j = 0; j < gameInfo[i].length; j++) {
                if (!gameInfo[i][j]) {
                    break;
                }
                if (j == gameInfo[i].length - 1) {
                    clearableRows.add(i);
                }
            }
        }
        return clearableRows;

    }

    private void spawnPiece() {
        if (piece != null) {
            piece.generate(typesOfPieces[random.nextInt(typesOfPieces.length)]);
        } else {
            piece = new TetrisPiece(typesOfPieces[random.nextInt(typesOfPieces.length)]);
        }
        int k = checkEmptyRows(piece.tetrisPieceSpace);
        boolean canSpawn = true;
        outerLoop:
        for (int i = 0; i + k < piece.tetrisPieceSpace.length; i++) {
            for (int j = 0; j < piece.tetrisPieceSpace.length; j++) {
                if (gameInfo[i][j + 3]) {
                    canSpawn = false;
                    break outerLoop;
                }
            }
        }
        if (canSpawn) {
            for (int i = 0; i + k < piece.tetrisPieceSpace.length; i++) {
                System.arraycopy(piece.tetrisPieceSpace[i + k], 0, ghostGameInfo[i], 3, piece.tetrisPieceSpace.length);
            }
        }

    }

    private void rotateMatrix() {
        int indexI = ghostGameInfo.length;
        int indexJ = ghostGameInfo[0].length;
        boolean[][] rotatableMatrix = new boolean[5][5];
        for (int i = 0; i < ghostGameInfo.length; i++) {
            for (int j = 0; j < ghostGameInfo[i].length; j++) {
                if (ghostGameInfo[i][j]) {
                    if (i < indexI) {
                        indexI = i;
                    }
                    if (j < indexJ) {
                        indexJ = j;
                    }
                }
            }
        }
        for (int i = 0; i < rotatableMatrix.length; i++) {
            if (indexI + i < ghostGameInfo.length)
                rotatableMatrix[i] = Arrays.copyOfRange(ghostGameInfo[indexI + i], indexJ, indexJ + 5);
            else
                Arrays.fill(rotatableMatrix[i], false);
        }
        //move matrix to the right
        boolean secondCellFilled = false;
        boolean secondRowFilled = false;

        while (!secondCellFilled) {

            for (int i = rotatableMatrix.length - 1; i >= 0; i--) {
                for (int j = rotatableMatrix[i].length - 1; j >= 1; j--) {
                    rotatableMatrix[i][j] = rotatableMatrix[i][j - 1];
                }
                rotatableMatrix[i][0] = false;
            }
            for (int i = rotatableMatrix.length - 1; i >= 0; i--) {
                if (rotatableMatrix[i][2]) {
                    secondCellFilled = true;
                    break;
                }
            }
        }
        while (!secondRowFilled) {

            for (int i = rotatableMatrix.length - 1; i >= 1; i--) {
                System.arraycopy(rotatableMatrix[i - 1], 0, rotatableMatrix[i], 0, rotatableMatrix[i].length);
            }
            Arrays.fill(rotatableMatrix[0], false);
            for (int j = 0; j < rotatableMatrix[0].length; j++) {
                if (rotatableMatrix[2][j]) {
                    secondRowFilled = true;
                    break;
                }
            }
        }
        for (int i = 0; i < rotatableMatrix.length; i++) {
            for (int j = i + 1; j < rotatableMatrix[i].length; j++) {
                boolean holder = rotatableMatrix[i][j];
                rotatableMatrix[i][j] = rotatableMatrix[j][i];
                rotatableMatrix[j][i] = holder;
            }
        }
        for (int i = 0; i < rotatableMatrix.length; i++) {
            for (int j = 0; j < rotatableMatrix[i].length / 2; j++) {
                boolean holder = rotatableMatrix[i][j];
                rotatableMatrix[i][j] = rotatableMatrix[i][rotatableMatrix[i].length - j - 1];
                rotatableMatrix[i][rotatableMatrix[i].length - j - 1] = holder;
            }
        }

        while (secondCellFilled) {
            for (boolean[] matrix : rotatableMatrix) {
                if (matrix[0]) {
                    secondCellFilled = false;
                    break;
                }
            }
            if (!secondCellFilled)
                break;
            for (int i = 0; i < rotatableMatrix.length; i++) {
                for (int j = 0; j < rotatableMatrix[i].length - 1; j++) {
                    rotatableMatrix[i][j] = rotatableMatrix[i][j + 1];
                }
                rotatableMatrix[i][rotatableMatrix[i].length - 1] = false;
            }

        }
        while (secondRowFilled) {
            for (int i = 0; i < rotatableMatrix.length - 1; i++) {
                System.arraycopy(rotatableMatrix[i + 1], 0, rotatableMatrix[i], 0, rotatableMatrix[i].length);
            }
            Arrays.fill(rotatableMatrix[rotatableMatrix.length - 1], false);
            for (int j = 0; j < rotatableMatrix[0].length; j++) {
                if (rotatableMatrix[0][j]) {
                    secondRowFilled = false;
                    break;
                }
            }
        }
        int k = 0;
        boolean successfulInsertion = false;
        boolean canRotate = true;
        for (int i = 0; i < rotatableMatrix.length; i++) {
            for (int j = 0; j < rotatableMatrix[i].length; j++) {
                if (rotatableMatrix[i][j]) {
                    try {

                        canRotate = !gameInfo[i + indexI][j + indexJ - k];

                    } catch (ArrayIndexOutOfBoundsException e) {
                        k++;
                    }
                }
            }
        }
        if (canRotate) {
            while (!successfulInsertion) {
                outerLoop:
                for (int i = 0; i < rotatableMatrix.length; i++) {
                    if (i + indexI < ghostGameInfo.length)
                        Arrays.fill(ghostGameInfo[i + indexI], false);
                    for (int j = 0; j < rotatableMatrix[i].length; j++) {
                        if (rotatableMatrix[i][j]) {
                            try {
                                successfulInsertion = true;
                                ghostGameInfo[i + indexI][j + indexJ - k] = rotatableMatrix[i][j];
                            } catch (ArrayIndexOutOfBoundsException e) {
                                k++;
                                successfulInsertion = false;
                                break outerLoop;
                            }
                        }
                    }
                }
            }
        }


    }

    private int checkEmptyRows(boolean[][] matrix) {
        int k = 0;
        for (boolean[] booleans : matrix) {
            for (boolean aBoolean : booleans) {
                if (aBoolean) {
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
            if (checkEmptyColumn(0) && checkLeftMovement()) {
                for (int i = 0; i < ghostGameInfo.length; i++) {
                    for (int j = 0; j < ghostGameInfo[i].length - 1; j++) {
                        ghostGameInfo[i][j] = ghostGameInfo[i][j + 1];
                    }
                    ghostGameInfo[i][ghostGameInfo[i].length - 1] = false;
                }
            }
            repaint();

        } else if (keyCode == KeyEvent.VK_RIGHT) {
            if (checkEmptyColumn(ghostGameInfo[0].length - 1) && checkRightMovement()) {
                for (int i = ghostGameInfo.length - 1; i >= 0; i--) {
                    for (int j = ghostGameInfo[i].length - 1; j >= 1; j--) {
                        ghostGameInfo[i][j] = ghostGameInfo[i][j - 1];
                    }
                    ghostGameInfo[i][0] = false;
                }
            }
            repaint();
        } else if (keyCode == KeyEvent.VK_DOWN) {
            moveDown();

            // Move the current piece down
        } else if (keyCode == KeyEvent.VK_UP) {
            rotateMatrix();
            repaint();
        }
    }

    private void clearFullRows(ArrayList<Integer> fullRows) {
        for (int fullRow : fullRows) {
            Arrays.fill(gameInfo[fullRow], false);
        }
        for (int k : fullRows) {
            for (int i = k; i > 0; i--) {
                gameInfo[i] = gameInfo[i - 1];
            }
            Arrays.fill(gameInfo[0], false);
        }
        try {
            // Add a delay of 1000 milliseconds (1 second) before making the move
            Thread.sleep(100);
            repaint();
        } catch (InterruptedException e) {
            // Handle the interruption if needed
            Thread.currentThread().interrupt();
        }


    }


    public void moveDown() {
        if (checkEmptyRow(ghostGameInfo.length - 1) && checkDownMovement()) {
            for (int i = ghostGameInfo.length - 1; i > 0; i--) {
                System.arraycopy(ghostGameInfo[i - 1], 0, ghostGameInfo[i], 0, ghostGameInfo[i].length);
            }
            Arrays.fill(ghostGameInfo[0], false);

        } else {
            count++;
            moveDownController.setAllowAutomaticMovement(false);
            for (int i = 0; i < ghostGameInfo.length; i++) {
                for (int j = 0; j < ghostGameInfo[i].length; j++) {
                    if (ghostGameInfo[i][j]) {
                        gameInfo[i][j] = true;
                    }
                }
            }
            for (boolean[] booleans : ghostGameInfo) {
                Arrays.fill(booleans, false);
            }
            fullRows = checkFullRows();

            if (!fullRows.isEmpty()) {
                clearFullRows(fullRows);
            }
            System.out.println(count);
            spawnPiece();
            moveDownController.setAllowAutomaticMovement(true);

        }
        repaint();
    }
    public void setAutoDownController(MoveDownController moveDownController){
        this.moveDownController=moveDownController;
    }

    private boolean checkDownMovement() {
        for (int i = 0; i < ghostGameInfo.length; i++) {
            for (int j = 0; j < ghostGameInfo[i].length; j++) {
                if (ghostGameInfo[i][j]) {
                    if (i + 1 < ghostGameInfo.length) {
                        if (gameInfo[i + 1][j]) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean checkLeftMovement() {
        for (int i = 0; i < ghostGameInfo.length; i++) {
            for (int j = 0; j < ghostGameInfo[i].length; j++) {
                if (ghostGameInfo[i][j]) {
                    if (j - 1 >= 0) {
                        if (gameInfo[i][j - 1]) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean checkRightMovement() {
        for (int i = 0; i < ghostGameInfo.length; i++) {
            for (int j = 0; j < ghostGameInfo[i].length; j++) {
                if (ghostGameInfo[i][j]) {
                    if (j + 1 < ghostGameInfo[i].length) {
                        if (gameInfo[i][j + 1]) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean checkEmptyColumn(int column) {
        for (boolean[] booleans : ghostGameInfo) {
            if (booleans[column]) {
                return false;
            }
        }
        return true;
    }

    private boolean checkEmptyRow(int row) {
        for (int i = 0; i < ghostGameInfo[row].length; i++) {
            if (ghostGameInfo[row][i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}