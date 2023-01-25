import java.io.File;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class OpeningPractice
{
    private static final Set<String> PLAY_AS_WHITE_CHOICES = Set.of("white", "w");
    private static final Set<String> PLAY_AS_BLACK_CHOICES = Set.of("black", "b");

    // TODO: Add a static data structure that allows me to not have to pick my color

    public static void main(String[] args)
    {
        practice();
    }

    /**
     * Practices openings using the console. Should handle all exceptions gracefully.
     */
    public static void practice()
    {
        Scanner sc = new Scanner(System.in);
        File pgn;
        MoveTreeNode curr;
        Random r = new Random();
        long seed = r.nextLong();
        r.setSeed(seed);
        Board b;
        boolean playersTurn;

        final String FOLDER_NAME = "Test PGNs";

        // Gets the file and converts it to a move tree
        while (true)
        {
            System.out.print("Let's get started!\n\nThese are the files you have:\n\n");
            File dir = new File(FOLDER_NAME);
            String[] files = dir.list();

            for(int i = 0; i < files.length; i++)
            {
                System.out.printf("%d) %s\n", i, files[i]);
            }

            System.out.print("\n\n\nEnter the number of the file you wish to learn from: ");

            String filename = sc.nextLine();
            System.out.println();

            if (filename.equals("qqq"))
            {
                quitSequence();
            }

            if (!Utils.isDecimal(filename) || Integer.parseInt(filename) >= files.length)
            {
                System.out.println("Input must be a whole number from the list. Please try again, or type \"qqq\" to quit.\n");
                continue;
            }

            int filenum = Integer.parseInt(filename);

            try
            {
                b = new Board();
                pgn = new File(FOLDER_NAME + "/" + files[filenum]);
                if (!pgn.exists())
                {
                    System.out.println("That file doesn't exist. Please try again, or type \"qqq\" to quit.\n");
                    continue;
                }
                curr = PGNManager.convertPGNToTree(pgn, b);
                try
                {
                    b.startFromFen(curr.getFen());
                }
                catch (Exception e)
                {
                    System.out.println("The FEN held in the root of the tree caused an exception. Please try again, or type \"qqq\" to quit.\n");
                    continue;
                }
                break;
            }
            catch (Exception e)
            {
                System.out.println("There was an error with message: " + e.getMessage());
                System.out.println("Please try again, or type \"qqq\" to quit.\n");
                continue;
            }
        }

        String playAs;

        // Player chooses their side
        while (true)
        {
            System.out.print("Play as black or white? ");
            playAs = sc.nextLine().toLowerCase();

            if (playAs.equals("qqq"))
            {
                quitSequence();
                continue;
            }
            if (playAs.equals("rrr"))
            {
                if (restartSequence())
                {
                    continue;
                }
                else
                {
                    return;
                }
            }

            if (PLAY_AS_WHITE_CHOICES.contains(playAs) || PLAY_AS_BLACK_CHOICES.contains(playAs))
            {
                break;
            }
            System.out.println("\nThat's not an option. Please try again, type \"qqq\" to quit, or \"rrr\" to start over.");
        }

        Side side;
        String playerNumSuffix, compNumSuffix;

        if (PLAY_AS_WHITE_CHOICES.contains(playAs))
        {
            side = Side.WHITE;
            playerNumSuffix = ". ";
            compNumSuffix = "... ";
            playersTurn = true;
        }
        else
        {
            side = Side.BLACK;
            playerNumSuffix = "... ";
            compNumSuffix = ". ";
            playersTurn = false;
        }
        
        StringBuilder sb = new StringBuilder();
        int wrongCount = 0;
        int moveCount = 1;

        // Player plays
        while (true)
        {
            // If CURR has no more children, the player wins
            int choices = curr.getChildren().size();
            if (choices == 0)
            {
                playerWins(sb.toString());
            }

            // If it's the player's turn
            if (playersTurn)
            {
                // If turn 1, print the starting position
                if (curr.isRoot())
                {
                    System.out.println();
                    System.out.println(b.seeBoardAs(side));
                    System.out.println();
                }

                // Prompt for move
                System.out.print(b.getFen().getFullmoves() + playerNumSuffix);
                String move = sc.nextLine();

                // Check quit, restart, or give up
                if (move.toLowerCase().equals("qqq"))
                {
                    quitSequence();
                    continue;
                }
                if (move.toLowerCase().equals("rrr"))
                {
                    if (restartSequence())
                    {
                        continue;
                    }
                    else
                    {
                        return;
                    }
                }
                if (move.toLowerCase().equals("idk"))
                {
                    wrongCount = 2;
                }

                // If they played correctly, play the move and continue to end of loop
                if (curr.isChild(move))
                {
                    curr = curr.getChild(move);
                    try
                    {
                        b.move(new Move(move, b));
                        if (side == Side.WHITE)
                        {
                            sb.append(moveCount).append(". ");
                        }
                        sb.append(move).append(' ');
                    }
                    catch (Exception e)
                    {
                        System.out.printf("The selected move (%s) caused an exception. Please check the PGN. For reference, the seed was %d and the line went %s\n", move, seed, sb.toString());
                        System.out.println("Error message: " + e.getMessage());
                        System.out.println("Please try again, type \"qqq\" to quit, or \"rrr\" to start over.");
                        continue;
                    }

                    System.out.println();
                    System.out.println(b.seeBoardAs(side));
                    System.out.println();
                    try
                    {
                        TimeUnit.SECONDS.sleep(1);
                    }
                    catch (Exception e)
                    {
                        System.out.println("Sleepy time error");
                        System.out.println(e.getMessage());
                        System.exit(1);
                    }
                }
                // If they didn't play correctly
                else
                {
                    // If they have played incorrectly three times, or gave up, tell them the correct moves and run the exit sequence.
                    if (wrongCount == 2)
                    {
                        System.out.println("Uh-oh! The correct moves were:"); // TODO: Change this to continue if there is one move
                        for (MoveTreeNode n : curr.getChildren())
                        {
                            System.out.println(n.getMove());
                        }
                        System.out.println();
                        System.out.println("That's okay! Practice and come back. I'll always be here.");
                        exitSequence();
                    }
                    // If neither of the above cases is true, increment the WRONGCOUNT, and have them try again.
                    else
                    {
                        System.out.println("That is not a correct move. Please try again, type \"qqq\" to quit, or \"rrr\" to start over.\n\n");
                        wrongCount++;
                        continue;
                    }
                }
                if (side == Side.BLACK)
                {
                    moveCount++;
                }
            }
            else
            {
                // Computer's turn to play
                int kidIndex = r.nextInt(choices);
                curr = curr.getChild(kidIndex);
                try
                {
                    b.move(new Move(curr.getMove(), b));
                    if (side == Side.BLACK)
                    {
                        sb.append(moveCount).append(". ");
                    }
                    sb.append(curr.getMove()).append(' ');
                }
                catch (Exception e)
                {
                    System.out.printf("The selected move (%s) caused an exception. Please check the PGN. For reference, the seed was %d and the line went %s\n", curr.getMove(), seed, sb.toString());
                    System.out.println("Error message: " + e.getMessage());
                    System.exit(1);
                }
                
                System.out.println(b.getFen().getFullmoves() + compNumSuffix + curr.getMove());
                System.out.println();
                System.out.println(b.seeBoardAs(side));
                System.out.println();


                if (side == Side.WHITE)
                {
                    moveCount++;
                }
            }
            
            playersTurn = !playersTurn;
            wrongCount = 0;
        }
    }

    /**
     * Asks for assurance to quit. If yes, quits. Else return to previous state.
     */
    private static void quitSequence()
    {
        Scanner sc = new Scanner(System.in);

        System.out.print("\nAre you sure you want to quit? (Y/N) ");
        String ans = sc.nextLine().toLowerCase();

        if (ans.equals("y") || ans.equals("yes"))
        {
            System.out.println("\n\nBye-bye!");
            System.exit(0);
        }
        else
        {
            System.out.println("\nI'm going to take that as a no. Type \"qqq\" if you want to quit.\n");
        }
    }

    /**
     * Asks for assurance to restart. If yes, restart and return false, so the caller knows to quit afterwards. Otherwise,
     * return to previous state and return true, so the caller knows to continue.
     *
     * @return True if the code should continue running after method call, false otherwise
     */
    private static boolean restartSequence()
    {
        Scanner sc = new Scanner(System.in);

        System.out.print("\nAre you sure you want to quit? (Y/N) ");
        String ans = sc.nextLine().toLowerCase();

        if (ans.equals("y") || ans.equals("yes"))
        {
            System.out.println("\n\nBye-bye!");
            practice();
            return false;
        }
        else
        {
            System.out.println("\nI'm going to take that as a no. Type \"rrr\" if you want to restart.\n");
            return true;
        }
    }

    /**
     * Congratulate player on winning, then run exit sequence.
     */
    private static void playerWins(String line)
    {
        System.out.println("\n\nYou did it! You played the correct line!");
        System.out.println("Just so you remember, the line went: " + line);
        System.out.println();
        System.out.println("Great job! Thanks for playing!");

        exitSequence();
    }

    /**
     * Asks the player if they'd like to play again. If yes, run practice() again, then quit after. Else quit now.
     */
    private static void exitSequence()
    {
        System.out.print("\n\n\nWould you like to play again? (y/n) ");
        String playAgain = new Scanner(System.in).nextLine().toLowerCase();

        if (playAgain.equals("y") || playAgain.equals("yes"))
        {
            System.out.println("\nAlright! Starting over...\n\n\n");
            practice();
        }
        else
        {
            System.out.println("\nOk! Bye-bye!");
        }

        System.exit(0);
    }
}


