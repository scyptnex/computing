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
		try {
			Robot rbt = new Robot();
			rbt.delay(1000);
			System.out.println("top");
			rbt.mouseMove(100, 100);
			rbt.delay(5000);
			System.out.println("mid");
			rbt.mouseMove(500, 500);
			rbt.delay(5000);
			System.out.println("bot");
			rbt.mouseMove(900, 900);
			BufferedImage screenShot = rbt.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
			ImageIO.write(screenShot, "PNG", new File("test.png"));
		} catch (AWTException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}