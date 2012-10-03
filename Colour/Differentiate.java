import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;

public class Differentiate extends JComponent implements KeyListener{
	
	public Color background;
	public Color c1, c2;
	public Color textcol;
	public boolean same;
	double diffamt;
	public double h1;
	public double h2;
	
	public final double maxdev;
	public final double devprob;
	
	boolean showlog;
	public ArrayList<Boolean> answers;
	public ArrayList<Boolean> guesses;
	public ArrayList<Double> h1s;
	public ArrayList<Double> h2s;
	public ArrayList<Double> diffs;
	
	public static void main(String[] args){
		JFrame frm = new JFrame("Colour Differentiation");
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = frm.getContentPane();
		c.setLayout(new BorderLayout());
		Differentiate d = new Differentiate();
		d.setFocusable(true);
		d.addKeyListener(d);
		c.add(d, BorderLayout.CENTER);
		frm.setContentPane(c);
		frm.pack();
		frm.setVisible(true);
	}
	
	public Differentiate(){
		maxdev = 0.1;
		devprob = 0.5;
		
		answers = new ArrayList<Boolean>();
		guesses = new ArrayList<Boolean>();
		h1s = new ArrayList<Double>();
		h2s = new ArrayList<Double>();
		diffs = new ArrayList<Double>();
		
		background = null;
		showlog = false;
	}
	
	public void log(boolean guessedSame){
		answers.add(same);
		guesses.add(guessedSame);
		h1s.add(h1);
		h2s.add(h2);
		diffs.add(diffamt);
	}
	
	public void viewLog(){
		background = null;
		showlog = true;
		repaint();
	}
	
	public void newTest(){
		setCols(Math.random(), 1.0, 1.0);
	}
	public void setCols(double hue, double sat, double bright){
		h1 = hue;
		if(Math.random() > devprob){
			h2 = h1;
			same = true;
		}
		else{
			diffamt = Math.random()*maxdev;
			h2 = hue+diffamt;
			while(h2 > 1) h2 = h2-1;
			same = false;
		}
		c1 = Color.getHSBColor((float)h1, (float)sat, (float)bright);
		c1 = Color.getHSBColor((float)h2, (float)sat, (float)bright);
		if(Math.random() > 0.5) hue = h2;
		double bgh = (hue) > 0.5 ? hue-0.5 : hue+0.5;
		background = Color.getHSBColor((float)bgh, (float)sat, (float)bright);
		int avg = (background.getBlue() + background.getGreen() + background.getRed())/3;
		if(avg > 128) textcol = Color.BLACK;
		else textcol = Color.WHITE;
		repaint();
	}
	
	public void paint(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		if(background == null){
			if(showlog){
				
			}
			else{
				g2.setFont(new Font("serif", Font.PLAIN, 32));
				g2.drawString("Colour Differentiation test", 50, 100);
				g2.setFont(new Font("serif", Font.PLAIN, 20));
				g2.drawString("Press up if you think the colours are different", 50, 150);
				g2.drawString("Press down if you think the colours are the same", 50, 175);
				g2.drawString("Guess until you get bored", 50, 200);
				g2.drawString("Press q to end the test", 50, 225);
				g2.drawString("Press any key to continue", 50, 275);
			}
		}
		else{
			g2.setColor(background);
			g2.fill(new Rectangle2D.Float(0, 0, getPreferredSize().width, getPreferredSize().height));
			g2.setColor(c1);
			g2.fill(new Ellipse2D.Float(getPreferredSize().width/4 - (getPreferredSize().width/6), getPreferredSize().height/2 - (getPreferredSize().width/6), getPreferredSize().width/3, getPreferredSize().width/3));
			g2.setColor(c2);
			g2.fill(new Ellipse2D.Float(3*getPreferredSize().width/4 - (getPreferredSize().width/6), getPreferredSize().height/2 - (getPreferredSize().width/6), getPreferredSize().width/3, getPreferredSize().width/3));
			g2.setColor(textcol);
			g2.setFont(new Font("serif", Font.BOLD, 32));
			g2.drawString("Different", getPreferredSize().width/2 - 70, 40);
			g2.drawString("Same", getPreferredSize().width/2 - 50, getPreferredSize().height-10);
		}
		
	}
	
	public Dimension getPreferredSize(){
		return new Dimension(800, 600);
	}
	public Dimension getMinimumSize(){return getPreferredSize();}
	public Dimension getMaximumSize(){return getPreferredSize();}

	@Override
	public void keyPressed(KeyEvent arg0) { }

	@Override
	public void keyReleased(KeyEvent arg0) {
		if(background == null){
			newTest();
		}
		else{
			if(arg0.getKeyCode() == KeyEvent.VK_UP){//different
				log(false);
				newTest();
			}
			else if(arg0.getKeyCode() == KeyEvent.VK_DOWN){//same
				log(true);
				newTest();
			}
			else if(arg0.getKeyCode() == KeyEvent.VK_Q){
				viewLog();
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) { }
	
	
}