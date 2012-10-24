import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;

import javax.imageio.ImageIO;
public class Robomatic {
	
	public static void main(String[] args){
		new Robomatic();
	}
	
	public Robomatic(){
		System.out.println("robotiq");
		try {
			Thread.sleep(30000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			System.out.println("entering");
			Robot rbt = new Robot();
			System.out.println("entered");
			BufferedImage screenShot = rbt.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
			ImageIO.write(screenShot, "PNG", new File("test.png"));
			//rbt.delay();
			System.out.println("top");
			rbt.mouseMove(100, 100);
			System.out.println("mid");
			rbt.mouseMove(200, 200);
			System.out.println("bot");
			rbt.mouseMove(300, 300);
		} catch (AWTException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}