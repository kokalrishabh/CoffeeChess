import java.util.ArrayList;

public class Board
{
    private final Square[][] board; // The 8×8 2D array consisting of the 64 squares comprising the chessboard

    protected Rank first; // The first rank
    protected Rank second; // The second rank
    protected Rank third; // The third rank
    protected Rank fourth; // The fourth rank
    protected Rank fifth; // The fifth rank
    protected Rank sixth; // The sixth rank
    protected Rank seventh; // The seventh rank
    protected Rank eighth; // The eighth rank

    protected File aFile; // The A file
    protected File bFile; // The B file
    protected File cFile; // The C file
    protected File dFile; // The D file
    protected File eFile; // The E file
    protected File fFile; // The F file
    protected File gFile; // The G file
    protected File hFile; // The H file

    protected Diagonal a8a8Diagonal; // The diagonal from a8 to a8
    protected Diagonal a7b8Diagonal; // The diagonal from a7 to b8
    protected Diagonal a6c8Diagonal; // The diagonal from a6 to c8
    protected Diagonal a5d8Diagonal; // The diagonal from a5 to d8
    protected Diagonal a4e8Diagonal; // The diagonal from a4 to e8
    protected Diagonal a3f8Diagonal; // The diagonal from a3 to f8
    protected Diagonal a2g8Diagonal; // The diagonal from a2 to g8
    protected Diagonal a1h8Diagonal; // The diagonal from a1 to h8
    protected Diagonal b1h7Diagonal; // The diagonal from b1 to h7
    protected Diagonal c1h6Diagonal; // The diagonal from c1 to h6
    protected Diagonal d1h5Diagonal; // The diagonal from d1 to h5
    protected Diagonal e1h4Diagonal; // The diagonal from e1 to h4
    protected Diagonal f1h3Diagonal; // The diagonal from f1 to h3
    protected Diagonal g1h2Diagonal; // The diagonal from g1 to h2
    protected Diagonal h1h1Diagonal; // The diagonal from h1 to h1

    protected Diagonal a1a1Diagonal; // The diagonal from a1 to a1
    protected Diagonal a2b1Diagonal; // The diagonal from a2 to b1
    protected Diagonal a3c1Diagonal; // The diagonal from a3 to c1
    protected Diagonal a4d1Diagonal; // The diagonal from a4 to d1
    protected Diagonal a5e1Diagonal; // The diagonal from a5 to e1
    protected Diagonal a6f1Diagonal; // The diagonal from a6 to f1
    protected Diagonal a7g1Diagonal; // The diagonal from a7 to g1
    protected Diagonal a8h1Diagonal; // The diagonal from a8 to h1
    protected Diagonal b8h2Diagonal; // The diagonal from b8 to h2
    protected Diagonal c8h3Diagonal; // The diagonal from c8 to h3
    protected Diagonal d8h4Diagonal; // The diagonal from d8 to h4
    protected Diagonal e8h5Diagonal; // The diagonal from e8 to h5
    protected Diagonal f8h6Diagonal; // The diagonal from f8 to h6
    protected Diagonal g8h7Diagonal; // The diagonal from g8 to h7
    protected Diagonal h8h8Diagonal; // The diagonal from h8 to h8

    private FEN fen; // The FEN of the board
    private Side toMove; // The side with the next move
    private boolean whiteOO; // True if White can legally castle kingside on the next move, false otherwise
    private boolean whiteOOO; // True if White can legally castle queenside on the next move, false otherwise
    private boolean blackOO; // True if Black can legally castle kingside on the next move, false otherwise
    private boolean blackOOO; // True if Black can legally castle queenside on the next move, false otherwise

    private final Player white; // The White player
    private final Player black; // The Black player

    private boolean built; // True if the board is fully constructed, false otherwise. See .toString().

    private final ArrayList<FEN> fenHistory;
    private final ArrayList<Move> moveHistory;
    private int fenHistIndex;
    private int moveHistIndex;

    protected GameState state;

    /**
     * No parameters. Just lots and lots of construction and assignment.
     *
     * @throws InvalidSquareException @see1, @see2, @see3, @see4
     * @throws InvalidFENException @see4
     * @throws InvalidPieceException @see3
     * @throws TooManyKingsException @see3
     * @throws InvalidMoveException @see3
     * @see Square#Square(String, Board)
     * @see #setAllStraights()
     * @see #loadFromFen(FEN)
     * @see FEN#FEN(String, Board)
     */
    public Board() throws InvalidSquareException, InvalidFENException, InvalidPieceException, TooManyKingsException, InvalidMoveException
    {
        built = false;

        // Creates players
        white = new Player(Side.WHITE);
        black = new Player(Side.BLACK);

        fenHistory = new ArrayList<>();
        moveHistory = new ArrayList<>();
        fenHistIndex = -1;
        moveHistIndex = -1;

        state = GameState.IN_PROGRESS;

        // Creates the board and the squares that comprise it
        board = new Square[][]
                {
                    {new Square("a8", this), new Square("b8", this), new Square("c8", this), new Square("d8", this), new Square("e8", this), new Square("f8", this), new Square("g8", this), new Square("h8", this)},
                    {new Square("a7", this), new Square("b7", this), new Square("c7", this), new Square("d7", this), new Square("e7", this), new Square("f7", this), new Square("g7", this), new Square("h7", this)},
                    {new Square("a6", this), new Square("b6", this), new Square("c6", this), new Square("d6", this), new Square("e6", this), new Square("f6", this), new Square("g6", this), new Square("h6", this)},
                    {new Square("a5", this), new Square("b5", this), new Square("c5", this), new Square("d5", this), new Square("e5", this), new Square("f5", this), new Square("g5", this), new Square("h5", this)},
                    {new Square("a4", this), new Square("b4", this), new Square("c4", this), new Square("d4", this), new Square("e4", this), new Square("f4", this), new Square("g4", this), new Square("h4", this)},
                    {new Square("a3", this), new Square("b3", this), new Square("c3", this), new Square("d3", this), new Square("e3", this), new Square("f3", this), new Square("g3", this), new Square("h3", this)},
                    {new Square("a2", this), new Square("b2", this), new Square("c2", this), new Square("d2", this), new Square("e2", this), new Square("f2", this), new Square("g2", this), new Square("h2", this)},
                    {new Square("a1", this), new Square("b1", this), new Square("c1", this), new Square("d1", this), new Square("e1", this), new Square("f1", this), new Square("g1", this), new Square("h1", this)}
                };

        // Assign the squares to their corresponding rank, file, and diagonals
        first = new Rank(board[7]);
        second = new Rank(board[6]);
        third = new Rank(board[5]);
        fourth = new Rank(board[4]);
        fifth = new Rank(board[3]);
        sixth = new Rank(board[2]);
        seventh = new Rank(board[1]);
        eighth = new Rank(board[0]);

        aFile = new File(new Square[] {board[0][0], board[1][0], board[2][0], board[3][0], board[4][0], board[5][0], board[6][0], board[7][0]});
        bFile = new File(new Square[] {board[0][1], board[1][1], board[2][1], board[3][1], board[4][1], board[5][1], board[6][1], board[7][1]});
        cFile = new File(new Square[] {board[0][2], board[1][2], board[2][2], board[3][2], board[4][2], board[5][2], board[6][2], board[7][2]});
        dFile = new File(new Square[] {board[0][3], board[1][3], board[2][3], board[3][3], board[4][3], board[5][3], board[6][3], board[7][3]});
        eFile = new File(new Square[] {board[0][4], board[1][4], board[2][4], board[3][4], board[4][4], board[5][4], board[6][4], board[7][4]});
        fFile = new File(new Square[] {board[0][5], board[1][5], board[2][5], board[3][5], board[4][5], board[5][5], board[6][5], board[7][5]});
        gFile = new File(new Square[] {board[0][6], board[1][6], board[2][6], board[3][6], board[4][6], board[5][6], board[6][6], board[7][6]});
        hFile = new File(new Square[] {board[0][7], board[1][7], board[2][7], board[3][7], board[4][7], board[5][7], board[6][7], board[7][7]});

        a8a8Diagonal = new Diagonal(new Square[] {board[0][0]});
        a7b8Diagonal = new Diagonal(new Square[] {board[1][0], board[0][1]});
        a6c8Diagonal = new Diagonal(new Square[] {board[2][0], board[1][1], board[0][2]});
        a5d8Diagonal = new Diagonal(new Square[] {board[3][0], board[2][1], board[1][2], board[0][3]});
        a4e8Diagonal = new Diagonal(new Square[] {board[4][0], board[3][1], board[2][2], board[1][3], board[0][4]});
        a3f8Diagonal = new Diagonal(new Square[] {board[5][0], board[4][1], board[3][2], board[2][3], board[1][4], board[0][5]});
        a2g8Diagonal = new Diagonal(new Square[] {board[6][0], board[5][1], board[4][2], board[3][3], board[2][4], board[1][5], board[0][6]});
        a1h8Diagonal = new Diagonal(new Square[] {board[7][0], board[6][1], board[5][2], board[4][3], board[3][4], board[2][5], board[1][6], board[0][7]});

        b1h7Diagonal = new Diagonal(new Square[] {board[7][1], board[6][2], board[5][3], board[4][4], board[3][5], board[2][6], board[1][7]});
        c1h6Diagonal = new Diagonal(new Square[] {board[7][2], board[6][3], board[5][4], board[4][5], board[3][6], board[2][7]});
        d1h5Diagonal = new Diagonal(new Square[] {board[7][3], board[6][4], board[5][5], board[4][6], board[3][7]});
        e1h4Diagonal = new Diagonal(new Square[] {board[7][4], board[6][5], board[5][6], board[4][7]});
        f1h3Diagonal = new Diagonal(new Square[] {board[7][5], board[6][6], board[5][7]});
        g1h2Diagonal = new Diagonal(new Square[] {board[7][6], board[6][7]});
        h1h1Diagonal = new Diagonal(new Square[] {board[7][7]});

        a1a1Diagonal = new Diagonal(new Square[] {board[7][0]});
        a2b1Diagonal = new Diagonal(new Square[] {board[6][0], board[7][1]});
        a3c1Diagonal = new Diagonal(new Square[] {board[5][0], board[6][1], board[7][2]});
        a4d1Diagonal = new Diagonal(new Square[] {board[4][0], board[5][1], board[6][2], board[7][3]});
        a5e1Diagonal = new Diagonal(new Square[] {board[3][0], board[4][1], board[5][2], board[6][3], board[7][4]});
        a6f1Diagonal = new Diagonal(new Square[] {board[2][0], board[3][1], board[4][2], board[5][3], board[6][4], board[7][5]});
        a7g1Diagonal = new Diagonal(new Square[] {board[1][0], board[2][1], board[3][2], board[4][3], board[5][4], board[6][5], board[7][6]});
        a8h1Diagonal = new Diagonal(new Square[] {board[0][0], board[1][1], board[2][2], board[3][3], board[4][4], board[5][5], board[6][6], board[7][7]});

        b8h2Diagonal = new Diagonal(new Square[] {board[0][1], board[1][2], board[2][3], board[3][4], board[4][5], board[5][6], board[6][7]});
        c8h3Diagonal = new Diagonal(new Square[] {board[0][2], board[1][3], board[2][4], board[3][5], board[4][6], board[5][7]});
        d8h4Diagonal = new Diagonal(new Square[] {board[0][3], board[1][4], board[2][5], board[3][6], board[4][7]});
        e8h5Diagonal = new Diagonal(new Square[] {board[0][4], board[1][5], board[2][6], board[3][7]});
        f8h6Diagonal = new Diagonal(new Square[] {board[0][5], board[1][6], board[2][7]});
        g8h7Diagonal = new Diagonal(new Square[] {board[0][6], board[1][7]});
        h8h8Diagonal = new Diagonal(new Square[] {board[0][7]});

        // Tell each square its rank, file, and diagonals
        setAllStraights();

        // Load the board to the starting chess position
        startFromFen(new FEN(FEN.DEFAULT_FEN, this));

        // Set BUILT to true
        built = true;
    }

    /**
     * Tell each square its Rank, File, and Diagonals
     *
     * @throws InvalidSquareException @see
     * @see Square#setStraights()
     */
    public void setAllStraights() throws InvalidSquareException
    {
        for (Square[] arr : board)
        {
            for (Square s : arr)
            {
                s.setStraights();
            }
        }
    }

    /**
     * Set visions for White and Black
     */
    public void setAllVisions()
    {
        setVisionForWhite();
        setVisionForBlack();
    }

    /**
     * Sets the vision for the white player
     */
    protected void setVisionForWhite()
    {
        for (Man m : white.getPieces())
        {
            m.setVision();
        }
        white.setVision();

        whiteOO = fen.isWk() && getCastlingLegality('k', Side.WHITE);
        whiteOOO = fen.isWq() && getCastlingLegality('q', Side.WHITE);
    }

    /**
     * Sets the vision for the black player
     */
    protected void setVisionForBlack()
    {
        for (Man m : black.getPieces())
        {
            m.setVision();
        }
        black.setVision();

        blackOO = fen.isBk() && getCastlingLegality('k', Side.BLACK);
        blackOOO = fen.isBq() && getCastlingLegality('q', Side.BLACK);
    }

    /**
     * Clear, then load the board from the FEN object
     *
     * @param fen A FEN object
     * @throws InvalidSquareException @see
     * @throws InvalidPieceException @see
     * @throws TooManyKingsException @see
     * @throws InvalidMoveException @see
     * @see #loadFromFen(FEN)
     */
    public void startFromFen(FEN fen) throws InvalidSquareException, InvalidPieceException, TooManyKingsException, InvalidMoveException
    {
        fenHistory.clear();
        moveHistory.clear();
        fenHistIndex = -1;
        moveHistIndex = -1;
        loadFromFen(fen);
        updateFenHistory(fen);
    }

    /**
     * Load the board from the FEN object (without clearing)
     *
     * @param fen A FEN object
     * @throws InvalidSquareException @see2
     * @throws InvalidPieceException @see1, @see2
     * @throws TooManyKingsException @see1
     * @throws InvalidMoveException @see2
     * @see #placePieces(String)
     * @see #setAllLegalMoves()
     */
    public void loadFromFen(FEN fen) throws TooManyKingsException, InvalidSquareException, InvalidPieceException, InvalidMoveException
    {
        clearBoard();

        String[] fenArray = fen.toString().split(" ");
        this.fen = fen;

        toMove = fen.getToMove();

        toMove = fenArray[1].charAt(0) == 'w' ? Side.WHITE : Side.BLACK;

        placePieces(fen.getBoardString());

        setAllVisions();

        setLegalMovesForPlayer(toMove);
    }

    /**
     * Deletes from FENHISTORY any FENs of moves that have been undone, then adds the newest one
     *
     * @param f The newest FEN to be added
     */
    private void updateFenHistory(FEN f)
    {
        if (fenHistIndex != fenHistory.size() - 1)
        {
            for (int i = fenHistIndex + 1; i < fenHistory.size(); )
            {
                fenHistory.remove(i);
            }

        }
        fenHistory.add(f);
        fenHistIndex++;

        int count = 0;
        for (FEN ff : fenHistory)
        {
            if (f.equals(ff) && ++count == 3)
            {
                gameOver(null);
                break;
            }
        }
    }

    /**
     * Deletes from MOVEHISTORY any moves that have been undone, then adds the newest one
     * @param m The newest move to be added
     */
    private void updateMoveHistory(Move m)
    {
        if (moveHistIndex != moveHistory.size() - 1)
        {
            for (int i = moveHistIndex + 1; i < moveHistory.size(); )
            {
                moveHistory.remove(i);
            }
        }
        moveHistory.add(m);
        moveHistIndex++;
    }

    /**
     * Undoes the last move by moving back in the history and loading from that FEN. Does nothing if
     * the board has no moves played from the initial FEN.
     *
     * @throws TooManyKingsException @see
     * @throws InvalidMoveException @see
     * @throws InvalidSquareException @see
     * @throws InvalidPieceException @see
     * @see #loadFromFen(FEN)
     */
    protected void undoMove() throws TooManyKingsException, InvalidMoveException, InvalidSquareException, InvalidPieceException
    {
        if (moveHistIndex >= 0)
        {
            moveHistIndex--;
            fenHistIndex--;
            loadFromFen(fenHistory.get(fenHistIndex));
        }
    }

    /**
     * Redoes a single move that has been undone. Does nothing if there are no undone moves.
     *
     * @throws TooManyKingsException @see
     * @throws InvalidMoveException @see
     * @throws InvalidSquareException @see
     * @throws InvalidPieceException @see
     * @see #loadFromFen(FEN)
     */
    protected void redoMove() throws TooManyKingsException, InvalidMoveException, InvalidSquareException, InvalidPieceException
    {
        if (moveHistIndex < moveHistory.size() - 1)
        {
            moveHistIndex++;
            fenHistIndex++;
            loadFromFen(fenHistory.get(fenHistIndex));
        }
    }

    /**
     * @param dir Either 'k' or 'q', representing in which direction castling is being checked
     * @param s The side whose castling is being checked
     * @return True if S can castle in the direction DIR, false otherwise
     */
    private boolean getCastlingLegality(char dir, Side s)
    {
        Square k = getPlayer(s).getKing().getSquare();
        Square one = dir == 'k' ? k.getEastSquare() : k.getWestSquare();
        Square two = dir == 'k' ? one.getEastSquare() : one.getWestSquare();
        ArrayList<Square> v = getOpponent(s).getVision();
        return one.getPiece() == null
            && two.getPiece() == null
            && (dir != 'q' || two.getWestSquare().getPiece() == null)
            && !v.contains(one)
            && !v.contains(two);
    }

    /**
     * Sets legal moves for both players
     *
     * @throws InvalidMoveException @see
     * @throws InvalidPieceException @see
     * @throws InvalidSquareException @see
     * @see Man#setLegalMoves()
     */
    private void setAllLegalMoves() throws InvalidMoveException, InvalidPieceException, InvalidSquareException
    {
        setLegalMovesForWhite();
        setLegalMovesForBlack();
    }

    /**
     * Sets legal moves for the player playing with S
     *
     * @param s The side of the player whose legal moves are to be set
     * @throws InvalidSquareException @see
     * @throws InvalidPieceException @see
     * @throws InvalidMoveException @see
     * @see #setLegalMovesForWhite()
     * @see #setLegalMovesForBlack()
     */
    private void setLegalMovesForPlayer(Side s) throws InvalidSquareException, InvalidPieceException, InvalidMoveException
    {
        if (s == Side.WHITE) setLegalMovesForWhite();
        else                 setLegalMovesForBlack();
    }

    /**
     * Sets legal moves for the white player
     *
     * @throws InvalidMoveException @see
     * @throws InvalidPieceException @see
     * @throws InvalidSquareException @see
     * @see Man#setLegalMoves()
     */
    private void setLegalMovesForWhite() throws InvalidMoveException, InvalidPieceException, InvalidSquareException
    {
        for (Man m : white.getPieces())
        {
            m.setLegalMoves();
        }
        white.setLegalMoves();
    }

    /**
     * Sets legal moves for the black player
     *
     * @throws InvalidMoveException @see
     * @throws InvalidPieceException @see
     * @throws InvalidSquareException @see
     * @see Man#setLegalMoves()
     */
    private void setLegalMovesForBlack() throws InvalidMoveException, InvalidPieceException, InvalidSquareException
    {
        for (Man m : black.getPieces())
        {
            m.setLegalMoves();
        }
        black.setLegalMoves();
    }

    /**
     * Creates a FEN object from S and clears and then the board to that state.
     *
     * @param s A FEN String to be loaded
     * @throws InvalidSquareException @see
     * @throws InvalidFENException @see
     * @throws InvalidPieceException @see
     * @throws TooManyKingsException @see
     * @throws InvalidMoveException @see
     * @see #startFromFen(FEN)
     */
    public void startFromFen(String s) throws InvalidSquareException, InvalidFENException, InvalidPieceException, TooManyKingsException, InvalidMoveException
    {
        startFromFen(new FEN(s, this));
    }

    /**
     * Creates a FEN object from S and loads the board to that state.
     *
     * @param s A FEN String to be loaded
     * @throws InvalidSquareException @see
     * @throws InvalidFENException @see
     * @throws InvalidPieceException @see
     * @throws TooManyKingsException @see
     * @throws InvalidMoveException @see
     * @see #loadFromFen(FEN)
     */
    public void loadFromFen(String s) throws InvalidSquareException, InvalidFENException, InvalidPieceException, TooManyKingsException, InvalidMoveException
    {
        loadFromFen(new FEN(s, this));
    }

    /**
     * Clears the histories, sets all squares' pieces to be null, and resets all player pieces
     */
    public void clearBoard()
    {
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                board[i][j].setPiece(null);
            }
        }
        white.reset();
        black.reset();
    }

    /**
     * Place pieces on the board according the BOARDSTRING. Give the corresponding players the pieces placed.
     *
     * @param boardString A FEN board string
     * @throws TooManyKingsException @see
     * @see Player#givePiece
     */
    private void placePieces(String boardString) throws TooManyKingsException
    {
        String[] ranks = boardString.split("/");
        for (int i = 0; i < 8; i++)
        {
            char[] rankArr = ranks[i].toCharArray();
            int index = 0;
            for (int j = 0; j < rankArr.length; j++)
            {
                if (FEN.VALID_FEN_CHARS.substring(0, 12).indexOf(rankArr[j]) >= 0)
                {
                    Man m = pieceFromLetter(rankArr[j], board[i][index]);
                    if (FEN.VALID_FEN_CHARS.substring(0, 6).indexOf(rankArr[j]) >= 0)
                    {
                        white.givePiece(m);
                    }
                    else
                    {
                        black.givePiece(m);
                    }
                    board[i][index].setPiece(m);
                    index++;
                }
                else // if (FEN.VALID_FEN_CHARS.substring(12).indexOf((rankArr[j])) >= 0)
                {
                    index += rankArr[j] - '0';
                }
            }
        }
    }

    /**
     * @param san The SAN of the square
     * @return The square notated if the square is a valid notation. Null if "-" is the SAN (for en passant purposes).
     * @throws InvalidSquareException if the square notation is invalid and SAN is also not "-".
     */
    protected Square getSquare(String san) throws InvalidSquareException
    {
        if (san.equals("-"))
        {
            return null;
        }
        if (!Square.isValidSquare(san))
        {
            throw new InvalidSquareException("Invalid square");
        }
        return board[Square.getRankFromName(san)][Square.getFileNumberFromName(san)];
    }

    /**
     * @param i Row of the Square to be returned
     * @param j Column of the Square to be returned
     * @return BOARD[I][J]
     */
    protected Square getSquare(int i, int j)
    {
        return board[i][j];
    }

    /**
     * @param c A character representing which piece is to be created
     * @param square A Square on which the piece is to be created
     * @return A newly constructed piece, depending on C and placed on S
     */
    protected static Man pieceFromLetter(char c, Square square)
    {
        return switch (c)
        {
            case 'K' -> new King(Side.WHITE, square);
            case 'Q' -> new Queen(Side.WHITE, square);
            case 'R' -> new Rook(Side.WHITE, square);
            case 'B' -> new Bishop(Side.WHITE, square);
            case 'N' -> new Knight(Side.WHITE, square);
            case 'P' -> new Pawn(Side.WHITE, square);
            case 'k' -> new King(Side.BLACK, square);
            case 'q' -> new Queen(Side.BLACK, square);
            case 'r' -> new Rook(Side.BLACK, square);
            case 'b' -> new Bishop(Side.BLACK, square);
            case 'n' -> new Knight(Side.BLACK, square);
            case 'p' -> new Pawn(Side.BLACK, square);
            default -> null;
        };
    }

    /**
     * Overrides the toString() method of all objects for printing purposes
     *
     * @return An ASCII representation of a chessboard, labeled by rank and file, with capital
     * letters representing white pieces and lower-case letters representing black pieces
     */
    @Override
    public String toString()
    {
        if (built)
        {
            return seeBoardAs(Side.WHITE);
        }
        else
        {
            return "tempBoardObjectString";
        }
    }

    /**
     * @param side The side from which the board is being viewed
     * @return An ASCII representation of a chessboard, labeled by rank and file, with capital letters representing
     * white pieces and lower-case letters representing black pieces from SIDE's perspective
     */
    public String seeBoardAs(Side side)
    {
        StringBuilder sb = new StringBuilder();
        String str = side == Side.WHITE ? "    a   b   c   d   e   f   g   h" : "    h   g   f   e   d   c   b   a";
        sb.append(str);
        sb.append("\n  +---+---+---+---+---+---+---+---+\n");
        for (int i = (side == Side.WHITE ? 0 : 7); side == Side.WHITE ? i < 8 : i >= 0; i += (side == Side.WHITE ? 1 : -1))
        {
            sb.append(8 - i);
            for (int j = (side == Side.WHITE ? 0 : 7); side == Side.WHITE ? j < 8 : j >= 0; j += (side == Side.WHITE ? 1 : -1))
            {
                sb.append(" | ");
                Man m = board[i][j].getPiece();
                String s = m == null ? " " : m.toString();
                sb.append(s);
            }
            sb.append(" |\n");
            sb.append("  +---+---+---+---+---+---+---+---+\n");
        }
        sb.append(str);
        sb.append("\n");
        return sb.toString();
    }

    /**
     * @param typeNum Either 1 or 2. 1 represents top-left to bottom-right, 2 represents bottom-left to top-right.
     * @param diagNum The number of the diagonal, counting from left to right
     * @return The specified Diagonal
     */
    protected Diagonal getDiagonalByNumber(int typeNum, int diagNum)
    {
        if (typeNum == 1)
        {
            return switch (diagNum)
            {
                case 1 -> a1a1Diagonal;
                case 2 -> a2b1Diagonal;
                case 3 -> a3c1Diagonal;
                case 4 -> a4d1Diagonal;
                case 5 -> a5e1Diagonal;
                case 6 -> a6f1Diagonal;
                case 7 -> a7g1Diagonal;
                case 8 -> a8h1Diagonal;
                case 9 -> b8h2Diagonal;
                case 10 -> c8h3Diagonal;
                case 11 -> d8h4Diagonal;
                case 12 -> e8h5Diagonal;
                case 13 -> f8h6Diagonal;
                case 14 -> g8h7Diagonal;
                case 15 -> h8h8Diagonal;
                default -> null;
            };
        }
        else if (typeNum == 2)
        {
            return switch (diagNum)
                    {
                        case 1 -> a8a8Diagonal;
                        case 2 -> a7b8Diagonal;
                        case 3 -> a6c8Diagonal;
                        case 4 -> a5d8Diagonal;
                        case 5 -> a4e8Diagonal;
                        case 6 -> a3f8Diagonal;
                        case 7 -> a2g8Diagonal;
                        case 8 -> a1h8Diagonal;
                        case 9 -> b1h7Diagonal;
                        case 10 -> c1h6Diagonal;
                        case 11 -> d1h5Diagonal;
                        case 12 -> e1h4Diagonal;
                        case 13 -> f1h3Diagonal;
                        case 14 -> g1h2Diagonal;
                        case 15 -> h1h1Diagonal;
                        default -> null;
                    };
        }
        else
        {
            return null;
        }
    }

    /**
     * @return The board, represented as an 8×8 2D array
     */
    protected Square[][] getBoard()
    {
        return board;
    }

    /**
     * @return The player with the next move
     */
    protected Side getToMove()
    {
        return toMove;
    }

    /**
     * @return The white Player
     */
    protected Player getWhite()
    {
        return white;
    }

    /**
     * @return The black Player
     */
    protected Player getBlack()
    {
        return black;
    }

    /**
     * @param i The number of the rank
     * @return The ith Rank
     */
    protected Rank getRankFromNumber(int i)
    {
        return switch (i)
        {
            case 1 -> first;
            case 2 -> second;
            case 3 -> third;
            case 4 -> fourth;
            case 5 -> fifth;
            case 6 -> sixth;
            case 7 -> seventh;
            case 8 -> eighth;
            default -> null;
        };
    }

    /**
     * @param c A character representing a file
     * @return The specified File
     */
    protected File getFileFromLetter(char c)
    {
        return switch (c)
        {
            case 'a' -> aFile;
            case 'b' -> bFile;
            case 'c' -> cFile;
            case 'd' -> dFile;
            case 'e' -> eFile;
            case 'f' -> fFile;
            case 'g' -> gFile;
            case 'h' -> hFile;
            default -> null;
        };
    }

    /**
     * @return The en passant square, if it exists. Null otherwise.
     */
    protected Square getEnPassantSquare()
    {
        return fen.getEp();
    }

    /**
     * @return True if White can legally castle kingside on the next move, false otherwise
     */
    protected boolean isWhiteOO()
    {
        return whiteOO;
    }

    /**
     * @return True if White can legally castle queenside on the next move, false otherwise
     */
    protected boolean isWhiteOOO()
    {
        return whiteOOO;
    }

    /**
     * @return True if Black can legally castle kingside on the next move, false otherwise
     */
    protected boolean isBlackOO()
    {
        return blackOO;
    }

    /**
     * @return True if Black can legally castle queenside on the next move, false otherwise
     */
    protected boolean isBlackOOO()
    {
        return blackOOO;
    }

    /**
     * Plays M on the board
     *
     * @param m A move to be played
     * @throws InvalidMoveException if M's Board is not THIS or if M is illegal - @see4
     * @throws InvalidPieceException @see1, @see4
     * @throws TooManyKingsException @see2
     * @throws InvalidSquareException @see3, @see4
     * @see Player#takePiece(Man)
     * @see Player#givePiece(Man)
     * @see #updateFen(Move)
     * @see Player#setLegalMoves()
     */
    public void move(Move m) throws InvalidMoveException, InvalidPieceException, TooManyKingsException, InvalidSquareException
    {
        // If game in progress
        if (state == GameState.IN_PROGRESS)
        {
            // Ensures M is of the right board
            if (m.getBoard() != this)
            {
                throw new InvalidMoveException("Move does not apply to this board");
            }

            // Assigns players to variables as applicable
            Player mover = getPlayer(toMove);
            Player oppo = getOpponent(toMove);

            // Ensure the move is legal
            if (!mover.getLegalMoves().contains(m))
            {
                throw new InvalidMoveException("Illegal move");
            }

            // Store information that will be used a lot locally
            Square to = m.getTo();
            Square from = m.getFrom();
            Man piece = m.getPiece();

            from.setPiece(null);

            // Handle castling
            Castle castle = m.getCastle();
            if (castle != Castle.NO)
            {
                to.setPiece(piece);
                (castle == Castle.KING ? to.getWestSquare() : to.getEastSquare()).setPiece(m.getRookSquare().getPiece());
                m.getRookSquare().setPiece(null);
            }
            // Handle en passant
            else if (m.isEnPassant())
            {
                to.setPiece(piece);
                Square epSquare = toMove == Side.WHITE ? to.getSouthSquare() : to.getNorthSquare();
                oppo.takePiece(epSquare.getPiece());
                epSquare.setPiece(null);
            }
            // Handle all other cases
            else
            {
                if (to.getPiece() != null)
                {
                    oppo.takePiece(to.getPiece());
                }
                // Handle promotion
                if (m.getPromotionPiece() != 0)
                {
                    to.setPiece(pieceFromLetter((toMove == Side.WHITE ? m.getPromotionPiece() : Character.toLowerCase(m.getPromotionPiece())), m.getTo()));
                    mover.takePiece(piece);
                    mover.givePiece(to.getPiece());
                }
                else
                {
                    to.setPiece(piece);
                }
            }

            // En-passant-pin any pawns as necessary
            oppo.epUnpinAll();
            if (piece instanceof Pawn && Math.abs(from.getRankInt() - to.getRankInt()) == 2 && oppo.getKing().getRank() == to.getRank())
            {
                // Get pawns east and west of TO, if they exist
                Man east = to.getEastSquare().getPiece();
                east = east instanceof Pawn ? east : null;
                Man west = to.getWestSquare().getPiece();
                west = west instanceof Pawn ? west : null;

            /*
            If they're both there, then neither is pinned, because if either takes en passant, the other will be there to block check.
            If neither is there, then the whole question is irrelevant, because there's nothing to be pinned.
             */
                if (east == null ^ west == null)
                {
                    Rank r = to.getRank();
                    int kingIndex = r.getIndexOfSquare(oppo.getKing().getSquare());
                    int pawnIndex = r.getIndexOfSquare(to);
                    boolean greater = kingIndex > pawnIndex;
                    boolean b = west == null;

                    // Get the list of all pieces between the king and the side of the board, traversing towards and then past the pawn that just moved
                    ArrayList<Man> arr = new ArrayList<>();
                    int pm = greater ? -1 : 1;
                    for (int i = kingIndex + pm; greater ? i >= 0 : i < 8; i += pm)
                    {
                        Man man = r.getSquares()[i].getPiece();
                        if (man != null)
                        {
                            arr.add(man);
                        }
                    }

                    // Set PAWN to the non-null pawn
                    Pawn pawn = (Pawn) (b ? east : west);

                    // Based on a logic table
                    if (arr.size() >= 3 && arr.get(0) == (greater == b ? pawn : piece) && arr.get(1) == (greater == b ? piece : pawn) && arr.get(2) instanceof Rook && arr.get(2).getColor() == toMove)
                    {
                        pawn.epPin();
                    }
                }
            }

            // Update the FEN and set both players' visions
            updateFen(m);
            setAllVisions();

            // if (mover.i)

            // Put the king in check as necessary, and uncheck the moving player
            King ok = oppo.getKing();
            Square okSquare = ok.getSquare();
            boolean flag = false;
            Man c1 = null;
            Straight s1 = null;
            Man c2 = null;
            Straight s2 = null;

            for (Man man : mover.getPieces())
            {
                if (man.getVision().contains(okSquare))
                {
                    if (flag)
                    {
                        c2 = man;
                        if (c2 instanceof Queen || c2 instanceof Rook)
                        {
                            if (c2.getFile().containsPiece(ok))
                            {
                                s2 = c2.getFile();
                            }
                            else if (c2.getRank().containsPiece(ok))
                            {
                                s2 = c2.getRank();
                            }
                        }
                        if (c2 instanceof Queen || c2 instanceof Bishop)
                        {
                            if (c2.getDiagonal1().containsPiece(ok))
                            {
                                s2 = c2.getDiagonal1();
                            }
                            else if (c2.getDiagonal2().containsPiece(ok))
                            {
                                s2 = c2.getDiagonal2();
                            }
                        }
                        break;
                    }
                    else
                    {
                        c1 = man;
                        if (c1 instanceof Queen || c1 instanceof Rook)
                        {
                            if (c1.getFile().containsPiece(ok))
                            {
                                s1 = c1.getFile();
                            }
                            else if (c1.getRank().containsPiece(ok))
                            {
                                s1 = c1.getRank();
                            }
                        }
                        if (c1 instanceof Queen || c1 instanceof Bishop)
                        {
                            if (c1.getDiagonal1().containsPiece(ok))
                            {
                                s1 = c1.getDiagonal1();
                            }
                            else if (c1.getDiagonal2().containsPiece(ok))
                            {
                                s1 = c1.getDiagonal2();
                            }
                        }
                        flag = true;
                    }
                }
            }
            boolean checkFlag = false;
            if (c1 != null)
            {
                m.makeCheck();
                if (c2 == null)
                {
                    oppo.check(c1);
                }
                else
                {
                    oppo.doubleCheck();
                }
                ok.check(s1, s2, c1, c2);
                checkFlag = true;
            }
            mover.getKing().check(null, null, null, null);
            mover.uncheck();

            // Unpin all pieces, then pin the opponent's pieces as applicable
            mover.unpinAll();
            oppo.unpinAll();
            oppo.pinAll();

            // Set the legal moves for the next player to move. Note that TOMOVE has been updated in updateFen().
            if (toMove == Side.WHITE)
            {
                setLegalMovesForWhite();
            }
            else
            {
                setLegalMovesForBlack();
            }

            if (getPlayer(toMove).getLegalMoves().size() == 0)
            {
                if (checkFlag)
                {
                    m.makeCheckmate();
                    gameOver(Utils.flip(toMove));
                }
                else
                {
                    gameOver(null);
                }
            }

            updateMoveHistory(m);
        }
    }

    /**
     * Sets STATE to the applicable GAMESTATE
     *
     * @param s WHITE if white wins, BLACK if black wins, null if draw
     */
    private void gameOver(Side s)
    {
        if (s == null)
        {
            state = GameState.DRAW;
        }
        else if (s == Side.WHITE)
        {
            state = GameState.WHITE_WINS;
        }
        else
        {
            state = GameState.BLACK_WINS;
        }
    }

    /**
     * @param s A side
     * @return The player playing with S
     */
    protected Player getPlayer(Side s)
    {
        if (s == Side.WHITE)
        {
            return white;
        }
        else
        {
            return black;
        }
    }

    /**
     * @param s A side
     * @return The player playing against S
     */
    private Player getOpponent(Side s)
    {
        if (s == Side.WHITE)
        {
            return black;
        }
        else
        {
            return white;
        }
    }

    /**
     * Updates THIS.FEN according to M
     *
     * @param m The move played requiring THIS.FEN to be updated
     * @throws InvalidSquareException @see
     * @see #getSquare(String)
     */
    private void updateFen(Move m) throws InvalidSquareException
    {
        Man man = m.getPiece();
        boolean wkMoved = man instanceof King && man.getColor() == Side.WHITE;
        boolean bkMoved = man instanceof King && man.getColor() == Side.BLACK;

        boolean wqrMoved = man instanceof Rook && m.getFrom().equals(getSquare("a1"));
        boolean wkrMoved = man instanceof Rook && m.getFrom().equals(getSquare("h1"));
        boolean bqrMoved = man instanceof Rook && m.getFrom().equals(getSquare("a8"));
        boolean bkrMoved = man instanceof Rook && m.getFrom().equals(getSquare("h8"));

        Square to = m.getTo();
        boolean epFlag = man instanceof Pawn && Math.abs(to.getRankInt() - m.getFrom().getRankInt()) == 2;
        Square epSquare = null;
        if (epFlag)
        {
            epSquare = toMove == Side.WHITE ? to.getSouthSquare() : to.getNorthSquare();
        }

        boolean zeroHM = man instanceof Pawn || m.isCapture();

        this.fen = new FEN(board, Utils.flip(toMove), fen.isWk() && !wkMoved && !wkrMoved, fen.isWq() && !wkMoved && !wqrMoved, fen.isBk() && !bkMoved && !bkrMoved, fen.isBq() && !bkMoved && !bqrMoved, epSquare, zeroHM ? 0 : fen.getHalfmoves() + 1, toMove == Side.WHITE ? fen.getFullmoves() : fen.getFullmoves() + 1);
        updateFenHistory(this.fen);
        this.toMove = Utils.flip(toMove);
    }

    /**
     * @return The FEN object
     */
    protected FEN getFen()
    {
        return fen;
    }

    /**
     * @return A list of all moves played
     */
    protected ArrayList<Move> getMoveHistory()
    {
        return moveHistory;
    }

    /**
     * @return A list of all FEN states of the game
     */
    protected ArrayList<FEN> getFenHistory()
    {
        return fenHistory;
    }

    /**
     * @return The game state - in progress, white has won, black has won, draw
     */
    protected GameState getState()
    {
        return state;
    }
}