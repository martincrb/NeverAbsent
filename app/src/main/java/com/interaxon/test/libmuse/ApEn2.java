package com.interaxon.test.libmuse;

import java.util.Arrays;

import com.interaxon.test.libmuse.GenericMath;

public final class ApEn2 {
	
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
		
		for (int m=dim, k=0; m < dim+2; m++,k++){
			float[] cM = new float[u.length - m + 1];
			
			// setting up data matrix
//			float[] x = new float[m * (u.length - m + 1)];
			float[][] x = new float[m][u.length - m + 1];
			for (int i = 0; i < m; m++){
				// copyArrayIntoArray(x, i, u, i, u.length - m + i);
				copyArrayIntoMatrix(x, i, u, i,u.length - m + i);
			}
			
			// counting similar patterns using distance calculation
			for (int i = 0; i < u.length - m + 1; i++ ) {
				for (int j = 0; j < u.length - m + 1; j++) {
					// El siguiente bloque equivale a:
					// d=max(x[:][i] - x[:][column]);
					float d = 0;
					for (int row = 0; row < m; row++) {
						float aDiff = Math.abs(x[row][i] - x[row][j]);
						if (aDiff > d) {
							d = aDiff;
						}
					}
					
					if (d <= r)
						cM[i]++;
				}
			}
			
			GenericMath.logF(cM);
			phi[m-dim] =  GenericMath.sum(cM) / (u.length - m + 1);			
		}
		
		return  (phi[2] - phi[1]);
	}
	
	/**
	 * 
	 * @param matrix
	 * @param nRow
	 * @param array
	 * @param from Numero del �ndice desde el cual copiar, inclusivo
	 * @param to Numero del �ndice hasta el cual copiar, exclusivo
	 */
	private static void copyArrayIntoMatrix(float[][] matrix, int nRow, float[] array, int from, int to ){
//		for (int i = nRow * array.length; i < (nRow + 1) * array.length; i++)
		for (int i = from, j = 0; i < to - from; i++, j++)
			matrix[nRow][j] = array[i];
	}
	
	/**
	 * 
	 * @param array1
	 * @param from1
	 * @param array2
	 * @param from2 Numero del �ndice desde el cual copiar, inclusivo
	 * @param to2 Numero del �ndice hasta el cual copiar, exclusivo
	 */
	private static void copyArrayIntoArray(float[] array1, int from1, float[] array2, int from2, int to2 ){		
		for (int i = from2, j = from1; i < to2 - from2; i++, j++)
			array1[j] = array2[i];
	}
}
