import java.util.ArrayList;

public class Queen extends Piece
{
    public Queen(Side side, Square square) {
        super(side, square);
    }

    /**
     * @return Capital "Q" if white queen, lower-case "q" if black queen
     */
    @Override
    public String toString()
    {
        return getColor() == Side.WHITE ? "Q" : "q";
    }

    /**
     * Calculates the Queen's vision from S
     *
     * @param s A Square on a chess Board
     * @return An ArrayList of Squares in the Bishop's vision from S
     */
    @Override
    protected ArrayList<Square> calcVisionFromSquare(Square s) {
        ArrayList<Square> ret = new ArrayList<>();
        ret.addAll(rank.getUnblockedVision(s));
        ret.addAll(file.getUnblockedVision(s));
        ret.addAll(diagonal1.getUnblockedVision(s));
        ret.addAll(diagonal2.getUnblockedVision(s));
        return ret;
    }

    /**
     * @return Unicode character of Queen with color
     */
    @Override
    protected String getPicture()
    {
        return getColor() == Side.WHITE ? "♛" : "♕";
    }
}
