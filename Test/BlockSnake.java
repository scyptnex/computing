
public class BlockSnake {

	public enum Seg{
		END, STRAIGHT, TURN;
	}
	
	public enum Dir{
		POS_X, NEG_X, POS_Y, NEG_Y, POS_Z, NEG_Z;
		public String toString(){
			switch(this){
			case POS_X :{
				return "+X";
			}
			case NEG_X :{
				return "-X";
			}
			case POS_Y :{
				return "+Y";
			}
			case NEG_Y :{
				return "-Y";
			}
			case POS_Z :{
				return "+Z";
			}
			default :{
				return "-Z";
			}
			}
		}
	}
	
	public static final Seg[] snake={
		Seg.END,      Seg.STRAIGHT, Seg.TURN, Seg.STRAIGHT, Seg.TURN, Seg.STRAIGHT, Seg.TURN, Seg.STRAIGHT, Seg.TURN,
		Seg.TURN,     Seg.TURN,     Seg.TURN, Seg.STRAIGHT, Seg.TURN, Seg.STRAIGHT, Seg.TURN, Seg.TURN,     Seg.TURN,
		Seg.STRAIGHT, Seg.TURN,     Seg.TURN, Seg.STRAIGHT, Seg.TURN, Seg.TURN,     Seg.TURN, Seg.STRAIGHT, Seg.END
		};
	
	public static class Coord3{
		
		public final int x, y, z;
		
		public Coord3(int xLoc, int yLoc, int zLoc){
			x = xLoc;
			y = yLoc;
			z = zLoc;
		}
		
		public Coord3 next(Dir dir){
			switch(dir){
			case POS_X :{
				return new Coord3(x+1, y, z);
			}
			case NEG_X :{
				return new Coord3(x-1, y, z);
			}
			case POS_Y :{
				return new Coord3(x, y+1, z);
			}
			case NEG_Y :{
				return new Coord3(x, y-1, z);
			}
			case POS_Z :{
				return new Coord3(x, y, z+1);
			}
			default :{//NZ
				return new Coord3(x, y, z-1);
			}
			}
		}
		
		public int getState(){
			return 1 << (x + 3*y + 9*z);
		}
	}

	public static int take(int state, Coord3 loc){
		return state | loc.getState();
	}

	public static boolean taken(int state, Coord3 loc){
		return (state & loc.getState()) > 0;
	}

	public static boolean isTurn(Dir cur, Dir next){
		switch(cur){
		case POS_X :{
			return next != Dir.POS_X && next != Dir.NEG_X;
		}
		case NEG_X :{
			return next != Dir.POS_X && next != Dir.NEG_X;
		}
		case POS_Y :{
			return next != Dir.POS_Y && next != Dir.NEG_Y;
		}
		case NEG_Y :{
			return next != Dir.POS_Y && next != Dir.NEG_Y;
		}
		default:{
			return next != Dir.POS_Z && next != Dir.NEG_Z;
		}
		}
	}

	public static void move(int state, Coord3 lastLoc, Dir dirToNext, int blocksUsed, Dir[] dirsUsed){
		Coord3 next = lastLoc.next(dirToNext);
		if(next.x < 0 || next.x >= 3 ||
				next.y < 0 || next.y >= 3 ||
				next.z < 0 || next.z >= 3 ||
				taken(state, next)){
			return;
		}
		int nextState = take(state, next);
		if(snake[blocksUsed] == Seg.END){
			System.out.print("solved ");
			for(int i=0; i<blocksUsed; i++){
				System.out.print(dirsUsed[i] + " ");
			}
			System.out.println();
		} else if(snake[blocksUsed] == Seg.TURN){
			for(Dir newDir : Dir.values()) if(isTurn(dirToNext, newDir)){
				dirsUsed[blocksUsed] = newDir;
				move(nextState, next, newDir, blocksUsed+1, dirsUsed);
			}
		} else {
			dirsUsed[blocksUsed] = dirToNext;
			move(nextState, next, dirToNext, blocksUsed+1, dirsUsed);
		}
	}

	public static void start(int x, int y){
		Coord3 loc = new Coord3(x, y, 0);
		int state = take(0, loc);
		Dir[] dirs = new Dir[snake.length];
		for(Dir dir : Dir.values()){
			dirs[0] = dir;
			move(state, loc, dir, 1, dirs);
		}
	}

	public static boolean test(int amt){
		int state = 0;
		boolean[][][] check = new boolean[3][3][3];
		for(int i=0; i<amt; i++){
			int x = (int)Math.floor(Math.random()*3);
			int y = (int)Math.floor(Math.random()*3);
			int z = (int)Math.floor(Math.random()*3);
			check[x][y][z] = true;
			state = take(state, new Coord3(x, y, z));
		}
		for(int x=0; x<3; x++){
			for(int y=0; y<3; y++){
				for(int z=0; z<3; z++){
					if(check[x][y][z] != taken(state, new Coord3(x, y, z))){
						System.err.println("Test " + amt + " failed " + x + y + z);
						return false;
					}
				}
			}
		}
		System.out.println("Test " + amt + " passed");
		return true;
	}

	public static void main(String[] args){
		System.out.println(snake.length);
		for(int i=0; i<=27; i++){
			test(i);
		}
		//there are only 3 potential starting positions
		start(0, 0);//corner
		start(1, 0);//edge
		start(1, 1);//center
		System.out.println("Unsolved");
	}

}
