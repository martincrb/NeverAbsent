package com.interaxon.test.libmuse;


public final class ApEn {
	
	public static float computeF(float[] u){
		return computeF(u, 2, 0.15f, true);
	}
	
	/**
	 * No aplica ning�n tipo de estrategia si phi_m = 0 --> cuando se realice log(phi), la entropia dar� -infinito
	 * @param u
	 * @param dim
	 * @param r
	 * @param rNormalized
	 * @return
	 */
	public static float computeF(float[] u, int dim, float r, boolean rNormalized){
		float[] phi = new float[2];
		
		// Desnormalizamos el valor de r
		if (rNormalized)
			r = r * GenericMath.std(u);
		
		for (int m=dim; m < dim+2; m++){
			phi[m-dim] =  getPhi(u, m, r);			
		}
		
		return  (phi[0] - phi[1]);
	}
	
	private static float getPhi(float[] u, int m, float r){		
		// setting up data matrix
//		float[] x = new float[(u.length - m + 1) * m];
		float[][] x = new float[m][u.length - m + 1];
		
		for (int i = 0; i < m; i++){
//			 copyArrayIntoArray(x, i, u, i, u.length - m + i);
			Utils.copyArrayIntoMatrix(x, i, u, i,u.length - m + i + 1);
		}			
		
		// counting similar patterns using distance calculation
		float[] cM = new float[u.length - m + 1];
		for (int i = 0; i < u.length - m + 1; i++ ) {
			cM[i] = getCmi(x, m, u.length - m + 1, i, r) / (u.length - m + 1);
		}
		
		GenericMath.logF(cM);
		return GenericMath.sum(cM) / (u.length - m + 1);
	}
	
	private static float getCmi(float[][] x, int nRows, int nColuns, int iColumn, float r){
		int cmi=0;
		for (int j = 0; j < nColuns; j++) {
			// El siguiente bloque equivale a:
			// d=max(x[:][i] - x[:][column]);
			float d = 0;
			for (int row = 0; row < nRows; row++) {
				float aDiff = Math.abs(x[row][iColumn] - x[row][j]);
				if (aDiff > d) {
					d = aDiff;
				}
			}
			
			if (d <= r)
				cmi++;
		}	
		System.out.println(cmi);
		return cmi;
	}
}
