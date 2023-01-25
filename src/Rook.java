import java.util.ArrayList;

public class Rook extends Piece
{
    public Rook(Side side, Square square)
    {
        super(side, square);
    }

    /**
     * @return Capital "R" if white bishop, lower-case "r" if black bishop
     */
    @Override
    public String toString()
    {
        return getColor() == Side.WHITE ? "R" : "r";
    }

    /**
     * Calculates the Rook's vision from S
     *
     * @param s A Square on a chess Board
     * @return An ArrayList of Squares in the Rook's vision from S
     */
    @Override
    protected ArrayList<Square> calcVisionFromSquare(Square s)
    {
        ArrayList<Square> ret = new ArrayList<>();
        ret.addAll(rank.getUnblockedVision(s));
        ret.addAll(file.getUnblockedVision(s));
        return ret;
    }

    /**
     * @return Unicode character of Rook with color
     */
    protected String getPicture()
    {
        return getColor() == Side.WHITE ? "♜" : "♖";
    }
}
