import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class PGNMerger extends JFrame
{
    /**
     * GUI that merges PGN files
     */
    public PGNMerger()
    {
        super();

        setTitle("PGN Merger");

        JPanel pane = new JPanel();
        pane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel buttons = new JPanel();

        JButton choose = new JButton("Select File");
        JButton merge = new JButton("Merge");

        buttons.add(choose);
        buttons.add(merge);

        JPanel label = new JPanel();
        JLabel filesLabel = new JLabel("no files selected", SwingConstants.CENTER);
        label.add(filesLabel, SwingConstants.CENTER);

        pane.add(buttons);
        pane.add(label);

        JTextArea text = new JTextArea(30, 70);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK), BorderFactory.createEmptyBorder(5,5,5,5)));
        text.setEditable(false);

        JScrollPane sp = new JScrollPane(text);
        sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        pane.add(sp);

        add(pane);

        pack();
        setVisible(true);

        final File[][] files = {null};
        final int[] numFiles = {0};

        choose.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JFrame frame = new JFrame();
                JFileChooser fc = new JFileChooser(".");
                fc.setMultiSelectionEnabled(true);
                fc.setFileFilter(new FileNameExtensionFilter("PGNs", "pgn"));
                frame.add(fc);
                int r = fc.showOpenDialog(null);

                StringBuilder sb = new StringBuilder();
                if (r == JFileChooser.APPROVE_OPTION)
                {
                    files[0] = fc.getSelectedFiles();

                    sb.setLength(0);
                    sb.append("Files selected: <br />");
                    for (File file : files[0])
                    {
                        sb.append(file.getName());
                        sb.append("<br />");
                    }

                    text.setRows(text.getRows() + (numFiles[0] - files[0].length));
                    numFiles[0] = files[0].length;
                }
                else
                {
                    files[0] = null;
                    sb.setLength(0);
                    sb.append("no files selected");
                }
                filesLabel.setText("<html>" + sb.toString() + "</html>");
            }
        });

        merge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (files[0] == null)
                {
                    JOptionPane.showMessageDialog(null, "Make sure you pick at least one file!", "No files selected", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try
                {
                    Board b = new Board();
                    ArrayList<MoveTreeNode> roots = new ArrayList<>();
                    for (File file : files[0])
                    {
                        roots.add(PGNManager.convertPGNToTree(file, b));
                    }
                    MoveTreeNode trueRoot = PGNManager.mergeTrees(roots);

                    StringBuilder sb = new StringBuilder();
                    sb.append("Files merged:\n");
                    for (File file : files[0])
                    {
                        sb.append(file.getName());
                        sb.append('\n');
                    }
                    sb.append("\n\n");
                    sb.append(PGNManager.convertTreeToPGN(trueRoot));
                    text.setText(sb.toString());
                }
                catch (Exception ex)
                {
                    System.out.println("Something went wrong during board creation");
                    ex.printStackTrace();
                }
            }
        });
    }
}
