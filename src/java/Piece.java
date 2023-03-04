public class Piece {

    public enum PieceType{
        W_PAWN(1),
        W_ROOK(5),
        W_KNIGHT(3),
        W_BISHOP(3),
        W_QUEEN(9),
        W_KING(0),
        B_PAWN(1),
        B_ROOK(5),
        B_KNIGHT(3),
        B_BISHOP(3),
        B_QUEEN(9),
        B_KING(0);

        public final int points;

        PieceType(int p){
            points = p;
        }

    }

    public PieceType pieceType;
    public int isWhite;
    private Square square;
    public Piece(PieceType p, Square s) {
        this.pieceType = p;
        this.square = s;
        switch (pieceType) {
            case W_PAWN, W_ROOK, W_KNIGHT, W_BISHOP, W_QUEEN, W_KING -> isWhite = 1;
            case B_PAWN, B_ROOK, B_KNIGHT, B_BISHOP, B_QUEEN, B_KING -> isWhite = -1;
            default -> isWhite = 0;
        }
    }

    public Square getSquare() {
        return square;
    }

    public void setSquare(Square s){
        this.square = s;
    }
}
