import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.geom.*;

public class Zssr extends JComponent{
	
	public final SharedState state;
	
	public static void main(String[] args){
		JFrame frm = new JFrame();
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Zssr rend = new Zssr(new SharedState(1024, 1024));
		
		Container c = frm.getContentPane();
		c.setLayout(new BorderLayout());
		c.add(rend);
		frm.setContentPane(c);
		
		frm.pack();
		frm.setVisible(true);
	}
	
	public Zssr(SharedState ss){
		state = ss;
		this.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent arg0) {}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mousePressed(MouseEvent evt) {
				click(evt.getX(), evt.getY(), evt.getButton());
			}
			public void mouseReleased(MouseEvent arg0) {}
		});
	}
	
	public Dimension getPreferredSize(){
		return new Dimension(800, 600);
	}
	
	public Dimension getMinimumSize(){
		return getPreferredSize();
	}
	
	public Dimension getMaximumSize(){
		return getPreferredSize();
	}
	
	public void click(int x, int y, int b){
		System.out.println(x + ", " + y + " = " + b);
	}
	
}