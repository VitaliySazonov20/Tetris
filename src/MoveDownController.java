/*public class MoveDownController extends Thread{
    long timeStart;
    long currentTime;
    TetrisPiece piece;
    MoveDownController(TetrisPiece piece){
        this.piece=piece;
        timeStart=System.currentTimeMillis();
        this.start();
    }
    public void run(){
        while (true){
            currentTime=System.currentTimeMillis();
            if(currentTime-timeStart>1000){
                piece.moveDown();
                timeStart=currentTime;
            }
        }
    }
}*/
import java.util.concurrent.*;

public class MoveDownController {
    private final ScheduledExecutorService scheduler;
    private final Panel panel;

    public MoveDownController(Panel panel) {
        this.panel = panel;
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::moveDown, 0, 1, TimeUnit.SECONDS);
    }

    private void moveDown() {
        if (panel.piece.noBottomBorderCollision() && panel.checkDownMovement()) {
            panel.piece.moveDown();
            }
        else{
            panel.placePiece();
            try {
                panel.removeFullRows();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            panel.spawnPiece();
        }
        panel.repaint();
    }

    public void stop() {
        scheduler.shutdown();
    }
}
