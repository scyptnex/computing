package freecellize;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;

public class Screenterface {

    private final Robot robot = new Robot();

    public Screenterface() throws AWTException {
    }

    public void moveMosue(int x, int y){
        robot.mouseMove(x, y);
    }

    public void lclick(int x, int y){
        moveMosue(x, y);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }

    public BufferedImage screenGrab(){
        //first get the mouse out of the way
        moveMosue(0, 0);
        // now grab
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        return robot.createScreenCapture(screenRect);
    }

}
