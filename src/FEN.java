import java.util.Set;

public class FEN
{
    // The set of all possible FEN castle Strings
    private static final Set<String> CASTLE_STRINGS = Set.of("-", "K", "Q", "k", "q", "KQ", "Kk", "Kq", "Qk", "Qq", "kq", "KQk", "KQq", "Kkq", "Qkq", "KQkq");

    // A string containing all possible characters of a FEN board String, excluding the slash
    protected static final String VALID_FEN_CHARS = "KQRBNPkqrbnp12345678";

    // The FEN of the starting position of a chess game
    public static final String DEFAULT_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    private final String fen;  // The full FEN string

    private final String boardString; // The board string
    private final Side toMove; // The side which has the next move
    private String castle; // The castle String
    private final boolean wk; // The potential for White to castle kingside
    private final boolean wq; // The potential for White to castle queenside
    private final boolean bk; // The potential for Black to castle kingside
    private final boolean bq; // The potential for Black to castle queenside
    private final Square ep; // The square to which a pawn capturing en passant would move
    private final int halfmoves; // The number of halfmoves since the last pawn move or capture
    private final int fullmoves; // The number of fullmoves since the start of the game

    /**
     * If the FEN String is invalid, throw an error. Otherwise save it to THIS.FEN, and parse it.
     *
     * @param fen The FEN String
     * @param b The Board to which the FEN object applies
     * @throws InvalidFENException if FEN is not a valid FEN String
     * @throws InvalidSquareException @see
     * @see Board#getSquare(String)
     */
    public FEN(String fen, Board b) throws InvalidFENException, InvalidSquareException
    {
        if (isValidFEN(fen))
        {
            this.fen = fen;
        }
        else
        {
            throw new InvalidFENException("Invalid FEN");
        }

        String[] fenArray = fen.split(" ");
        boardString = fenArray[0];
        toMove = fenArray[1].charAt(0) == 'w' ? Side.WHITE : Side.BLACK;

        castle = fenArray[2];
        wk = fenArray[2].indexOf('K') != -1;
        wq = fenArray[2].indexOf('Q') != -1;
        bk = fenArray[2].indexOf('k') != -1;
        bq = fenArray[2].indexOf('q') != -1;

        ep = b.getSquare(fenArray[3]);
        halfmoves = Integer.parseInt(fenArray[4]);
        fullmoves = Integer.parseInt(fenArray[5]);
    }

    /**
     * Overrides the .equals() method of all objects for comparison purposes.
     * Two FENs are effectively equal if their FEN Strings are the same.
     *
     * @param obj An object
     * @return True if OBJ is effectively equal to THIS, false otherwise
     */
    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof FEN))
        {
            return false;
        }
        return fen.equals(((FEN) obj).fen);
    }

    /**
     * Processes all parameters and assigns their values to instance variables as applicable.
     *
     * @param arr An 8×8 2D array of Squares that make up a board
     * @param toMove The side which has the next move
     * @param wk The potential for White to castle kingside
     * @param wq The potential for White to castle queenside
     * @param bk The potential for Black to castle kingside
     * @param bq The potential for Black to castle queenside
     * @param ep The square to which a pawn capturing en passant would move
     * @param halfmoves The number of halfmoves since the last pawn move or capture
     * @param fullmoves The number of fullmoves since the start of the game
     */
    public FEN(Square[][] arr, Side toMove, boolean wk, boolean wq, boolean bk, boolean bq, Square ep, int halfmoves, int fullmoves)
    {
        StringBuilder sb = new StringBuilder();
        for (Square [] a : arr)
        {
            for (Square s : a)
            {
                Man curr = s.getPiece();
                if (curr != null)
                {
                    sb.append(curr.toString());
                }
                else
                {
                    sb.append(1);
                }
            }
            sb.append('/');
        }
        sb.setLength(sb.length() - 1);

        String temp = sb.toString();
        StringBuilder sb2 = new StringBuilder();

        int count = 0;
        for (char c : temp.toCharArray())
        {
            if (c == '1')
            {
                count++;
            }
            else
            {
                if (count != 0)
                {
                    sb2.append(count);
                    count = 0;
                }
                sb2.append(c);
            }
        }
        if (count != 0)
        {
            sb2.append(count);
        }

        boardString = sb2.toString();

        this.toMove = toMove;
        this.wk = wk;
        this.wq = wq;
        this.bk = bk;
        this.bq = bq;
        this.ep = ep;
        this.halfmoves = halfmoves;
        this.fullmoves = fullmoves;

        sb = new StringBuilder();
        sb.append(boardString);
        sb.append(' ');
        sb.append(toMove == Side.WHITE ? "w " : "b ");
        sb2 = new StringBuilder();
        sb2.append(wk ? "K" : "");
        sb2.append(wq ? "Q" : "");
        sb2.append(bk ? "k" : "");
        sb2.append(bq ? "q" : "");
        sb.append(sb2.toString().equals("") ? "-" : sb2.toString());
        sb.append(' ');
        sb.append(ep != null ? ep.toString() : '-');
        sb.append(' ');
        sb.append(halfmoves);
        sb.append(' ');
        sb.append(fullmoves);

        fen = sb.toString();
    }

    /**
     * Overrides the toString() method of all objects for printing purposes
     *
     * @return The FEN String as the String value of the FEN object
     */
    @Override
    public String toString()
    {
        return this.fen;
    }

    /**
     * @param fen A FEN String
     *
     * @return True if FEN is a valid FEN String. False otherwise.
     */
    protected static boolean isValidFEN(String fen)
    {
        String[] fenArray = fen.split(" ");

        // FEN Strings must have 6 parts
        if (fenArray.length != 6)
        {
            return false;
        }
        // Check to make sure the board string is valid
        if (!isValidBoard(fenArray[0]))
        {
            return false;
        }
        // The substring representing the side with the next move must be either "b" or "w"
        if (!fenArray[1].equals("w") && !fenArray[1].equals("b"))
        {
            return false;
        }
        // The castle String must be in the set of all possible castle Strings
        if (!CASTLE_STRINGS.contains(fenArray[2]))
        {
            return false;
        }
        // If a side has the potential to castle in a particular direction, the corresponding pieces must not have moved
        if (fenArray[2].indexOf('K') >= 0)
        {
            if (castlePiecesMoved(fenArray[0], 'K'))
            {
                return false;
            }
        }
        if (fenArray[2].indexOf('Q') >= 0)
        {
            if (castlePiecesMoved(fenArray[0], 'Q'))
            {
                return false;
            }
        }
        if (fenArray[2].indexOf('k') >= 0)
        {
            if (castlePiecesMoved(fenArray[0], 'k'))
            {
                return false;
            }
        }
        if (fenArray[2].indexOf('q') >= 0)
        {
            if (castlePiecesMoved(fenArray[0], 'q'))
            {
                return false;
            }
        }
        // The en passant substring must either be "-" or a valid square notation
        if ((fenArray[3].length() == 1 && fenArray[3].charAt(0) != '-') && !Square.isValidSquare(fenArray[3]))
        {
            return false;
        }
        // The en passant substring, if a square notation, must be on the third or sixth rank
        if (Square.isValidSquare(fenArray[3]))
        {
            char rank = fenArray[3].charAt(1);
            String pawnSquare;
            if (rank == '3')
            {
                pawnSquare = fenArray[3].charAt(0) + "4";
            }
            else if (rank == '6')
            {
                pawnSquare = fenArray[3].charAt(0) + "5";
            }
            else
            {
                return false;
            }
            try
            {
                char c = getPieceOnSquare(fenArray[0], pawnSquare);
                if (rank == '3')
                {
                    if (c != 'P')
                    {
                        return false;
                    }
                }
                else
                {
                    if (c != 'p')
                    {
                        return false;
                    }
                }
            }
            catch (InvalidSquareException e)
            {
                return false;
            }
        }
        // The halfmoves and fullmoves counter must be whole numbers
        if (!Utils.isDecimal(fenArray[4]) || !Utils.isDecimal(fenArray[5]))
        {
            return false;
        }

        return true;
    }

    /**
     * @param board A board String of a FEN
     * @return True if BOARD is a valid board String. False otherwise.
     */
    private static boolean isValidBoard(String board)
    {
        String[] ranks = board.split("/");

        // Return false if there are not exactly 8 ranks
        if (ranks.length != 8)
        {
            return false;
        }

        // For each rank, ensure there are exactly 8 values, whether empty squares or pieces
        for (int i = 0; i < 8; i++)
        {
            String s = ranks[i];
            int index = 0;
            for (int j = 0; j < s.length(); j++)
            {
                char c = s.charAt(j);
                if (VALID_FEN_CHARS.indexOf(c) == -1)
                {
                    return false;
                }
                else
                {
                    if (VALID_FEN_CHARS.substring(0, 12).indexOf(c) >= 0)
                    {
                        index++;
                    }
                    else if (VALID_FEN_CHARS.substring(12).indexOf(c) >= 0)
                    {
                        index += c - '0';
                        if (index > 8)
                        {
                            return false;
                        }
                    }
                }
            }
            if (index != 8)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * @param board A board String to be examined
     * @param castle A character representing a particular side castling a particular direction
     * @return True if the pieces necessary for the castling represented by CASTLE have moved. False otherwise.
     */
    private static boolean castlePiecesMoved(String board, char castle)
    {
        int rookIndex = -1;
        char color = 0;
        switch (castle)
        {
            case 'K':
                rookIndex = 7;
                color = 'w';
                break;
            case 'Q':
                rookIndex = 0;
                color = 'w';
                break;
            case 'k':
                rookIndex = 7;
                color = 'b';
                break;
            case 'q':
                rookIndex = 0;
                color = 'b';
                break;
        }

        int rank = 7;
        char kingChar = 'K';
        char rookChar = 'R';

        if (color == 'b')
        {
            rank = 0;
            kingChar = Character.toLowerCase(kingChar);
            rookChar = Character.toLowerCase(rookChar);
        }

        String[] ranks = board.split("/");
        String backrank = ranks[rank];
        int index = 0;
        boolean kingFlag = false;
        boolean rookFlag = false;
        for (int i = 0; i < backrank.length(); i++)
        {
            char c = backrank.charAt(i);
            if (index == 4 && c == kingChar)
            {
                kingFlag = true;
            }
            if (index == rookIndex && c == rookChar)
            {
                rookFlag = true;
            }
            if (VALID_FEN_CHARS.substring(0, 12).indexOf(c) >= 0)
            {
                index++;
            }
            else if (VALID_FEN_CHARS.substring(12).indexOf(c) >= 0)
            {
                index += c - '0';
            }
        }
        return !kingFlag || !rookFlag;
    }

    /**
     * @param boardString A board string
     * @param san A square notation
     * @return The toString() representation, as a char, of the piece contained by the square if any exists. The null character otherwise.
     */
    private static char getPieceOnSquare(String boardString, String san) throws InvalidSquareException
    {
        if (!Square.isValidSquare(san))
        {
            throw new InvalidSquareException("Invalid square");
        }

        int rank = Square.getRankFromName(san);
        int file = Square.getFileNumberFromName(san);

        char[] rankArr = boardString.split("/")[rank].toCharArray();
        int i = 0, j = -1;
        boolean pieceFlag = false;
        while (i <= file)
        {
            j++;
            if (VALID_FEN_CHARS.substring(0, 12).indexOf(rankArr[j]) >= 0)
            {
                i++;
                pieceFlag = true;
            }
            else if (VALID_FEN_CHARS.substring(12).indexOf(rankArr[j]) >= 0)
            {
                i += rankArr[j] - '0';
                pieceFlag = false;
            }
            else
            {
                throw new InvalidSquareException("Square was valid but I'm lazy and something went wrong");
            }
        }
        if (pieceFlag)
        {
            return rankArr[j];
        }
        else
        {
            return 0;
        }
    }

    /**
     * @return The board string
     */
    public String getBoardString()
    {
        return boardString;
    }

    /**
     * @return The side which has the next move
     */
    public Side getToMove()
    {
        return toMove;
    }

    /**
     * @return The castle string
     */
    public String getCastle()
    {
        return castle;
    }

    /**
     * @return The potential for Black to castle kingside
     */
    public boolean isBk()
    {
        return bk;
    }

    /**
     * @return The potential for Black to castle queenside
     */
    public boolean isBq()
    {
        return bq;
    }

    /**
     * @return The potential for White to castle kingside
     */
    public boolean isWk()
    {
        return wk;
    }

    /**
     * @return The potential for White to castle queenside
     */
    public boolean isWq()
    {
        return wq;
    }

    /**
     * @return The number of full moves since the game started
     */
    public int getFullmoves()
    {
        return fullmoves;
    }

    /**
     * @return The number of halfmoves since the last pawn move or capture
     */
    public int getHalfmoves()
    {
        return halfmoves;
    }

    /**
     * @return The en passant square if it exists. Null otherwise.
     */
    public Square getEp() {
        return ep;
    }

    /**
     * @return True if FEN is the FEN String for the starting position of a standard chess game, false otherwise
     */
    public boolean isStartingPositionFen()
    {
        return fen.equals(DEFAULT_FEN);
    }
}
