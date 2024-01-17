public class MoveDownController implements Runnable {
    int time = 1000;
    private final Panel panel;
    private boolean running = true;

    public MoveDownController(Panel panel) {
        this.panel = panel;
        start();
    }

    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        long lastTime = System.currentTimeMillis();
        while (running) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastTime >= time) {
                lastTime = currentTime;
                if(!panel.placing) {
                    panel.moveDown();
                    if(panel.cantSpawnPiece()){
                        stop();
                    }
                    recalculateTime();
                }
            }
        }
    }
    private void recalculateTime(){
        this.time= (int) (1000*Math.pow(0.85, panel.difficulty)+panel.difficulty);
    }

}
