package start;

import java.awt.*;

public class Const {
    //为常量单独创建一个类以便于修改参数
    public static final int FRAME_W = 400;       //width of window
    public static final int FRAME_H = 400;       //height of window
    public static final int SCREEN_W = Toolkit.getDefaultToolkit().getScreenSize().width;    //width of screen
    public static final int SCREEN_H = Toolkit.getDefaultToolkit().getScreenSize().height;   //height of screen
    public static final int FRAME_X = (SCREEN_W - FRAME_W) / 2;       //location of window based on x-axis position
    public static final int FRAME_Y = (SCREEN_H - FRAME_H) / 2;       //location of window based on y-axis position
    public static final String TITLE = "计算器";            //title of window
}
