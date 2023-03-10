import java.util.ArrayList;
import java.util.Collection;

public class ChessGame {

    private final String INITIAL_POSITION = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    private Board board;
    private int turn;
    private boolean[] castlingRights;
    private Square enPassant;
    private int _50moveCounter;
    private int fullMoveCounter;

    public ChessGame(String FEN){
        String[] split = FEN.split(" ");
        //set up board
        board = new Board(split[0]);

        //identify turn
        if(split[1].equals("b")){
            turn = -1;
        } else {
            turn = 1;
        }

        //castling rights
        castlingRights = new boolean[4];
        if(split[2].contains("K")){
            castlingRights[0] = true;
        }
        if(split[2].contains("Q")){
            castlingRights[1] = true;
        }
        if(split[2].contains("k")){
            castlingRights[2] = true;
        }
        if(split[2].contains("q")){
            castlingRights[3] = true;
        }
        //en passant
        if(!split[3].equals("-")) {
            enPassant = new Square(split[3]);
        } else {
            enPassant = null;
        }

        //50 move rule
        try{
            _50moveCounter = Integer.parseInt(split[4]);
        } catch(Exception e){
            _50moveCounter = 0;
        }

        //# moves
        try{
            fullMoveCounter = Integer.parseInt(split[5]);
        } catch(Exception e){
            fullMoveCounter = 0;
        }
    }
    ChessGame(){
        new ChessGame(INITIAL_POSITION);
    }

    public void movePiece(Piece p, Square s){
        if(turn != p.isWhite){
            return;
        }

        switch (p.pieceType){
            case W_KING -> {
                ArrayList<Square> illegalSquares = new ArrayList<>();
                for(Piece b : board.getBlackPieces()){
                    illegalSquares.addAll(controlledSquares(b));
                }
                if(illegalSquares.contains(s)){
                    return;
                }
                if(controlledSquares(p).contains(s) && board.getPiece(s).isWhite != p.isWhite){
                    board.movePiece(p.getSquare(), s);
                }
                enPassant = null;
            }

            case B_KING -> {
                ArrayList<Square> illegalSquares = new ArrayList<>();
                for(Piece w : board.getWhitePieces()){
                    illegalSquares.addAll(controlledSquares(w));
                }
                if(illegalSquares.contains(s)){
                    return;
                }
                if(controlledSquares(p).contains(s) && board.getPiece(s).isWhite != p.isWhite){
                    board.movePiece(p.getSquare(), s);
                }
                enPassant = null;
            }

            case W_PAWN, B_PAWN -> {
                //one square forward
                int[] loc = p.getSquare().toIntArray();
                if(s.equals(new Square(loc[0], loc[1] + p.isWhite))){
                    if(board.getPiece(s).isWhite != p.isWhite){
                        board.movePiece(p.getSquare(), s);
                        enPassant = null;
                    }
                }
                //two square first move
                if(s.equals(new Square(loc[0], loc[1] + p.isWhite * 2)) && board.getPiece(new Square(loc[0], loc[1] + p.isWhite)) == null){
                    if(board.getPiece(s).isWhite != p.isWhite){
                        board.movePiece(p.getSquare(), s);
                        enPassant = new Square(loc[0], loc[1] + p.isWhite);
                    }
                }

                //capture
                if(controlledSquares(p).contains(s) && board.getPiece(s).isWhite != p.isWhite){
                    board.movePiece(p.getSquare(), s);
                    enPassant = null;
                }
                //en passant
                if(s.equals(enPassant)){
                    board.enPassant(p.getSquare(), s, p.isWhite);
                    enPassant = null;
                }

                _50moveCounter = 0;
            }

            case W_BISHOP, W_KNIGHT, W_QUEEN, W_ROOK, B_BISHOP, B_KNIGHT, B_QUEEN, B_ROOK -> {
                if(controlledSquares(p).contains(s) && board.getPiece(s).isWhite != p.isWhite){
                    board.movePiece(p.getSquare(), s);
                }
                enPassant = null;
                _50moveCounter ++;

            }
        }
        if(p.isWhite == -1){
            fullMoveCounter ++;
        }
        turn *= -1;

        if(_50moveCounter >= 100){
            System.out.println("draw game");
        }
    }

    public Collection<Square> controlledSquares(Piece p){

        int[] loc = p.getSquare().toIntArray();

        Collection<Square> controlled = new ArrayList<>();

        switch(p.pieceType){
            case W_PAWN, B_PAWN -> {
                Square current;
                //captures
                if(loc[0] > 0) {
                    current = new Square(loc[0] - 1, loc[1] + p.isWhite);
                    controlled.add(current);
                }
                if(loc[0] < 7) {
                    current = new Square(loc[0] + 1, loc[1] + p.isWhite);
                    controlled.add(current);
                }
            }

            case W_ROOK, B_ROOK -> {
                int temp = loc[0] + 1;
                Square current;
                //right
                while(temp < 8){
                    current = new Square(temp, loc[1]);
                    controlled.add(current);
                    if(board.getPiece(current) != null){
                        break;
                    }
                    temp ++;
                }
                //left
                temp = loc[0] - 1;
                while(temp > -1){
                    current = new Square(temp, loc[1]);
                    controlled.add(current);
                    if(board.getPiece(current) != null){
                        break;
                    }
                    temp --;
                }
                //up?
                temp = loc[1] + 1;
                while(temp < 8){
                    current = new Square(loc[0], temp);
                    controlled.add(current);
                    if(board.getPiece(current) != null){
                        break;
                    }
                    temp ++;
                }
                //down?
                temp = loc[1] - 1;
                while(temp > -1){
                    current = new Square(loc[0], temp);
                    controlled.add(current);
                    if(board.getPiece(current) != null){
                        break;
                    }
                    temp --;
                }
            }
            case W_KNIGHT, B_KNIGHT -> {
                Square current;
                //right
                if(loc[0] < 6){
                    if(loc[1] > 0){
                        current = new Square(loc[0] + 2, loc[1] - 1);
                        controlled.add(current);
                    }
                    if(loc[1] < 7){
                        current = new Square(loc[0] + 2, loc[1] + 1);
                        controlled.add(current);
                    }
                }
                //left
                if(loc[0] > 1){
                    if(loc[1] > 0){
                        current = new Square(loc[0] - 2, loc[1] - 1);
                        controlled.add(current);
                    }
                    if(loc[1] < 7){
                        current = new Square(loc[0] - 2, loc[1] + 1);
                        controlled.add(current);
                    }
                }
                //up
                if(loc[1] < 6){
                    if(loc[0] > 0){
                        current = new Square(loc[0] - 1, loc[1] + 2);
                        controlled.add(current);
                    }
                    if(loc[0] < 7){
                        current = new Square(loc[0] + 1, loc[1] + 2);
                        controlled.add(current);
                    }
                }
                //down
                if(loc[1] > 1){
                    if(loc[0] > 0){
                        current = new Square(loc[0] - 1, loc[1] - 2);
                        controlled.add(current);
                    }
                    if(loc[0] < 7){
                        current = new Square(loc[0] + 1, loc[1] - 2);
                        controlled.add(current);
                    }
                }
            }
            case W_BISHOP, B_BISHOP -> {
                int temp = loc[0] + 1;
                int temp2 = loc[1] + 1;
                Square current;
                //q1
                while(temp < 8 && temp2 < 8){
                    current = new Square(temp, temp2);
                    controlled.add(current);
                    if(board.getPiece(current) != null){
                        break;
                    }
                    temp ++;
                    temp2 ++;
                }
                //q2
                temp = loc[0] - 1;
                temp2 = loc[1] + 1;
                while(temp > -1 && temp2 < 8){
                    current = new Square(temp, temp2);
                    controlled.add(current);
                    if(board.getPiece(current) != null){
                        break;
                    }
                    temp --;
                    temp2 ++;
                }
                //q3
                temp = loc[0] - 1;
                temp2 = loc[1] - 1;
                while(temp > -1 && temp2 > -1){
                    current = new Square(temp, temp2);
                    controlled.add(current);
                    if(board.getPiece(current) != null){
                        break;
                    }
                    temp --;
                    temp2 --;
                }
                //q4
                temp = loc[0] + 1;
                temp2 = loc[1] - 1;
                while(temp > -1){
                    current = new Square(temp, temp2);
                    controlled.add(current);
                    if(board.getPiece(current) != null){
                        break;
                    }
                    temp ++;
                    temp2 --;
                }
            }
            case W_QUEEN, B_QUEEN -> {
                int temp = loc[0] + 1;
                Square current;
                //right
                while(temp < 8){
                    current = new Square(temp, loc[1]);
                    controlled.add(current);
                    if(board.getPiece(current) != null){
                        break;
                    }
                    temp ++;
                }
                //left
                temp = loc[0] - 1;
                while(temp > -1){
                    current = new Square(temp, loc[1]);
                    controlled.add(current);
                    if(board.getPiece(current) != null){
                        break;
                    }
                    temp --;
                }
                //up?
                temp = loc[1] + 1;
                while(temp < 8){
                    current = new Square(loc[0], temp);
                    controlled.add(current);
                    if(board.getPiece(current) != null){
                        break;
                    }
                    temp ++;
                }
                //down?
                temp = loc[1] - 1;
                while(temp > -1){
                    current = new Square(loc[0], temp);
                    controlled.add(current);
                    if(board.getPiece(current) != null){
                        break;
                    }
                    temp --;
                }
                temp = loc[0] + 1;
                int temp2 = loc[1] + 1;
                //q1
                while(temp < 8 && temp2 < 8){
                    current = new Square(temp, temp2);
                    controlled.add(current);
                    if(board.getPiece(current) != null){
                        break;
                    }
                    temp ++;
                    temp2 ++;
                }
                //q2
                temp = loc[0] - 1;
                temp2 = loc[1] + 1;
                while(temp > -1 && temp2 < 8){
                    current = new Square(temp, temp2);
                    controlled.add(current);
                    if(board.getPiece(current) != null){
                        break;
                    }
                    temp --;
                    temp2 ++;
                }
                //q3
                temp = loc[0] - 1;
                temp2 = loc[1] - 1;
                while(temp > -1 && temp2 > -1){
                    current = new Square(temp, temp2);
                    controlled.add(current);
                    if(board.getPiece(current) != null){
                        break;
                    }
                    temp --;
                    temp2 --;
                }
                //q4
                temp = loc[0] + 1;
                temp2 = loc[1] - 1;
                while(temp > -1){
                    current = new Square(temp, temp2);
                    controlled.add(current);
                    if(board.getPiece(current) != null){
                        break;
                    }
                    temp ++;
                    temp2 --;
                }
            }
            case W_KING, B_KING -> {
                Square current;
                //right
                if(loc[0] < 7) {
                    current = new Square(loc[0] + 1, loc[1]);
                    controlled.add(current);
                    if(board.getPiece(current) == null){
                        //KCastle
                        if(loc[0] < 6 && castlingRights[1 - p.isWhite]) {
                            current = new Square(loc[0] + 2, loc[1]);
                            if (board.getPiece(current) == null) {
                                controlled.add(current);
                            }
                        }
                    }

                }
                //left
                if(loc[0] > 0) {
                    current = new Square(loc[0] + 1, loc[1]);
                    controlled.add(current);
                    if (board.getPiece(current) == null) {
                        //QCastle
                        if (loc[0] > 1 && castlingRights[2 - p.isWhite]) {
                            current = new Square(loc[0] - 2, loc[1]);
                            if (board.getPiece(current) == null) {
                                controlled.add(current);
                            }
                        }
                    }
                }
                //up
                if(loc[1] < 7) {
                    current = new Square(loc[0], loc[1] + 1);
                    controlled.add(current);
                    //q1
                    if(loc[0] < 7){
                        current = new Square(loc[0] + 1, loc[1] + 1);
                        controlled.add(current);
                    }
                    //q2
                    if(loc[0] > 0){
                        current = new Square(loc[0] - 1, loc[1] + 1);
                        controlled.add(current);
                    }
                }
                //down
                if(loc[1] > 0) {
                    current = new Square(loc[0], loc[1] + 1);
                    controlled.add(current);
                    //q3
                    if(loc[0] > 0){
                        current = new Square(loc[0] - 1, loc[1] - 1);
                        controlled.add(current);
                    }
                    //q4
                    if(loc[0] < 7){
                        current = new Square(loc[0] + 1, loc[1] - 1);
                        controlled.add(current);
                    }
                }
            }
        }
        return controlled;
    }
}
