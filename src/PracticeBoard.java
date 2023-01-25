import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class PracticeBoard extends VisualBoard
{
    private int moveTime;
    private int tpm;
    private int numReveals;

    private int wrongCount;
    private MoveTreeNode curr;

    private final Random r;
    private final long seed;

    private final Runnable compMover;

    private final ArrayList<Integer> undoneMoveIndices;

    private int revealCount;

    public PracticeBoard(Side s, MoveTreeNode node) throws TooManyKingsException, InvalidMoveException, InvalidSquareException, InvalidPieceException, InvalidFENException
    {
        super(s);

        moveTime = 200;
        tpm = 3;
        numReveals = 3;

        wrongCount = 1;
        curr = node;
        playable = curr != null;

        r = new Random();
        seed = r.nextLong();
        r.setSeed(seed);

        compMover = new Runnable()
        {
            @Override
            public void run()
            {
                playComputerMove();
            }
        };

        undoneMoveIndices = new ArrayList<>();

        revealCount = 1;
    }

    /**
     * Takes the toolbar created in VisualBoard and adds a Reroll, an "I don't know", and a Restart button.
     * 
     * @return The edited JToolbar
     */
    @Override
    protected JToolBar makeToolbar()
    {
        JToolBar tools = super.makeToolbar();

        JButton reroll = new JButton("Reroll");
        reroll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try
                {
                    undoOnce();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                SwingUtilities.invokeLater(compMover);
            }
        });

        JButton idk = new JButton("I don't know");
        idk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                revealAnswers();
            }
        });

        Component c2 = tools.getComponentAtIndex(2);
        assert c2 instanceof JPanel;
        JPanel panel2 = (JPanel) c2;
        panel2.add(reroll);
        panel2.add(idk);

        JButton restart =  new JButton("Restart");
        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int exit = JOptionPane.showOptionDialog(null, "Are you sure you would like to restart?", "Play again?", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, YN_OPTIONS, YN_OPTIONS[0]);
                if (exit == 0)
                {
                    host.replaySequence();
                }
            }
        });
        Component c4 = tools.getComponentAtIndex(4);
        assert c4 instanceof JPanel;
        JPanel panel4 = (JPanel) c4;
        panel4.add(restart);

        return tools;
    }

    /**
     * Handles square clicks for the PracticeBoard
     * 
     * @param vs The square clicked on
     * @throws InvalidSquareException @see1, @see2, @see3
     * @throws InvalidMoveException @see1, @see2
     * @throws InvalidPieceException @see1, @see2
     * @throws TooManyKingsException @see2
     * @see Move#Move(Man, Square, Board, char)
     * @see #move(Move)
     * @see #getVisualSquare(String)
     */
    @Override
    protected void squareClicked(VisualSquare vs) throws InvalidSquareException, InvalidMoveException, InvalidPieceException, TooManyKingsException
    {
        if (playable)
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

                    if (curr.isChild(move.toString()))
                    {
                        curr = curr.getChild(move.toString());
                        undoneMoveIndices.clear();
                        move(move);
                        playable = false;

                        if (!checkWin())
                        {
                            SwingUtilities.invokeLater(compMover);
                            playable = true;
                            wrongCount = 1;
                        }
                    }
                    else
                    {
                        if (tpm >= 0)
                        {
                            if (wrongCount == tpm)
                            {
                                StringBuilder errorMsgBuilder = new StringBuilder();
                                errorMsgBuilder.append("Uh-oh! The correct moves were:\n");
                                for (MoveTreeNode n : curr.getChildren())
                                {
                                    errorMsgBuilder.append(n.getMove()).append('\n');
                                }
                                errorMsgBuilder.append('\n');
                                errorMsgBuilder.append("That's okay! Practice and come back. I'll always be here.");
                                JOptionPane.showMessageDialog(null, errorMsgBuilder.toString());
                                exitSequence();
                            }
                            else
                            {
                                JOptionPane.showMessageDialog(null, String.format("That move is incorrect. %d tries left.", 3 - wrongCount));
                                wrongCount++;
                            }
                        }
                    }
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
    }

    /**
     * Has computer play a move
     */
    private void playComputerMove()
    {
        Utils.sleepy(moveTime);

        int kidIndex = r.nextInt(curr.getChildren().size());
        curr = curr.getChild(kidIndex);
        try
        {
            move(new Move(curr.getMove(), board));
        }
        catch (Exception e)
        {
            String s = createLineString();
            System.out.printf("The selected move (%s) caused an exception. Please check the PGN. For reference, the seed was %d and the line went %s\n", curr.getMove(), seed, s);
            System.out.println("Error message: " + e.getMessage());
            System.exit(1);
        }

        checkWin();
    }

    /**
     * Checks to see if the line is finished.
     *
     * @return True if the line is finished, false otherwise
     */
    private boolean checkWin()
    {
        int choices = curr.getChildren().size();
        if (choices == 0)
        {
            playerWins();
            return true;
        }
        return false;
    }

    /**
     * Called when player finishes their line. Congratulates player and ends program/sets up restart.
     */
    private void playerWins()
    {
        Utils.sleepy(500);

        JOptionPane.showMessageDialog(null, String.format("You did it! You played the correct line!\nJust so you remember, the line went:\n%s\nGreat job! Thanks for playing!", createLineString())); // TODO: Make this better
        exitSequence();
        Random temp = new Random();
        temp.nextInt();
    }

    /**
     * Sets CURR to N if it is null. Does nothing otherwise.
     *
     * @param n A MoveTreeNode, probably a root node
     * @throws TooManyKingsException @see
     * @throws InvalidMoveException @see
     * @throws InvalidSquareException @see
     * @throws InvalidPieceException @see
     * @see Board#loadFromFen(FEN)
     */
    protected void setRoot(MoveTreeNode n) throws TooManyKingsException, InvalidMoveException, InvalidSquareException, InvalidPieceException
    {
        if (n.isRoot())
        {
            loadFromFen(n.getFen());
            /// reloadBoard();
        }
        if (curr == null)
        {
            curr = n;
        }
        playable = true;
    }

    /**
     * Makes the board playable, provided CURR is not null
     *
     * @see VisualBoard#makePlayable()
     */
    @Override
    protected void makePlayable()
    {
        if (curr != null)
        {
            super.makePlayable();
        }
    }

    /**
     * Makes the computer play the first move, if no moves have been played.
     */
    protected void playFirstMove()
    {
        if (board.getMoveHistory().size() == 0)
        {
            SwingUtilities.invokeLater(compMover);
        }
    }

    /**
     * @return A String containing the line played so far
     */
    private String createLineString()
    {
        StringBuilder sb = new StringBuilder();
        boolean whiteFlag = board.getFenHistory().get(0).getToMove() == Side.WHITE;
        int i = 1;
        int len = 0;
        for (Move m : board.getMoveHistory())
        {
            if (whiteFlag)
            {
                sb.append(i).append(". ");
                len += 2;
            }
            else
            {
                i++;
            }
            sb.append(m).append(' ');
            len += m.toString().length() + 1;
            if (len >= 60)
            {
                sb.append('\n');
                len = 0;
            }
            whiteFlag = !whiteFlag;
        }
        return sb.toString();
    }

    /**
     * Asks the player if they'd like to play again. If yes, run practice() again, then quit after. Else quit now.
     */
    @Override
    protected void exitSequence()
    {
        host.setVisible(false);
        int exit = JOptionPane.showOptionDialog(null, "Would you like to play again?", "Play again?", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, YN_OPTIONS, YN_OPTIONS[0]);
        if (exit == 0)
        {
            host.replaySequence();
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Ok! Bye-bye!");
            System.exit(0);
        }
    }

    /**
     * Undoes the last two moves (in order to remain playable)
     *
     * @throws TooManyKingsException @see
     * @throws InvalidMoveException @see
     * @throws InvalidSquareException @see
     * @throws InvalidPieceException @see
     * @see #undoOnce()
     */
    @Override
    protected void undoMove() throws TooManyKingsException, InvalidMoveException, InvalidSquareException, InvalidPieceException
    {
        for (int i = 0; i < 2; i++)
        {
            undoOnce();
        }
    }

    /**
     * Undoes a single move, then updates CURR
     * @throws TooManyKingsException @see
     * @throws InvalidMoveException @see
     * @throws InvalidSquareException @see
     * @throws InvalidPieceException @see
     * @see VisualBoard#undoMove()
     */
    protected void undoOnce() throws TooManyKingsException, InvalidMoveException, InvalidSquareException, InvalidPieceException
    {
        super.undoMove();
        if (!curr.isRoot())
        {
            MoveTreeNode p = curr.getParent();
            for (int j = 0; j < p.getChildren().size(); j++)
            {
                if (p.getChild(j) == curr)
                {
                    undoneMoveIndices.add(j);
                    break;
                }
            }
            curr = p;
        }
    }

    /**
     * Redoes two moves (in order to remain playable)
     *
     * @throws TooManyKingsException @see
     * @throws InvalidMoveException @see
     * @throws InvalidSquareException @see
     * @throws InvalidPieceException @see
     * @see VisualBoard#redoMove()
     */
    @Override
    protected void redoMove() throws TooManyKingsException, InvalidMoveException, InvalidSquareException, InvalidPieceException
    {
        if (undoneMoveIndices.size() >= 2)
        {
            for (int i = 0; i < 2; i++)
            {
                super.redoMove();
                curr = curr.getChild(undoneMoveIndices.remove(undoneMoveIndices.size() - 1));
            }
        }
    }

    /**
     * Reveals the answers via dialog box up to 3 times
     */
    private void revealAnswers()
    {
        StringBuilder sb = new StringBuilder();

        boolean last = numReveals >= 0 && revealCount == numReveals;

        if (last)
        {
            sb.append("You've used all your reveals! \n");
        }

        sb.append("The correct moves are:\n");
        for (MoveTreeNode n : curr.getChildren())
        {
            sb.append(n.getMove()).append('\n');
        }
        sb.append('\n');

        if (numReveals >= 0 && revealCount == numReveals - 1)
        {
            sb.append("This is your last reveal!");
        }
        else if (last)
        {
            sb.append("That's okay! Practice and come back. I'll always be here.");
        }

        JOptionPane.showMessageDialog(null, sb.toString());

        if (last)
        {
            exitSequence();
        }

        revealCount++;
    }

    protected void setMoveTime(int mt)
    {
        moveTime = mt;
    }

    protected void setTPM(int t)
    {
        tpm = t;
    }

    protected void setNumReveals(int nr)
    {
        numReveals = nr;
    }
}
