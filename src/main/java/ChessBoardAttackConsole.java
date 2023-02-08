import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

// Убираем все тесты, работаем :)
// Чтобы обновилось

public class ChessBoardAttackConsole {
    private static char[][] chessBoard;
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
    private static void initBoard(int x, int y, int k){
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
            initBoard (x, y, k);
        }

    }

    private static boolean playRound() throws IOException {
        do {
            System.out.println("Введиде на экран координаты в шахматной нотации");
            String buffer = SCANNER.nextLine().toUpperCase();
            int coordX = (int) buffer.charAt(0);
            coordX -= 65;
            int coordY = Integer.parseInt(buffer.substring(1)) - 1;
        } while (!isCellValid(coordX, coordY));

        if (isCellEmpty(coordX, coordY) || isCellOpen(coordX, coordY)){
            chessBoard[coordX][coordY] = DOT_OPEN;
            return true;
        } else {
            // просто поставить печать "вы проиграли" - пока так
            System.out.println("Вы проиграли");
            return false;
        }
    }

    // Процедура лишена смысла, вывести можно и в цикле.
    // убрать, сейчас просто тестируем ввод и проверку координат
    // Процедура удалена, вывод переннесен в цикл.


    private static void ifReadCoordinates() throws IOException {
        if(readCoordinates()){
            readCoordinates();
        }
    }

    public static void main(String... args) throws IOException {
        initBoard(10, 10, 6);
        printBoard(true);
        printBoard(false);

        while (playRound()){
            printBoard(true);
            printBoard(false);
        }
        System.out.println("Игра окончена");



    }
}
