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
    JLabel score;
    MoveDownController moveDownController;
    ArrayList<Integer> fullRows;
    String[] typesOfPieces = {"Line", "Square", "Sblock", "RSblock", "Tblock", "Lblock", "RLblock"};

    Panel(int width, int height,JLabel score) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.gameInfo = new boolean[(int) (height / cellSize)][(int) (width / cellSize)];
        this.ghostGameInfo = new boolean[(int) (height / cellSize)][(int) (width / cellSize)];
        this.score=score;
        spawnPiece();
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);
        moveDownController = new MoveDownController(this);
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

        TetrisPiece piece = new TetrisPiece(typesOfPieces[random.nextInt(typesOfPieces.length)]);

        int k = checkEmptyRows(piece.tetrisPieceSpace);
        boolean canSpawn = true;
        for (int i = 0; i + k < piece.tetrisPieceSpace.length; i++) {
            for (int j = 0; j < piece.tetrisPieceSpace.length; j++) {
                if (gameInfo[i][j + 3])
                    canSpawn = false;
            }
        }
        if (canSpawn) {
            for (int i = 0; i + k < piece.tetrisPieceSpace.length; i++) {
                for (int j = 0; j < piece.tetrisPieceSpace.length; j++) {
                    ghostGameInfo[i][j + 3] = piece.tetrisPieceSpace[i + k][j];
                }
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
                for (int j = 0; j < rotatableMatrix[i].length; j++) {
                    rotatableMatrix[i][j] = rotatableMatrix[i - 1][j];
                }
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
            for (int i = 0; i < rotatableMatrix.length; i++) {
                if (rotatableMatrix[i][0]) {
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
                for (int j = 0; j < rotatableMatrix[i].length; j++) {
                    rotatableMatrix[i][j] = rotatableMatrix[i + 1][j];
                }
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
        boolean succefullInsertion = false;
        boolean canRotate = true;
        for (int i = 0; i < rotatableMatrix.length; i++) {
            for (int j = 0; j < rotatableMatrix[i].length; j++) {
                if (rotatableMatrix[i][j]) {
                    try {

                        canRotate= !gameInfo[i + indexI][j + indexJ - k];

                    } catch (ArrayIndexOutOfBoundsException e) {
                        k++;
                    }
                }
            }
        }
        if(canRotate) {
            while (!succefullInsertion) {
                outerLoop:
                for (int i = 0; i < rotatableMatrix.length; i++) {
                    if (i + indexI < ghostGameInfo.length)
                        Arrays.fill(ghostGameInfo[i + indexI], false);
                    for (int j = 0; j < rotatableMatrix[i].length; j++) {
                        if (rotatableMatrix[i][j]) {
                            try {
                                succefullInsertion = true;
                                ghostGameInfo[i + indexI][j + indexJ - k] = rotatableMatrix[i][j];
                            } catch (ArrayIndexOutOfBoundsException e) {
                                k++;
                                succefullInsertion = false;
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
        for (int i = 0; i < fullRows.size(); i++) {
            for (int j = 0; j < gameInfo[i].length; j++) {
                gameInfo[fullRows.get(i)][j] = false;

            }
        }
        if (!fullRows.isEmpty()) {
            try {
                // Add a delay of 1000 milliseconds (1 second) before making the move
                Thread.sleep(500);
                repaint();
            } catch (InterruptedException e) {
                // Handle the interruption if needed
                Thread.currentThread().interrupt();
            }
        }
        for (int k : fullRows) {
            for (int i = k; i > 0; i--) {
                gameInfo[i] = gameInfo[i - 1];
            }
        }
        if (!fullRows.isEmpty()) {
            try {
                // Add a delay of 1000 milliseconds (1 second) before making the move
                Thread.sleep(100);
                repaint();
            } catch (InterruptedException e) {
                // Handle the interruption if needed
                Thread.currentThread().interrupt();
            }
        }
        score.setText(String.valueOf(Integer.parseInt(score.getText())+(int)(100*fullRows.size()*Math.pow(2,fullRows.size()-1))));

    }


    public void moveDown() {
        if (checkEmptyRow(ghostGameInfo.length - 1) && checkDownMovement()) {
            for (int i = ghostGameInfo.length - 1; i >0; i--) {
                for (int j = 0; j < ghostGameInfo[i].length; j++) {
                    ghostGameInfo[i][j] = ghostGameInfo[i - 1][j];
                }
            }
            Arrays.fill(ghostGameInfo[0], false);
            /*for(int i=0;i<gh)*/

        } else {
            for (int i = 0; i < gameInfo.length; i++) {
                for (int j = 0; j < gameInfo[i].length; j++) {
                    if (ghostGameInfo[i][j]) {
                        gameInfo[i][j] = ghostGameInfo[i][j];
                    }
                }

            }
            for (int i = 0; i < ghostGameInfo.length; i++) {
                //System.out.println(Arrays.toString(ghostGameInfo[i]));
                Arrays.fill(ghostGameInfo[i], false);
            }
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            //duplicate bug happens in one of these 3 methods.
            fullRows = checkFullRows();
            clearFullRows(fullRows);

            spawnPiece();
        }
        repaint();

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
