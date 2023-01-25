import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class VisualOpeningPractice
{
    public static void main(String[] args)
    {
        practice();
    }

    /**
     * The GUI for VisualPractice
     */
    public static void practice()
    {
        int[] moveTime = {200};
        int[] tpm = {3};
        int[] numReveals = {3};

        JFrame optionFrame = new JFrame();
        optionFrame.setLayout(new BoxLayout(optionFrame.getContentPane(), BoxLayout.Y_AXIS));
        optionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        optionFrame.setTitle("Choose your PGN and color");

        JPanel filePanel = new JPanel();
        JPanel colorPanel = new JPanel();
        JPanel goPanel = new JPanel();
        JPanel infoMorePanel = new JPanel();

        JButton choose = new JButton("Select File");

        filePanel.add(choose);

        JLabel fileLabel = new JLabel("No file selected");
        filePanel.add(fileLabel);

        JRadioButton w = new JRadioButton("White");
        JRadioButton b = new JRadioButton("Black");

        ButtonGroup group = new ButtonGroup();
        group.add(w);
        group.add(b);

        colorPanel.add(w);
        colorPanel.add(b);

        JButton go = new JButton("Go");
        goPanel.add(go);

        JButton info = new JButton("Info");
        JButton moreButton = new JButton("More");
        infoMorePanel.add(info);
        infoMorePanel.add(moreButton);

        optionFrame.add(filePanel);
        optionFrame.add(colorPanel);
        optionFrame.add(infoMorePanel);
        optionFrame.add(goPanel);

        final File[] pgn = {null};

        info.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String infoMessage = "Hi! Welcome to Rishabh Kokal's Opening Trainer!\n\n" +
                        "" +
                        "This program tests you on your opening knowledge. Simply pick a PGN file to practice from and the color you want to play as\n" +
                        "and the computer will test you on random lines. By default, you get 3 guesses per move and 3 chances to get told the answer.\n" +
                        "If you don't know the next move, you can click the \"I don't know\" button in the toolbar to be told the move. If you want to\n" +
                        "practice a different line than the one the computer provides, you can click the \"Reroll\" button and the computer will rechoose\n" +
                        "a line.\n\n" +
                        "" +
                        "You can also change some of the settings. By clicking \"More\", you can change how quickly the computer plays (in milliseconds),\n" +
                        "how many guesses per move you get, and how many times you can get an answer reveal. To get an infinite number of guesses per move or\n" +
                        "answer reveals, set the corresponding value to -1.\n\n" +
                        "" +
                        "Best of luck and happy practicing!";
                JOptionPane.showMessageDialog(null, infoMessage);
            }
        });

        choose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame();
                JFileChooser fc = new JFileChooser(".");
                fc.setFileFilter(new FileNameExtensionFilter("PGNs", "pgn"));
                frame.add(fc);
                int r = fc.showOpenDialog(null);
                if (r == JFileChooser.APPROVE_OPTION)
                {
                    pgn[0] = fc.getSelectedFile();
                    fileLabel.setText(pgn[0].getName());
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            optionFrame.setSize(new Dimension(Math.max(choose.getWidth() + fileLabel.getWidth() + 50, 250), optionFrame.getHeight()));
                        }
                    });
                }
                else
                {
                    pgn[0] = null;
                    fileLabel.setText("no file selected");
                }
            }
        });

        final Side[] s = new Side[1];

        moreButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JPanel morePanel = new JPanel();

                morePanel.setLayout(new GridLayout(3, 2));

                SpinnerNumberModel timeModel = new SpinnerNumberModel(moveTime[0], 1, 9999, 1);
                SpinnerNumberModel tpmModel = new SpinnerNumberModel(tpm[0], -1, 99, 1);
                SpinnerNumberModel revealsModel = new SpinnerNumberModel(numReveals[0], -1, 99, 1);

                JLabel moveTimeLabel = new JLabel("Computer move time (ms)");
                morePanel.add(moveTimeLabel);
                JSpinner moveTimeSpinner = new JSpinner(timeModel);
                morePanel.add(moveTimeSpinner);

                JLabel tpmLabel = new JLabel("Tries per move");
                morePanel.add(tpmLabel);
                JSpinner tpmSpinner = new JSpinner(tpmModel);
                morePanel.add(tpmSpinner);

                JLabel numRevealsLabel = new JLabel("Number of reveals");
                morePanel.add(numRevealsLabel);
                JSpinner numRevealsSpinner = new JSpinner(revealsModel);
                morePanel.add(numRevealsSpinner);

                int moreQ = JOptionPane.showOptionDialog(null, morePanel, "More Options", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (moreQ == 0)
                {
                    Object moveTimeObj = moveTimeSpinner.getValue();
                    Object tpmObj = tpmSpinner.getValue();
                    Object numRevealsObj = numRevealsSpinner.getValue();

                    if (moveTimeObj instanceof Integer && tpmObj instanceof Integer && numRevealsObj instanceof Integer)
                    {
                        moveTime[0] = (Integer) moveTimeObj;
                        tpm[0] = (Integer) tpmObj;
                        numReveals[0] = (Integer) numRevealsObj;
                    }
                }
            }
        });

        go.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!w.isSelected() && !b.isSelected() || pgn[0] == null)
                {
                    JOptionPane.showMessageDialog(null, "Did you forget to select something?", "Review your choices", JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    if (w.isSelected())
                    {
                        s[0] = Side.WHITE;
                    }
                    else
                    {
                        s[0] = Side.BLACK;
                    }
                    PracticeBoard pb = null;
                    boolean goFlag = true;
                    MoveTreeNode curr = null;
                    try
                    {
                        pb = new PracticeBoard(s[0], null);
                    }
                    catch (Exception ex)
                    {
                        JOptionPane.showMessageDialog(null, "Something went wrong with board creation.", "Internal Error: Board Creation", JOptionPane.ERROR_MESSAGE);
                        goFlag = false;
                    }
                    try
                    {
                        assert pb != null;
                        curr = PGNManager.convertPGNToTree(pgn[0], pb.getBoard());
                        pb.setRoot(curr);
                    }
                    catch (Exception ex)
                    {
                        JOptionPane.showMessageDialog(null, "The file you selected is somehow invalid.", "Error: Faulty PGN File", JOptionPane.ERROR_MESSAGE);
                        goFlag = false;
                    }
                    if (goFlag)
                    {
                        optionFrame.setVisible(false);
                        pb.setMoveTime(moveTime[0]);
                        pb.setTPM(tpm[0]);
                        pb.setNumReveals(numReveals[0]);
                        process(pb, pgn[0].getName(), optionFrame);
                    }
                }
            }
        });
        optionFrame.pack();
        optionFrame.setSize(new Dimension(250, optionFrame.getHeight()));
        optionFrame.setVisible(true);

        optionFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    /**
     * Processes the PracticeBoard, name, and JFrame from PRACTICE, creating a new ChessHostFrame
     * @param pb The PracticeBoard
     * @param name The name of the PGN file being practiced
     * @param optionFrame The JFrame from which the options were selected
     */
    public static void process(PracticeBoard pb, String name, JFrame optionFrame)
    {
        new ChessHostFrame(pb, name, optionFrame);
    }
}
