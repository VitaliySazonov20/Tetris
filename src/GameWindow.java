import javax.swing.*;
import java.awt.*;

public class GameWindow {
    JFrame window;
    JPanel gameScreen;
    JLabel score;
    int WIDTH=600;
    int HEIGHT= 1000;
    GameWindow(){
        window= new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(WIDTH, HEIGHT);
        window.setLayout(null);
        window.setVisible(true);

        score=new JLabel();
        score.setBounds(WIDTH-105,25,100,100);
        score.setFont(new Font("Arial", Font.BOLD, 20));
        score.setText("00000");
        window.add(score);


        gameScreen= new Panel(WIDTH-175,HEIGHT-107,score);
        gameScreen.setBounds(25,25,WIDTH-175,HEIGHT-107);
        gameScreen.setBackground(Color.lightGray);
        window.add(gameScreen);
        gameScreen.requestFocusInWindow();
        window.revalidate();
    }
}
