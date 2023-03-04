import java.util.ArrayList;

public class Board {
    private Piece[][] board;
    private ArrayList<Piece> whitePieces;
    private ArrayList<Piece> blackPieces;

    public Board(String FEN){
        board = new Piece[8][8];
        whitePieces = new ArrayList<>();
        blackPieces = new ArrayList<>();

        String[] split = FEN.split("/");
        for (int i = 0; i < 8; i++){
            for(int j = 0; j < split.length; j++){
                Piece newPiece = null;
                switch (split[i].substring(j, j + 1)) {
                    case "P" -> {
                        newPiece = new Piece(Piece.PieceType.W_PAWN, new Square(i, j));
                        whitePieces.add(newPiece);
                    }
                    case "R" -> {
                        newPiece = new Piece(Piece.PieceType.W_ROOK, new Square(i, j));
                        whitePieces.add(newPiece);
                    }
                    case "N" -> {
                        newPiece = new Piece(Piece.PieceType.W_KNIGHT, new Square(i, j));
                        whitePieces.add(newPiece);
                    }
                    case "B" -> {
                        newPiece = new Piece(Piece.PieceType.W_BISHOP, new Square(i, j));
                        whitePieces.add(newPiece);
                    }
                    case "Q" -> {
                        newPiece = new Piece(Piece.PieceType.W_QUEEN, new Square(i, j));
                        whitePieces.add(newPiece);
                    }
                    case "K" -> {
                        newPiece = new Piece(Piece.PieceType.W_KING, new Square(i, j));
                        whitePieces.add(newPiece);
                    }
                    case "p" -> {
                        newPiece = new Piece(Piece.PieceType.B_PAWN, new Square(i, j));
                        blackPieces.add(newPiece);
                    }
                    case "r" -> {
                        newPiece = new Piece(Piece.PieceType.B_ROOK, new Square(i, j));
                        blackPieces.add(newPiece);
                    }
                    case "n" -> {
                        newPiece = new Piece(Piece.PieceType.B_KNIGHT, new Square(i, j));
                        blackPieces.add(newPiece);
                    }
                    case "b" -> {
                        newPiece = new Piece(Piece.PieceType.B_BISHOP, new Square(i, j));
                        blackPieces.add(newPiece);
                    }
                    case "q" -> {
                        newPiece = new Piece(Piece.PieceType.B_QUEEN, new Square(i, j));
                        blackPieces.add(newPiece);
                    }
                    case "k" -> {
                        newPiece = new Piece(Piece.PieceType.B_KING, new Square(i, j));
                        blackPieces.add(newPiece);
                    }
                    default -> {
                        try {
                            j += Integer.parseInt(split[i].substring(j, j + 1));
                        } catch (Exception ignored) {

                        }
                    }
                }
                board[i][j] = newPiece;
            }
        }
    }

    public Piece getPiece(Square square){
        int[] coords = square.toIntArray();
        return board[coords[0]][coords[1]];
    }

    public Piece movePiece(Square start, Square end){
        Piece move = getPiece(start);
        Piece captured = getPiece(end);

        int[] coords = end.toIntArray();
        board[coords[0]][coords[1]] = move;
        move.setSquare(captured.getSquare());

        switch (captured.pieceType) {
            case W_PAWN, W_ROOK, W_KNIGHT, W_BISHOP, W_QUEEN, W_KING -> whitePieces.remove(captured);
            case B_PAWN, B_ROOK, B_KNIGHT, B_BISHOP, B_QUEEN, B_KING -> blackPieces.remove(captured);
            default -> {}
        }

        return captured;
    }

    public Piece enPassant(Square start, Square end, int color){
        movePiece(start, end);
        int[] coords = end.toIntArray();
        Piece captured = getPiece(new Square(coords[0], coords[1] - color));
        board[coords[0]][coords[1] - color] = null;
        return captured;
    }

    public ArrayList<Piece> getBlackPieces() {
        return blackPieces;
    }

    public ArrayList<Piece> getWhitePieces() {
        return whitePieces;
    }
}
