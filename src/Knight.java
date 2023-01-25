import java.util.ArrayList;

public class Knight extends Piece
{

    public Knight(Side side, Square square) {
        super(side, square);
    }

    /**
     * @return Capital "N" if white knight, lower-case "n" if black knight
     */
    @Override
    public String toString()
    {
        return getColor() == Side.WHITE ? "N" : "n";
    }

    /**
     * @return Unicode character of Knight with color
     */
    @Override
    protected String getPicture()
    {
        return getColor() == Side.WHITE ? "♞" : "♘";
    }

    /**
     * Calculates the Knight's vision from S
     * Moves one square along each diagonal and finds the two squares orthogonally adjacent in the same direction to each square
     *
     * @param s A Square on a chess Board
     * @return An ArrayList of Squares in the Knight's vision from S
     */
    @Override
    protected ArrayList<Square> calcVisionFromSquare(Square s) {
        ArrayList<Square> ret = new ArrayList<>();

        Square nw = s.getNWSquare();
        Square ne = s.getNESquare();
        Square se = s.getSESquare();
        Square sw = s.getSWSquare();

        if (nw != null)
        {
            Utils.addIfNotNull(ret, nw.getNorthSquare());
            Utils.addIfNotNull(ret, nw.getWestSquare());
        }
        if (ne != null)
        {
            Utils.addIfNotNull(ret, ne.getNorthSquare());
            Utils.addIfNotNull(ret, ne.getEastSquare());
        }
        if (se != null)
        {
            Utils.addIfNotNull(ret, se.getSouthSquare());
            Utils.addIfNotNull(ret, se.getEastSquare());
        }
        if (sw != null)
        {
            Utils.addIfNotNull(ret, sw.getSouthSquare());
            Utils.addIfNotNull(ret, sw.getWestSquare());
        }

        return ret;
    }
}