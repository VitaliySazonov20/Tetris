import javax.swing.*;
import java.awt.*;

public class NextPiece extends JPanel {
    TetrisPiece piece;
    int size;
    Color[] colors = {Color.red, Color.ORANGE, Color.yellow, Color.gray, Color.blue, Color.CYAN, Color.MAGENTA};
    NextPiece(TetrisPiece piece,int size) {
        this.piece=piece;
        this.size=size;
    }
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
        drawBlocks(g);
    }
    protected void drawGrid(Graphics graphics){
        int i = 0;
        graphics.setColor(Color.black);
        while (i < 4) {
            graphics.drawLine((i * size/4), 0, (i * size/4), size);
            i++;
        }
        i = 0;
        graphics.drawLine(size - 1, 0, size - 1, size);
        while (i < 4) {
            graphics.drawLine(0, (i *size/4), size,(i * size/4));
            i++;
        }
        graphics.drawLine(0, size - 1, size, size - 1);
    }
    protected void drawBlocks(Graphics graphics){
        for(int i=0;i<piece.futurePiece.length;i++){
            for(int j=0;j<piece.futurePiece[i].length;j++){
                if(piece.futurePiece[i][j]>0) {
                    graphics.setColor(colors[piece.futurePiece[i][j]-1]);
                    graphics.fillRect(j*size/4 + 3,i*size/4+3,size/4-5,size/4-5);
                    graphics.setColor(Color.BLACK);
                    graphics.drawRect(j*size/4 + 2,i*size/4+2,size/4-4,size/4-4);
                }
            }
        }
    }
}
