import javax.swing.*;
import java.awt.*;

public class GameWindow {
    JFrame window;
    JPanel gameScreen;
    int WIDTH=500;
    int HEIGHT= 1000;
    GameWindow(){
        window= new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(WIDTH, HEIGHT);
        window.setLayout(null);
        window.setVisible(true);

        gameScreen= new Panel(WIDTH-75,HEIGHT-107);
        gameScreen.setBounds(25,25,WIDTH-75,HEIGHT-107);
        gameScreen.setBackground(Color.lightGray);
        window.add(gameScreen);
        gameScreen.requestFocusInWindow();
    }
}
