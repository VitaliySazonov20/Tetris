public class MoveDownController extends Thread{
    long timeStart;
    long currentTime;
    Panel panel;
    MoveDownController(Panel panel){
        this.panel=panel;
        timeStart=System.currentTimeMillis();
        this.start();
    }
    public void run(){
        while (true){
            currentTime=System.currentTimeMillis();
            if(currentTime-timeStart>1000){
                panel.piece.moveDown();
                timeStart=currentTime;
            }
        }
    }
}
/*import java.util.concurrent.*;

public class MoveDownController {
    private ScheduledExecutorService scheduler;
    private Panel panel;

    public MoveDownController(Panel panel) {
        this.panel = panel;
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::moveDown, 0, 1, TimeUnit.SECONDS);
    }

    private void moveDown() {
        panel.piece.moveDown();
    }

    public void stop() {
        scheduler.shutdown();
    }
}*/
