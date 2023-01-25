import java.util.ArrayList;
import java.util.HashMap;

public class MoveTreeNode
{
    private final boolean root; // True if the node is the root of the tree. False otherwise.
    private final String move; // The SAN of the move of the node
    private final MoveTreeNode parent; // The parent of the node
    private final ArrayList<MoveTreeNode> children; // List of children of the node
    private final FEN fen;

    /**
     * If the root node (indicated by a FEN String in place of a SAN), set the boolean root to true, and the non-list variables to null.
     * Otherwise, set the boolean root to false, and initialize the non-list variables as specified by the parameters.
     * In either case, initialize CHILDREN to be empty.
     *
     * @param san The SAN of the move of the node
     * @param p The parent of the node
     * @param b The board for which the move tree exists
     * @throws InvalidFENException @see
     * @throws InvalidSquareException @see
     * @see FEN#FEN(String, Board)
     */
    public MoveTreeNode(String san, MoveTreeNode p, Board b) throws InvalidFENException, InvalidSquareException
    {
        if (FEN.isValidFEN(san))
        {
            root = true;
            move = null;
            parent = null;
            fen = new FEN(san, b);
        }
        else
        {
            root = false;
            move = san;
            parent = p;
            fen = null;
        }
        children = new ArrayList<>();
    }

    /**
     * Adds CHILD to the children list of this node
     *
     * @param child A node to be added to the children list of this node
     */
    protected void addChild(MoveTreeNode child)
    {
        children.add(child);
    }

    /**
     * @return The parent of the node
     */
    protected MoveTreeNode getParent()
    {
        return parent;
    }

    /**
     * Overrides the toString() method of all objects for printing purposes
     *
     * @return The SAN as the String value of the node
     */
    @Override
    public String toString() {
        return move;
    }

    /**
     * Overrides the equals() method of all objects for comparison purposes.
     * Two MoveTreeNode instances are equal if <br/>
     *      &#9;1) They both have null moves and have equal FEN objects, or <br/>
     *      &#9;2) They both have the same non-null move, and their root nodes are effectively equal
     *
     * @param obj Any object being compared to THIS
     * @return True if the objects are effectively equal, false otherwise
     */
    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof MoveTreeNode))
        {
            return false;
        }
        MoveTreeNode n = (MoveTreeNode) obj;

        if (root != n.isRoot())
        {
            return false;
        }

        if (root)
        {
            return fen.equals(n.fen);
        }
        else
        {
            return move.equals(n.move) && getRoot().equals(n.getRoot());
        }
    }

    /**
     * @return The SAN of the move
     */
    protected String getMove()
    {
        return move;
    }

    /**
     * @return The list of children of the node
     */
    protected ArrayList<MoveTreeNode> getChildren()
    {
        return children;
    }

    /**
     * @param i An integer
     * @return The ith (zero-indexed) child of the node if it exists, null otherwise.
     */
    protected MoveTreeNode getChild(int i)
    {
        if (i < children.size() && i >= 0)
        {
            return children.get(i);
        }
        else
        {
            return null;
        }
    }

    /**
     * @param san A move notated in SAN
     * @return The node's child with move SAN if it exists, null otherwise
     */
    protected MoveTreeNode getChild(String san)
    {
        for (MoveTreeNode n : children)
        {
            String s = n.getMove();
            char c = s.charAt(s.length() - 1);
            if (c == '+' || c == '#')
            {
                s = s.substring(0, s.length() - 1);
            }
            if (s.equals(san))
            {
                return n;
            }
        }
        return null;
    }

    /**
     * @param san A move notated in SAN
     * @return True if one of the node's children is that move, false otherwise
     */
    protected boolean isChild(String san)
    {
        for (MoveTreeNode n : children)
        {
            String s = n.getMove();
            char c = s.charAt(s.length() - 1);
            if (c == '+' || c == '#')
            {
                s = s.substring(0, s.length() - 1);
            }
            if (s.equals(san))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * @return The fen if ROOT, null otherwise
     */
    protected FEN getFen()
    {
        return fen;
    }

    /**
     * @return True if the node is a root node, false otherwise
     */
    public boolean isRoot()
    {
        return root;
    }

    /**
     * @return The root node of this node's tree
     */
    protected MoveTreeNode getRoot()
    {
        return (root ? this : parent.getRoot());
    }

    /**
     * Pairs off equal children of NODE1 and NODE2 into two-element arrays, then adds all unpaired children to arrays respective to their parents.
     * Puts all arrays in a list and returns.
     *
     * @param node1 A MoveTreeNode
     * @param node2 A MoveTreeNode
     * @return An ArrayList of MoveTreeNode arrays. The first n - 2 are two-element arrays of equal pairs. The penultimate is an array of all unpaired
     * children of NODE1 and the last is an array of all unpaired children of NODE2.
     */
    protected static ArrayList<MoveTreeNode[]> pairChildren(MoveTreeNode node1, MoveTreeNode node2)
    {
        ArrayList<MoveTreeNode[]> ret = new ArrayList<>();

        ArrayList<MoveTreeNode> c1 = node1.getChildren();
        ArrayList<MoveTreeNode> c2 = node2.getChildren();

        HashMap<MoveTreeNode, Integer> found1 = new HashMap<>();
        HashMap<MoveTreeNode, Integer> found2 = new HashMap<>();

        HashMap<MoveTreeNode, Integer> exists1 = new HashMap<>();
        HashMap<MoveTreeNode, Integer> exists2 = new HashMap<>();

        for (MoveTreeNode n1 : c1)
        {
            int count = 1;
            for (MoveTreeNode n2 : c2)
            {
                if (n1.equals(n2))
                {
                    if (count > found2.getOrDefault(n2, 0))
                    {
                        ret.add(new MoveTreeNode[] {n1, n2});
                        found1.put(n1, found1.getOrDefault(n1, 0) + 1);
                        found2.put(n2, found2.getOrDefault(n2, 0) + 1);
                        break;
                    }
                    else
                    {
                        count++;
                    }
                }
            }
            if (!found1.containsKey(n1))
                found1.put(n1, 0);
            exists1.put(n1, exists1.getOrDefault(n1, 0) + 1);
        }

        ArrayList<MoveTreeNode> remaining1 = new ArrayList<>();
        ArrayList<MoveTreeNode> remaining2 = new ArrayList<>();

        for (MoveTreeNode n1 : c1)
        {
            for (int i = 0; i < exists1.get(n1) - found1.get(n1); i++)
            {
                remaining1.add(n1);
            }
        }

        for (MoveTreeNode n2 : c2)
        {
            exists2.put(n2, exists2.getOrDefault(n2, 0) + 1);
        }

        for (MoveTreeNode n2 : c2)
        {
            for (int i = 0; i < exists2.get(n2) - (found2.getOrDefault(n2, 0)); i++)
            {
                remaining2.add(n2);
            }
        }

        ret.add(MoveTreeNode.toArray(remaining1));
        ret.add(MoveTreeNode.toArray(remaining2));

        return ret;
    }

    /**
     * @param a An ArrayList of MoveTreeNode instances
     * @return A as an array instead of an ArrayList
     */
    protected static MoveTreeNode[] toArray(ArrayList<MoveTreeNode> a)
    {
        MoveTreeNode[] ret = new MoveTreeNode[a.size()];
        for (int i = 0; i < a.size(); i++)
        {
            ret[i] = a.get(i);
        }
        return ret;
    }

    /**
     * @return True if each node in the tree has at most one child. False otherwise.
     */
    protected boolean isLinearTree()
    {
        if (children.size() == 0)
        {
            return true;
        }
        return children.size() == 1 && getChild(0).isLinearTree();
    }
}
