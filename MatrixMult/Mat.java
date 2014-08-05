
public class Mat {

	public final int n;
	public final int m;
	public final int[][] mat;
	
	public Mat(int n){
		this(n, n);
	}
	
	public Mat(int n, int m){
		this.n = n;
		this.m = m;
		this.mat = new int[n][m];
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Mat){
			Mat other = (Mat)o;
			if(other.n != n || other.m != m) return false;
			for(int i=0; i<n; i++){
				for(int j=0; j<m; j++){
					if(this.mat[i][j] != other.mat[i][j]) return false;
				}
			}
			return true;
		}
		return false;
	}
	
	public void rdm(int min, int max){
		for(int i=0; i<n; i++){
			for(int j=0; j<m; j++){
				mat[i][j] = min + (int)Math.floor(Math.random()*(max - min));
			}
		}
	}
	
	public Mat add(Mat b){
		Mat ret = new Mat(n, m);
		for(int r=0; r<n; r++){
			for(int c=0; c<m; c++){
				ret.mat[r][c] = this.mat[r][c] + b.mat[r][c];
			}
		}
		return ret;
	}
	
	public Mat subtract(Mat b){
		Mat ret = new Mat(n, m);
		for(int r=0; r<n; r++){
			for(int c=0; c<m; c++){
				ret.mat[r][c] = this.mat[r][c] - b.mat[r][c];
			}
		}
		return ret;
	}
	
	public Mat multiply(Mat b){
		Mat ret = new Mat(n, b.m);
		for(int r=0; r<n; r++){
			for(int c=0; c<b.m; c++){
				int tmp = 0;
				for(int k=0; k<m; k++){
					tmp += mat[r][k]*b.mat[k][c];
				}
				ret.mat[r][c] = tmp;
			}
		}
		return ret;
	}
	
	public Mat scale(int s){
		Mat ret = new Mat(n, m);
		for(int r=0; r<n; r++){
			for(int c=0; c<m; c++){
				ret.mat[r][c] = this.mat[r][c]*s;
			}
		}
		return ret;
	}
	
	public Mat subMatrix(int lowRow, int highRow, int lowCol, int highCol){
		Mat ret = new Mat(highRow - lowRow, highCol - lowCol);
		for(int i=0; i<ret.n; i++){
			for(int j=0; j<ret.m; j++){
				ret.mat[i][j] = this.mat[i+lowRow][j+lowCol];
			}
		}
		return ret;
	}
	
}
