import java.util.Arrays;

public class TetrisPiece {
    int[][] tetrisPieceSpace;
    int ID;
    int[][] rotatableMatrix = new int[5][5];

    TetrisPiece(int id, int height, int width) {
        this.ID = id;
        tetrisPieceSpace = new int[height][width];
        generate(ID);
    }

    public void generate(int id) {
        for (int[] row : tetrisPieceSpace) {
            Arrays.fill(row, 0);
        }
        switch (id) {
            case (1): {
                for (int i = 0; i < 4; i++) {
                    tetrisPieceSpace[i][tetrisPieceSpace[i].length / 2 - 1] = id;
                }
                break;
            }
            case (2): {
                tetrisPieceSpace[0][tetrisPieceSpace[0].length / 2 - 1] = id;
                tetrisPieceSpace[0][tetrisPieceSpace[0].length / 2] = id;
                tetrisPieceSpace[1][tetrisPieceSpace[1].length / 2 - 1] = id;
                tetrisPieceSpace[1][tetrisPieceSpace[1].length / 2] = id;
                break;
            }
            case (3): {
                tetrisPieceSpace[1][tetrisPieceSpace[0].length / 2 - 2] = id;
                tetrisPieceSpace[1][tetrisPieceSpace[0].length / 2 - 1] = id;
                tetrisPieceSpace[0][tetrisPieceSpace[0].length / 2 - 1] = id;
                tetrisPieceSpace[0][tetrisPieceSpace[0].length / 2] = id;
                break;
            }
            case (4): {
                tetrisPieceSpace[0][tetrisPieceSpace[0].length / 2 - 2] = id;
                tetrisPieceSpace[0][tetrisPieceSpace[0].length / 2 - 1] = id;
                tetrisPieceSpace[1][tetrisPieceSpace[0].length / 2 - 1] = id;
                tetrisPieceSpace[1][tetrisPieceSpace[0].length / 2] = id;
                break;
            }
            case (5): {
                tetrisPieceSpace[0][tetrisPieceSpace[0].length / 2 - 2] = id;
                tetrisPieceSpace[0][tetrisPieceSpace[0].length / 2 - 1] = id;
                tetrisPieceSpace[0][tetrisPieceSpace[0].length / 2] = id;
                tetrisPieceSpace[1][tetrisPieceSpace[0].length / 2 - 1] = id;
                break;
            }
            case (6): {
                for (int i = 0; i < 3; i++) {
                    tetrisPieceSpace[i][tetrisPieceSpace[0].length / 2] = id;
                }
                tetrisPieceSpace[0][tetrisPieceSpace[0].length / 2 - 1] = id;
                break;
            }
            case (7): {
                for (int i = 0; i < 3; i++) {
                    tetrisPieceSpace[i][tetrisPieceSpace[0].length / 2 - 1] = id;
                }
                tetrisPieceSpace[0][tetrisPieceSpace[0].length / 2] = id;
            }
        }
    }

    public void moveLeft() {
        if (noBorderCollision(0)) {
            for (int i = 0; i < tetrisPieceSpace.length; i++) {
                for (int j = 0; j < tetrisPieceSpace[i].length - 1; j++) {
                    tetrisPieceSpace[i][j] = tetrisPieceSpace[i][j + 1];
                }
                tetrisPieceSpace[i][tetrisPieceSpace[i].length - 1] = 0;
            }
        }
    }

    public void moveRight() {
        if (noBorderCollision(tetrisPieceSpace[0].length - 1)) {
            for (int i = 0; i < tetrisPieceSpace.length; i++) {
                for (int j = tetrisPieceSpace[i].length - 1; j > 0; j--) {
                    tetrisPieceSpace[i][j] = tetrisPieceSpace[i][j - 1];
                }
                tetrisPieceSpace[i][0] = 0;
            }
        }
    }

    public void moveDown() {

        for (int i = tetrisPieceSpace.length - 1; i > 0; i--) {
            for (int j = 0; j < tetrisPieceSpace[i].length; j++) {
                tetrisPieceSpace[i][j] = tetrisPieceSpace[i - 1][j];
            }
        }
        Arrays.fill(tetrisPieceSpace[0], 0);

    }

    public void rotate(int[][] gameInfo) {
        int indexI = tetrisPieceSpace.length;
        int indexJ = tetrisPieceSpace[0].length;
        for (int i = 0; i < tetrisPieceSpace.length; i++) {
            for (int j = 0; j < tetrisPieceSpace[i].length; j++) {
                if (tetrisPieceSpace[i][j] > 0) {
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
            if (indexI + i < tetrisPieceSpace.length)
                rotatableMatrix[i] = Arrays.copyOfRange(tetrisPieceSpace[indexI + i], indexJ, indexJ + 5);
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
            for(int[] row: tetrisPieceSpace){
                Arrays.fill(row,0);
            }
            for(int i=0;i<rotatableMatrix.length;i++){
                for(int j=0;j<rotatableMatrix[i].length;j++){
                    if(rotatableMatrix[i][j]>0)
                        tetrisPieceSpace[i+indexI][j+indexJ-k]=rotatableMatrix[i][j];
                }
            }

        }


    }

    private boolean noBorderCollision(int column) {
        for (int[] row : tetrisPieceSpace) {
            if (row[column] > 0) {
                return false;
            }
        }
        return true;
    }

    public boolean noBottomBorderCollision() {
        for (int j = 0; j < tetrisPieceSpace[0].length; j++) {
            if (tetrisPieceSpace[tetrisPieceSpace.length - 1][j] > 0) {
                return false;
            }
        }
        return true;
    }
}
