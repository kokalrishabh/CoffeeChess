import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class VisualSquare extends JLabel
{
    private final Square square;
    private final int rankInt;
    private final int fileInt;
    private ImageIcon icon;
    private String iconString;
    private final VisualBoard visualBoard;

    protected static final int ICON_SIZE = 60;

    public VisualSquare(Square s, VisualBoard vb)
    {
        square = s;
        rankInt = s.getRankInt();
        fileInt = s.getFileInt();

        visualBoard = vb;

        setUnselectedIcon();

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                try
                {
                    clicked();
                }
                catch (Exception ex) // Should never happen
                {
                    ex.printStackTrace();
                    for (Move m : visualBoard.getBoard().getMoveHistory())
                    {
                        System.out.println(m);
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    /**
     * Called when THIS is clicked. Tells VISUALBOARD that THIS has been clicked.
     *
     * @throws InvalidSquareException Should never happen
     */
    public void clicked() throws InvalidSquareException, TooManyKingsException, InvalidPieceException, InvalidMoveException
    {
        visualBoard.squareClicked(this);
    }

    /**
     * Sets ICON to the corresponding unselected icon of SQUARE.
     */
    protected void setUnselectedIcon()
    {
        StringBuilder sb = new StringBuilder();

        if ((rankInt + fileInt) % 2 == 0)
        {
            sb.append('l');
        }
        else
        {
            sb.append('d');
        }

        Man m = square.getPiece();
        if (m != null)
        {
            sb.append(m.getColor() == Side.WHITE ? 'w' : 'b');
            sb.append(m.toString().toLowerCase());
        }
        else
        {
            sb.append('0');
        }

        sb.append('0');

        iconString = sb.toString();

        icon = new ImageIcon(new ImageIcon(this.getClass().getResource("/Images/" + iconString + ".png")).getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH)); // icon = Icons.dir.get(iconString); // new javax.swing.ImageIcon(this.getClass().getResource("/Images/" + iconString + ".png")).getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH)); // Icons.dir.get(iconString);
        setIcon(icon);
    }

    /**
     * Overrides Object#toString() for printing purposes
     *
     * @return The String representation of SQUARE
     * @see Square#toString()
     */
    @Override
    public String toString()
    {
        return square.toString();
    }

    /**
     * @return The square THIS represents
     */
    protected Square getSquare()
    {
        return square;
    }

    /**
     * Changes ICON to show THIS as selected
     */
    protected void select()
    {
        iconString = iconString.substring(0, iconString.length() - 1) + "1";
        icon = new ImageIcon(new ImageIcon(this.getClass().getResource("/Images/" + iconString + ".png")).getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
        setIcon(icon); // setIcon(Icons.dir.get(iconString));
    }

    /**
     * Changes ICON to show THIS as unselected
     */
    protected void unselect()
    {
        if (iconString.charAt(2) == 'd')
        {
            iconString = iconString.charAt(0) + "00";
        }
        else if (iconString.length() == 3)
        {
            iconString = iconString;
        }
        else
        {
            iconString = iconString.substring(0, 3) + "0";
        }

        icon = new ImageIcon(new ImageIcon(this.getClass().getResource("/Images/" + iconString + ".png")).getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
        setIcon(icon);
        // setIcon(Icons.dir.get(iconString));
    }

    /**
     * Called when VISUALBOARD has a square clicked whose piece has a legal move to SQUARE.
     * Shows THIS as dotted if SQUARE has no piece, or as selected otherwise.
     */
    protected void showAsLegal()
    {
        if (square.getPiece() == null)
        {
            iconString = iconString.charAt(0) + (visualBoard.getPlaySide() == Side.WHITE ? "w" : "b") + "d0";
            icon = new ImageIcon(new ImageIcon(this.getClass().getResource("/Images/" + iconString + ".png")).getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
            setIcon(icon);
            //setIcon(Icons.dir.get(iconString));
        }
        else
        {
            select();
        }
    }

    /**
     * @return The integer of the rank of the square represented by THIS
     */
    protected int getRankInt()
    {
        return rankInt;
    }

    /**
     * @return The integer corresponding to the file of the square represented by THIS
     */
    protected int getFileInt()
    {
        return fileInt;
    }
}
