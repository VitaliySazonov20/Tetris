import java.util.concurrent.*;

public class MoveDownController {
    private ScheduledExecutorService scheduler;
    private Panel panel;
    private boolean allowAutomaticMovement;

    public MoveDownController(Panel panel) {
        this.panel = panel;
        allowAutomaticMovement = true;
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::scheduledMoveDown, 0, 1, TimeUnit.SECONDS);
    }

    private void scheduledMoveDown() {
        if (allowAutomaticMovement) {
            panel.moveDown();
        }
    }

    public void stop() {
        scheduler.shutdown();
    }

    public void setAllowAutomaticMovement(boolean allow) {
        this.allowAutomaticMovement = allow;
    }
}