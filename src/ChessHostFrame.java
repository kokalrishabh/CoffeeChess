import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ChessHostFrame extends JFrame
{
    VisualBoard board;
    JFrame optionFrame;

    /**
     * Hosts VB
     *
     * @param vb The VisualBoard to be hosted
     * @param name The title of the frame
     * @param of The JFrame with the options from which this host frame was created
     */
    public ChessHostFrame(VisualBoard vb, String name, JFrame of)
    {
        super();

        board = vb;

        board.setHost(this);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setTitle(name);
        optionFrame = of;
        add(board);
        pack();
        setVisible(true);

        if (board instanceof PracticeBoard && board.getPlaySide() == Side.BLACK)
        {
            ((PracticeBoard) board).playFirstMove();
        }

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e)
            {
                if (board.quitSequence())
                {
                    dispose();
                }
            }
        });
    }

    /**
     * Sets the initial option frame to be visible, then disposes itself
     */
    public void replaySequence()
    {
        optionFrame.setVisible(true);
        dispose();
    }
}
