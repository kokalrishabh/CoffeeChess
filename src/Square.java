import java.util.ArrayList;

public class Square
{
    private static final String FILE_LETTERS = "abcdefghABCDEFGH"; // The letters, capital and lower-case, of the files

    private Man man; // The man that currently stands on the square
    private final String san; // The SAN notation of the square
    private final Board board; // The Board to which the square belongs
    private Rank rank; // The Rank to which the square belongs
    private int rankInt; // The number of the Rank to which the square belongs
    private File file; // The File to which the square belongs
    private int fileInt; // The zero-indexed number (A -> 0, B -> 1, etc.) of the File to which the square belongs
    private Diagonal diagonal1, diagonal2; // The Diagonals to which the square belongs
    private int d1Int, d2Int; // The numbers (as counted from left to right) of the Diagonals to which the square belongs

    /**
     * Throws an InvalidSquareException if SAN is not a valid square notation.
     * Otherwise, sets MAN to null and SAN and BOARD as provided by the parameters.
     *
     * @param san The SAN notation of the square
     * @param b The Board to which the square belongs
     */
    public Square(String san, Board b) throws InvalidSquareException
    {
        if (isValidSquare(san))
        {
            this.san = san.toLowerCase();
            man = null;
            board = b;
        }
        else
        {
            throw new InvalidSquareException("Square is invalid");
        }
    }

    /**
     * Initializes all straights and numbers.
     * Not part of constructor because the Ranks, Files, and Diagonals don't exist at the time of construction.
     */
    public void setStraights() throws InvalidSquareException
    {
        rankInt = getRankFromName(san);
        rank = switch (rankInt)
                {
                    case 7 -> board.first;
                    case 6 -> board.second;
                    case 5 -> board.third;
                    case 4 -> board.fourth;
                    case 3 -> board.fifth;
                    case 2 -> board.sixth;
                    case 1 -> board.seventh;
                    case 0 -> board.eighth;
                    default -> null;
                };
        fileInt = getFileNumberFromName(san);
        file = switch (fileInt)
                {
                    case 0 -> board.aFile;
                    case 1 -> board.bFile;
                    case 2 -> board.cFile;
                    case 3 -> board.dFile;
                    case 4 -> board.eFile;
                    case 5 -> board.fFile;
                    case 6 -> board.gFile;
                    case 7 -> board.hFile;
                    default -> null;
                };
        int[] diagonalPairNums = getDiagonalPairFromName(san);
        d1Int = diagonalPairNums[0];
        d2Int = diagonalPairNums[1];
        diagonal1 = board.getDiagonalByNumber(1, d1Int);
        diagonal2 = board.getDiagonalByNumber(2, d2Int);
    }

    /**
     * @param san The name of the square to be checked for validity
     * @return True if SAN is a valid square notation. False otherwise.
     */
    protected static boolean isValidSquare(String san)
    {
        if (san.length() != 2)
        {
            return false;
        }

        char[] arr = san.toCharArray();

        if (FILE_LETTERS.indexOf(arr[0]) == -1)
        {
            return false;
        }
        if (FEN.VALID_FEN_CHARS.substring(12).indexOf(arr[1]) == -1)
        {
            return false;
        }

        return true;
    }

    /**
     * Overrides the toString() method of all objects for printing purposes
     *
     * @return The SAN as the String value of the square
     */
    @Override
    public String toString() {
        return san;
    }

    /**
     * @param san The name of the square from which to get the rank number
     * @return The number of the Rank to which the square belongs
     */
    protected static int getRankFromName(String san) throws InvalidSquareException
    {
        if (!isValidSquare(san))
        {
            throw new InvalidSquareException("Invalid square");
        }
        return 8 - (san.charAt(1) - '0');
    }

    /**
     * @param san The name of the square from which to get the file number
     * @return The number (A -> 1, B -> 2, etc.) of the File to which the square belongs
     */
    protected static int getFileNumberFromName(String san) throws InvalidSquareException
    {
        if (!isValidSquare(san))
        {
            throw new InvalidSquareException("Invalid square");
        }
        return san.charAt(0) - 'a';
    }

    /**
     * @param san The name of the square from which to get the Diagonal numbers
     * @return {The number of the top-left-to-bottom-right Diagonal, the number of the bottom-left-to-top-right Diagonal}
     */
    protected static int[] getDiagonalPairFromName(String san) throws InvalidSquareException
    {
        if (!isValidSquare(san))
        {
            throw new InvalidSquareException("Invalid Square");
        }
        int rInt = 8 - getRankFromName(san);
        int fInt = getFileNumberFromName(san) + 1;

        return new int[] {fInt + rInt - 1, fInt - rInt + 8};
    }

    /**
     * Places the piece, then sets all related attributes of the piece.
     *
     * @param m The piece to be placed on the square
     */
    protected void setPiece(Man m)
    {
        man = m;
        if (m != null)
        {
            m.setSquare(this);
            m.setRank(getRank());
            m.setFile(getFile());
            m.setDiagonal1(getDiagonal1());
            m.setDiagonal2(getDiagonal2());
        }
    }

    /**
     * @return The piece currently standing on the square
     */
    public Man getPiece()
    {
        return man;
    }

    /**
     * @return The Rank to which the square belongs
     */
    public Rank getRank()
    {
        return rank;
    }

    /**
     * @return The File to which the square belongs
     */
    public File getFile()
    {
        return file;
    }

    /**
     * @return The top-left-to-bottom-right Diagonal to which the square belongs
     */
    public Diagonal getDiagonal1()
    {
        return diagonal1;
    }

    /**
     * @return The bottom-left-to-top-right Diagonal to which the square belongs
     */
    public Diagonal getDiagonal2()
    {
        return diagonal2;
    }

    /**
     * @return The square one square up, if a1 is the bottom-left square
     */
    protected Square getNorthSquare()
    {
        if (rankInt == 0)
        {
            return null;
        }
        return board.getBoard()[rankInt - 1][fileInt];
    }

    /**
     * @return The square one square right, if a1 is the bottom-left square
     */
    protected Square getEastSquare()
    {
        if (fileInt == 7)
        {
            return null;
        }
        return board.getBoard()[rankInt][fileInt + 1];
    }

    /**
     * @return The square one square down, if a1 is the bottom-left square
     */
    protected Square getSouthSquare()
    {
        if (rankInt == 7)
        {
            return null;
        }
        return board.getBoard()[rankInt + 1][fileInt];
    }

    /**
     * @return The square one square left, if a1 is the bottom-left square
     */
    protected Square getWestSquare()
    {
        if (fileInt == 0)
        {
            return null;
        }
        return board.getBoard()[rankInt][fileInt - 1];
    }

    /**
     * @return The square one square up and to the right, if a1 is the bottom-left square
     */
    protected Square getNESquare()
    {
        if (rankInt == 0 || fileInt == 7)
        {
            return null;
        }
        return board.getBoard()[rankInt - 1][fileInt + 1];
    }

    /**
     * @return The square one square down and to the right, if a1 is the bottom-left square
     */
    protected Square getSESquare()
    {
        if (rankInt == 7 || fileInt == 7)
        {
            return null;
        }
        return board.getBoard()[rankInt + 1][fileInt + 1];
    }

    /**
     * @return The square one square down and to the left, if a1 is the bottom-left square
     */
    protected Square getSWSquare()
    {
        if (rankInt == 7 || fileInt == 0)
        {
            return null;
        }
        return board.getBoard()[rankInt + 1][fileInt - 1];
    }

    /**
     * @return The square one square up and to the left, if a1 is the bottom-left square
     */
    protected Square getNWSquare()
    {
        if (rankInt == 0 || fileInt == 0)
        {
            return null;
        }
        return board.getBoard()[rankInt - 1][fileInt - 1];
    }

    /**
     * @return The list of every square touching the square
     */
    protected ArrayList<Square> getAllAdjacentSquares()
    {
        ArrayList<Square> ret = new ArrayList<>();

        Utils.addIfNotNull(ret, getNorthSquare());
        Utils.addIfNotNull(ret, getSouthSquare());
        Utils.addIfNotNull(ret, getEastSquare());
        Utils.addIfNotNull(ret, getWestSquare());
        Utils.addIfNotNull(ret, getNESquare());
        Utils.addIfNotNull(ret, getSESquare());
        Utils.addIfNotNull(ret, getNWSquare());
        Utils.addIfNotNull(ret, getSWSquare());

        return ret;
    }

    /**
     * @return The letter of the File to which the square belongs
     */
    protected char getFileLetter()
    {
        return (char) ('a' + fileInt);
    }

    /**
     * @return The number of the Rank to which the square belongs
     */
    protected int getRankInt()
    {
        return 8 - rankInt;
    }

    /**
     * @return The Board to which the square belongs
     */
    protected Board getBoard()
    {
        return board;
    }

    /**
     * @return The number of the file of the square on the board (A -> 0, B -> 1, etc.)
     */
    public int getFileInt()
    {
        return fileInt;
    }
}
