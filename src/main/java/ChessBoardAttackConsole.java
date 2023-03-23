import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

// 12.02 - Немного почистил код, убран пучтые строки внутри процедур, так приятнее читать код


public class ChessBoardAttackConsole {
    private static char[][] chessBoard;
    private static int countFiguresOnBoard;
    private static int countAtTemps;
    private static final char[] FIGURES = {'K','Q','B','B','N','N','R','R','P','P','P','P','P','P','P','P'};
    private static final char DOT_EMPTY = '*';
    private static final char DOT_OPEN = ' ';
    private static final boolean DEBUG_MODE = false;
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Random RANDOM = new Random();
    private static int chessBoardX;
    private static int chessBoardY;

    private static void printHeaderFooter(){
        System.out.print("   ");
        for(int j=0; j<chessBoardY; j++){
            System.out.print((char) (65+j));
            System.out.print(" ");
        }
        System.out.println("");
    }

    private static void printBoard(boolean openFigures){
        DecimalFormat dF = new DecimalFormat("00");
        printHeaderFooter();
        for(int i =(chessBoardX-1); i>=0; i--){
            System.out.print(dF.format(i+1));
            System.out.print("|");
            for(int j=0; j<chessBoardY; j++){
                char currentSymbol = chessBoard[i][j];
                if(openFigures || DEBUG_MODE) {
                    System.out.print(currentSymbol);
                }else {
                    if(currentSymbol==DOT_OPEN){
                        System.out.print(DOT_OPEN);
                    } else {
                        System.out.print(DOT_EMPTY);
                    }
                }
                System.out.print("|");
            }
            System.out.println();
        }
        printHeaderFooter();
    }

    private static void chessPieces(int k){
        countFiguresOnBoard = k*16;
        for(int i=1; i<=k; i++) {
            for (int f = 0; f < FIGURES.length; f++) {
                int x, y;
                do {
                    x = RANDOM.nextInt(chessBoardX - 1)+1;
                    y = RANDOM.nextInt(chessBoardY);
                } while (!isCellEmpty(x, y));
                chessBoard[x][y] = FIGURES[f];
            }
        }
    }

    private static boolean isCellEmpty (int x, int y){
        return (chessBoard[x][y] == DOT_EMPTY);
    }
    private static boolean isCellOpen (int x, int y){
        return (chessBoard[x][y] == DOT_OPEN );
    }
    private static boolean isCellValid(int x,int y){
        return (x>=0 && y>=0 && x<chessBoardX && y<chessBoardY);
    }
    private static void initBoard(int x, int y, int k, int at){
        countAtTemps = at;
        if((x-1)*y>=k*16) {
            chessBoard = new char[x][y];
            chessBoardX = x;
            chessBoardY = y;
            for (char[] row : chessBoard) {
                Arrays.fill(row, DOT_EMPTY);
            }
            chessPieces(k);
        } else {
            System.out.println("Количество комплектов не вмещается на доске, будет размещенно максимально большое количство комплектов для данной строки");
            k = ((x-1)*y)/16;
            initBoard (x, y, k, at);
        }

    }

    private static boolean playRound() throws IOException {
        int coordX, coordY;
        boolean figureFound;

        do {
            figureFound = false;
            System.out.println("Введиде на экран координаты в шахматной нотации");
            String buffer = SCANNER.nextLine().toUpperCase();
            if(buffer.charAt(0) == '!'){
                figureFound = true;
                buffer = buffer.substring(1);
            }
            coordY = (int) buffer.charAt(0);
            coordY -= 65;
            coordX = Integer.parseInt(buffer.substring(1)) - 1;
        } while (!isCellValid(coordX, coordY));
        if(figureFound){
            System.out.printf("Этот ход является ходом снятия фигуры %d, %d %n", coordX, coordY );
            checkFigureFound(coordX, coordY);
        }

        if (isCellEmpty(coordX, coordY) || isCellOpen(coordX, coordY)){
            chessBoard[coordX][coordY] = DOT_OPEN;
            isAttached(coordX, coordY);
            return true;
        } else {
            // просто поставить печать "вы проиграли" - пока так
            System.out.println("Вы проиграли");
            return false;
        }
    }
    private static boolean checkFigureFound(int x, int y){
        if(isCellOpen(x, y)){
            System.out.println("Эта ячейка уже открыта");
            return true;
        }
        if (isCellEmpty(x, y)){
            countAtTemps--  ;
            if(countAtTemps == 0){
                System.out.println("У Вас все попытки закончились, Вы проиграли");
                return false;
            } else {
                System.out.printf("У Вас осталось %d попыток %n", countAtTemps );
                return true;
            }
        } else {
            System.out.printf("В этой ячейке была фигура %s, сейчас она снята %n", nameFigures(x, y));
            countFiguresOnBoard--;
            chessBoard[x][y] = DOT_OPEN;

        }
        if (countFiguresOnBoard == 0){
            System.out.println("Фигур больше не осталось, Вы выиграли");
            return false;
        } else {
            System.out.printf("На доске осталось %d фигур %n", countFiguresOnBoard);
            return true;
        }
    }

    private static String nameFigures(int x, int y){
        String name = null;

        switch (chessBoard[x][y]){
            case ('K'):
                name = "Король";
                break;
            case ('Q'):
                name = "Ферзь";
                break;
            case ('R'):
                name = "Ладья";
                break;
            case ('N'):
                name = "Конь";
                break;
            case ('B'):
                name = "Слон";
                break;
            case ('P'):
                name = "Пешка";
                break;
        }
        return name;
    }

    public static void isAttached(int x,int y){
        Knight(x, y);
    }

    public static void Knight(int x, int y){
        if ( chessBoard[x-1][y-2]== 'N'
                || chessBoard[x-2][y-1]== 'N'
                || chessBoard[x-2][y+1]== 'N'
                || chessBoard[x-1][y+2]== 'N'
                || chessBoard[x+1][y+2]== 'N'
                || chessBoard[x+2][y+1]== 'N'
                || chessBoard[x+2][y-1]== 'N'
                || chessBoard[x+1][y-2]== 'N'){
            System.out.println("Бьет конь");
        }
    }





    public static void main(String... args) throws IOException {
        initBoard(10, 10, 2, 5);
        printBoard(true);
        printBoard(false);
        while (playRound()){
            printBoard(true);
            printBoard(false);
        }
        System.out.println("Игра окончена");
    }
}
