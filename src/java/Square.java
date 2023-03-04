import java.util.Objects;

public class Square {
    private char file;
    private int rank;

    public Square(char file, int rank){
        this.file = file;
        this.rank = rank;
    }
    Square(String s){
        char[] split = s.toCharArray();
        if(s.length() != 2){
            return;
        }
        file = split[0];

        try{
            rank = Integer.parseInt(String.valueOf(split[1]));
        } catch (Exception ignored){

        }
    }

    Square(int i, int j){
        switch (i) {
            case 0 -> file = 'a';
            case 1 -> file = 'b';
            case 2 -> file = 'c';
            case 3 -> file = 'd';
            case 4 -> file = 'e';
            case 5 -> file = 'f';
            case 6 -> file = 'g';
            case 7 -> file = 'h';
        }
        rank = j + 1;
    }

    @Override
    public String toString(){
        return String.valueOf(file) + rank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Square square = (Square) o;
        return file == square.file && rank == square.rank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(file, rank);
    }

    public int[] toIntArray(){
        int[] pos = new int[2];
        switch (file) {
            case 'a' -> {}
            case 'b' -> pos[0] = 1;
            case 'c' -> pos[0] = 2;
            case 'd' -> pos[0] = 3;
            case 'e' -> pos[0] = 4;
            case 'f' -> pos[0] = 5;
            case 'g' -> pos[0] = 6;
            case 'h' -> pos[0] = 7;
        }
        pos[1] = rank - 1;

        return pos;
    }
}
