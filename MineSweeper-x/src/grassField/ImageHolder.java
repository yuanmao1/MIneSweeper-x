package grassField;

import javax.swing.ImageIcon;

public class ImageHolder {
    public static final ImageIcon blank = new ImageIcon("resources/images/num0.png");
    public static final ImageIcon frontMan = new ImageIcon("resources/images/frontMan.png");
    public static final ImageIcon backMan = new ImageIcon("resources/images/backMan.png");
    public static final ImageIcon leftMan = new ImageIcon("resources/images/leftMan.png");
    public static final ImageIcon rightMan = new ImageIcon("resources/images/rightMan.png");
    public static final ImageIcon cover = new ImageIcon("resources/images/cover.png");

    public static final ImageIcon[] numList = {
            new ImageIcon("resources/images/d0.png"),
            new ImageIcon("resources/images/d1.png"),
            new ImageIcon("resources/images/d2.png"),
            new ImageIcon("resources/images/d3.png"),
            new ImageIcon("resources/images/d4.png"),
            new ImageIcon("resources/images/d5.png"),
            new ImageIcon("resources/images/d6.png"),
            new ImageIcon("resources/images/d7.png"),
            new ImageIcon("resources/images/d8.png"),
            new ImageIcon("resources/images/d9.png")
    };
    public static final ImageIcon[] mineLevelList = {
            null,
            new ImageIcon("resources/images/mineLv1.png"),
            new ImageIcon("resources/images/mineLv2.png"),
            new ImageIcon("resources/images/mineLv3.png"),
            new ImageIcon("resources/images/mineLv4.png"),
            new ImageIcon("resources/images/mineLv5.png"),
            new ImageIcon("resources/images/mineLv6.png"),
            new ImageIcon("resources/images/mineLv7.png")
    };
    public static final ImageIcon playerLevel = new ImageIcon("resources/images/playerLevel.png");
    public static final ImageIcon nextLevelAfter = new ImageIcon("resources/images/nextLevelAfter.png");
    public static final ImageIcon playerHP = new ImageIcon("resources/images/playerHP.png");
}
