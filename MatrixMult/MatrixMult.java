
public abstract class MatrixMult {

	public static final int MAX_ELEMENT = 9;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final int procs = 2;
		final int subSize = 600;
		final int n = procs*subSize;
		
		final Mat a = new Mat(n);
		final Mat b = new Mat(n);
		
		for(int i=0; i<10; i++){
			a.rdm(0, MAX_ELEMENT);
			b.rdm(0, MAX_ELEMENT);
			Mat nai = new Naive().multiply(a, b);
			Mat str = new Strassen().multiply(a, b);
			System.out.println(nai.equals(str));
		}
		
		//print(n/2, subMat(a, n/2, 0, 0), subMat(a, n/2, 0, 1), subMat(a, n/2, 1, 0), subMat(a, n/2, 1, 1));
	}
	
	public Mat multiply(Mat a, Mat b){
		Mat ret = new Mat(a.n, b.m);
		Long time = System.currentTimeMillis();
		this.multiplyInternal(a, b, ret);
		System.out.println(this.getClass().toString() + " completed in: " + (System.currentTimeMillis() - time));
		return ret;
	}

	public abstract void multiplyInternal(Mat a, Mat b, Mat result);

	public static void print(int n, Mat ... a){
		int max = 0;
		for(int i=0; i<n; i++){
			for(int j=0; j<n; j++){
				for(int x=0; x<a.length; x++){
					max = Math.max(max, (a[x].mat[i][j] + "").toString().length());
				}
			}
		}
		String fmtStr = " %" + max + "d";
		for(int i=0; i<n; i++){
			for(int x=0; x<a.length; x++){
				for(int j=0; j<n; j++){
					System.out.print(String.format(fmtStr, a[x].mat[i][j]));
				}
				System.out.print("  ");
			}
			System.out.println();
		}
	}
	
	public static int[][] subMat(int[][] mat, int divis, int rd, int cd){
		int rn = mat.length/divis;
		int[][] ret = new int[rn][rn];
		for(int i=0; i<rn; i++){
			for(int j=0; j<rn; j++){
				ret[i][j] = mat[i + rd*rn][j + cd*rn];
			}
		}
		return ret;
	}
	
	public static class Naive extends MatrixMult{
		@Override
		public void multiplyInternal(Mat a, Mat b, Mat result) {
			for(int i=0; i<result.n; i++){
				for(int j=0; j<result.m; j++){
					result.mat[i][j] = 0;
					for(int k=0; k<a.m; k++){
						result.mat[i][j] += a.mat[i][k]*b.mat[k][j];
					}
				}
			}
		}
	}
	
	public static class Strassen extends MatrixMult{
		@Override
		public void multiplyInternal(Mat a, Mat b, Mat result) {
			Mat a11 = a.subMatrix(0, a.n/2, 0, a.m/2);
			Mat a12 = a.subMatrix(0, a.n/2, a.m/2, a.m);
			Mat a21 = a.subMatrix(a.n/2, a.n, 0, a.m/2);
			Mat a22 = a.subMatrix(a.n/2, a.n, a.m/2, a.m);
			Mat b11 = b.subMatrix(0, b.n/2, 0, b.m/2);
			Mat b12 = b.subMatrix(0, b.n/2, b.m/2, b.m);
			Mat b21 = b.subMatrix(b.n/2, b.n, 0, b.m/2);
			Mat b22 = b.subMatrix(b.n/2, b.n, b.m/2, b.m);
			
			Mat m1 = a11.add(a22).multiply(b11.add(b22));
			Mat m2 = a21.add(a22).multiply(b11);
			Mat m3 = a11.multiply(b12.subtract(b22));
			Mat m4 = a22.multiply(b21.subtract(b11));
			Mat m5 = a11.add(a12).multiply(b22);
			Mat m6 = a21.subtract(a11).multiply(b11.add(b12));
			Mat m7 = a12.subtract(a22).multiply(b21.add(b22));
			
			fill(result, 0, 0, m1.add(m4).subtract(m5).add(m7));
			fill(result, 0, a.m/2, m3.add(m5));
			fill(result, a.n/2, 0, m2.add(m4));
			fill(result, a.n/2, a.m/2, m1.subtract(m2).add(m3).add(m6));
		}
		
		private static void fill(Mat into, int rowOff, int colOff, Mat from){
			for(int i=0; i<from.n; i++){
				for(int j=0; j<from.m; j++){
					into.mat[i+rowOff][j+colOff] = from.mat[i][j];
				}
			}
		}
	}

}

