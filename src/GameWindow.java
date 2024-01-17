import javax.swing.*;
import java.awt.*;


public class GameWindow {
    JFrame window;
    JPanel gameScreen;
    JLabel score;
    JPanel nextPiece;
    MoveDownController moveDownController;
    int WIDTH=700;
    int HEIGHT= 1000;
    GameWindow(){
        window= new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(WIDTH, HEIGHT);
        window.setLayout(null);
        window.setVisible(true);

        score= new JLabel("000000");
        score.setBounds(WIDTH-225,75,100,50);
        score.setFont(new Font("Arial", Font.BOLD, 25));
        window.add(score);

        TetrisPiece tetrisPiece= new TetrisPiece((HEIGHT-107)/((WIDTH-275)/10),10);



        nextPiece=new NextPiece(tetrisPiece,150);
        nextPiece.setBounds(WIDTH-225,150,150,150);
        nextPiece.setBackground(Color.lightGray);
        window.add(nextPiece);

        gameScreen= new Panel(WIDTH-275,HEIGHT-107,tetrisPiece, (NextPiece) nextPiece,score);
        gameScreen.setBounds(25,25,WIDTH-275,HEIGHT-107);
        gameScreen.setBackground(Color.lightGray);
        window.add(gameScreen);
        gameScreen.requestFocusInWindow();

        window.revalidate();





        moveDownController = new MoveDownController((Panel) gameScreen);

    }
}
