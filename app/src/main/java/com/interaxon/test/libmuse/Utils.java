package com.interaxon.test.libmuse;

public class Utils {
	/**
	 * 
	 * @param matrix
	 * @param nRow
	 * @param array
	 * @param from Numero del �ndice desde el cual copiar, inclusivo
	 * @param to Numero del �ndice hasta el cual copiar, exclusivo
	 */
	public static void copyArrayIntoMatrix(float[][] matrix, int nRow, float[] array, int from, int to ){
//		for (int i = nRow * array.length; i < (nRow + 1) * array.length; i++)
		for (int i = from, j = 0; j < to - from; i++, j++)
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
	public static void copyArrayIntoArray(float[] array1, int from1, float[] array2, int from2, int to2 ){		
		for (int i = from2, j = from1; i < to2; i++, j++)
			array1[j] = array2[i];
	}
}
