import java.util.Date;

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
                panel.moveDown();
                timeStart=currentTime;
            }
        }
    }
}
