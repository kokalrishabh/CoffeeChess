import javax.swing.*;

public class TwoPlayerGame
{
    public static void main(String[] args) throws TooManyKingsException, InvalidMoveException, InvalidSquareException, InvalidPieceException, InvalidFENException
    {
        VisualBoard vb = new VisualBoard(Side.WHITE);
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(vb);
        f.pack();
        f.setVisible(true);
    }
}
