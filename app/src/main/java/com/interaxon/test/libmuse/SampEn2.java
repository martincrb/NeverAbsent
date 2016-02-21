package com.interaxon.test.libmuse;



public final class SampEn2 {

	public static float computeF(float[] u) {
		return computeF(u, 2, 0.15f, true);
	}

	public static float computeF(float[] u, int dim, float r, boolean rNormalized) {
		// setting up data matrix
		float[] x = new float[(dim + 1) * (u.length - dim)];
		for (int i = 0; i < dim + 1; i++) {
			// copyArrayIntoArray(x, i, u, i, u.length - dim + i - 1);
			copyArrayIntoArray(x, i * (u.length - dim), u, i, u.length - dim + i - 1 + 1);
		}

		int c1 = counter(x, u.length - dim, dim, r);
		int c2 = counter(x, u.length - dim, dim + 1, r);
		
		if (c1 == 0 && c2 == 0)
			return Float.NaN;
		else if (c1 != 0 && c2 == 0)
			return (c1 > 0) ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
		
		return (float) Math.log(c1 / c2);
	}

	private static int counter(float[] data, int row, int m, float r) {
		// Copia de una persona de mathworks y revisado por m�.
		int count = 0, i, j, ii;
		float dif, s;

		for (i = 0; i < row - m; i++) {
			for (ii = i + 1; ii < row; ii++) {
				s = 0;
				for (j = 0; j < m; j++) {
					dif = data[j * row + i] - data[j * row + ii];
					if (dif < 0)
						dif = -dif;
					if (dif > s)
						s = dif;
				}
				if (s < r)
					count++;
			}
		}
		return count * 2 / (row * row);
	}

	/**
	 * 
	 * @param matrix
	 * @param nRow
	 * @param array
	 * @param from
	 *            Numero del �ndice desde el cual copiar, inclusivo
	 * @param to
	 *            Numero del �ndice hasta el cual copiar, exclusivo
	 */
	private static void copyArrayIntoMatrix(float[][] matrix, int nRow, float[] array, int from, int to) {
		// for (int i = nRow * array.length; i < (nRow + 1) * array.length; i++)
		for (int i = from, j = 0; i < to - from; i++, j++)
			matrix[nRow][j] = array[i];
	}

	/**
	 * 
	 * @param array1
	 * @param from1
	 * @param array2
	 * @param from2
	 *            Numero del �ndice desde el cual copiar, inclusivo
	 * @param to2
	 *            Numero del �ndice hasta el cual copiar, exclusivo
	 */
	private static void copyArrayIntoArray(float[] array1, int from1, float[] array2, int from2, int to2) {
		for (int i = from2, j = from1; i < to2 - from2; i++, j++)
			array1[j] = array2[i];
	}

	private void sampen(double[] y, int M, double r, int n) {
		double[] p = new double[M + 1];
		double[] e;
		long[] run = new long[n];
		long[] lastrun = new long[n];
		long N;
		double[] A = new double[M + 1];
		double[] B = new double[M + 1];
		int M1, j, nj, jj, m;
		int i;
		double y1;

		M++;

		/* start running */
		for (i = 0; i < n - 1; i++) {
			nj = n - i - 1;
			y1 = y[i];
			for (jj = 0; jj < nj; jj++) {
				j = jj + i + 1;
				if (((y[j] - y1) < r) && ((y1 - y[j]) < r)) {
					run[jj] = lastrun[jj] + 1;
					M1 = (int) (M < run[jj] ? M : run[jj]);
					for (m = 0; m < M1; m++) {
						A[m]++;
						if (j < n - 1)
							B[m]++;
					}
				} else
					run[jj] = 0;
			} /* for jj */
			for (j = 0; j < nj; j++)
				lastrun[j] = run[j];
		} /* for i */

		N = (long) (n * (n - 1) / 2);
		p[0] = A[0] / N;
		// printf("SampEn(0,%g,%d) = %lf\n", r, n, -Math.log(p[0]));

		for (m = 1; m < M; m++) {
			p[m] = A[m] / B[m - 1];
			// if (p[m] == 0)
			// printf("No matches! SampEn((%d,%g,%d) = Inf!\n", m, r, n);
			// else
			// printf("SampEn(%d,%g,%d) = %lf\n", m, r, n, -log(p[m]));
		}
	}
}