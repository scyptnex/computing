package Instance;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class Site {
	
	
	/*
	 * MASKS
	 */
	public static final int GRID_UNKNOWN = 0x80000000;
	public static final int GRID_HIDDEN = 0x40000000;
	//public static final int GRID_ = 0x20000000; Flooded, open breathable, hazardous, lava? acid?
	//public static final int GRID_ = 0x10000000;
	//public static final int GRID_ = 0x80000000;
	//public static final int GRID_ = 0x40000000;
	//public static final int GRID_ = 0x20000000;
	//public static final int GRID_ = 0x10000000;
	
	public static final int COARSE_WIDTH = 16;
	public static final int COARSE_HEIGHT = 16;

	public static final int SUBSITE_Y = COARSE_WIDTH*COARSE_HEIGHT;//down-up
	public static final int SUBSITE_X = SUBSITE_Y;//west-east
	public static final int SUBSITE_Z = SUBSITE_Y;//south-north
	
	private final int subsiteX;
	private final int subsiteY;
	private final int subsiteZ;
	private BufferedImage[][][] siteContents;
	
	public static void main(String[] args){
		Site s = new Site(1,1,1);
		for(int x=0; x<256; x++){
			for(int y=0; y<256; y++){
				for(int z=0; z<256; z++){
					s.set(x, y, z, (255<<24));
					if(x > 15 && x < 64 && z > 112 && z < 140 && y > 3 && y < 33){
						s.set(x, y, z, s.get(x, y, z)|255);
					}
				}
			}
		}
		s.save(new File("."));
	}
	
	public Site(int xLen, int yLen, int zLen){
		subsiteX = xLen;
		subsiteY = yLen;
		subsiteZ = zLen;
		siteContents = new BufferedImage[subsiteX][subsiteY][subsiteZ];
		for(int x=0; x<subsiteX; x++) for(int y=0; y<subsiteY; y++) for (int z=0; z<subsiteZ; z++){
			siteContents[x][y][z] = new BufferedImage(COARSE_WIDTH*SUBSITE_X, COARSE_HEIGHT*SUBSITE_Z, BufferedImage.TYPE_INT_ARGB);
		}
	}
	
	public boolean save(File dirLoc){
		if(!dirLoc.isDirectory()) return false;
		try{
			for(int x=0; x<subsiteX; x++) for(int y=0; y<subsiteY; y++) for (int z=0; z<subsiteZ; z++){
				File write = new File(dirLoc, "site-"+x+"-"+y+"-"+z+".png");
				ImageIO.write(siteContents[x][y][z], "png", write);
			}
		}
		catch(IOException exc){
			return false;
		}
		return true;
	}
	
	public void set(int x, int y, int z, int val){
		BufferedImage subsite = siteContents[x/SUBSITE_X][y/SUBSITE_Y][z/SUBSITE_Z];
		x %= SUBSITE_X;
		y %= SUBSITE_Y;
		z %= SUBSITE_Z;
		//CROSS PIXEL is Y, thanks minecraft
		int heightGridWidth = y%COARSE_WIDTH;
		int heightGridHeight = y/COARSE_WIDTH;
		subsite.setRGB(heightGridWidth*SUBSITE_X + x, heightGridHeight*SUBSITE_Z + z, val);
	}
	
	public int get(int x, int y, int z){
		BufferedImage subsite = siteContents[x/SUBSITE_X][y/SUBSITE_Y][z/SUBSITE_Z];
		x %= SUBSITE_X;
		y %= SUBSITE_Y;
		z %= SUBSITE_Z;
		//CROSS PIXEL is Y, thanks minecraft
		int heightGridWidth = y%COARSE_WIDTH;
		int heightGridHeight = y/COARSE_WIDTH;
		return subsite.getRGB(heightGridWidth*SUBSITE_X + x, heightGridHeight*SUBSITE_Z + z);
	}
	
}
