import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Move
{
    // The String containing most valid SAN characters
    protected static final String VALID_SAN_CHARS = "KQRBNabcdefgh12345678xO-=+#";
    // The RegEx pattern any legal non-castling move notated in SAN must match
    private static final Pattern SAN_PATTERN = Pattern.compile("([KQRBN]?)([a-h]?)([1-8]?)(x?)([a-h][1-8])((=[QRBN])?)([+#]?)");

    private Man piece; // The piece moving
    private Square from; // The Square from which the piece is moving
    private Square to; // The Square to which the piece is moving

    private char promotionPiece; // The character representing the piece to which a pawn will promoted

    private String san; // The SAN notation of the move
    private final Player player; // The player moving
    private final Board board; // The board on which the move happens
    private boolean takes; // True if the move is a capture, false otherwise
    private Man takePiece; // The piece being captured
    private boolean enPassant; // True if the move is an en passant capture, false otherwise

    private Castle castle; // NO if the move is not a castle, KING if the move is a kingside castle, QUEEN if the move is a queenside castle
    private Square rookSquare; // The Square on which the rook involved in a castle is standing before the castle happens (i.e., a1, a8, h1, or h8)

    /**
     * Decode SAN to get all variable assignments. Throw an exception if this isn't possible.
     *
     * @param san The Standard Algebraic Notation for the move
     * @param b The Board on which the move is to occur
     */
    public Move(String san, Board b) throws InvalidMoveException, InvalidSquareException, InvalidPieceException
    {
        board = b;

        // Get the player moving
        if (b.getToMove() == Side.WHITE)
        {
            player = b.getWhite();
        }
        else
        {
            player = b.getBlack();
        }

        // Handle castling
        if (san.equals("O-O") || san.equals("O-O-O") || san.equals("0-0") || san.equals("0-0-0"))
        {
            this.san = san;
            setCastle();
            castle();
            return;
        }
        // If not castling, assign variables as applicable and continue
        castle = Castle.NO;
        rookSquare = null;

        // Create a RegEx matcher to test for non-castling and ensure SAN is a match
        Matcher matcher = SAN_PATTERN.matcher(san);

        if (!matcher.matches())
        {
            throw new InvalidMoveException("Invalid move");
        }

        /*
        Extract the substrings representing the piece, file, and rank as provided.
        Set TAKES to true if the move is a capture.
        Set TO to the Square to which the piece is moving.
        Set TAKEPIECE to the piece being captured if a piece is being captured, and null otherwise.
        Extract the substring representing promotion as provided.
         */
        String pieceNameStr = matcher.group(1);
        String pieceFileStr = matcher.group(2);
        String pieceRankStr = matcher.group(3);
        takes = matcher.group(4).equals("x");
        to = b.getSquare(matcher.group(5));
        takePiece = to.getPiece();
        String promotionStr = matcher.group(6);

        // Ensure that either the piece is provided or the move is a non-capturing pawn move
        if (pieceNameStr.equals(""))
        {
            if (pieceFileStr.equals(""))
            {
                if (takes || !pieceRankStr.equals(""))
                {
                    throw new InvalidPieceException("No piece provided");
                }
            }
            else
            {
                if (!takes)
                {
                    throw new InvalidMoveException("Non-capture pawn moves are notated with only the square to which the pawn moves (and any suffix symbols)");
                }
            }
        }
        // Ensure that either a capturing move ends on a Square containing a piece of the opposite color, or that the move is an en passant capture
        if (takes)
        {
            if (b.getEnPassantSquare() != to)
            {
                if (to.getPiece() == null)
                {
                    throw new InvalidMoveException("No piece to take on " + to.toString());
                }
                if (to.getPiece().getColor() == player.getColor())
                {
                    throw new InvalidMoveException("You can't take your own piece");
                }
            }
        }
        // Ensure that if a move contains a promotion, the move is a pawn move
        if (!pieceNameStr.equals("") && !promotionStr.equals(""))
        {
            throw new InvalidMoveException("Only pawns can promote");
        }
        // Ensure that promotions only happen on the correct rank
        int promotionRank = player.getColor() == Side.WHITE ? 8 : 1;
        if (to.getRankInt() != promotionRank && !promotionStr.equals(""))
        {
            throw new InvalidMoveException(Utils.toTitleCase(player.getColor().toString()) + " pawns can only promote on the " + Utils.toOrdinal(promotionRank) + " rank");
        }

        // Extract characters representing piece name, file, rank, and promotion as provided
        char pieceChar = 0;
        char pieceFile = 0;
        int pieceRank = 0;

        promotionPiece = 0;

        if (!pieceNameStr.equals(""))
            pieceChar = pieceNameStr.charAt(0);
        if (!pieceFileStr.equals(""))
            pieceFile = pieceFileStr.charAt(0);
        if (!pieceRankStr.equals(""))
            pieceRank = Integer.parseInt(pieceRankStr);
        if (!promotionStr.equals(""))
        {
            promotionPiece = promotionStr.charAt(1);
        }

        // Assign the correct chessman to PIECE

        // If piece
        if (pieceChar != 0)
        {
            // If the move is a king move, this is easy
            if (pieceChar == 'K')
            {
                piece = player.getKing();
            }
            else
            {
                // Get the list of all pieces of the necessary type, throwing an exception for pieces that don't exist
                ArrayList<Piece> possible0 = switch (pieceChar)
                {
                    case 'Q' -> new ArrayList<>(player.getQueens());
                    case 'R' -> new ArrayList<>(player.getRooks());
                    case 'B' -> new ArrayList<>(player.getBishops());
                    case 'N' -> new ArrayList<>(player.getKnights());
                    default -> null;
                };
                if (possible0 == null)
                {
                    throw new InvalidPieceException("Invalid piece");
                }

                // If ranks or files are provided, narrow down the list of possible pieces
                ArrayList<Piece> possible1 = new ArrayList<>();
                if (pieceFile != 0)
                {
                    File f = b.getFileFromLetter(pieceFile);
                    for (Piece p : possible0)
                    {
                        if (p.getFile() == f)
                        {
                            possible1.add(p);
                        }
                    }
                }
                else
                {
                    possible1 = possible0;
                }

                ArrayList<Piece> possible2 = new ArrayList<>();
                if (pieceRank != 0)
                {
                    Rank r = b.getRankFromNumber(pieceRank);
                    for (Piece p : possible1)
                    {
                        if (p.getRank() == r)
                        {
                            possible2.add(p);
                        }
                    }
                }
                else
                {
                    possible2 = possible1;
                }

                ArrayList<Piece> possible3 = new ArrayList<>();
                for (Piece p : possible2)
                {
                    if (p.canMoveToSquare(to))
                    {
                        possible3.add(p);
                    }
                }

                // Using the information, get the piece specified. If the piece was underspecified, throw an error.
                piece = getOnlyPossiblePiece(possible3);
            }
        }
        // Else if pawn
        else
        {
            // If the file is provided, i.e., if the move is a capture, this is relatively simple, as at most one pawn per file can capture on any particular square.
            if (pieceFile != 0)
            {
                File f = b.getFileFromLetter(pieceFile);

                if (pieceRank != 0)
                {
                    from = f.getSquareFromRank(pieceRank);
                    piece = from.getPiece();
                    if (piece == null)
                    {
                        throw new InvalidPieceException("No pawn on specified square");
                    }
                    if (!player.getPawns().contains(piece))
                    {
                        throw new InvalidPieceException(pieceFileStr + pieceRankStr + " does not have a " + Utils.toTitleCase(player.getColor().toString()) + " pawn");
                    }
                }
                else
                {
                    piece = getOnlyPossiblePawn(f);
                }
            }
            // Otherwise still relatively simple, as at most one pawn can move forward to any particular square.
            else
            {
                Square pawnSquare1 = player.getColor() == Side.WHITE ? to.getSouthSquare() : to.getNorthSquare();
                Man option1 = pawnSquare1.getPiece();

                if (option1 == null)
                {
                    if (to.getRankInt() == (player.getColor() == Side.WHITE ? 4 : 5))
                    {
                        Square pawnSquare2 = player.getColor() == Side.WHITE ? pawnSquare1.getSouthSquare() : pawnSquare1.getNorthSquare();
                        Man option2 = pawnSquare2.getPiece();

                        if (option2 == null)
                        {
                            throw new InvalidPieceException("No pawn can move to " + to.toString() + " without capturing");
                        }
                        else
                        {
                            if (option2 instanceof Pawn && option2.getColor() == player.getColor())
                            {
                                piece = option2;
                            }
                            else
                            {
                                throw new InvalidPieceException("No pawn can move to " + to.toString() + " without capturing");
                            }
                        }
                    }
                    else
                    {
                        throw new InvalidPieceException("No pawn can move to " + to.toString() + " without capturing");
                    }
                }
                else
                {
                    if (option1 instanceof Pawn && option1.getColor() == player.getColor())
                    {
                        piece = option1;
                    }
                    else
                    {
                        throw new InvalidPieceException("No pawn can move to " + to.toString() + " without capturing");
                    }
                }
            }
        }

        // Ensure a piece was found
        if (piece == null)
        {
            throw new InvalidPieceException("Invalid piece");
        }

        // Get the Square from which the piece moved
        from = piece.getSquare();

        // Handle en passant variable assignment
        enPassant = false;
        Square ep = b.getEnPassantSquare();
        if (ep != null && piece instanceof Pawn)
        {
            if (player.getColor() == Side.WHITE)
            {
                if ((ep == from.getNWSquare() || ep == from.getNESquare()) && to == ep)
                {
                    enPassant = true;
                    takes = true;
                }
            }
            else
            {
                if ((ep == from.getSWSquare() || ep == from.getSESquare()) && to == ep)
                {
                    enPassant = true;
                    takes = true;
                }
            }
        }

        // Get the Standard Algebraic Notation, excluding check and checkmate symbols, for the move
        this.san = getSAN();
    }

    /**
     * ARR contains a list of possible pieces with THIS.TO in their vision. Of them, only one can be found.
     * Finds said one by brute force.
     *
     * @param arr A list of possible pieces that can move to THIS.TO
     * @return The only possible piece that can move to THIS.TO
     */
    private Piece getOnlyPossiblePiece(ArrayList<Piece> arr) throws InvalidMoveException
    {
        boolean foundPieceFlag = false;
        Piece ret = null;

        for (Piece p : arr)
        {
            if (p.getVision().contains(to)) // TODO: Should this be `p.getVision().contains(to)` or `p.getLegalMoves().contains(to)`?
            {
                if (foundPieceFlag)
                {
                    throw new InvalidMoveException("Piece not properly specified");
                }
                else
                {
                    ret = p;
                    foundPieceFlag = true;
                }
            }
        }
        return ret;
    }

    /**
     * @param f A chessboard File
     * @return The only pawn on F with THIS.TO in its vision]
     */
    private Pawn getOnlyPossiblePawn(File f) throws InvalidMoveException
    {
        boolean foundPawnFlag = false;
        Pawn ret = null;
        Pawn p;

        for (Square s : f.getSquares())
        {
            if (s.getPiece() != null)
            {
                if (s.getPiece() instanceof Pawn)
                {
                    p = (Pawn) s.getPiece();
                    if (p.getColor() == player.getColor() && p.getVision().contains(to))
                    {
                        if (foundPawnFlag)
                        {
                            throw new InvalidMoveException("Too many pawns???");
                        }
                        else
                        {
                            ret = p;
                            foundPawnFlag = true;
                        }
                    }
                }
            }
        }
        return ret;
    }

    /**
     * Assigns instance variables as applicable
     *
     * @param piece The chessman moving
     * @param to The Square to which PIECE is moving
     * @param b The Board on which the move occurs
     * @param promotionPiece A character representing the piece to which a pawn will promoted
     */
    public Move(Man piece, Square to, Board b, char promotionPiece) throws InvalidMoveException, InvalidPieceException, InvalidSquareException {
        if (b.getToMove() == Side.WHITE)
        {
            player = b.getWhite();
        }
        else
        {
            player = b.getBlack();
        }

        this.board = b;
        this.piece = piece;
        this.from = piece.square;
        this.to = to;
        this.takePiece = to.getPiece();

        if (piece instanceof King && !piece.getVision().contains(to))
        {
            castle();
            return;
        }
        castle = Castle.NO;

        takes = false;
        if (takePiece != null)
        {
            if (to.getPiece().getColor() == player.getColor())
            {
                throw new InvalidMoveException("Destination square is already occupied by a piece of your color");
            }
            takes = true;
        }

        // Handles en passant variable assignment as applicable
        enPassant = false;
        Square ep = b.getEnPassantSquare();
        if (ep != null && piece instanceof Pawn)
        {
            if (player.getColor() == Side.WHITE)
            {
                if ((ep == from.getNWSquare() || ep == from.getNESquare()) && to == ep)
                {
                    enPassant = true;
                    takes = true;
                }
            }
            else
            {
                if ((ep == from.getSWSquare() || ep == from.getSESquare()) && to == ep)
                {
                    enPassant = true;
                    takes = true;
                }
            }
        }

        // Handles promotion exceptions
        if (!(piece instanceof Pawn) && promotionPiece != 0)
        {
            throw new InvalidMoveException("Only pawns can promote");
        }
        int promotionRank = player.getColor() == Side.WHITE ? 8 : 1;
        if (to.getRankInt() != promotionRank && promotionPiece != 0)
        {
            throw new InvalidMoveException(Utils.toTitleCase(player.getColor().toString()) + " pawns can only promote on the " + Utils.toOrdinal(promotionRank) + " rank");
        }
        if (piece instanceof Pawn && to.getRankInt() == (piece.getColor() == Side.WHITE ? 8 : 1))
        {
            if (!(promotionPiece == 'Q' || promotionPiece == 'R' || promotionPiece == 'B' || promotionPiece == 'N'))
            {
                throw new InvalidMoveException("Pawn must promote to a non-King piece");
            }
        }
        this.promotionPiece = promotionPiece;

        this.san = getSAN(); // Get the Standard Algebraic Notation for the move, excluding checks and checkmates
    }

    /**
     * Calls the other constructor, with promotion piece 0. Makes for simpler presentation, given that most moves will not be promotion moves.
     *
     * @param piece The chessman moving
     * @param to The Square to which PIECE is moving
     * @param b The Board on which the move occurs
     */
    public Move(Man piece, Square to, Board b) throws InvalidMoveException, InvalidPieceException, InvalidSquareException {
        this(piece, to, b, (char) 0);
    }

    /**
     * No parameters, because instance variables are used.
     * Uses instance variables provided by constructor to generate the Standard Algebraic Notation for the move.
     *
     * @return The Standard Algebraic Notation for the move
     */
    public String getSAN() throws InvalidMoveException, InvalidPieceException
    {
        String pieceStr = piece.toString().toUpperCase(); // Get piece
        StringBuilder middle = new StringBuilder(); // Initialize empty StringBuilder to consist of things like row and file specification and capture denotation
        String squareStr = to.toString(); // Get TO square
        StringBuilder suffix = new StringBuilder(); // Initialize empty StringBuilder to consist of promotion suffix

        // Handles captures
        if (to.getPiece() != null)
        {
            if (to.getPiece().getColor() == piece.getColor())
            {
                throw new InvalidMoveException("Destination square is occupied by piece of same color");
            }
            else
            {
                middle.append("x");
            }
        }

        // If King move, nothing else is relevant
        if (pieceStr.equals("K"))
        {
            return pieceStr + middle.toString() + squareStr;
        }

        // Gets the list of the corresponding pieces to perform necessary specifications
        ArrayList<Man> arr = switch(pieceStr)
        {
            case "Q" -> new ArrayList<>(player.getQueens());
            case "R" -> new ArrayList<>(player.getRooks());
            case "B" -> new ArrayList<>(player.getBishops());
            case "N" -> new ArrayList<>(player.getKnights());
            case "P" -> new ArrayList<>(player.getPawns());
            default -> null;
        };
        if (arr == null)
        {
            throw new InvalidPieceException("Invalid piece in SAN dev");
        }

        // If not pawn, get all possible pieces, and add specifications if more than one piece have vision on THIS.TO
        if (!pieceStr.equals("P"))
        {
            ArrayList<Man> posses = getAllPossiblePieces(arr);

            boolean rFlag = false;
            boolean fFlag = false;

            if (posses.size() == 1)
            {
                return pieceStr + middle.toString() + squareStr + suffix.toString();
            }

            for (Man m : posses)
            {
                if (m == piece)
                {
                    continue;
                }
                if (m.getSquare().getRank() == piece.getSquare().getRank())
                {
                    rFlag = true;
                }
                if (m.getSquare().getFile() == piece.getSquare().getFile())
                {
                    fFlag = true;
                }
            }

            if (fFlag)
            {
                if (rFlag)
                {
                    middle.insert(0, from.toString());
                }
                else
                {
                    middle.insert(0, from.getRankInt());
                }
            }
            else
            {
                middle.insert(0, from.getFileLetter());
            }
        }
        // if pawn, handle captures and non-captures as necessary. Handle promotion.
        else
        {
            pieceStr = String.valueOf(from.getFileLetter());
            if (enPassant)
            {
                middle.append("x");
            }
            else
            {
                if (!takes)
                {
                    pieceStr = "";
                }
            }
            if (promotionPiece != 0)
            {
                suffix.append('=');
                suffix.append(promotionPiece);
            }
        }

        return pieceStr + middle.toString() + squareStr + suffix.toString();
    }

    /**
     * @param arr A list of pieces
     * @return A sublist of pieces that has THIS.TO in their vision
     */
    private ArrayList<Man> getAllPossiblePieces(ArrayList<Man> arr)
    {
        ArrayList<Man> ret = new ArrayList<>();
        for (Man m : arr)
        {
            if (m.getVision().contains(to) && (!m.pinned || m.getPinStraight().getIndexOfSquare(to) >= 0))
            {
                ret.add(m);
            }
        }
        return ret;
    }

    /**
     * Overrides the toString() method of all objects for printing purposes
     *
     * @return The SAN as the String value of the move
     */
    @Override
    public String toString()
    {
        return san;
    }

    /**
     * Sets all remaining variables as necessary
     */
    private void castle() throws InvalidMoveException, InvalidSquareException
    {
        promotionPiece = 0;
        takes = false;
        enPassant = false;

        if (to == from.getEastSquare().getEastSquare())
        {
            if (player.getColor() == Side.WHITE ? board.isWhiteOO() : board.isBlackOO())
            {
                san = "O-O";
                castle = Castle.KING;
                rookSquare = player.getColor() == Side.WHITE ? board.getSquare("h1") : board.getSquare("h8");
            }
            else
            {
                throw new InvalidMoveException("Castling is illegal in this position");
            }
        }
        else if (to == from.getWestSquare().getWestSquare())
        {
            if (player.getColor() == Side.WHITE ? board.isWhiteOOO() : board.isBlackOOO())
            {
                san = "O-O-O";
                castle = Castle.QUEEN;
                rookSquare = player.getColor() == Side.WHITE ? board.getSquare("a1") : board.getSquare("a8");
            }
            else
            {
                throw new InvalidMoveException("Castling is illegal in this position");
            }
        }
    }

    /**
     * The two constructors have different information provided, which means only certain variables have been set.
     * This method puts them both on the same page so they can call castle() and have no errors.
     */
    private void setCastle() throws InvalidMoveException
    {
        piece = player.getKing();
        from = piece.getSquare();
        if (san.equals("O-O") || san.equals("0-0"))
        {
            to = from.getEastSquare().getEastSquare();
        }
        else if (san.equals("O-O-O") || san.equals("0-0-0"))
        {
            to = from.getWestSquare().getWestSquare();
        }
        else
        {
            throw new InvalidMoveException("Somehow castling by SAN went wrong");
        }
    }

    /**
     * @return Castle.NO if not castling, Castle.KING if castling kingside, Castle.QUEEN if castling queenside
     */
    protected Castle getCastle()
    {
        return castle;
    }

    /**
     * @return The Board on which the move happens
     */
    protected Board getBoard()
    {
        return board;
    }

    /**
     * @return True if the move is an en passant capture, false otherwise
     */
    protected boolean isEnPassant()
    {
        return enPassant;
    }

    /**
     * @return The Square from which the moving piece moves
     */
    protected Square getFrom()
    {
        return from;
    }

    /**
     * @return The Square to which the moving piece moves
     */
    protected Square getTo()
    {
        return to;
    }

    /**
     * @return The piece moving
     */
    protected Man getPiece()
    {
        return piece;
    }

    /**
     * @return If not castling, null. If castling, the square on which the involved rook is standing before the move occurs.
     */
    protected Square getRookSquare()
    {
        return rookSquare;
    }

    /**
     * @return The character representing the piece to which a pawn is promoting
     */
    protected char getPromotionPiece()
    {
        return promotionPiece;
    }

    /**
     * @return The piece being captured by the moving piece
     */
    protected Man getTakePiece()
    {
        return takePiece;
    }

    /**
     * @return True if the move is a capture, false otherwise
     */
    protected boolean isCapture()
    {
        return takes;
    }

    /**
     * Overrides the .equals() method of all Objects.
     * Two moves are equal if they occur on the same board, moving the same piece from the same square to the same square.
     */
    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Move))
        {
            return false;
        }
        Move m = (Move) obj;
        if (board != m.getBoard())
        {
            return false;
        }
        if (to != m.getTo())
        {
            return false;
        }
        if (from != m.getFrom())
        {
            return false;
        }
        return true;
    }

    /**
     * Adds the check suffix to the notation of the move
     */
    protected void makeCheck()
    {
        san = san + "+";
    }

    /**
     * Adds the checkmate suffix to the notation of the move
     */
    protected void makeCheckmate()
    {
        san = san.substring(0, san.length() - 1) + "#";
    }

    /**
     * @return The color of the moving piece
     */
    protected Side getMoverSide()
    {
        return piece.getColor();
    }
}
