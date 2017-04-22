package freecellize;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
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
        nothowSleep(50);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }

    public void returnKey(){
        robot.keyPress(KeyEvent.VK_ENTER);
        nothowSleep(50);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }

    public BufferedImage holdRightScreenGrab(int mouseX, int mouseY){
        moveMosue(mouseX, mouseY);
        robot.mousePress(InputEvent.BUTTON3_MASK);
        nothowSleep(50);
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage ret = robot.createScreenCapture(screenRect);
        nothowSleep(50);
        robot.mouseRelease(InputEvent.BUTTON3_MASK);
        return ret;
    }

    public BufferedImage screenGrab(int mouseX, int mouseY){
        //first get the mouse out of the way
        moveMosue(mouseX, mouseY);
        // now grab
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        return robot.createScreenCapture(screenRect);
    }

    public BufferedImage screenGrab() {
        return screenGrab(0, 0);
    }

    private static void nothowSleep(int ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
