import java.util.ArrayList;

public class Man
{
    protected Side side; // The color of the man
    protected Square square; // The Square on which the man currently stands
    protected Rank rank; // The Rank of the Square on which the man currently stands
    protected File file; // The File of the Square on which the man currently stands
    protected Diagonal diagonal1, diagonal2; // The Diagonals of the Square on which the man currently stands
    protected ArrayList<Square> vision; // All Squares visible to the man
    protected ArrayList<Move> legalMoves; // All legal moves available to the man
    protected boolean pinned; // True if the man is absolutely pinned. False otherwise.
    protected Straight pinStraight; // The straight along which the man is absolutely pinned if PINNED, null otherwise

    /**
     * Sets THIS.SIDE and THIS.SQUARE according to parameters.
     * Sets Rank, File, and Diagonals according to the parameter square.
     * Initializes ArrayLists to be empty.
     * Sets PINNED to false.
     *
     * @param side The color of the man
     * @param square The Square on which the man initially stands
     */
    public Man(Side side, Square square)
    {
        this.side = side;
        this.square = square;
        this.rank = square.getRank();
        this.file = square.getFile();
        this.diagonal1 = square.getDiagonal1();
        this.diagonal2 = square.getDiagonal2();
        this.vision = new ArrayList<>();
        this.legalMoves = new ArrayList<>();
        this.pinned = false;
        this.pinStraight = null;
    }

    /**
     * To be overridden by each leaf subclass.
     *
     * @return An empty string
     */
    protected String getPicture()
    {
        return "";
    }

    /**
     * @return The Square on which the man currently stands
     */
    public Square getSquare()
    {
        return square;
    }

    /**
     * @return The color of the man
     */
    public Side getColor()
    {
        return side;
    }

    /**
     * @return The list of all squares visible to the man
     */
    public ArrayList<Square> getVision()
    {
        return vision;
    }

    /**
     * To be overridden by each leaf subclass.
     *
     * @return null
     */
    protected ArrayList<Square> calcVisionFromSquare(Square s)
    {
        return null;
    }

    /**
     * Sets the ArrayList<Square> vision to the result of calculating the vision of the man from the Square on which
     * it currently stands.
     */
    protected void setVision()
    {
        vision = calcVisionFromSquare(square);
    }

    /**
     * Empties and refills the list of all legal moves for the man. To be overridden by the King and Pawn subclasses.
     *
     * If the man is pinned, it cannot move.
     * Otherwise, for every square it sees, if moving to it would not capture the player's own piece, add moving to it as a legal move for the man.
     */
    protected void setLegalMoves() throws InvalidPieceException, InvalidSquareException, InvalidMoveException
    {
        legalMoves.clear();

        for (Square s : vision)
        {
            if ((s.getPiece() == null || s.getPiece().getColor() != this.getColor()) && (!pinned || pinStraight.getIndexOfSquare(s) >= 0))
            {
                legalMoves.add(new Move(this, s, square.getBoard()));
            }
        }
    }

    protected boolean canMoveToSquare(Square s)
    {
        for (Move m : legalMoves)
        {
            if (m.getTo() == s)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * @return The Rank of the Square on which the man currently stands
     */
    protected Rank getRank()
    {
        return rank;
    }

    /**
     * @return The Rank of the Square on which the man currently stands
     */
    protected File getFile()
    {
        return file;
    }

    /**
     * @return The top-left-to-bottom-right Diagonal of the Square on which the man currently stands
     */
    protected Diagonal getDiagonal1()
    {
        return diagonal1;
    }

    /**
     * @return The bottom-left-to-top-right Diagonal of the Square on which the man currently stands
     */
    protected Diagonal getDiagonal2()
    {
        return diagonal2;
    }

    /**
     * @return The list of legal moves the man can make
     */
    protected ArrayList<Move> getLegalMoves()
    {
        return legalMoves;
    }

    /**
     * Unpins the man so it can move
     */
    protected void unpin()
    {
        pinned = false;
        pinStraight = null;
    }

    /**
     * @param s The straight on which THIS is absolutely pinned
     *
     * Pins the man so it can't move
     */
    protected void pin(Straight s)
    {
        pinned = true;
        pinStraight = s;
    }

    /**
     * Changes the square of the man. Called when the man moves.
     */
    protected void setSquare(Square s)
    {
        square = s;
    }

    /**
     * Sets the Rank of the man according to the Square on which it stands. Called when the man moves.
     */
    protected void setRank(Rank r)
    {
        rank = r;
    }

    /**
     * Sets the File of the man according to the Square on which it stands. Called when the man moves.
     */
    protected void setFile(File f)
    {
        file = f;
    }

    /**
     * Sets the top-left-to-bottom-right Diagonal of the man according to the Square on which it stands. Called when the man moves.
     */
    protected void setDiagonal1(Diagonal d)
    {
        diagonal1 = d;
    }

    /**
     * Sets the bottom-left-to-top-right Diagonal of the man according to the Square on which it stands. Called when the man moves.
     */
    protected void setDiagonal2(Diagonal d)
    {
        diagonal2 = d;
    }

    /**
     * @return The straight along which THIS is absolutely pinned
     */
    protected Straight getPinStraight()
    {
        return pinStraight;
    }
}
