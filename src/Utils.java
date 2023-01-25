// import java.lang.reflect.Array;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public final class Utils
{
    private Utils()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Adds t to a if t is not null
     *
     * @param a A list of instances of T
     * @param t An instance of T
     */
    public static <T> void addIfNotNull(ArrayList<T> a, T t)
    {
        if (t != null)
        {
            a.add(t);
        }
    }

    /**
     * @param s A string
     * @return True if S is a whole number. False otherwise.
     */
    public static boolean isDecimal(String s)
    {
        for (char c : s.toCharArray())
        {
            if (c < 48 || c > 57)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * @param i A cardinal integer
     * @return The ordinal number corresponding to I
     */
    public static String toOrdinal(int i)
    {
        return i +

        switch (i % 10)
        {
            case 1 -> "st";
            case 2 -> "nd";
            case 3 -> "rd";
            default -> "th";
        };
    }

    /**
     * @param str The string to be transformed
     * @return STR, but with the first letter of every word capitalized and all others lower-case
     */
    public static String toTitleCase(String str)
    {
        StringBuilder sb = new StringBuilder();
        String[] arr = str.split(" ");

        for (String s : arr)
        {
            sb.append(s.substring(0, 1).toUpperCase()).append(s.substring(1).toLowerCase());
            sb.append(' ');
        }

        return sb.toString();
    }

    /**
     * @param s Either Side.BLACK or Side.WHITE
     * @return The other color
     */
    public static Side flip(Side s)
    {
        if (s == Side.BLACK)
        {
            return Side.WHITE;
        }
        else
        {
            return Side.BLACK;
        }
    }

    /**
     * Pauses for aesthetic purposes
     *
     * @param ms Number of milliseconds for which to pause
     */
    public static void sleepy(int ms)
    {
        try
        {
            TimeUnit.MILLISECONDS.sleep(ms);
        }
        catch (Exception e)
        {
            System.out.println("Sleepy time error");
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
}