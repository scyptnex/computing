package p081t120;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import util.Collect;
import util.Read;

public class E096Sudoku {
	
	public static void main(String[] args) throws IOException{
		
		List<String> fi = Read.streamLines(E096Sudoku.class).filter(s -> !s.startsWith("Grid")).collect(Collectors.toList());
		int sum = 0;
		for(int i=0; i < fi.size(); i+=9){
			int[][] sdk = new int[9][9];
			for(int r=0; r<9; r++){
				for(int c=0; c<9; c++){
					sdk[r][c] = fi.get(i + r).charAt(c) - '0';
				}
			}
			prnt(sdk);
			System.out.println();
			int[][] sol = solve(sdk);
			prnt(sol);
			System.out.println("=================");
			sum = sum + 100*sol[0][0] + 10*sol[0][1] + sol[0][2];
		}
		System.out.println(sum);
	}
	
	public static void prnt(int[][] sdk){
		if(sdk == null){
			System.out.println("---NO SOLUTION---");
			return;
		}
		for(int r=0; r<9; r++){
			for(int c=0; c<9; c++){
				System.out.print(sdk[r][c] + " ");
			}
			System.out.println();
		}
	}
	
	public static int[][] solve(int[][] sdk){
		if(!isValid(sdk)) return null;
		for(int r=0; r<9; r++){
			for(int c=0; c<9; c++){
				if(sdk[r][c] == 0){
					for(int i=1; i<=9; i++){
						int[][] g = guess(sdk, r, c, i);
						//prnt(g);
						//System.out.println(isValid(g));
						int[][] ss = solve(g);
						if(ss != null) return ss;
					}
					return null;
				}
			}
		}
		return sdk;
	}
	
	public static int[][] guess(int [][] sdk, int ro, int co, int va){
		int[][] ret = new int[9][9];
		for(int r=0; r<9; r++) for(int c=0; c<9; c++) ret[r][c] = sdk[r][c];
		ret[ro][co] = va;
		return ret;
	}
	
	public static boolean isValid(int[][] sdk){
		for(int r=0; r<9; r++){
			final int row = r;
			if(! validGroup(sdk, i -> new Collect.Pair<>(row, i))) return false;
		}
		for(int c=0; c<9; c++){
			final int col = c;
			if(! validGroup(sdk, i -> new Collect.Pair<>(i, col))) return false;
		}
		for(int b=0; b<9; b++){
			final int box = b;
			if(! validGroup(sdk, i -> new Collect.Pair<>((box%3)*3 + (i%3), (box/3)*3 + (i/3)))) return false;
		}
		return true;
	}
	
	public static boolean validGroup(int[][] sdk, IntFunction<Collect.Pair<Integer, Integer>> selector){
		List<Integer> all = IntStream.range(0, 9)
				.mapToObj(selector)
				.map(p -> sdk[p.first][p.second])
				.filter(i -> i != 0)
				.collect(Collectors.toList());
		return all.size() == all.stream().distinct().count();
	}
	
}
