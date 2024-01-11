import java.util.Arrays;
import java.util.Random;

public class TetrisPiece {
    boolean[][] tetrisPieceSpace= new boolean[4][4];
    Random random= new Random();
    TetrisPiece(String type){
        generate(type);
    }
    public void generate(String type){
        for(boolean[] bool: tetrisPieceSpace){
            Arrays.fill(bool,false);
        }
        switch(type){
            case ("Line"):{
                for(int i=0; i< tetrisPieceSpace.length;i++){
                    tetrisPieceSpace[2][i]=true;
                }
                break;
            }
            case ("Square"):{
                tetrisPieceSpace[1][1]=true;
                tetrisPieceSpace[1][2]=true;
                tetrisPieceSpace[2][1]=true;
                tetrisPieceSpace[2][2]=true;
                break;
            }
            case ("Sblock"):{
                tetrisPieceSpace[2][1]=true;
                tetrisPieceSpace[2][2]=true;
                tetrisPieceSpace[1][2]=true;
                tetrisPieceSpace[1][3]=true;
                break;
            }
            case ("RSblock"):{
                tetrisPieceSpace[1][1]=true;
                tetrisPieceSpace[1][2]=true;
                tetrisPieceSpace[2][2]=true;
                tetrisPieceSpace[2][3]=true;
                break;
            }
            case ("Tblock"):{
                tetrisPieceSpace[1][1]=true;
                tetrisPieceSpace[1][2]=true;
                tetrisPieceSpace[1][3]=true;
                tetrisPieceSpace[2][2]=true;
                break;
            }
            case("Lblock"):{
                for (int i=1;i< tetrisPieceSpace.length;i++){
                    tetrisPieceSpace[i][1]=true;
                }
                tetrisPieceSpace[1][2]=true;
                break;
            }
            case("RLblock"):{
                for (int i=1;i< tetrisPieceSpace.length;i++){
                    tetrisPieceSpace[i][1]=true;
                }
                tetrisPieceSpace[1][0]=true;
            }


        }
        for(int i=0;i< random.nextInt(4);i++){
            rotate();
        }
    }
    public void rotate(){
        for(int i=0;i<tetrisPieceSpace.length;i++){
            for(int j=i+1;j<tetrisPieceSpace[i].length;j++){
                boolean holder=tetrisPieceSpace[i][j];
                tetrisPieceSpace[i][j]=tetrisPieceSpace[j][i];
                tetrisPieceSpace[j][i]=holder;
            }
        }
        for(int i=0;i<tetrisPieceSpace.length;i++){
            for(int j=0;j<tetrisPieceSpace[i].length/2;j++){
                boolean holder= tetrisPieceSpace[i][j];
                tetrisPieceSpace[i][j]=tetrisPieceSpace[i][tetrisPieceSpace[i].length-j-1];
                tetrisPieceSpace[i][tetrisPieceSpace[i].length-j-1]=holder;
            }
        }
    }
}
