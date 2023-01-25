import javax.swing.*;

public class VisualizationPractice
{
    private MoveTreeNode curr;
    private final VisualizationTestBoard vtb;
    private final Board b;
    private final int halfmoves;

    public VisualizationPractice(MoveTreeNode node, int halfmoves) throws TooManyKingsException, InvalidMoveException, InvalidSquareException, InvalidPieceException, InvalidFENException
    {
        curr = node;
        this.halfmoves = halfmoves;

        if (node.getFen() == null)
        {
            throw new InvalidFENException("Node must be a root node");
        }
        if (!node.getFen().isStartingPositionFen())
        {
            throw new InvalidFENException("FEN must be the starting position FEN");
        }
        boolean breakFlag = false;

        vtb = new VisualizationTestBoard(this);
        b = vtb.getBoard();

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(vtb);
        frame.pack();
        frame.setVisible(true);

        runRound();
    }

    private void win()
    {
        System.exit(0);
    }

    protected void runRound() throws InvalidPieceException, InvalidSquareException, InvalidMoveException, TooManyKingsException
    {
        boolean correct = vtb.compare();

        if (correct)
        {
            for (int i = 0; i < halfmoves; i++)
            {
                if (curr.getChildren().size() != 0)
                {
                    curr = curr.getChild(0);
                    System.out.println(curr.getMove());
                    b.move(new Move(curr.getMove(), b));
                    JOptionPane.showMessageDialog(null, curr.getMove());
                }
                else
                {
                    break;
                }
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null, "You lose"); // TODO: Make this better
        }
    }
}
