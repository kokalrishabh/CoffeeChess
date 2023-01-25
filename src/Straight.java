import java.util.ArrayList;

public class Straight
{
    private final Square[] squares; // Array of all squares contained by the straight

    /**
     * Sets THIS.SQUARES to SQUARES.
     *
     * @param squares The array of all squares contained by the straight
     */
    public Straight(Square[] squares)
    {
        this.squares = squares;
    }

    /**
     * @param m A chessman
     * @return True if M is on a square on the straight, false otherwise
     */
    public boolean containsPiece(Man m)
    {
        return getIndexOfSquare(m.getSquare()) >= 0;
    }

    /**
     * @param s A Square on a Board
     * @return The index of the square if it is contained by the straight. -1 otherwise.
     */
    protected int getIndexOfSquare(Square s)
    {
        for (int i = 0; i < squares.length; i++)
        {
            if (s.equals(squares[i]))
            {
                return i;
            }
        }
        return -1;
    }

    /**
     *
     * @param s A Square on a Board
     * @return A list of all squares along the straight visible by a long-range piece from S. Null if S is not contained by the straight.
     */
    protected ArrayList<Square> getUnblockedVision(Square s)
    {
        ArrayList<Square> ret = new ArrayList<>();

        int index = getIndexOfSquare(s);
        if (index == -1)
        {
            return null;
        }

        for (int i = index - 1; i >= 0; i--)
        {
            Square curr = squares[i];
            ret.add(curr);
            if (curr.getPiece() != null)
            {
                break;
            }
        }
        for (int i = index + 1; i < squares.length; i++)
        {
            Square curr = squares[i];
            ret.add(curr);
            if (curr.getPiece() != null)
            {
                break;
            }
        }

        return ret;
    }

    /**
     * @return The array of squares contained by the straight
     */
    protected Square[] getSquares()
    {
        return squares;
    }

    /**
     *
     * @param i Index of square
     * @return The (8-i)th square of the rank
     */
    protected Square getSquareFromRank(int i)
    {
        return getSquares()[8-i];
    }
}
