import java.util.ArrayList;

public class Bishop extends Piece
{
    public Bishop(Side side, Square square)
    {
        super(side, square);
    }

    /**
     * @return Capital "B" if white bishop, lower-case "b" if black bishop
     */
    @Override
    public String toString()
    {
        return getColor() == Side.WHITE ? "B" : "b";
    }

    /**
     * Calculates the Bishop's vision from S
     *
     * @param s A Square on a chess Board
     * @return An ArrayList of Squares in the Bishop's vision from S
     */
    @Override
    protected ArrayList<Square> calcVisionFromSquare(Square s) {
        ArrayList<Square> ret = new ArrayList<>();
        ret.addAll(diagonal1.getUnblockedVision(s));
        ret.addAll(diagonal2.getUnblockedVision(s));
        return ret;
    }

    /**
     * @return Unicode character of Bishop with color
     */
    @Override
    protected String getPicture()
    {
        return getColor() == Side.WHITE ? "♝" : "♗";
    }
}
