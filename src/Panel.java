import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Panel extends JPanel implements KeyListener {

    int WIDTH;
    int HEIGHT;
    double cellSize;
    int[][] gameInfo;
    TetrisPiece piece;
    Random random = new Random();
    //MoveDownController moveDownController;
    int count = 0;
    ArrayList<Integer> fullRows= new ArrayList<>();
    Color[] colors = {Color.red, Color.ORANGE, Color.yellow, Color.gray, Color.blue, Color.CYAN, Color.MAGENTA};

    Panel(int width, int height) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.cellSize = (double) width / 10;
        this.gameInfo = new int[(int) (height / cellSize)][(int) (width / cellSize)];
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
        graphics.setColor(Color.black);
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
                if (gameInfo[i][j] > 0) {
                    graphics.setColor(colors[gameInfo[i][j] - 1]);
                    graphics.fillRect((int) (j * cellSize + 5), (int) (i * cellSize + 5), (int) (cellSize - 10), (int) (cellSize - 10));
                }
                if (piece.tetrisPieceSpace[i][j] > 0) {
                    graphics.setColor(colors[piece.tetrisPieceSpace[i][j] - 1]);
                    graphics.fillRect((int) (j * cellSize + 5), (int) (i * cellSize + 5), (int) (cellSize - 10), (int) (cellSize - 10));
                }
            }
        }
    }

    public void spawnPiece() {
        count++;
        if (piece != null) {
            piece.generate(random.nextInt(colors.length) + 1);
        } else {
            piece = new TetrisPiece(random.nextInt(colors.length) + 1, (int) (HEIGHT / cellSize), (int) (WIDTH / cellSize));
        }
        repaint();
        System.out.println(count);

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT) {
            if (checkLeftMovement()) {
                piece.moveLeft();
            }
        }
        if (keyCode == KeyEvent.VK_RIGHT) {
            if(checkRightMovement()) {
                piece.moveRight();
            }
        }
        if (keyCode == KeyEvent.VK_DOWN) {
            if (piece.noBottomBorderCollision() && checkDownMovement()) {
                piece.moveDown();
                repaint();

            } else {
                placePiece();
                try {
                    removeFullRows();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                spawnPiece();

            }
        }
        if(keyCode == KeyEvent.VK_UP){
            piece.rotate(gameInfo);
        }
        repaint();
    }

    private void removeFullRows() throws InterruptedException {
        for(int i=0;i< gameInfo.length;i++){
            if(checkFullRow(gameInfo[i])){
                Arrays.fill(gameInfo[i],0);
                fullRows.add(i);
            }
        }
        if(!fullRows.isEmpty()) {
            Thread.sleep(300);
            repaint();
            for (Integer fullRow : fullRows) {
                for (int j = fullRow; j > 0; j--) {
//                    gameInfo[j] = gameInfo[j - 1];
                    System.arraycopy(gameInfo[j-1],0,gameInfo[j],0,gameInfo[j].length);
                }
                Arrays.fill(gameInfo[0], 0);
            }
            Thread.sleep(300);
            repaint();
        }
        fullRows.clear();
    }
    private boolean checkFullRow(int[] row){
        for (int num: row){
            if(num<=0){
                return false;
            }
        }
        return true;
    }
    private void placePiece() {
        for (int i = 0; i < gameInfo.length; i++) {
            for (int j = 0; j < gameInfo[i].length; j++) {
                if (piece.tetrisPieceSpace[i][j] > 0) {
                    gameInfo[i][j] = piece.tetrisPieceSpace[i][j];
                }
            }
        }
    }

    private boolean checkDownMovement() {
        for (int i = 0; i < piece.tetrisPieceSpace.length; i++) {
            for (int j = 0; j < piece.tetrisPieceSpace[i].length; j++) {
                if (piece.tetrisPieceSpace[i][j] > 0 && i + 1 < piece.tetrisPieceSpace.length) {
                    if (gameInfo[i + 1][j] > 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean checkLeftMovement() {
        for (int i = 0; i < piece.tetrisPieceSpace.length; i++) {
            for (int j = 0; j < piece.tetrisPieceSpace[i].length; j++) {
                if (piece.tetrisPieceSpace[i][j] > 0) {
                    if (j - 1 >= 0) {
                        if (gameInfo[i][j - 1] > 0) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
    private boolean checkRightMovement() {
        for (int i = 0; i < piece.tetrisPieceSpace.length; i++) {
            for (int j = 0; j < piece.tetrisPieceSpace[i].length; j++) {
                if (piece.tetrisPieceSpace[i][j]>0) {
                    if (j + 1 < piece.tetrisPieceSpace[i].length) {
                        if (gameInfo[i][j + 1]>0) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
