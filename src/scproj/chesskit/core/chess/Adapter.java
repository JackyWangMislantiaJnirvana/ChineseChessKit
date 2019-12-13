package scproj.chesskit.core.chess;

public class Adapter {
    public static Adapter1Result adapter(String[] picture) {
        ChessGridElement[][] grid = new ChessGridElement[10][9];
        Adapter1Mistake mistake = Adapter1Mistake.Valid;
        String wrongMessage = null;
        int[] num = new int[14];                                          //收集棋子个数
        boolean t = false;                                                //判断是否有楚河汉界
        boolean LengthOk = false;                                          //判断长宽
        int top = 0;
        if (picture.length != 11) return new Adapter1Result(null, Adapter1Mistake.InvalidDimension, "InvalidDimension");
        else for (int i = 0; i < picture.length; i++) {
            char[] element = picture[i].toCharArray();
            if (element.length != 9) return new Adapter1Result(null, Adapter1Mistake.SpaceMissing, "SpaceMissing");
            for (int j = 0; j < element.length; j++) {
                if (element[j] == '.') grid[top][j] = ChessGridElement.EMPTY;
                else if (element[j] == 'C') {
                    num[0]++;
                    grid[top][j] = ChessGridElement.BLACK_VEHICLE;
                } else if (element[j] == 'H') {
                    num[1]++;
                    grid[top][j] = ChessGridElement.BLACK_RIDER;
                } else if (element[j] == 'E') {
                    num[2]++;
                    grid[top][j] = ChessGridElement.BLACK_MINISTER;
                } else if (element[j] == 'A') {
                    num[3]++;
                    grid[top][j] = ChessGridElement.BLACK_SERVANT;
                } else if (element[j] == 'G') {
                    num[4]++;
                    grid[top][j] = ChessGridElement.BLACK_GENERAL;
                } else if (element[j] == 'N') {
                    num[5]++;
                    grid[top][j] = ChessGridElement.BLACK_CANNON;
                } else if (element[j] == 'S') {
                    num[6]++;
                    grid[top][j] = ChessGridElement.BLACK_SOLDIER;
                } else if (element[j] == 'c') {
                    num[7]++;
                    grid[top][j] = ChessGridElement.RED_VEHICLE;
                } else if (element[j] == 'h') {
                    num[8]++;
                    grid[top][j] = ChessGridElement.RED_RIDER;
                } else if (element[j] == 'e') {
                    num[9]++;
                    grid[top][j] = ChessGridElement.RED_MINISTER;
                } else if (element[j] == 'a') {
                    num[10]++;
                    grid[top][j] = ChessGridElement.RED_SERVANT;
                } else if (element[j] == 'g') {
                    num[11]++;
                    grid[top][j] = ChessGridElement.RED_GENERAL;
                } else if (element[j] == 'n') {
                    num[12]++;
                    grid[top][j] = ChessGridElement.RED_CANNON;
                } else if (element[j] == 's') {
                    num[13]++;
                    grid[top][j] = ChessGridElement.RED_SOLDIER;
                } else if (element[j] == '-') {
                    t = true;
                    top--;
                    break;
                }
            }
            top++;
        }

        if (t) {
            mistake = Adapter1Mistake.RiverMissing;
            return new Adapter1Result(null, mistake, "RiverMissing");
        }
        if (Invalid(num, grid, wrongMessage)) {
            mistake = Adapter1Mistake.InvalidChessAmount;
            return new Adapter1Result(null, mistake, wrongMessage);
        }
        return new Adapter1Result(grid, mistake, wrongMessage);
    }

    private static boolean Invalid(int[] num, ChessGridElement[][] grid, String wrongMessage) {
        if (num[0] > 2) {
            wrongMessage = "BLACK VEHICLE number is out of range!";
            return true;
        }
        if (num[1] > 2) {
            wrongMessage = "BLACK RIDER number is out of range!";
            return true;
        }
        if (num[2] > 2) {
            wrongMessage = "BLACK MINISTER number is out of range!";
            return true;
        }
        if (num[3] > 2) {
            wrongMessage = "BLACK SERVANT number is out of range!";
            return true;
        }
        if (num[4] > 1 && num[4] <= 0) {
            wrongMessage = "BLACK GENERAL number is out of range!";
            return true;
        }
        if (num[5] > 2) {
            wrongMessage = "BLACK CANNON number is out of range!";
            return true;
        }
        if (num[6] > 5) {
            wrongMessage = "BLACK SOLDIER number is out of range!";
            return true;
        }
        if (num[7] > 2) {
            wrongMessage = "RED VEHICLE number is out of range!";
            return true;
        }
        if (num[8] > 2) {
            wrongMessage = "RED RIDER number is out of range!";
            return true;
        }
        if (num[9] > 2) {
            wrongMessage = "RED MINISTER number is out of range!";
            return true;
        }
        if (num[10] > 2) {
            wrongMessage = "RED SERVANT number is out of range!";
            return true;
        }
        if (num[11] > 1 && num[11] <= 0) {
            wrongMessage = "RED GENERAL number is out of range!";
            return true;
        }
        if (num[12] > 2) {
            wrongMessage = "RED CANNON number is out of range!";
            return true;
        }
        if (num[13] > 5) {
            wrongMessage = "RED SOLDIER number is out of range!";
            return true;
        }
        for (int i = 0; i <= 9; i++) {
            for (int j = 0; j <= 8; j++) {
                if (grid[i][j] == ChessGridElement.BLACK_SOLDIER &&
                        (i <= 2 || ((i == 3 || i == 4) && (j == 1 || j == 3 || j == 5 || j == 7)))) {
                    wrongMessage = "BLACK SOLDIER is on the wrong position(" + i + "," + j + ")";
                    return true;
                }
                if (grid[i][j] == ChessGridElement.BLACK_MINISTER &&
                        !((i == 0 && j == 2) || (i == 0 && j == 6) || (i == 2 && j == 0) || (i == 2 && j == 4) || (i == 2 && j == 8) || (i == 4 && j == 2) || (i == 4 && j == 6))) {
                    wrongMessage = "BLACK MINISTER is on the wrong position(" + i + "," + j + ")";
                    return true;
                }
                if (grid[i][j] == ChessGridElement.BLACK_SERVANT &&
                        !((i == 0 && j == 3) || (i == 0 && j == 5) || (i == 1 && j == 4) || (i == 2 && j == 3) || (i == 2 && j == 5))) {
                    wrongMessage = "BLACK SERVANT is on the wrong position(" + i + "," + j + ")";
                    return true;
                }
                if (grid[i][j] == ChessGridElement.BLACK_GENERAL &&
                        !(i >= 0 && i <= 2 && j >= 3 && j <= 5)) {
                    wrongMessage = "BLACK GENERAL is on the wrong position(" + i + "," + j + ")";
                    return true;
                }
                if (grid[i][j] == ChessGridElement.RED_SOLDIER &&
                        (i >= 7 || ((i == 6 || i == 5) && (j == 1 || j == 3 || j == 5 || j == 7)))) {
                    wrongMessage = "RED SOLDIER is on the wrong position(" + i + "," + j + ")";
                    return true;
                }
                if (grid[i][j] == ChessGridElement.RED_MINISTER &&
                        !((i == 9 && j == 2) || (i == 9 && j == 6) || (i == 7 && j == 0) || (i == 7 && j == 4) || (i == 7 && j == 8) || (i == 5 && j == 2) || (i == 5 && j == 6))) {
                    wrongMessage = "RED MINISTER is on the wrong position(" + i + "," + j + ")";
                    return true;
                }
                if (grid[i][j] == ChessGridElement.RED_SERVANT &&
                        !((i == 9 && j == 3) || (i == 9 && j == 5) || (i == 8 && j == 4) || (i == 7 && j == 3) || (i == 7 && j == 5))) {
                    wrongMessage = "RED SERVANT is on the wrong position(" + i + "," + j + ")";
                    return true;
                }
                if (grid[i][j] == ChessGridElement.RED_GENERAL &&
                        !(i <= 9 && i >= 7 && j >= 3 && j <= 5)) {
                    wrongMessage = "RED GENERAL is on the wrong position(" + i + "," + j + ")";
                    return true;
                }
            }
        }
        wrongMessage = "Loading perfectly";
        return false;
    }
}
