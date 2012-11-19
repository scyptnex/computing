import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;


public class USpiral extends JComponent{
	
	public static final int CELL_SIZE = 1;
	
	public static final int[] X_DIFFS = {1, 0, -1, 0};
	public static final int[] Y_DIFFS = {0, 1, 0, -1};
	
	public final int run;
	public final int[][] nums;
	public final boolean[][] primes;
	
	public static void main(String[] args){
		JFrame frm = new JFrame("Ullam Spiral");
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = frm.getContentPane();
		c.setLayout(new BorderLayout());
		USpiral d = new USpiral(480);
		c.add(d, BorderLayout.CENTER);
		frm.setContentPane(c);
		frm.pack();
		frm.setVisible(true);
	}
	
	public USpiral(int radius){
		run = 1+2*radius;
		nums = new int[run][run];
		primes = new boolean[run][run];
		for(int i=0; i<run; i++) for(int j=0; j<run; j++){
			nums[i][j] = -1;
		}
		
		spiral(0, 0, run*run);
		
		for(int y=0; y<run; y++){
			for(int x=0; x<run; x++){
				primes[x][y] = isprime(nums[x][y]);
				//System.out.print(nums[x][y] + "\t");
			}
			//System.out.println();
		}
	}
	
	public void paint(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		for(int x=0; x<run; x++){
			for(int y=0; y<run; y++){
				g2.setColor(Color.black);
				if(primes[x][y]) g2.setColor(Color.white);
				g2.fillRect(x*CELL_SIZE, y*CELL_SIZE, CELL_SIZE, CELL_SIZE);
			}
		}
	}
	
	public static void spiralRecursive(int x, int y, int amt, int dir, int[][] write){
		if(amt <= 1){
			write[x][y] = 1;
			return;
		}
		write[x][y] = amt;
		int nextx = x + X_DIFFS[dir];
		int nexty = y + Y_DIFFS[dir];
		if(nextx < 0 || nextx >= write.length || nexty < 0 || nexty >= write[nextx].length || write[nextx][nexty] != -1){
			dir = (dir+1)%X_DIFFS.length;
		}
		spiralRecursive(x + X_DIFFS[dir], y + Y_DIFFS[dir], amt-1, dir, write);
	}
	
	public void spiral(int x, int y, int amt){
		int dir = 0;
		while(amt > 1){
			nums[x][y] = amt;
			int nextx = x + X_DIFFS[dir];
			int nexty = y + Y_DIFFS[dir];
			if(nextx < 0 || nextx >= nums.length || nexty < 0 || nexty >= nums[nextx].length || nums[nextx][nexty] != -1){
				dir = (dir+1)%X_DIFFS.length;
			}
			x += X_DIFFS[dir];
			y += Y_DIFFS[dir];
			amt--;
		}
		nums[x][y] = amt;
		
		//spiralRecursive(x, y, amt, 0, nums);
	}
	
	public Dimension getPreferredSize(){
		return new Dimension(CELL_SIZE*run, CELL_SIZE*run);
	}
	public Dimension getMinimumSize(){return getPreferredSize();}
	public Dimension getMaximumSize(){return getPreferredSize();}
	
	public static boolean isprime(int n){
		if(n == 1) return false;
		for(int check=2; check<Math.sqrt(n); check++){
			if(n%check == 0) return false;
		}
		return true;
	}
	
}
