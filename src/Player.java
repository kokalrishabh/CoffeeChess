import java.util.ArrayList;

public class Player
{
    private final Side side; // The color with which the player is playing

    private ArrayList<Man> pieces; // The list of all pieces the player has on the board
    private King king; // The player's king
    private ArrayList<Queen> queens; // The list of all queens the player has on the board
    private ArrayList<Rook> rooks; // The list of all rooks the player has on the board
    private ArrayList<Bishop> bishops; // The list of all bishops the player has on the board
    private ArrayList<Knight> knights; // The list of all knights the player has on the board
    private ArrayList<Pawn> pawns; // The list of all pawns the player has on the board
    private ArrayList<Man>[] pieceLists; // The array of all lists containing pieces that the player has on the board

    private final ArrayList<Square> vision; // The list of all squares visible to at least one piece the player has on the board
    private final ArrayList<Move> legalMoves; // The list of all legal moves the player can make

    private boolean inCheck; // True if the player's king is in check. False otherwise.
    private boolean doubleCheck; // True if the player's king is in check by two pieces at once. False otherwise.
    private Man checkPiece; // The piece checking the player (only relevant if the player is not in double-check)

    /**
     * Sets SIDE to S.
     *
     * Initializes all lists to be empty lists.
     * Initializes PIECELISTS to contain the necessary lists.
     * Sets INCHECK AND DOUBLECHECK to false and CHECKPIECE to null.
     *
     * @param s The color with which the player is playing
     */
    public Player(Side s)
    {
        side = s;

        queens = new ArrayList<>();
        rooks = new ArrayList<>();
        bishops = new ArrayList<>();
        knights = new ArrayList<>();
        pawns = new ArrayList<>();

        vision = new ArrayList<>();
        pieces = new ArrayList<>();
        legalMoves = new ArrayList<>();
        pieceLists = new ArrayList[] {queens, rooks, bishops, knights, pawns};

        inCheck = false;
        doubleCheck = false;
        checkPiece = null;
    }

    /**
     * @return The color with which the player is playing
     */
    protected Side getColor()
    {
        return side;
    }

    /**
     * Adds M to PIECES and to the corresponding list
     *
     * @param m The piece being added to the player's pieces
     * @throws TooManyKingsException if M is a King and THIS.KING is not null
     */
    protected void givePiece(Man m) throws TooManyKingsException
    {
        pieces.add(m);

        if (m instanceof King)
        {
            if (king != null)
            {
                throw new TooManyKingsException((side == Side.WHITE ? "White" : "Black") + " already has a king.");
            }
            king = (King) m;
        }
        else if (m instanceof Queen)
        {
            queens.add((Queen) m);
        }
        else if (m instanceof Rook)
        {
            rooks.add((Rook) m);
        }
        else if (m instanceof Bishop)
        {
            bishops.add((Bishop) m);
        }
        else if (m instanceof Knight)
        {
            knights.add((Knight) m);
        }
        else if (m instanceof Pawn)
        {
            pawns.add((Pawn) m);
        }
    }

    /**
     * @return The list of all pieces the player has on the board
     */
    protected ArrayList<Man> getPieces()
    {
        return pieces;
    }

    /**
     * @return The player's king
     */
    protected King getKing()
    {
        return king;
    }

    /**
     * @return A list of the player's queens
     */
    protected ArrayList<Queen> getQueens()
    {
        return queens;
    }

    /**
     * @return A list of the player's rooks
     */
    protected ArrayList<Rook> getRooks()
    {
        return rooks;
    }

    /**
     * @return A list of the player's bishops
     */
    protected ArrayList<Bishop> getBishops()
    {
        return bishops;
    }

    /**
     * @return A list of the player's knights
     */
    protected ArrayList<Knight> getKnights()
    {
        return knights;
    }

    /**
     * @return A list of the player's pawns
     */
    protected ArrayList<Pawn> getPawns()
    {
        return pawns;
    }

    /**
     * @return A list of all squares visible to at least one piece the player has on the board
     */
    protected ArrayList<Square> getVision()
    {
        return vision;
    }

    /**
     * Empties VISION, then adds all squares seen by at least one piece the player has on the board
     */
    protected void setVision()
    {
        vision.clear();

        vision.addAll(king.getVision());

        for (ArrayList<Man> l : pieceLists)
        {
            for (Man m : l)
            {
                vision.addAll(m.getVision());
            }
        }
    }

    /**
     * Empties LEGALMOVES and refills it with all legal moves the player can make. Clear all illegal moves from respective pieces if in check.
     */
    protected void setLegalMoves()
    {
        legalMoves.clear();

        // Adds all legal king moves
        legalMoves.addAll(king.getLegalMoves());

        // Only king moves are legal in double-check
        if (doubleCheck)
        {
            for (ArrayList<Man> l : pieceLists)
            {
                l.clear();
            }
            return;
        }

        if (inCheck)
        {
            // If in check by a knight or a pawn, defend by capturing the checking piece or moving out of check
            if (checkPiece instanceof Knight || checkPiece instanceof Pawn)
            {
                for (ArrayList<Man> l : pieceLists)
                {
                    for (Man man : l)
                    {
                        ArrayList<Move> illegal = new ArrayList<>();
                        for (Move move : man.getLegalMoves())
                        {
                            if (move.getTo() == checkPiece.getSquare())
                            {
                                legalMoves.add(move);
                            }
                            else
                            {
                                illegal.add(move);
                            }
                        }
                        man.legalMoves.removeAll(illegal);
                    }
                }
            }
            // Otherwise, defend by capturing the checking piece, interposition, or moving out of check
            else
            {
                Straight straight;
                if (king.getRank().containsPiece(checkPiece))
                {
                    straight = king.getRank();
                }
                else if (king.getFile().containsPiece(checkPiece))
                {
                    straight  = king.getFile();
                }
                else if (king.getDiagonal1().containsPiece(checkPiece))
                {
                    straight = king.getDiagonal1();
                }
                else // if (king.getDiagonal2().containsPiece(checkPiece))
                {
                    straight = king.getDiagonal2();
                }

                int pieceIndex = straight.getIndexOfSquare(checkPiece.getSquare());
                int kingIndex = straight.getIndexOfSquare(king.getSquare());

                for (ArrayList<Man> l : pieceLists)
                {
                    for (Man man : l)
                    {
                        ArrayList<Move> illegal = new ArrayList<>();
                        for (Move move : man.getLegalMoves())
                        {
                            int toIndex = straight.getIndexOfSquare(move.getTo());

                            if (pieceIndex > kingIndex)
                            {
                                if (toIndex > kingIndex && toIndex <= pieceIndex)
                                {
                                    legalMoves.add(move);
                                }
                                else
                                {
                                    illegal.add(move);
                                }
                            }
                            else // if (pieceIndex < kingIndex)
                            {
                                if (toIndex < kingIndex && toIndex >= pieceIndex)
                                {
                                    legalMoves.add(move);
                                }
                                else
                                {
                                    illegal.add(move);
                                }
                            }
                        }
                        man.legalMoves.removeAll(illegal);
                    }
                }
            }
        }
        // If not in check, all legal moves are legal
        else
        {
            for (ArrayList<Man> l : pieceLists)
            {
                for (Man man : l)
                {
                    legalMoves.addAll(man.getLegalMoves());
                }
            }
        }
    }

    /**
     * @return A list of all legal moves
     */
    protected ArrayList<Move> getLegalMoves()
    {
        return legalMoves;
    }

    /**
     * Called if the player is in check, but not in double-check. Changes variables as necessary.
     *
     * @param m The piece putting the player's king in check
     */
    protected void check(Man m)
    {
        inCheck = true;
        doubleCheck = false;
        checkPiece = m;
    }

    /**
     * Called if the player is in double-check. Changes variables as necessary.
     */
    protected void doubleCheck()
    {
        inCheck = true;
        doubleCheck = true;
    }

    /**
     * Called when the player defends check
     */
    protected void uncheck()
    {
        inCheck = false;
        doubleCheck = false;
        checkPiece = null;
    }

    /**
     * Called when M is captured. Removes M from all lists.
     *
     * @param m The man being captured
     * @throws InvalidPieceException if tries to take a King
     */
    public void takePiece(Man m) throws InvalidPieceException
    {
        if (m instanceof King)
        {
            throw new InvalidPieceException("That can't be taken");
        }

        pieces.remove(m);

        if (m instanceof Queen)
        {
            queens.remove(m);
        }
        else if (m instanceof Rook)
        {
            rooks.remove(m);
        }
        else if (m instanceof Bishop)
        {
            bishops.remove(m);
        }
        else if (m instanceof Knight)
        {
            knights.remove(m);
        }
        else if (m instanceof Pawn)
        {
            pawns.remove(m);
        }
        else
        {
            throw new InvalidPieceException("You done messed up");
        }
    }

    /**
     * Unpins all of the player's pieces.
     */
    protected void unpinAll()
    {
        for (Man m : pieces)
        {
            m.unpin();
        }
    }

    /**
     * Call .pin() any pieces absolutely pinned
     * @see Man#pin(Straight)
     */
    protected void pinAll()
    {
        pinOnStraight(king.getRank());
        pinOnStraight(king.getFile());
        pinOnStraight(king.getDiagonal1());
        pinOnStraight(king.getDiagonal2());
    }

    /**
     * Starting with the king, traverse S in each direction checking for pins.
     * If any of the player's pieces are pinned, call .pin() on them.
     *
     * @param s The straight being checked for pins
     * @see Man#pin(Straight)
     */
    private void pinOnStraight(Straight s)
    {
        int index = s.getIndexOfSquare(king.getSquare());

        boolean flag = false;
        Man m = null;
        for (int i = index - 1; i >= 0; i--)
        {
            Square curr = s.getSquares()[i];
            Man currMan = curr.getPiece();
            if (currMan != null)
            {
                /*
                If the nearest piece along the straight in the negative direction is the player's
                and there is a pinning piece, call .pin() on that piece.
                 */
                if (flag)
                {
                    if (currMan.getColor() != side)
                    {
                        if (s instanceof Rank || s instanceof File)
                        {
                            if (currMan instanceof Queen || currMan instanceof Rook)
                            {
                                m.pin(s);
                            }
                        }
                        else if (s instanceof Diagonal)
                        {
                            if (currMan instanceof Queen || currMan instanceof Bishop)
                            {
                                m.pin(s);
                            }
                        }
                    }
                    break;
                }
                // If the nearest piece along the straight in the negative direction is the player's, note that piece.
                else
                {
                    if (currMan.getColor() == side)
                    {
                        m = currMan;
                        flag = true;
                    }
                    else
                    {
                        break;
                    }
                }
            }
        }

        flag = false;
        m = null;
        for (int i = index + 1; i < s.getSquares().length; i++)
        {
            Square curr = s.getSquares()[i];
            Man currMan = curr.getPiece();
            if (currMan != null)
            {
                /*
                If the nearest piece along the straight in the positive direction is the player's
                and there is a pinning piece, call .pin() on that piece.
                 */
                if (flag)
                {
                    if (currMan.getColor() != side)
                    {
                        if (s instanceof Rank || s instanceof File)
                        {
                            if (currMan instanceof Queen || currMan instanceof Rook)
                            {
                                m.pin(s);
                            }
                        }
                        else if (s instanceof Diagonal)
                        {
                            if (currMan instanceof Queen || currMan instanceof Bishop)
                            {
                                m.pin(s);
                            }
                        }
                    }
                    break;
                }
                // If the nearest piece along the straight in the positive direction is the player's, note that piece.
                else
                {
                    if (currMan.getColor() == side)
                    {
                        m = currMan;
                        flag = true;
                    }
                    else
                    {
                        break;
                    }
                }
            }
        }
    }

    /**
     * Unpins all pawns that were laterally pinned and thereby prevented from performing en passant captures.
     */
    protected void epUnpinAll()
    {
        for (Pawn p : pawns)
        {
            p.epUnpin();
        }
    }

    /**
     * Sets KING to null and resets all piece lists. Called when loading the board from a new FEN.
     */
    protected void reset()
    {
        king = null;
        pieces = new ArrayList<>();
        queens = new ArrayList<>();
        rooks = new ArrayList<>();
        bishops = new ArrayList<>();
        knights = new ArrayList<>();
        pawns = new ArrayList<>();
        pieceLists = new ArrayList[]{queens, rooks, bishops, knights, pawns};
    }
}
