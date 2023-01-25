import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class VisualBoard extends JPanel
{
    private static final JLabel[] RANK_LABELS = {new JLabel("1"), new JLabel("2"), new JLabel("3"), new JLabel("4"), new JLabel("5"), new JLabel("6"), new JLabel("7"), new JLabel("8")};
    private static final JLabel[] FILE_LABELS = {new JLabel("a"), new JLabel("b"), new JLabel("c"), new JLabel("d"), new JLabel("e"), new JLabel("f"), new JLabel("g"), new JLabel("h")};
    protected static final String[] PROMOTION_OPTIONS = new String[] {"Knight", "Bishop", "Rook", "Queen"};
    protected final static String[] YN_OPTIONS = new String[] {"Yes", "No"};

    protected final Board board;
    protected Side playSide;
    protected Side viewSide;
    protected final VisualSquare[][] vsquares;

    protected VisualSquare selectedSquare;
    protected ArrayList<Square> legalSquares;

    protected boolean playable;

    protected ChessHostFrame host;

    public VisualBoard(Side side) throws TooManyKingsException, InvalidMoveException, InvalidSquareException, InvalidPieceException, InvalidFENException
    {
        super();
        board = new Board();
        playSide = side;
        viewSide = side;
        selectedSquare = null;
        legalSquares = new ArrayList<>();
        playable = true;
        host = null;

        setMinimumSize(new Dimension(Icons.ICON_SIZE * 10, Icons.ICON_SIZE * 10));

        vsquares = new VisualSquare[8][8];

        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                vsquares[i][j] = new VisualSquare(board.getSquare(i, j), this);
            }
        }

        setLayout(new GridBagLayout());
        addComponents();
    }

    /**
     * Adds the components of the board (i.e., the VisualSquares, the labels, the toolbar)
     */
    private void addComponents()
    {
        GridBagConstraints c = new GridBagConstraints();

        for (int j = 1; j < 9; j++)
        {
            c.gridx = j;
            c.gridy = 0;
            c.fill = GridBagConstraints.NONE;
            add(FILE_LABELS[viewSide == Side.WHITE ? j - 1 : 8 - j], c);
        }

        for (int i = 1; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                c.gridx = j;
                c.gridy = i;
                c.fill = GridBagConstraints.BOTH;
                if (j == 0)
                {
                    add(RANK_LABELS[viewSide == Side.WHITE ? 8 - i : i - 1], c);
                }
                else
                {
                    add(vsquares[viewSide == Side.WHITE ? i - 1 : 8 - i][viewSide == Side.WHITE ? j-1 : 8 - j], c);
                }
            }
        }

        JToolBar tools = makeToolbar();
        c.gridx = 1;
        c.gridy = 10;
        c.gridwidth = 8;
        add(tools, c);
    }

    /**
     * Creates the toolbar for the board
     *
     * @return The JToolbar created
     */
    protected JToolBar makeToolbar()
    {
        JToolBar tools = new JToolBar();

        tools.setFloatable(false);

        JButton prev = new JButton("<");
        prev.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (playable)
                {
                    try
                    {
                        undoMove();
                    }
                    catch (Exception ex)
                    {
                        System.out.println("Something went wrong with move undoing");
                        ex.printStackTrace();
                    }
                }
            }
        });
        JButton next = new JButton(">");
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try
                {
                    redoMove();
                }
                catch (Exception ex)
                {
                    System.out.println("Something went wrong with move redoing");
                    ex.printStackTrace();
                }
            }
        });

        JButton flip = new JButton("Flip");
        flip.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                flipBoard();
            }
        });
        JButton quit = new JButton("Quit");
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                quitSequence();
            }
        });

        JPanel pnPanel = new JPanel();
        JPanel flipPanel = new JPanel();
        JPanel quitPanel = new JPanel();

        pnPanel.add(prev);
        pnPanel.add(next);
        tools.add(pnPanel);
        tools.addSeparator();
        flipPanel.add(flip);
        tools.add(flipPanel);
        tools.addSeparator();
        quitPanel.add(quit);
        tools.add(quitPanel);

        return tools;
    }

    /**
     * Flips the board to be viewed from the other perspective
     */
    private void flipBoard()
    {
        viewSide = Utils.flip(viewSide);
        removeAll();
        addComponents();
        revalidate();
        repaint();
        legalSquares.clear();
    }

    /**
     * Loads the board from FEN
     *
     * @param fen The FEN from which the board should be loaded
     * @throws TooManyKingsException @see
     * @throws InvalidMoveException @see
     * @throws InvalidSquareException @see
     * @throws InvalidPieceException @see
     * @see Board#loadFromFen(FEN)
     */
    void loadFromFen(FEN fen) throws TooManyKingsException, InvalidMoveException, InvalidSquareException, InvalidPieceException
    {
        board.loadFromFen(fen);
        reloadBoard();
    }

    /**
     * For every square, set the icon correctly
     */
    private void reloadBoard()
    {
        for (VisualSquare[] vsArr : vsquares)
        {
            for (VisualSquare vs : vsArr)
            {
                vs.setUnselectedIcon();
            }
        }
    }

    /**
     * Undoes the last move on the board, then reloads the visuals
     *
     * @throws TooManyKingsException @see
     * @throws InvalidMoveException @see
     * @throws InvalidSquareException @see
     * @throws InvalidPieceException @see
     * @see Board#undoMove()
     */
    void undoMove() throws TooManyKingsException, InvalidMoveException, InvalidSquareException, InvalidPieceException
    {
        board.undoMove();
        reloadBoard();
    }

    /**
     * Redoes a single move, then reloads the visuals
     *
     * @throws TooManyKingsException @see
     * @throws InvalidMoveException @see
     * @throws InvalidSquareException @see
     * @throws InvalidPieceException @see
     * @see Board#redoMove()
     */
    void redoMove() throws TooManyKingsException, InvalidMoveException, InvalidSquareException, InvalidPieceException
    {
        board.redoMove();
        reloadBoard();
    }

    /**
     * Called by MouseListener of VS. Handles square click.
     *
     * @param vs The square clicked on
     * @throws InvalidSquareException @see1, @see2, @see3
     * @throws InvalidMoveException @see1, @see2
     * @throws InvalidPieceException @see1, @see2
     * @throws TooManyKingsException @see1
     * @see Move#Move(String, Board)
     * @see Board#move(Move)
     * @see #getVisualSquare(String)
     */
    protected void squareClicked(VisualSquare vs) throws InvalidSquareException, InvalidMoveException, InvalidPieceException, TooManyKingsException
    {
        Square s = vs.getSquare();
        Man m = s.getPiece();

        unselectAll();

        if (board.getToMove() == playSide)
        {
            if (legalSquares.contains(s))
            {
                char c = 0;
                Man piece = selectedSquare.getSquare().getPiece();
                if (piece instanceof Pawn && s.getRankInt() == (playSide == Side.WHITE ? 8 : 1))
                {
                    int promotionPiece = JOptionPane.showOptionDialog(
                            null,
                            "Promote to which piece?",
                            "Promotion",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            PROMOTION_OPTIONS,
                            PROMOTION_OPTIONS[3]);

                    c = switch(promotionPiece)
                            {
                                case 0 -> 'N';
                                case 1 -> 'B';
                                case 2 -> 'R';
                                case 3 -> 'Q';
                                default -> (char) 0;
                            };
                }

                Move move = new Move(piece, s, board, c);
                move(move);

                flipPlaySide();
            }
            else
            {
                legalSquares.clear();
                if (m != null && m.getColor() == playSide)
                {
                    selectedSquare = vs;
                    vs.select();
                    for (Move move : m.getLegalMoves())
                    {
                        Square to = move.getTo();
                        legalSquares.add(to);
                        VisualSquare vto = getVisualSquare(to.toString());
                        vto.showAsLegal();
                    }
                }
            }
        }
    }

    /**
     * Plays M on the underlying Board and the VisualBoard
     *
     * @param m A move to be played
     * @throws InvalidSquareException @see1, @see2
     * @throws TooManyKingsException @see1
     * @throws InvalidPieceException @see1
     * @throws InvalidMoveException @see1
     * @see Board#move(Move)
     * @see #getVisualSquare(String)
     */
    protected void move(Move m) throws InvalidSquareException, TooManyKingsException, InvalidPieceException, InvalidMoveException
    {
        board.move(m);
        legalSquares.clear();

        Square to = m.getTo();

        getVisualSquare(m.getFrom().toString()).setUnselectedIcon();
        getVisualSquare(to.toString()).setUnselectedIcon();

        if (m.isEnPassant())
        {
            getVisualSquare((m.getPiece().getColor() == Side.WHITE ? to.getSouthSquare() : to.getNorthSquare()).toString()).setUnselectedIcon();
        }
        else if (m.getCastle() == Castle.KING)
        {
            getVisualSquare(m.getTo().getWestSquare().toString()).setUnselectedIcon();
            getVisualSquare(m.getRookSquare().toString()).setUnselectedIcon();
        }
        else if (m.getCastle() == Castle.QUEEN)
        {
            getVisualSquare(m.getTo().getEastSquare().toString()).setUnselectedIcon();
            getVisualSquare(m.getRookSquare().toString()).setUnselectedIcon();
        }

        GameState gs = board.getState();
        if (gs != GameState.IN_PROGRESS)
        {
            if (gs == GameState.DRAW)
            {
                JOptionPane.showMessageDialog(null, "It's a draw!", "Game over", JOptionPane.PLAIN_MESSAGE);
            }
            else
            {
                JOptionPane.showMessageDialog(null, (gs == GameState.WHITE_WINS ? "White" : "Black") + " wins!", "Game over", JOptionPane.PLAIN_MESSAGE);
            }
            playable = false;
        }
    }

    /**
     * Changes icons of all VisualSquares to be unselected
     */
    protected void unselectAll()
    {
        for (VisualSquare[] arr : vsquares)
        {
            for (VisualSquare vs : arr)
            {
                vs.unselect();
            }
        }
    }

    /**
     * @param san The SAN of the square
     * @return The square notated if the square is a valid notation. Null if "-" is the SAN (for en passant purposes).
     * @throws InvalidSquareException if the square notation is invalid and SAN is also not "-".
     */
    protected VisualSquare getVisualSquare(String san) throws InvalidSquareException
    {
        if (san.equals("-"))
        {
            return null;
        }
        if (!Square.isValidSquare(san))
        {
            throw new InvalidSquareException("Invalid square");
        }
        return vsquares[Square.getRankFromName(san)][Square.getFileNumberFromName(san)];
    }

    /**
     * @return The side with which the player is playing
     */
    protected Side getPlaySide()
    {
        return playSide;
    }

    /**
     * @return The Board object
     */
    protected Board getBoard()
    {
        return board;
    }

    /**
     * Flip PLAYSIDE to allow the other player to play
     */
    protected void flipPlaySide()
    {
        playSide = Utils.flip(playSide);
    }

    /**
     * Make the board playable.
     */
    protected void makePlayable()
    {
        playable = true;
    }

    /**
     * Make the board unplayable.
     */
    protected void makeUnplayable()
    {
        playable = false;
    }

    /**
     * Ensures the user wants to quit and acts accordingly
     *
     * @return False if not quitting. Doesn't return true because it will just kill the program instead.
     */
    protected boolean quitSequence()
    {
        int exit = JOptionPane.showOptionDialog(null, "Are you sure you want to quit?", "", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, YN_OPTIONS, YN_OPTIONS[0]);
        if (exit == 0)
        {
            host.setVisible(false);
            JOptionPane.showMessageDialog(null, "Ok! Bye-bye!");
            System.exit(0);
        }
        else
        {
            host.setVisible(true); // TODO: Fix quit sequence for two-player game
        }
        return false;
    }

    /**
     * Exit smoothly
     */
    protected void exitSequence()
    {
        int exit = JOptionPane.showOptionDialog(null, "Would you like to play again?", "Play again?", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, YN_OPTIONS, YN_OPTIONS[0]);
        if (exit == 0)
        {
            try
            {
                Window w = SwingUtilities.getWindowAncestor(this);
                w.removeAll();
                w.add(new VisualBoard(playSide));
                w.revalidate();
                w.repaint();
            }
            catch(Exception e)
            {
                System.exit(0);
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Ok! Bye-bye!");
            System.exit(0);
        }

    }

    /**
     * Save F as the host for closing purposes
     *
     * @param f The frame in which THIS is added
     */
    protected void setHost(ChessHostFrame f)
    {
        host = f;
    }
}
