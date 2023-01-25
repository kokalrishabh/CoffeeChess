import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public final class Icons
{
    private Icons()
    {
        throw new UnsupportedOperationException();
    }

    protected static final int ICON_SIZE = 60;

    private static final ImageIcon d00 = new ImageIcon(new ImageIcon("resources/Images/d00.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon d01 = new ImageIcon(new ImageIcon("resources/Images/d01.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon dbb0 = new ImageIcon(new ImageIcon("resources/Images/dbb0.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon dbb1 = new ImageIcon(new ImageIcon("resources/Images/dbb1.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon dwb0 = new ImageIcon(new ImageIcon("resources/Images/dwb0.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon dwb1 = new ImageIcon(new ImageIcon("resources/Images/dwb1.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon dbk0 = new ImageIcon(new ImageIcon("resources/Images/dbk0.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon dbk1 = new ImageIcon(new ImageIcon("resources/Images/dbk1.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon dwk0 = new ImageIcon(new ImageIcon("resources/Images/dwk0.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon dwk1 = new ImageIcon(new ImageIcon("resources/Images/dwk1.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon dbn0 = new ImageIcon(new ImageIcon("resources/Images/dbn0.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon dbn1 = new ImageIcon(new ImageIcon("resources/Images/dbn1.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon dwn0 = new ImageIcon(new ImageIcon("resources/Images/dwn0.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon dwn1 = new ImageIcon(new ImageIcon("resources/Images/dwn1.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon dbp0 = new ImageIcon(new ImageIcon("resources/Images/dbp0.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon dbp1 = new ImageIcon(new ImageIcon("resources/Images/dbp1.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon dwp0 = new ImageIcon(new ImageIcon("resources/Images/dwp0.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon dwp1 = new ImageIcon(new ImageIcon("resources/Images/dwp1.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon dbq0 = new ImageIcon(new ImageIcon("resources/Images/dbq0.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon dbq1 = new ImageIcon(new ImageIcon("resources/Images/dbq1.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon dwq0 = new ImageIcon(new ImageIcon("resources/Images/dwq0.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon dwq1 = new ImageIcon(new ImageIcon("resources/Images/dwq1.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon dbr0 = new ImageIcon(new ImageIcon("resources/Images/dbr0.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon dbr1 = new ImageIcon(new ImageIcon("resources/Images/dbr1.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon dwr0 = new ImageIcon(new ImageIcon("resources/Images/dwr0.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon dwr1 = new ImageIcon(new ImageIcon("resources/Images/dwr1.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon dbd0 = new ImageIcon(new ImageIcon("resources/Images/dbd0.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon dwd0 = new ImageIcon(new ImageIcon("resources/Images/dwd0.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon l00 = new ImageIcon(new ImageIcon("resources/Images/l00.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon l01 = new ImageIcon(new ImageIcon("resources/Images/l01.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon lbb0 = new ImageIcon(new ImageIcon("resources/Images/lbb0.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon lbb1 = new ImageIcon(new ImageIcon("resources/Images/lbb1.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon lwb0 = new ImageIcon(new ImageIcon("resources/Images/lwb0.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon lwb1 = new ImageIcon(new ImageIcon("resources/Images/lwb1.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon lbk0 = new ImageIcon(new ImageIcon("resources/Images/lbk0.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon lbk1 = new ImageIcon(new ImageIcon("resources/Images/lbk1.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon lwk0 = new ImageIcon(new ImageIcon("resources/Images/lwk0.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon lwk1 = new ImageIcon(new ImageIcon("resources/Images/lwk1.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon lbn0 = new ImageIcon(new ImageIcon("resources/Images/lbn0.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon lbn1 = new ImageIcon(new ImageIcon("resources/Images/lbn1.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon lwn0 = new ImageIcon(new ImageIcon("resources/Images/lwn0.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon lwn1 = new ImageIcon(new ImageIcon("resources/Images/lwn1.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon lbp0 = new ImageIcon(new ImageIcon("resources/Images/lbp0.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon lbp1 = new ImageIcon(new ImageIcon("resources/Images/lbp1.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon lwp0 = new ImageIcon(new ImageIcon("resources/Images/lwp0.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon lwp1 = new ImageIcon(new ImageIcon("resources/Images/lwp1.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon lbq0 = new ImageIcon(new ImageIcon("resources/Images/lbq0.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon lbq1 = new ImageIcon(new ImageIcon("resources/Images/lbq1.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon lwq0 = new ImageIcon(new ImageIcon("resources/Images/lwq0.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon lwq1 = new ImageIcon(new ImageIcon("resources/Images/lwq1.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon lbr0 = new ImageIcon(new ImageIcon("resources/Images/lbr0.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon lbr1 = new ImageIcon(new ImageIcon("resources/Images/lbr1.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon lwr0 = new ImageIcon(new ImageIcon("resources/Images/lwr0.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon lwr1 = new ImageIcon(new ImageIcon("resources/Images/lwr1.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon lbd0 = new ImageIcon(new ImageIcon("resources/Images/lbd0.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon lwd0 = new ImageIcon(new ImageIcon("resources/Images/lwd0.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));

    protected static Map<String, ImageIcon> dir;
    static
    {
        dir = new HashMap<>();
        dir.put("d00", d00);
        dir.put("d01", d01);
        dir.put("dbb0", dbb0);
        dir.put("dbb1", dbb1);
        dir.put("dwb0", dwb0);
        dir.put("dwb1", dwb1);
        dir.put("dbk0", dbk0);
        dir.put("dbk1", dbk1);
        dir.put("dwk0", dwk0);
        dir.put("dwk1", dwk1);
        dir.put("dbn0", dbn0);
        dir.put("dbn1", dbn1);
        dir.put("dwn0", dwn0);
        dir.put("dwn1", dwn1);
        dir.put("dbp0", dbp0);
        dir.put("dbp1", dbp1);
        dir.put("dwp0", dwp0);
        dir.put("dwp1", dwp1);
        dir.put("dbq0", dbq0);
        dir.put("dbq1", dbq1);
        dir.put("dwq0", dwq0);
        dir.put("dwq1", dwq1);
        dir.put("dbr0", dbr0);
        dir.put("dbr1", dbr1);
        dir.put("dwr0", dwr0);
        dir.put("dwr1", dwr1);
        dir.put("dbd0", dbd0);
        dir.put("dwd0", dwd0);
        dir.put("l00", l00);
        dir.put("l01", l01);
        dir.put("lbb0", lbb0);
        dir.put("lbb1", lbb1);
        dir.put("lwb0", lwb0);
        dir.put("lwb1", lwb1);
        dir.put("lbk0", lbk0);
        dir.put("lbk1", lbk1);
        dir.put("lwk0", lwk0);
        dir.put("lwk1", lwk1);
        dir.put("lbn0", lbn0);
        dir.put("lbn1", lbn1);
        dir.put("lwn0", lwn0);
        dir.put("lwn1", lwn1);
        dir.put("lbp0", lbp0);
        dir.put("lbp1", lbp1);
        dir.put("lwp0", lwp0);
        dir.put("lwp1", lwp1);
        dir.put("lbq0", lbq0);
        dir.put("lbq1", lbq1);
        dir.put("lwq0", lwq0);
        dir.put("lwq1", lwq1);
        dir.put("lbr0", lbr0);
        dir.put("lbr1", lbr1);
        dir.put("lwr0", lwr0);
        dir.put("lwr1", lwr1);
        dir.put("lbd0", lbd0);
        dir.put("lwd0", lwd0);
    }

    protected static Map<ImageIcon, String> revDir;
    static
    {
        revDir = new HashMap<>();
        revDir.put(d00, "d00");
        revDir.put(d01, "d01");
        revDir.put(dbb0, "dbb0");
        revDir.put(dbb1, "dbb1");
        revDir.put(dwb0, "dwb0");
        revDir.put(dwb1, "dwb1");
        revDir.put(dbk0, "dbk0");
        revDir.put(dbk1, "dbk1");
        revDir.put(dwk0, "dwk0");
        revDir.put(dwk1, "dwk1");
        revDir.put(dbn0, "dbn0");
        revDir.put(dbn1, "dbn1");
        revDir.put(dwn0, "dwn0");
        revDir.put(dwn1, "dwn1");
        revDir.put(dbp0, "dbp0");
        revDir.put(dbp1, "dbp1");
        revDir.put(dwp0, "dwp0");
        revDir.put(dwp1, "dwp1");
        revDir.put(dbq0, "dbq0");
        revDir.put(dbq1, "dbq1");
        revDir.put(dwq0, "dwq0");
        revDir.put(dwq1, "dwq1");
        revDir.put(dbr0, "dbr0");
        revDir.put(dbr1, "dbr1");
        revDir.put(dwr0, "dwr0");
        revDir.put(dwr1, "dwr1");
        revDir.put(dbd0, "dbd0");
        revDir.put(dwd0, "dwd0");
        revDir.put(l00, "l00");
        revDir.put(l01, "l01");
        revDir.put(lbb0, "lbb0");
        revDir.put(lbb1, "lbb1");
        revDir.put(lwb0, "lwb0");
        revDir.put(lwb1, "lwb1");
        revDir.put(lbk0, "lbk0");
        revDir.put(lbk1, "lbk1");
        revDir.put(lwk0, "lwk0");
        revDir.put(lwk1, "lwk1");
        revDir.put(lbn0, "lbn0");
        revDir.put(lbn1, "lbn1");
        revDir.put(lwn0, "lwn0");
        revDir.put(lwn1, "lwn1");
        revDir.put(lbp0, "lbp0");
        revDir.put(lbp1, "lbp1");
        revDir.put(lwp0, "lwp0");
        revDir.put(lwp1, "lwp1");
        revDir.put(lbq0, "lbq0");
        revDir.put(lbq1, "lbq1");
        revDir.put(lwq0, "lwq0");
        revDir.put(lwq1, "lwq1");
        revDir.put(lbr0, "lbr0");
        revDir.put(lbr1, "lbr1");
        revDir.put(lwr0, "lwr0");
        revDir.put(lwr1, "lwr1");
        revDir.put(lbd0, "lbd0");
        revDir.put(lwd0, "lwd0");
    }

    protected static ImageIcon reroll = new ImageIcon(new ImageIcon("Images/reroll.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
    protected static ImageIcon flip = new ImageIcon(new ImageIcon("Images/flip.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));


}
