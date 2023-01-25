import java.util.ArrayList;

public class Pawn extends Man
{
    private boolean epPinned; // True if an absolute pin along the rank restrains the pawn from performing an otherwise legal en passant. False otherwise.

    // epPinned starts false as the pawn is not initially restrained from performing en passant by a pin
    public Pawn(Side side, Square square)
    {
        super(side, square);
        epPinned = false;
    }

    /**
     * @return Capital "P" if white pawn, lower-case "p" if black pawn
     */
    @Override
    public String toString()
    {
        return getColor() == Side.WHITE ? "P" : "p";
    }

    /**
     * @return Unicode character of Pawn with color
     */
    @Override
    protected String getPicture()
    {
        return getColor() == Side.WHITE ? "♟" : "♙";
    }

    /**
     * Calculates the Pawn's vision from S
     * If white, the squares NW and NE of it. If black, SW and SE.
     *
     * @param s A Square on a chess Board
     * @return An ArrayList of Squares in the Pawn's vision from S
     */
    @Override
    protected ArrayList<Square> calcVisionFromSquare(Square s)
    {
        ArrayList<Square> ret = new ArrayList<>();

        if (side == Side.WHITE)
        {
            Utils.addIfNotNull(ret, s.getNWSquare());
            Utils.addIfNotNull(ret, s.getNESquare());
        }
        else
        {
            Utils.addIfNotNull(ret, s.getSWSquare());
            Utils.addIfNotNull(ret, s.getSESquare());
        }

        return ret;
    }

    /**
     * Empties and refills the ArrayList<Move> legalMoves with all legal moves for the pawn
     */
    @Override
    public void setLegalMoves() throws InvalidPieceException, InvalidMoveException, InvalidSquareException
    {
        legalMoves.clear();

        Board b = square.getBoard();

        /*
        If taking an opponent's piece is possible, add taking as a move.
        If the pawn is one rank away from promotion and can take, add taking and promoting as a legal move.
         */
        for (Square s : vision)
        {
            if (s.getPiece() != null && s.getPiece().getColor() != this.getColor() && (!pinned || pinStraight.getIndexOfSquare(s) >= 0))
            {
                if (square.getRankInt() == (side == Side.WHITE ? 7 : 2))
                {
                    legalMoves.add(new Move(this, s, b, 'Q'));
                    legalMoves.add(new Move(this, s, b, 'R'));
                    legalMoves.add(new Move(this, s, b, 'B'));
                    legalMoves.add(new Move(this, s, b, 'N'));
                }
                else
                {
                    legalMoves.add(new Move(this, s, b));
                }
            }
        }

        /*
        If the pawn hasn't moved, add up to two squares forward as legal moves,
        If the pawn is one rank away from promotion, add moving one square forward and promoting as legal moves.
        Otherwise, add moving one square forward as a legal move.
        In the last case, if en passant is possible and the pawn isn't laterally pinned, add en passant capture as a legal move.
        In all cases, check to ensure the pawn is moving into an empty square.
         */
        if (side == Side.WHITE)
        {
            Square s = square.getNorthSquare();
            if (square.getRankInt() == 2)
            {
                if (s.getPiece() == null)
                {
                    legalMoves.add(new Move(this, s, b));
                    if (s.getNorthSquare().getPiece() == null)
                    {
                        legalMoves.add(new Move(this, s.getNorthSquare(), b));
                    }
                }
            }
            else if (square.getRankInt() == 7)
            {
                if (s.getNorthSquare() == null)
                {
                    legalMoves.add(new Move(this, s, b, 'Q'));
                    legalMoves.add(new Move(this, s, b, 'R'));
                    legalMoves.add(new Move(this, s, b, 'B'));
                    legalMoves.add(new Move(this, s, b, 'N'));
                }
            }
            else
            {
                if (!epPinned && square.getRankInt() == 5)
                {
                    Square ep = b.getEnPassantSquare();
                    if (ep != null && (ep == square.getNWSquare() || ep == square.getNESquare()))
                    {
                        legalMoves.add(new Move(this, ep, b));
                    }
                }
                if (s.getPiece() == null)
                {
                    legalMoves.add(new Move(this, s, b));
                }
            }
        }
        else
        {
            Square s = square.getSouthSquare();
            if (square.getRankInt() == 7)
            {
                if (s.getPiece() == null)
                {
                    legalMoves.add(new Move(this, s, b));
                    if (s.getSouthSquare().getPiece() == null)
                    {
                        legalMoves.add(new Move(this, s.getSouthSquare(), b));
                    }
                }
            }
            else if (square.getRankInt() == 2)
            {
                if (s.getPiece() == null)
                {
                    legalMoves.add(new Move(this, s, b, 'Q'));
                    legalMoves.add(new Move(this, s, b, 'R'));
                    legalMoves.add(new Move(this, s, b, 'B'));
                    legalMoves.add(new Move(this, s, b, 'N'));
                }
            }
            else
            {
                if (!epPinned && square.getRankInt() == 4)
                {
                    Square ep = b.getEnPassantSquare();
                    if (ep != null && (ep == square.getSWSquare() || ep == square.getSESquare()))
                    {
                        legalMoves.add(new Move(this, ep, b));
                    }
                }
                if (s.getPiece() == null)
                {
                    legalMoves.add(new Move(this, s, b));
                }
            }
        }
    }

    /**
     * Sets the value of epPinned to true, preventing the pawn from taking en passant.
     */
    protected void epPin()
    {
        epPinned = true;
    }

    /**
     * Sets the value of epPinned to false, allowing the pawn to take en passant.
     */
    protected void epUnpin()
    {
        epPinned = false;
    }
}
