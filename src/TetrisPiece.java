import java.util.Arrays;
import java.util.Random;

public class TetrisPiece {
    Random random=new Random();
    int[][] ghostGameInfo;
    int[][] futurePiece=new int[4][4];
    int[][] rotatableMatrix = new int[5][5];

    TetrisPiece(int height, int width) {
        ghostGameInfo = new int[height][width];
        //generate();
        generateFuturePiece();
        generate();
    }

    public void generate() {
        for (int[] row : ghostGameInfo) {
            Arrays.fill(row, 0);
        }
        if(futurePiece[0][1]!=1) {
            for (int i = 1; i < futurePiece.length; i++) {
                System.arraycopy(futurePiece[i], 0, ghostGameInfo[i-1], 3, futurePiece[i].length);
            }
        }else{
            for (int i = 0; i < futurePiece.length; i++) {
                System.arraycopy(futurePiece[i], 0, ghostGameInfo[i], 3, futurePiece[i].length);
            }
        }
        generateFuturePiece();
        /*switch (id) {

            case (1): {
                for (int i = 0; i < 4; i++) {
                    ghostGameInfo[i][ghostGameInfo[i].length / 2 - 1] = id;
                }
                break;
            }
            case (2): {
                ghostGameInfo[0][ghostGameInfo[0].length / 2 - 1] = id;
                ghostGameInfo[0][ghostGameInfo[0].length / 2] = id;
                ghostGameInfo[1][ghostGameInfo[1].length / 2 - 1] = id;
                ghostGameInfo[1][ghostGameInfo[1].length / 2] = id;
                break;
            }
            case (3): {
                ghostGameInfo[1][ghostGameInfo[1].length / 2 - 2] = id;
                ghostGameInfo[1][ghostGameInfo[1].length / 2 - 1] = id;
                ghostGameInfo[0][ghostGameInfo[0].length / 2 - 1] = id;
                ghostGameInfo[0][ghostGameInfo[0].length / 2] = id;
                break;
            }
            case (4): {
                ghostGameInfo[0][ghostGameInfo[0].length / 2 - 2] = id;
                ghostGameInfo[0][ghostGameInfo[0].length / 2 - 1] = id;
                ghostGameInfo[1][ghostGameInfo[1].length / 2 - 1] = id;
                ghostGameInfo[1][ghostGameInfo[1].length / 2] = id;
                break;
            }
            case (5): {
                ghostGameInfo[0][ghostGameInfo[0].length / 2 - 2] = id;
                ghostGameInfo[0][ghostGameInfo[0].length / 2 - 1] = id;
                ghostGameInfo[0][ghostGameInfo[0].length / 2] = id;
                ghostGameInfo[1][ghostGameInfo[1].length / 2 - 1] = id;
                break;
            }
            case (6): {
                for (int i = 0; i < 3; i++) {
                    ghostGameInfo[i][ghostGameInfo[0].length / 2] = id;
                }
                ghostGameInfo[0][ghostGameInfo[0].length / 2 - 1] = id;
                break;
            }
            case (7): {
                for (int i = 0; i < 3; i++) {
                    ghostGameInfo[i][ghostGameInfo[0].length / 2 - 1] = id;
                }
                ghostGameInfo[0][ghostGameInfo[0].length / 2] = id;
            }
        }*/
    }
    private void generateFuturePiece(){
        int id = random.nextInt(7)+1;
        for(int[] row: futurePiece){
            Arrays.fill(row,0);
        }
        switch(id){
            case(1):{
                for(int i=0;i< futurePiece.length;i++){
                    futurePiece[i][1]=id;
                }
                break;
            }
            case(2):{
                futurePiece[1][1]=id;
                futurePiece[1][2]=id;
                futurePiece[2][2]=id;
                futurePiece[2][1]=id;
                break;
            }
            case(3):{
                futurePiece[1][2]=id;
                futurePiece[1][3]=id;
                futurePiece[2][1]=id;
                futurePiece[2][2]=id;
                break;
            }
            case(4):{
                futurePiece[1][1]=id;
                futurePiece[1][2]=id;
                futurePiece[2][2]=id;
                futurePiece[2][3]=id;
                break;
            }
            case(5):{
                futurePiece[1][1]=id;
                futurePiece[1][2]=id;
                futurePiece[1][3]=id;
                futurePiece[2][2]=id;
                break;
            }
            case(6):{
                futurePiece[1][1]=id;
                futurePiece[1][2]=id;
                futurePiece[2][1]=id;
                futurePiece[3][1]=id;
                break;
            }
            case(7):{
                futurePiece[1][1]=id;
                futurePiece[1][2]=id;
                futurePiece[2][2]=id;
                futurePiece[3][2]=id;
            }

        }
    }

    public void moveLeft() {
        if (noBorderCollision(0)) {
            for (int i = 0; i < ghostGameInfo.length; i++) {
                for (int j = 0; j < ghostGameInfo[i].length - 1; j++) {
                    ghostGameInfo[i][j] = ghostGameInfo[i][j + 1];
                }
                ghostGameInfo[i][ghostGameInfo[i].length - 1] = 0;
            }
        }
    }

    public void moveRight() {
        if (noBorderCollision(ghostGameInfo[0].length - 1)) {
            for (int i = 0; i < ghostGameInfo.length; i++) {
                for (int j = ghostGameInfo[i].length - 1; j > 0; j--) {
                    ghostGameInfo[i][j] = ghostGameInfo[i][j - 1];
                }
                ghostGameInfo[i][0] = 0;
            }
        }
    }

    public void moveDown() {

        for (int i = ghostGameInfo.length - 1; i > 0; i--) {
            System.arraycopy(ghostGameInfo[i - 1], 0, ghostGameInfo[i], 0, ghostGameInfo[i].length);
        }
        Arrays.fill(ghostGameInfo[0], 0);

    }

    public void rotate(int[][] gameInfo) {
        int indexI = ghostGameInfo.length;
        int indexJ = ghostGameInfo[0].length;
        for (int i = 0; i < ghostGameInfo.length; i++) {
            for (int j = 0; j < ghostGameInfo[i].length; j++) {
                if (ghostGameInfo[i][j] > 0) {
                    if (i < indexI) {
                        indexI = i;
                    }
                    if (j < indexJ) {
                        indexJ = j;
                    }
                }
            }
        }
        for (int i = 0; i < rotatableMatrix.length; i++) {
            if (indexI + i < ghostGameInfo.length)
                rotatableMatrix[i] = Arrays.copyOfRange(ghostGameInfo[indexI + i], indexJ, indexJ + 5);
            else
                Arrays.fill(rotatableMatrix[i], 0);
        }
        for (int i = 0; i < rotatableMatrix.length; i++) {
            for (int j = i + 1; j < rotatableMatrix[i].length; j++) {
                int holder = rotatableMatrix[i][j];
                rotatableMatrix[i][j] = rotatableMatrix[j][i];
                rotatableMatrix[j][i] = holder;
            }
        }
        for (int i = 0; i < rotatableMatrix.length; i++) {
            for (int j = 0; j < rotatableMatrix[i].length / 2; j++) {
                int holder = rotatableMatrix[i][j];
                rotatableMatrix[i][j] = rotatableMatrix[i][rotatableMatrix[i].length - j - 1];
                rotatableMatrix[i][rotatableMatrix[i].length - j - 1] = holder;
            }
        }

        outerLoop:
        while (true) {
            for (int[] row : rotatableMatrix) {
                if (row[0] > 0) {
                    break outerLoop;
                }
            }
            for (int i = 0; i < rotatableMatrix.length; i++) {
                for (int j = 0; j < rotatableMatrix[i].length - 1; j++) {
                    rotatableMatrix[i][j] = rotatableMatrix[i][j + 1];
                }
                rotatableMatrix[i][rotatableMatrix[i].length - 1] = 0;
            }

        }
        boolean canRotate=false;
        boolean taskCompleted=false;
        int k = 0;
        while(!taskCompleted)
        {
            for (int i = 0; i < rotatableMatrix.length; i++) {
                for (int j = 0; j < rotatableMatrix[i].length; j++) {
                    if (rotatableMatrix[i][j] > 0) {
                        try {
                            canRotate = gameInfo[i + indexI][j + indexJ - k] <= 0;
                            taskCompleted=true;
                        } catch (ArrayIndexOutOfBoundsException e) {
                            k++;
                            taskCompleted=false;
                        }

                    }
                }
            }
        }
        if(canRotate){
            for(int[] row: ghostGameInfo){
                Arrays.fill(row,0);
            }
            for(int i=0;i<rotatableMatrix.length;i++){
                for(int j=0;j<rotatableMatrix[i].length;j++){
                    if(rotatableMatrix[i][j]>0)
                        ghostGameInfo[i+indexI][j+indexJ-k]=rotatableMatrix[i][j];
                }
            }

        }


    }

    private boolean noBorderCollision(int column) {
        for (int[] row : ghostGameInfo) {
            if (row[column] > 0) {
                return false;
            }
        }
        return true;
    }

    public boolean noBottomBorderCollision() {
        for (int j = 0; j < ghostGameInfo[0].length; j++) {
            if (ghostGameInfo[ghostGameInfo.length - 1][j] > 0) {
                return false;
            }
        }
        return true;
    }
}
