package com.interaxon.test.libmuse;



public final class SampEn {

	public static float computeF(float[] u) {
		return computeF(u, 2, 0.15f, true);
	}
	
	public static float computeF(float[] u, int m, float r, boolean rNormalized) {
		int N = u.length;
		
		// Desnormalizamos el valor de r
		if (rNormalized)
			r = r * GenericMath.std(u);

		float A=0,B=0;
		for(int i=0; i < N - m; i++){
			for (int j=i+1; j < N - m; j++){
			boolean match =true;
			for (int k = 0; k < m; k++){
				if (((u[i+k] - u[j+k]) > r) || ((u[j+k] - u[i+k]) > r)){
					match = false;
					break;
				}					
			}
			
			if (match){
				B++;
				if (((u[i+m] - u[j+m]) > r) || ((u[j+m] - u[i+m]) > r)){
					A++;
				}				
			}
			}
		}
		
		if (m==1)
			B = N * (N - 1)/2f;
		
		if (B == 0 || A == 0)
			return (float) -Math.log((N-m)/ (N-m-1));
		
		return (float) -Math.log(A/B);		
	}
	
//	public static float computeF(float[] u, int dim, float r, boolean rNormalized) {
//		// setting up data matrix
////		float[] x = new float[(dim + 1) * (u.length - dim)];
//		float[][] x = new float[dim + 1][u.length - dim];
//		for (int i = 0; i < dim + 1; i++) {
//			 Utils.copyArrayIntoMatrix(x, i, u, i, u.length - dim + i - 1 + 1);
////			Utils.copyArrayIntoArray(x, i * (u.length - dim), u, i, u.length - dim + i - 1 + 1);
//		}
//
//		int c1 = counter(x, u.length - dim, dim, r);
//		int c2 = counter(x, u.length - dim, dim + 1, r);
//		
//		if (c1 == 0 && c2 == 0)
//			return Float.NaN;
//		else if (c1 != 0 && c2 == 0)
//			return (c1 > 0) ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
//		
//		return (float) Math.log(c1 / c2);
//	}
//
//	private static int counter(float[][] data, int row, int m, float r) {
////	    xTemp = x(1:m,:);
////	    yTemp = y(1:m,:);
////	    
////	    for i = 1:N-m
////	        % calculate Chebyshev distance, excluding self-matching case
////	        dist = max(abs(xTemp(:,i+1:N-dim) - repmat(yTemp(:,i),1,N-dim-i)));
////	        
//		int count = 0;
//		
//		for (int i = 0; i < row - m; i++ ) {
//			for (int )
//		}
//		
//		// Copia de una persona de mathworks y revisado por m�.
//		int count = 0, i, j, ii;
//		float dif, s;
//
//		for (i = 0; i < row - m; i++) {
//			for (ii = i + 1; ii < row; ii++) {
//				s = 0;
//				for (j = 0; j < m; j++) {
//					dif = data[j * row + i] - data[j * row + ii];
//					if (dif < 0)
//						dif = -dif;
//					if (dif > s)
//						s = dif;
//				}
//				if (s < r)
//					count++;
//			}
//		}
//		return count * 2 / (row * row);
//	}
//
////	public static float computeF(float[] u, int dim, float r, boolean rNormalized) {
////		// setting up data matrix
////		float[] x = new float[(dim + 1) * (u.length - dim)];
////		for (int i = 0; i < dim + 1; i++) {
////			// copyArrayIntoArray(x, i, u, i, u.length - dim + i - 1);
////			Utils.copyArrayIntoArray(x, i * (u.length - dim), u, i, u.length - dim + i - 1 + 1);
////		}
////
////		int c1 = counter(x, u.length - dim, dim, r);
////		int c2 = counter(x, u.length - dim, dim + 1, r);
////		
////		if (c1 == 0 && c2 == 0)
////			return Float.NaN;
////		else if (c1 != 0 && c2 == 0)
////			return (c1 > 0) ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
////		
////		return (float) Math.log(c1 / c2);
////	}
////
////	private static int counter(float[] data, int row, int m, float r) {
////		// Copia de una persona de mathworks y revisado por m�.
////		int count = 0, i, j, ii;
////		float dif, s;
////
////		for (i = 0; i < row - m; i++) {
////			for (ii = i + 1; ii < row; ii++) {
////				s = 0;
////				for (j = 0; j < m; j++) {
////					dif = data[j * row + i] - data[j * row + ii];
////					if (dif < 0)
////						dif = -dif;
////					if (dif > s)
////						s = dif;
////				}
////				if (s < r)
////					count++;
////			}
////		}
////		return count * 2 / (row * row);
////	}
}