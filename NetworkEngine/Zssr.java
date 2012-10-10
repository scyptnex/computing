import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class Zssr extends JComponent{
	
	public NetState ns;
	
	ArrayList<Integer> xobjs;
	ArrayList<Integer> yobjs;
	
	public static void main(String[] args){
		JFrame frm = new JFrame();
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Zssr rend = new Zssr();
		
		Container c = frm.getContentPane();
		c.setLayout(new BorderLayout());
		c.add(rend);
		frm.setContentPane(c);
		
		frm.pack();
		frm.setVisible(true);
		rend.ns.start();
	}
	
	public Zssr(){
		this.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent arg0) {}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mousePressed(MouseEvent evt) {
				click(evt.getX(), evt.getY(), evt.getButton());
			}
			public void mouseReleased(MouseEvent arg0) {}
		});
		
		ns = new NetState(50, 5, this);
	}
	
	public void paint(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g2.drawRect(50, 50, 100, 100);
	}
	
	public void advance(){
		System.out.println("wheee");
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
		if(b == MouseEvent.BUTTON3){//right click
			xobjs.add(ns.init(x, 0, "x coord"));
			yobjs.add(ns.init(y, 0, "x coord"));
		}
		else{//'left' click
			
		}
		System.out.println(x + ", " + y + " = " + b);
	}
	
	public class NetState extends SharedState{
		
		public final JComponent parent;

		public NetState(int tps, int tpu, JComponent comp) {
			super(tps, tpu);
			parent = comp;
		}
		
		public void tick() {
			advance();
		}

		@Override
		public void render() {
			parent.repaint();
		}
		
	}
	
}