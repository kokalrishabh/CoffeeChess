import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VisualizationTestBoard extends VisualBoard
{
    private final VisualizationPractice session;

    public VisualizationTestBoard(VisualizationPractice vp) throws TooManyKingsException, InvalidMoveException, InvalidSquareException, InvalidPieceException, InvalidFENException
    {
        super(Side.WHITE);

        session = vp;

        /*for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                VisualSquare vs = vsquares[i][j];
                vs.setIcon(Icons.dir.get(((vs.getRankInt() + vs.getFileInt()) % 2 == 0 ? "l" : "d") + "00"));
            }
        }*/
    }

    @Override
    protected JToolBar makeToolbar() // TODO: Fix quit and replay
    {
        JToolBar tools = super.makeToolbar();

        JButton submit = new JButton("Submit");
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try
                {
                    session.runRound();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });

        Component c0 = tools.getComponentAtIndex(0);
        assert c0 instanceof JPanel;
        JPanel panel0 = (JPanel) c0;
        panel0.add(submit);

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

    @Override
    protected void squareClicked(VisualSquare vs) throws InvalidSquareException, InvalidMoveException, InvalidPieceException, TooManyKingsException
    {
        char c = getPieceSelection();
        StringBuilder sb = new StringBuilder();

        if ((vs.getSquare().getRankInt() + vs.getSquare().getFileInt()) % 2 == 0)
        {
            sb.append('l');
        }
        else
        {
            sb.append('d');
        }

        if (c == 0)
        {
            sb.append('0');
        }
        else
        {
            if (Character.isUpperCase(c))
            {
                sb.append('w');
            }
            else
            {
                sb.append('b');
            }
            sb.append(Character.toLowerCase(c));
        }

        sb.append('0');

        vs.setIcon(Icons.dir.get(sb.toString()));
    }

    /**
     * Creates a pop-up to receive input representing a piece and its color
     * @return A character representing a piece and its color
     */
    protected static char getPieceSelection()
    {
        JPanel p = new JPanel();
        p.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JPanel left = new JPanel();
        JPanel right  = new JPanel();
        c.gridx = 0;
        c.gridy = 0;
        p.add(left, c);
        c.gridx = 1;
        p.add(right, c);

        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        JRadioButton white = new JRadioButton("White");
        JRadioButton black = new JRadioButton("Black");
        ButtonGroup colorGroup = new ButtonGroup();
        colorGroup.add(white);
        colorGroup.add(black);
        left.add(white);
        left.add(black);

        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        JRadioButton king = new JRadioButton("King");
        JRadioButton queen = new JRadioButton("Queen");
        JRadioButton rook = new JRadioButton("Rook");
        JRadioButton bishop = new JRadioButton("Bishop");
        JRadioButton knight = new JRadioButton("Knight");
        JRadioButton pawn = new JRadioButton("Pawn");
        ButtonGroup pieceGroup = new ButtonGroup();
        pieceGroup.add(king);
        pieceGroup.add(queen);
        pieceGroup.add(rook);
        pieceGroup.add(bishop);
        pieceGroup.add(knight);
        pieceGroup.add(pawn);
        right.add(king);
        right.add(queen);
        right.add(rook);
        right.add(bishop);
        right.add(knight);
        right.add(pawn);

        int pieceQ = JOptionPane.showOptionDialog(null, p, "Choose a piece", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        char pieceChar = 0;
        if (pieceQ == 0)
        {
            boolean whiteSel = white.isSelected();
            boolean blackSel = black.isSelected();
            if (whiteSel || blackSel)
            {
                if (king.isSelected())
                {
                    pieceChar = 'k';
                }
                else if (queen.isSelected())
                {
                    pieceChar = 'q';
                }
                else if (rook.isSelected())
                {
                    pieceChar = 'r';
                }
                else if (bishop.isSelected())
                {
                    pieceChar = 'b';
                }
                else if (knight.isSelected())
                {
                    pieceChar = 'n';
                }
                else if (pawn.isSelected())
                {
                    pieceChar = 'p';
                }

                if (whiteSel)
                {
                    pieceChar = Character.toUpperCase(pieceChar);
                }
            }
        }

        return pieceChar;
    }

    protected boolean compare()
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++)
        {
            int count = 0;
            for (int j = 0; j < 8; j++)
            {
                String iconStr = Icons.revDir.get((ImageIcon) vsquares[i][j].getIcon());
                iconStr = iconStr.substring(1, iconStr.length() - 1);

                if (iconStr.equals("0"))
                {
                    sb.append(1);
                }
                else
                {
                    if (iconStr.charAt(0) == 'w')
                    {
                        iconStr = iconStr.substring(1).toUpperCase();
                    }
                    else
                    {
                        iconStr = iconStr.substring(1);
                    }
                    sb.append(iconStr);
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

        String boardString = sb2.toString();

        return boardString.equals(board.getFen().getBoardString());
    }
}
