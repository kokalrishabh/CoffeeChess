import javax.swing.*;
import java.awt.*;
import java.io.File;

public class GUITest
{
    public static void main(String[] args) throws TooManyKingsException, InvalidMoveException, InvalidSquareException, InvalidPieceException, InvalidFENException
    {
        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout());
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        JSpinner s1 = new JSpinner();
        panel1.add(s1);
        JSpinner s2 = new JSpinner();
        panel2.add(s2);
        JSpinner s3 = new JSpinner();
        panel3.add(s3);
        frame.add(new JLabel("a"));
        frame.add(panel1);
        frame.add(new JLabel("b"));
        frame.add(panel2);
        frame.add(new JLabel("c"));
        frame.add(panel3);
        frame.add(new JLabel("d"));
        // frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void sampleScroll()
    {
        // Create and set up the window.
        final JFrame frame = new JFrame("Scroll Pane Example");

        // Display the window.
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // set flow layout for the frame
        frame.getContentPane().setLayout(new FlowLayout());

        JTextArea textArea = new JTextArea(20, 20);
        JScrollPane scrollableTextArea = new JScrollPane(textArea);

        scrollableTextArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollableTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        frame.add(scrollableTextArea);

        frame.setVisible(true);
    }

    public static void process(File pgn, Side s)
    {
        JOptionPane.showMessageDialog(null, String.format("Playing through %s as %s!", pgn.getPath(), s.toString()), "It works!", JOptionPane.PLAIN_MESSAGE, null);
    }

    public static void sampleToolbar()
    {
        JFrame frame = new JFrame("JToolBar Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JToolBar toolbar = new JToolBar();
        toolbar.setRollover(true);


        JButton button = new JButton("button");
        toolbar.add(button);
        toolbar.addSeparator();

        toolbar.add(new JButton("button 2"));

        toolbar.add(new JComboBox(new String[]{"A","B","C"}));

        Container contentPane = frame.getContentPane();
        contentPane.add(toolbar, BorderLayout.NORTH);
        JTextArea textArea = new JTextArea();
        JScrollPane pane = new JScrollPane(textArea);
        contentPane.add(pane, BorderLayout.CENTER);
        frame.setSize(350, 150);
        frame.setVisible(true);
    }

    public static void toolTest()
    {
        JToolBar tools = new JToolBar();

        JButton prev = new JButton("<");
        JButton next = new JButton(">");
        JButton reroll = new JButton(Icons.reroll);

        tools.add(prev);
        tools.add(next);
        tools.addSeparator();
        tools.add(reroll);
        tools.addSeparator();

        JFrame f  = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(tools);
        f.pack();
        f.setVisible(true);
    }
}
