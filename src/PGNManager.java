import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PGNManager
{
    /**
     * Converts the PGN file to a move tree
     *
     * @param pgn A PGN file to be converted to a move tree
     * @param b A Board used for FEN construction - MAY NOT BE USED IN FUTURE DELETE IF NECESSARY
     * @return The root of the move tree
     */
    protected static MoveTreeNode convertPGNToTree(File pgn, Board b) throws FileNotFoundException, InvalidPGNException, InvalidFENException, InvalidSquareException
    {
        String result;
        String fen;
        String moveStr;

        // Read and convert the file to a String array containing the result String, the FEN String, and the move String
        String[] temp = readFile(pgn);
        result = temp[0];
        fen = temp[1];

        // Process the big move String into an list of move Strings
        moveStr = temp[2].replaceAll("\n", " ");
        moveStr = moveStr.replaceAll("\\{(.|\\n)+?}", "");
        moveStr = moveStr.replaceAll("\\d+?\\.(\\.\\.)? ", "");
        moveStr = moveStr.replaceAll("\\$\\d+", "");
        moveStr = moveStr.replaceAll("\\(", " ( ");
        moveStr = moveStr.replaceAll("\\)", " ) ");
        moveStr = moveStr.replaceAll(" {2,}", " ");

        ArrayList<String> moves = new ArrayList<>(Arrays.asList(moveStr.split(" ")));

        // Create a tree from the list of move Strings, and return the root of the tree
        MoveTreeNode root = new MoveTreeNode(fen, null, b);
        createTreeFromMoves(moves, root, result);
        return root;
    }

    /**
     * Recursively creates a move tree from MOVES
     *
     * @param moves A list of moves to be converted to a move tree
     * @param parent The parent node for this recursive step
     * @param result The String after which to return
     */
    private static void createTreeFromMoves(ArrayList<String> moves, MoveTreeNode parent, String result) throws InvalidFENException, InvalidSquareException
    {
        String s = moves.remove(0);
        if (s.equals(result))
        {
            return;
        }
        else if (s.equals("("))
        {
            MoveTreeNode grandparent = parent.getParent();
            createTreeFromMoves(moves, grandparent, ")");
            createTreeFromMoves(moves, parent, result);
        }
        else
        {
            MoveTreeNode n = new MoveTreeNode(s, parent, null);
            parent.addChild(n);
            createTreeFromMoves(moves, n, result);
        }
    }

    /**
     * Reads the PGN file, extracting the result String from the Result tag, the FEN String from the FEN tag if it exists,
     *     and all moves and variations.
     *
     * @param pgn A PGN file to be read
     * @return {result, fenString, PGN moves}
     */
    protected static String[] readFile(File pgn) throws FileNotFoundException, InvalidPGNException
    {
        Scanner sc = new Scanner(pgn);
        boolean tags = true;
        StringBuilder sb = new StringBuilder();
        String result = null;
        String fen = FEN.DEFAULT_FEN;

        while (sc.hasNextLine())
        {
            String ln = sc.nextLine();

            if (tags)
            {
                if (!ln.equals(""))
                {
                    if (ln.charAt(0) == '[')
                    {
                        Pattern resultPattern = Pattern.compile("\\[Result \"(.+?)\"]");
                        Matcher resultMatcher = resultPattern.matcher(ln);
                        if (resultMatcher.matches())
                        {
                            result = resultMatcher.group(1);
                        }
                        else
                        {
                            Pattern fenPattern = Pattern.compile("\\[FEN \"(.+?)\"]");
                            Matcher fenMatcher = fenPattern.matcher(ln);
                            if (fenMatcher.matches())
                            {
                                fen = fenMatcher.group(1);
                            }
                        }
                    }
                    else if (ln.charAt(0) == '1')
                    {
                        if (result == null)
                        {
                            throw new InvalidPGNException("PGN must have a result tag");
                        }
                        tags = false;
                        sb.append(ln);
                    }
                    else if (ln.charAt(0) == '{')
                    {
                        while (ln.charAt(0) != '}')
                        {
                            if (ln.charAt(0) == '\\')
                            {
                                if (ln.charAt(1) == '}')
                                {
                                    ln = ln.substring(2);
                                }
                            }
                            ln = ln.substring(1);
                            if (ln.length() == 0)
                            {
                                ln = sc.nextLine();
                            }
                        }
                        ln = ln.substring(1).trim();
                        sb.append(ln);
                        tags = false;
                    }
                    else
                    {
                        throw new InvalidPGNException("Excluding tags, PGN file must start with \"1.\"");
                    }
                }
            }
            else
            {
                sb.append(' ');
                sb.append(ln);
            }
        }

        return new String[] {result, fen, sb.toString().trim()};
    }

    /**
     * Calls helper recursive method, which has more parameters, for each child of ROOT and concatenates them with necessary changes
     *
     * @param root The root node of the tree from which the PGN String is being returned. Must be a root node.
     * @return A PGN of the move tree, as a string (not a file)
     */
    protected static String convertTreeToPGN(MoveTreeNode root)
    {
        assert root.isRoot();

        StringBuilder sb = new StringBuilder();

        sb.append("[Event \"?\"]\n" +
                "[Site \"?\"]\n" +
                "[Date \"????.??.??\"]\n" +
                "[Round \"?\"]\n" +
                "[White \"?\"]\n" +
                "[Black \"?\"]\n" +
                "[Result \"*\"]\n");

        FEN f = root.getFen();
        if (!f.isStartingPositionFen())
        {
            sb.append(String.format("[FEN \"%s\"]\n", f));
        }

        sb.append('\n');
        sb.append(convertTreeToPGN(root, 1, Side.BLACK, false, false));
        sb.append('*'); // TODO: Add different results

        return sb.toString();
    }

    /**
     * Initially called on a root node and recurses through each node to create a PGN String for the move tree.
     *
     * @param node The acting root node
     * @param moveCount The fullmove count for legibility purposes
     * @param side The side playing NODE's move
     * @param isHead True if NODE's index in NODE's PARENT's CHILDREN list is greater than 0, false otherwise
     * @param isPrimary True if NODE's PARENT has more than one child and NODE's index in NODE's PARENT's CHILDREN list is 0, false if either one is not true
     * @return A PGN of the move tree, as a string (not a file)
     */
    private static String convertTreeToPGN(MoveTreeNode node, int moveCount, Side side, boolean isHead, boolean isPrimary)
    {
        assert !(isHead && isPrimary);

        StringBuilder sb = new StringBuilder();

        if (isHead)
        {
            sb.append(moveCount).append(side == Side.WHITE ? ". " : "... ").append(node.getMove()).append(' ');
        }

        ArrayList<MoveTreeNode> children = node.getChildren();
        if (children.size() != 0)
        {
            MoveTreeNode primary = children.get(0);
            if (isPrimary)
            {
                sb.append(moveCount).append(side == Side.WHITE ? "... " : ". ");
            }
            else if (side == Side.BLACK)
            {
                if (isHead)
                {
                    moveCount++;
                }
                sb.append(moveCount).append(". ");
            }
            sb.append(primary.getMove()).append(' ');
            boolean primFlag = false;
            for (int i = 1; i < children.size(); i++)
            {
                primFlag = true;
                String s = convertTreeToPGN(children.get(i), moveCount, Utils.flip(side), true, false);
                sb.append('(').append(s);
                sb.setLength(sb.length() - 1);
                sb.append(')').append(' ');
            }
            if (side == Side.WHITE)
            {
                moveCount++;
            }
            String s = convertTreeToPGN(primary, moveCount, Utils.flip(side), false, primFlag);
            sb.append(s);
        }

        return sb.toString();
    }

    /**
     * Destructively merges all trees in LST into one
     *
     * @param lst A list of root nodes of move trees
     * @return The root node of the merged tree
     */
    protected static MoveTreeNode mergeTrees(ArrayList<MoveTreeNode> lst)
    {
        for (MoveTreeNode n : lst)
        {
            assert n.isRoot();
        }

        MoveTreeNode trueRoot = lst.get(0);

        for (int i = 1; i < lst.size(); i++)
        {
            trueRoot = mergeTwoTrees(trueRoot, lst.get(i));
        }
        return trueRoot;
    }

    /**
     * Destructively and recursively merges N2 into N1
     *
     * @param n1 A MoveTreeNode
     * @param n2 A MoveTreeNode
     * @return N1, with N2 merged in
     */
    private static MoveTreeNode mergeTwoTrees(MoveTreeNode n1, MoveTreeNode n2)
    {
        if (n1.equals(n2))
        {
            ArrayList<MoveTreeNode[]> arr = MoveTreeNode.pairChildren(n1, n2);
            for (MoveTreeNode n : arr.remove(arr.size() - 1))
            {
                n1.addChild(n);
            }
            arr.remove(arr.size() - 1);
            for (MoveTreeNode[] a : arr)
            {
                mergeTwoTrees(a[0], a[1]);
            }
        }
        else if (n1.isRoot() && n2.isRoot())
        {
            throw new UnsupportedOperationException("Make sure your roots are compatible");
        }
        else if (n1.isRoot() ^ n2.isRoot())
        {
            throw new UnsupportedOperationException("Make sure nodes are either both roots or both children");
        }
        else
        {
            n1.getParent().addChild(n2);
        }

        return n1;
    }
}