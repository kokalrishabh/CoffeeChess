import java.io.FileNotFoundException;
import java.util.Scanner;

public class TestStuff
{
    public static void main(String[] args) throws FileNotFoundException, InvalidPGNException, TooManyKingsException, InvalidMoveException, InvalidSquareException, InvalidPieceException, InvalidFENException
    {
        // playAndPrintGame(new String[] {"e4", "e5", "Nf3", "Nf6", "Nxe5", "Nc6", "Nxc6", "dxc6", "Be2", "Bc5", "O-O", "h5", "Nc3", "Ng4", "Qe1", "Qh4", "Bxg4", "hxg4", "h3", "gxh3", "d4", "Qg4", "g3"});
        VisualOpeningPractice.practice();
        // System.out.println(VisualizationTestBoard.getPieceSelection());
        // new PGNMerger();
        // playVisualGame();

        // new VisualizationPractice(PGNManager.convertPGNToTree(new File("/Users/rish/Documents/Rishabh/Other Life/Chess/PGNs/Coffee PGNs/White/Vienna/Vienna Gambit.pgn"), new Board()), 6);
        //File f = new File("~/Documents/Rishabh/Other Life/Chess/PGNs/");
        //System.out.println(f.isDirectory());
        /*Scanner sc = new Scanner(f);
        while (sc.hasNextLine())
        {
            System.out.println(sc.nextLine());
        }*/
    }

    public static void playVisualGame() throws TooManyKingsException, InvalidMoveException, InvalidSquareException, InvalidPieceException, InvalidFENException
    {
        TwoPlayerGame.main(new String[] {});
    }

    public static void playGame() throws TooManyKingsException, InvalidMoveException, InvalidSquareException, InvalidPieceException, InvalidFENException
    {
        Board b = new Board();
        Scanner sc = new Scanner(System.in);

        while (true)
        {
            System.out.println(b);
            System.out.println();
            System.out.print("Enter next move: ");
            String s = sc.nextLine();
            if (s.equals("Quit Game"))
            {
                break;
            }
            b.move(new Move(s, b));
            System.out.println();
        }
    }

    public static void printTree(MoveTreeNode n, int i)
    {
        /*System.out.print(i);
        System.out.print(" - ");*/
        for (int j = 0; j < i; j++)
        {
            System.out.print("|   ");
        }
        System.out.println(n);
        for (MoveTreeNode child : n.getChildren())
        {
            printTree(child, i + 1);
        }
    }


    public static void playAndPrintGame(String[] moves) throws TooManyKingsException, InvalidMoveException, InvalidSquareException, InvalidPieceException, InvalidFENException
    {
        Board b = new Board();
        for (String move : moves)
        {
            b.move(new Move(move, b));
            System.out.println(b);
            if (move.equals("g6"))
            {
                System.out.println(move);
                System.out.println();
                System.out.println(b);
                System.out.println();
                for (Move m : b.getWhite().getLegalMoves())
                {
                    System.out.println(m);
                }
                System.out.println();
                System.out.println();
            }
        }
        System.out.println(b.getState());
    }
}
