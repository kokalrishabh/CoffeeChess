import java.util.ArrayList;

public class King extends Piece
{
    private Straight checkedOn1;
    private Straight checkedOn2;
    private Man man1;
    private Man man2;

    public King(Side side, Square square)
    {
        super(side, square);
        checkedOn1 = null;
        checkedOn2 = null;
        man1 = null;
        man2 = null;
    }

    /**
     * @return Capital "K" if white king, lower-case "k" if black king
     */
    @Override
    public String toString()
    {
        return getColor() == Side.WHITE ? "K" : "k";
    }

    /**
     * @return Unicode character of King with color
     */
    @Override
    protected String getPicture()
    {
        return getColor() == Side.WHITE ? "♚" : "♔";
    }

    /**
     * Calculates the King's vision from S
     *
     * @param s A Square on a chess Board
     * @return An ArrayList of Squares in the King's vision from S
     */
    @Override
    protected ArrayList<Square> calcVisionFromSquare(Square s)
    {
        return new ArrayList<>((s.getAllAdjacentSquares()));
    }

    /**
     * Empties and refills LEGALMOVES with all legal moves for the king
     */
    @Override
    public void setLegalMoves() throws InvalidPieceException, InvalidSquareException, InvalidMoveException
    {
        legalMoves.clear();

        Board b = square.getBoard();
        Player opponent = (side == Side.WHITE ? b.getBlack() : b.getWhite());

        /*
        For every square in the king's vision, if moving to that square is not moving into check or taking the player's
        own piece, add that square as a legal move for the king
         */
        for (Square s : vision)
        {
            if         (!opponent.getVision().contains(s)
                    && (checkedOn1 == null || checkedOn1.getIndexOfSquare(s) == -1 || s.getPiece() == man1)
                    && (checkedOn2 == null || checkedOn2.getIndexOfSquare(s) == -1 || s.getPiece() == man2)
                    && (s.getPiece() == null || s.getPiece().getColor() != this.getColor()))
            {
                legalMoves.add(new Move(this, s, b));
            }
        }

        // If castling to each side is legal, add castling as a legal move for the king
        if (side == Side.WHITE)
        {
            if (b.isWhiteOO())
            {
                legalMoves.add(new Move(this, square.getEastSquare().getEastSquare(), b));
            }
            if (b.isWhiteOOO())
            {
                legalMoves.add(new Move(this, square.getWestSquare().getWestSquare(), b));
            }
        }
        else
        {
            if (b.isBlackOO())
            {
                legalMoves.add(new Move(this, square.getEastSquare().getEastSquare(), b));
            }
            if (b.isBlackOOO())
            {
                legalMoves.add(new Move(this, square.getWestSquare().getWestSquare(), b));
            }
        }
    }

    /**
     * Called either to check or uncheck. Sets variables accordingly to prevent a king in check from moving along the
     * straight on which it is in check from moving away from the checking piece along the straight, thereby moving out
     * of the checking piece's VISION, but not out of its actual vision.
     *
     * @param s1 Straight 1 along which the king is in check
     * @param s2 Straight 2 along which the king is in check
     * @param m1 Piece on S1 checking the king
     * @param m2 Piece on S2 checking the king
     */
    protected void check(Straight s1, Straight s2, Man m1, Man m2)
    {
        checkedOn1 = s1;
        checkedOn2 = s2;
        man1 = m1;
        man2 = m2;
    }
}
