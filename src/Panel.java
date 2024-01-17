import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;

public class Panel extends JPanel implements KeyListener {

    int WIDTH;
    int HEIGHT;
    double cellSize;
    int[][] gameInfo;
    TetrisPiece piece;
    int count = 0;
    JLabel score;
    int difficulty = 1;
    boolean placing = false;
    boolean gameOver = false;
    NextPiece nextPiece;
    ArrayList<Integer> fullRows = new ArrayList<>();
    Color[] colors = {Color.red, Color.ORANGE, Color.yellow, Color.gray, Color.blue, Color.CYAN, Color.MAGENTA};

    Panel(int width, int height, TetrisPiece piece,NextPiece nextPiece, JLabel score) {
        this.nextPiece=nextPiece;
        this.piece = piece;
        this.WIDTH = width;
        this.HEIGHT = height;
        this.cellSize = (double) width / 10;
        this.gameInfo = new int[(int) (height / cellSize)][(int) (width / cellSize)];
        spawnPiece();
        this.score = score;
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
                    graphics.setColor(Color.BLACK);
                    graphics.drawRect((int) (j * cellSize + 4), (int) (i * cellSize + 4), (int) (cellSize - 9), (int) (cellSize - 9));
                }
                if (piece.ghostGameInfo[i][j] > 0) {
                    graphics.setColor(colors[piece.ghostGameInfo[i][j] - 1]);
                    graphics.fillRect((int) (j * cellSize + 5), (int) (i * cellSize + 5), (int) (cellSize - 10), (int) (cellSize - 10));
                    graphics.setColor(Color.BLACK);
                    graphics.drawRect((int) (j * cellSize + 4), (int) (i * cellSize + 4), (int) (cellSize - 9), (int) (cellSize - 9));
                }
            }
        }
    }

    public void spawnPiece() {

        piece.generate();

        if (cantSpawnPiece()) {
            /*for (int i=0; i<piece.futurePiece.length;i++){
                System.arraycopy(gameInfo[i],3,piece.futurePiece[i],0,piece.futurePiece[i].length);
            }
            nextPiece.repaint();*/
            for (int[] row : piece.ghostGameInfo) {
                Arrays.fill(row, 0);
            }
        }
        repaint();

    }

    public boolean cantSpawnPiece() {
        for (int i = 0; i < piece.ghostGameInfo.length; i++) {
            for (int j = 0; j < piece.ghostGameInfo[i].length; j++) {
                if (piece.ghostGameInfo[i][j] > 0 && gameInfo[i][j] > 0)
                    return true;
            }
        }
        return false;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (!gameOver) {
            if (keyCode == KeyEvent.VK_LEFT) {
                if (checkLeftMovement()) {
                    piece.moveLeft();
                }
            }
            if (keyCode == KeyEvent.VK_RIGHT) {
                if (checkRightMovement()) {
                    piece.moveRight();
                }
            }
            if (keyCode == KeyEvent.VK_DOWN) {
                moveDown();
            }
            if (keyCode == KeyEvent.VK_UP) {
                piece.rotate(gameInfo);
            }
            repaint();
        }
    }

    public void moveDown() {
        if (piece.noBottomBorderCollision() && checkDownMovement()) {
            piece.moveDown();
            repaint();

        } else {
            placePiece();
            placing = true;
            try {
                removeFullRows();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            spawnPiece();

        }

        if (cantSpawnPiece()) {
            gameOver = true;
        }
        placing = false;
        repaint();
    }

    public void removeFullRows() throws InterruptedException {
        for (int i = 0; i < gameInfo.length; i++) {
            if (checkFullRow(gameInfo[i])) {
                if (!fullRows.contains(i)) {
                    Arrays.fill(gameInfo[i], 0);
                    fullRows.add(i);
                }
            }
        }
        System.out.println(fullRows);
        if (!fullRows.isEmpty()) {
            Thread.sleep(400);
            repaint();
            for (Integer fullRow : fullRows) {
                for (int j = fullRow; j > 0; j--) {
                    System.arraycopy(gameInfo[j - 1], 0, gameInfo[j], 0, gameInfo[j].length);
                }
                Arrays.fill(gameInfo[0], 0);
            }
            Thread.sleep(300);
            repaint();
            findDifficulty();
            addScore(fullRows.size() * fullRows.size() * 100 * difficulty);
            count += fullRows.size();
        }

        fullRows.clear();
    }

    private boolean checkFullRow(int[] row) {
        for (int num : row) {
            if (num <= 0) {
                return false;
            }
        }
        return true;
    }

    public void placePiece() {
        for (int i = 0; i < gameInfo.length; i++) {
            for (int j = 0; j < gameInfo[i].length; j++) {
                if (piece.ghostGameInfo[i][j] > 0) {
                    gameInfo[i][j] = piece.ghostGameInfo[i][j];
                }
            }
        }
        nextPiece.repaint();
    }

    public boolean checkDownMovement() {
        for (int i = 0; i < piece.ghostGameInfo.length; i++) {
            for (int j = 0; j < piece.ghostGameInfo[i].length; j++) {
                if (piece.ghostGameInfo[i][j] > 0 && i + 1 < piece.ghostGameInfo.length) {
                    if (gameInfo[i + 1][j] > 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean checkLeftMovement() {
        for (int i = 0; i < piece.ghostGameInfo.length; i++) {
            for (int j = 0; j < piece.ghostGameInfo[i].length; j++) {
                if (piece.ghostGameInfo[i][j] > 0) {
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
        for (int i = 0; i < piece.ghostGameInfo.length; i++) {
            for (int j = 0; j < piece.ghostGameInfo[i].length; j++) {
                if (piece.ghostGameInfo[i][j] > 0) {
                    if (j + 1 < piece.ghostGameInfo[i].length) {
                        if (gameInfo[i][j + 1] > 0) {
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

    public void addScore(int points) {
        StringBuilder sb = new StringBuilder(String.valueOf(Integer.parseInt(score.getText()) + points));
        while(sb.length()<6)
            sb.insert(0, "0");

        score.setText(sb.toString());
    }

    private void findDifficulty() {
        if (count < 300) {
            this.difficulty = count / 10 + 1;
        } else {
            this.difficulty = 300;
        }
    }


}
