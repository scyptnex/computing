
public class TurtleCover {
	
	public static int min=-10;
	public static int max=10;
	
	public static char[][] tc = new char[max - min][max - min];//x y format
	
	public static void main(String[] args){
		for(int x=min; x<max; x++){
			for(int y=min; y<max; y++){
				set(x, y, ' ');
			}
		}
		for(int x=min; x<max; x++){
			for(int y=min; y<max; y++){
				if((x - 2*(y%5))%5 == 0){
					set(x, y, 'X');
					set(x-1, y, '-');
					set(x+1, y, '-');
					set(x, y-1, '-');
					set(x, y+1, '-');
				}
			}
		}
		prt();
		System.out.println((-5)%5);
	}
	
	public static void prt(){
		for(int y=max-1; y>=min; y--){
			System.out.print(String.format("%4d :", y));
			for(int x=min; x<max; x++){
				System.out.print(" " + tc[x-min][y-min]);
			}
			System.out.println();
		}
	}
	
	public static void set(int x, int y, char c){
		if(x >= min && x < max && y >= min && y < max){
			tc[x-min][y-min] = c;
		}
	}
	
}
