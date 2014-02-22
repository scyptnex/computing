import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

public class GridRandomer extends JComponent{

	public final int x;
	public final int y;
	public final int size;

	public final Vert[][] vertices;
	public final Edg[] edges;

	public static void main(String[] args){
		GridRandomer gr = new GridRandomer(20, 20, 40);
		JFrame frm = new JFrame();
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = frm.getContentPane();
		c.setLayout(new BorderLayout());
		c.add(gr, BorderLayout.CENTER);
		frm.setContentPane(c);
		frm.pack();
		frm.setVisible(true);
	}

	public GridRandomer(int xCount, int yCount, int boxSize){
		this.x = xCount;
		this.y =  yCount;
		this.size = boxSize;

		edges = new Edg[(x-1)*y + (y-1)*x];
		for(int i=0; i<edges.length; i++){
			edges[i] = new Edg();
		}
		vertices = new Vert[x-1][y-1];
		for(int vx=0; vx < x-1; vx++) for(int vy=0; vy < y-1; vy++){
			vertices[vx][vy] = new Vert(vx, vy);
		}

		cull();
	}

	public void cull(){
		while(culling()){
			while(!vertices[rdm(x-1)][rdm(y-1)].rdmCull());
		}
	}

	public static int rdm(int belowThis){
		return(int)Math.floor(Math.random()*belowThis);
	}

	public boolean culling(){
		for(int vx=0; vx<x-1; vx++){
			for(int vy=0; vy<y-1; vy++){
				if(vertices[vx][vy].visCount() > 3) return true;
			}
		}
		return false;
	}

	private static class Edg{
		public boolean shown = true;
	}

	private class Vert{
		public final int myX, myY;
		public final Edg nor, eas, sou, wes;
		public Vert(int vx, int vy){
			myX = vx;
			myY = vy;
			nor = edges[edgeNorth(myX, myY)];
			eas = edges[edgeEast(myX, myY)];
			sou = edges[edgeSouth(myX, myY)];
			wes = edges[edgeWest(myX, myY)];
		}
		public int visCount(){
			int ret = 0;
			ret += nor.shown ? 1 : 0;
			ret += eas.shown ? 1 : 0;
			ret += sou.shown ? 1 : 0;
			ret += wes.shown ? 1 : 0;
			return ret;
		}
		public boolean rdmCull(){
			if(visCount() < 2) return false;
			Edg edge = null;
			int ox = myX;
			int oy = myY;
			switch(rdm(4)){
			case 0 :{
				oy--;
				edge = nor;
				break;
			}
			case 1 :{
				ox++;
				edge = eas;
				break;
			}
			case 2 :{
				oy++;
				edge = sou;
				break;
			}
			case 3 :{
				ox--;
				edge = wes;
				break;
			}
			}
			if(edge.shown){
				if(ox < 0 || oy < 0 || ox >= x-1 || oy >= y-1 || vertices[ox][oy].visCount() > 1){
					edge.shown = false;
					return true;
				}
			}
			return false;
		}
		public void draw(Graphics2D g2, int gx, int gy, int size) {
			if(nor.shown){
				g2.drawLine(gx, gy, gx, gy-size);
			}
			if(eas.shown){
				g2.drawLine(gx, gy, gx+size, gy);
			}
			if(sou.shown){
				g2.drawLine(gx, gy, gx, gy+size);
			}
			if(wes.shown){
				g2.drawLine(gx, gy, gx-size, gy);
			}
		}
	}

	public int edgeNorth(int vx, int vy){
		return y*vx + vy;
	}

	public int edgeEast(int vx, int vy){
		return edgeWest(vx+1, vy);
	}

	public int edgeSouth(int vx, int vy){
		return edgeNorth(vx, vy+1);
	}

	public int edgeWest(int vx, int vy){
		return (x-1)*y + x*vy + vx;
	}

	@Override
	public Dimension getPreferredSize(){
		return new Dimension((x+2)*size, (y+2)*size);
	}

	@Override
	public Dimension getMinimumSize(){
		return getPreferredSize();
	}

	@Override
	public Dimension getMaximumSize(){
		return getPreferredSize();
	}

	@Override
	public void paint(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		for(int xv=0; xv<x-1; xv++){
			for(int yv=0; yv<y-1; yv++){
				vertices[xv][yv].draw(g2, (xv+2)*size, (yv+2)*size, size);
			}
		}
	}

}
