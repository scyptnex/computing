import java.util.*;
import java.io.*;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

public class NoisePattern {
	
	public static void main(String[] args){
		double[][] terrain = getNoisePattern(20, 20, 1, 20, 3, 10, 2, 5);
		
		JFrame frm = new JFrame();
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = frm.getContentPane();
		c.setLayout(new BorderLayout());
		c.add(new TerrainViewer(terrain, 20));
		frm.setContentPane(c);
		frm.pack();
		frm.setVisible(true);
	}
	
	public static double[][] getNoisePattern(int wid, int hei, double boundHeight, int count, double maxRad, double minRad, double maxHeight, double minHeight){
		int baseWidth = wid+(int)Math.ceil(2*maxRad);
		int baseHeight = hei+(int)Math.ceil(2*maxRad);
		double[][] blank = new double[baseWidth][baseHeight];
		
		for(int c=0; c<count; c++){
			int xc = (int)(maxRad+Math.random()*wid);
			int yc = (int)(maxRad+Math.random()*hei);
			double rad = minRad + Math.random()*(maxRad-minRad);
			double siz = minHeight + Math.random()*(maxHeight-minHeight);
			growCircle(blank, xc, yc, rad, siz);
		}
		
		//double[][] window = new double[wid][hei];
		//for(int x=0; x<wid; x++) for(int y=0; y<hei; y++){
		//	window[x][y] = blank[(int)(x+maxRad)][(int)(y+maxRad)];
		//}
		return blank;
	}
	
	public static double[][] growCircle(double[][] terrain, int x, int y, double r, double h){
		double b = h;
		double a = Math.sqrt(b)/(double)r;
		for(int xo = (int)Math.max(0, x-r-1); xo < Math.min(terrain.length, x+r+1); xo++){
			for(int yo = (int)Math.max(0, y-r-1); yo < Math.min(terrain[xo].length, y+r+1); yo++){
				int xd = Math.abs(x - xo);
				int yd = Math.abs(y - yo);
				double dist = Math.sqrt((xd*xd)+(yd*yd));
				double g = b-((a*dist)*(a*dist));
				if(g > 0){
					terrain[xo][yo] = terrain[xo][yo] + g;
				}
			}
		}
		return terrain;
	}
	
	public static class TerrainViewer extends JComponent{
		
		double[][] t;
		double min, max;
		int zoom;
		
		public TerrainViewer(double[][] terrain, int z){
			t = terrain;
			min = Double.MAX_VALUE;
			max = Double.MIN_VALUE;
			for(int x=0; x<t.length; x++) for(int y=0; y<t[x].length; y++){
				min = Math.min(min, t[x][y]);
				max = Math.max(max, t[x][y]);
			}
			zoom = z;
			if(max == min) max = max+1;
		}
		
		public Dimension getPreferredSize(){
			if(t.length == 0) return new Dimension(0, 0);
			return new Dimension(t.length*zoom, t[0].length*zoom);
		}
		
		public Dimension getMinimumSize(){
			return getPreferredSize();
		}
		
		public Dimension getMaximumSize(){
			return getPreferredSize();
		}
		
		public void paint(Graphics g){
			Graphics2D g2 = (Graphics2D) g;
			for(int x=0; x<t.length; x++){
				for(int y=0; y<t[x].length; y++){
					double col = (t[x][y]-min)*255.0/(max-min);
					g2.setColor(new Color((int)col, (int)col, (int)col));
					g2.fill(new Rectangle2D.Float(x*zoom, y*zoom, zoom, zoom));
				}
			}
		}
		
	}
	
}