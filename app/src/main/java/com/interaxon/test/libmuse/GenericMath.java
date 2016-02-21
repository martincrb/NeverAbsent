package com.interaxon.test.libmuse;

import java.util.Arrays;

// OPTIMIZATION Para evitar crear objetos y vectores innecesarios, todas las funciones de 
// esta clase NO deberian devolver nada. Si es necesario, se le pasa el vector donde se tiene 
// que guardar el resultado. De esta forma, en los lugares que se llama, se puede crear una
// variable auxiliar solo una vez y que se vaya reutilizando en cada llamada.
// Traer�a mucho trabajo cambiar todo el c�digo.
public class GenericMath {
	// Constantes para el m�todo find
	public static final boolean EQUAL = true;
	public static final boolean FIRST = true;
	public static final boolean GREATER = true;
	public static final boolean LAST = false;
	public static final boolean LOWER = false;
	public static final boolean NOTHING = false;

	// Constantes para el m�todo FindSide
	public static final int BOTHSIDES = 0;
	public static final int POSITIVESIDES = 1;
	public static final int NEGATIVESIDES = 2;
	// Constantes para el m�todo findMaxPosition
	public static final boolean ABSOLUTEPOSITION = true;
	public static final boolean RELATIVEPOSITION = true;

	/**
	 * Calcula el valor absoluto de los elementos del vector
	 * 
	 * @param x
	 *            Vector a aplicar el valor absoluto.
	 */
	public static void abs(float[] x) {
		int length = x.length;
		for (int i = 0; i < length; i++) {
			x[i] = Math.abs(x[i]);
		}
	}

	/**
	 * Calcula la diferencia entre cada muestra Equivale a la funci�n de matlab
	 * de diff
	 * 
	 * @param x
	 *            Vector de tama�o N
	 * @return Vector de tama�o N-1 con el valor de la diferencia.
	 */
	public static int[] diff(int[] x) {
		int length = x.length;
		int[] diff = new int[length - 1];

		for (int i = 1; i < length; i++) {
			diff[i - 1] = x[i] - x[i - 1];
		}

		return diff;
	}

	/**
	 * Calcula la diferencia entre cada muestra Equivale a la funci�n de matlab
	 * de diff
	 * 
	 * @param x
	 *            Vector de tama�o N
	 * @return Vector de tama�o N-1 con el valor de la diferencia.
	 */
	public static float[] diff(float[] x) {
		return GenericMath.diff(x, 0, x.length - 1);
	}

	/**
	 * Calcula la diferencia entre cada muestra Equivale a la funci�n de matlab
	 * de diff
	 * 
	 * @param x
	 *            Vector de tama�o N
	 * @param fromPos
	 *            A partir de que posici�n del vector (incluida) se utiliza
	 * @param toPos
	 *            Hasta que posici�n del vector (incluida) se utiliza
	 * @return Vector de tama�o N-1 -(toPos-fromPos) con el valor de la
	 *         diferencia.
	 */
	public static float[] diff(float[] x, int fromPos, int toPos) {
		int length = toPos - fromPos + 1;
		float[] diff = new float[length - 1];

		for (int i = fromPos + 1; i <= toPos; i++) {
			diff[i - 1 - fromPos] = x[i] - x[i - 1];
		}

		return diff;
	}

	/**
	 * Calcula la diferencia entre cada muestra Equivale a la funci�n de matlab
	 * de diff
	 * 
	 * @param x
	 *            Vector de tama�o N
	 * @return Vector de tama�o N-1 con el valor de la diferencia.
	 */
	public static double[] diff(double[] x) {
		int length = x.length;
		double[] diff = new double[length - 1];

		for (int i = 1; i < length; i++) {
			diff[i - 1] = x[i] - x[i - 1];
		}

		return diff;
	}

	/**
	 * Equivale a la funci�n de matlab find utilizandola de la forma:
	 * find(vector {>,>=,<,<=} threshold,'first/last',numero de indices a
	 * retornar). Es decir, devuelve aquellas posiciones del vector donde se
	 * cumple la condici�n especficada.
	 * 
	 * @param x
	 *            Vector de datos
	 * @param threshold
	 *            Umbral de la comparaci�n
	 * @param amount
	 *            Cantidad de resultados que se quieren. Si no hay suficientes,
	 *            se rellenan los que falta con el valor -1
	 * @param whereStart
	 *            Utils.FIRST -> devuelve las primeras amount condiciones
	 *            cumplidas. Utils.LAST -> devuelve las amount �ltimas
	 *            condiciones cumplidas.
	 * @param greaterOrLower
	 *            Utils.GREATER -> equivale al signo > . Utils.LOWER -> equivale
	 *            al signo <
	 * @param equalOrNothing
	 *            Utils.EQUAL -> equivale al signo == en una comparaci�n.
	 *            Utils.NOTHING -> no se hace nada
	 * @return Devuelve un vector de tama�o amount con las posiciones del vector
	 *         que cumplen con las condiciones especificadas. Si no se consigue
	 *         llenar todo el vector de esta forma, se rellenan las posiciones
	 *         restantes con -1
	 */
	public static float[] find(float[] x, boolean greaterOrLower,
			boolean equalOrNothing, float threshold, int amount,
			boolean whereStart) {
		return find(x, 0, 1, x.length - 1, greaterOrLower, equalOrNothing,
				threshold, amount, whereStart);
	}

	/**
	 * Equivale a la funci�n de matlab find utilizandola de la forma:
	 * find(vector(fromPos:step:toPos) {>,>=,<,<=} threshold,numero de indices a
	 * retornar,'first/last'). Es decir, devuelve aquellas posiciones del vector
	 * donde se cumple la condici�n especficada.
	 * 
	 * @param x
	 *            Vector de datos
	 * @param fromPos
	 *            Posici�n de inicio del vector donde se busca.
	 * @param toPos
	 *            Posici�n final (incluida) del vector donde se busca.
	 * @param step
	 *            Posici�n consecutiva que se consulta en la busqueda. Equivale
	 *            en matlab a vector(fromPos:step:toPos)
	 * @param threshold
	 *            Umbral de la comparaci�n
	 * @param amount
	 *            Cantidad de resultados que se quieren. Si no hay suficientes,
	 *            se rellenan los que falta con el valor -1
	 * @param whereStart
	 *            Utils.FIRST -> devuelve las primeras amount condiciones
	 *            cumplidas. Utils.LAST -> devuelve las amount �ltimas
	 *            condiciones cumplidas.
	 * @param greaterOrLower
	 *            Utils.GREATER -> equivale al signo > . Utils.LOWER -> equivale
	 *            al signo <
	 * @param equalOrNothing
	 *            Utils.EQUAL -> equivale al signo == en una comparaci�n.
	 *            Utils.NOTHING -> no se hace nada
	 * @return Devuelve un vector de tama�o amount con las posiciones del vector
	 *         que cumplen con las condiciones especificadas. Si no se consigue
	 *         llenar todo el vector de esta forma, se rellenan las posiciones
	 *         restantes con -1. En el caso de Utils.FIRST son las �ltimas
	 *         posiciones y en caso de Utils.LAST son las primeras.
	 */
	public static float[] find(float[] x, int fromPos, int step, int toPos,
			boolean greaterOrLower, boolean equalOrNothing, float threshold,
			int amount, boolean whereStart) {
		// OPTIMIZATION No se si dividir el m�todo en todas las posibes
		// funciones. Si lo separa solo necesito hacer una comparaci�n, sino
		// ahora hago 4.
		// En el ordenador el sistema es suficientemente r�pido.
		float[] result = new float[amount];

		if (x.length == 0) {
			// Si x es un array vacio: rellenamos el vector se resultados con -1
			// y se acab�.
			for (int i = 0; i < amount; i++) {
				result[i] = -1;
			}
			return result;
		}

		int i = (whereStart == GenericMath.FIRST) ? fromPos : toPos;
		int j = (whereStart == GenericMath.FIRST) ? 0 : amount - 1;
		boolean found;
		while (true) {
			found = false;
			if (greaterOrLower == GenericMath.GREATER) {
				if (x[i] > threshold) {
					result[j] = i;
					found = true;
				}
			} else {
				if (x[i] < threshold) {
					result[j] = i;
					found = true;
				}
			}

			if (equalOrNothing == GenericMath.EQUAL && x[i] == threshold) {
				result[j] = i;
				found = true;
			}

			if (found) {
				if (whereStart == GenericMath.FIRST) {
					j++;
					// �Hemos alcanzado la cantidad de resultados pedidos?
					if (j >= amount)
						break;
				} else {
					j--;
					if (j < 0)
						break;
				}
			}

			if (whereStart == GenericMath.FIRST && i <= toPos) {
				i = i + step;
				// Miramos si hemos llegado al final del recorrido:
				if (i > toPos)
					break;
			} else {
				i = i - step;
				// Miramos si hemos llegado al final del recorrido:
				if (i < fromPos)
					break;
			}
		}

		// Rellenamos el vector de resultados con -1 si no hemos encontrado
		// suficientes resultados.
		if (whereStart == GenericMath.FIRST) {
			for (i = j; i < amount; i++) {
				result[i] = -1;
			}
		} else {
			for (i = j; i >= 0; i--) {
				result[i] = -1;
			}
		}
		return result;
	}

	/**
	 * Busca los flancos positivos o negativos del se�al segun se le indique.
	 * Implementa las funciones de matlab: Implementa los m�todos de matlab:
	 * Ambos flancos: aux = sign(x(fromPos:toPos) - threshold); aux(aux+1)/2;
	 * flancos = find(abs(diff(aux))>0); Flancos positivos: aux =
	 * sign(x(fromPos:toPos) - threshold); aux(aux+1)/2; flancos =
	 * find(diff(aux)>0); Flancos negativos: aux = sign(x(fromPos:toPos) -
	 * threshold); aux(aux+1)/2; flancos = find(diff(aux)<0);
	 * 
	 * @param x
	 *            Vector de datos
	 * @param whichSide
	 *            Indica si se busca los flancos positivos,negativos o ambos del
	 *            se�al. Se le pasa como par�metro la constante
	 *            Utils.POSITIVESIDES, Utils.NEGATIVESIDES o Utils.BOTHSIDES
	 * @param threshold
	 *            Umbral para indicar cuando se considera que es un flanco del
	 *            se�al.
	 * @param amount
	 *            Cantidad de flancos (positivos o negativos) que se quieren. Si
	 *            no hay suficientes se rellenan los que falta con el valor -1
	 * @param whereStart
	 *            Utils.FIRST -> devuelve las primeras amount condiciones
	 *            cumplidas. Utils.LASR -> devuelve las amount �ltimas
	 *            condiciones cumplidas.
	 * @return Devuelve el resultado.
	 */
	public static float[] findSides(float[] x, int whichSide, float threshold,
			int amount, boolean whereStart) {
		return GenericMath.findSides(x, 0, x.length - 1, whichSide, threshold,
				amount, whereStart);
	}

	/**
	 * Busca los flancos positivos o negativos del se�al segun se le indique.
	 * Implementa las funciones de matlab: Implementa los m�todos de matlab:
	 * Ambos flancos: aux = sign(x(fromPos:toPos) - threshold); aux(aux+1)/2;
	 * flancos = find(abs(diff(aux))>0); Flancos positivos: aux =
	 * sign(x(fromPos:toPos) - threshold); aux(aux+1)/2; flancos =
	 * find(diff(aux)>0); Flancos negativos: aux = sign(x(fromPos:toPos) -
	 * threshold); aux(aux+1)/2; flancos = find(diff(aux)<0);
	 * 
	 * @param x
	 *            Vector de datos
	 * @param fromPos
	 *            Desde que posici�n del vector (incluida) se empieza a buscar
	 * @param toPos
	 *            Hasta que posici�n del vector (incluida) se busca.
	 * @param whichSide
	 *            Indica si se busca los flancos positivos,negativos o ambos del
	 *            se�al. Se le pasa como par�metro la constante
	 *            Utils.POSITIVESIDES, Utils.NEGATIVESIDES o Utils.BOTHSIDES
	 * @param threshold
	 *            Umbral para indicar cuando se considera que es un flanco del
	 *            se�al.
	 * @param amount
	 *            Cantidad de flancos (positivos o negativos) que se quieren. Si
	 *            no hay suficientes se rellenan los que falta con el valor -1
	 * @param whereStart
	 *            Utils.FIRST -> devuelve las primeras amount condiciones
	 *            cumplidas. Utils.LAST -> devuelve las amount �ltimas
	 *            condiciones cumplidas.
	 * @return Devuelve el resultado.
	 */
	public static float[] findSides(float[] x, int fromPos, int toPos,
			int whichSide, float threshold, int amount, boolean whereStart) {
		// instruciones equivalentes en matlab:
		// aux=sign(derivada(k:k+frameLength-1)/stdReference-diffPeak);
		// aux=(aux+1)/2;
		// flancosPositivos2= find(abs(diff(aux))>0);

		if (x.length == 0) {
			// Si x es un array vacio: rellenamos el vector de resultados con -1
			// y se acab�.
			float[] result = new float[amount];
			for (int i = 0; i < amount; i++) {
				result[i] = -1;
			}
			return result;
		}

		float[] result = new float[toPos - fromPos + 1];

		for (int i = fromPos; i <= toPos; i++) {
			if (x[i] > threshold) {
				result[i - fromPos] = 1;
			}
		}

		result = GenericMath.diff(result);
		if (whichSide == BOTHSIDES) {
			GenericMath.abs(result);
			return GenericMath.find(result, GenericMath.GREATER,
					GenericMath.NOTHING, 0, amount, whereStart);
		} else if (whichSide == POSITIVESIDES) {
			return GenericMath.find(result, GenericMath.GREATER,
					GenericMath.NOTHING, 0, amount, whereStart);
		} else if (whichSide == NEGATIVESIDES) {
			return GenericMath.find(result, GenericMath.LOWER,
					GenericMath.NOTHING, 0, amount, whereStart);
		} else {
			System.err.print("Error: tipo de flanco desconocido");
		}

		// Solo para que eclipse no se queje
		return GenericMath.find(result, GenericMath.GREATER,
				GenericMath.NOTHING, 0, amount, whereStart);
	}

	/**
	 * Encuentra la posici�n del m�ximo absoluto en el vector dado
	 * 
	 * @param array
	 *            Vector donde buscar el m�ximo
	 * @param fromIndex
	 *            Apartir de que indice en el vector busca el m�ximo
	 * @param toIndex
	 *            �tima posici�n donde ya se busca el m�ximo.
	 * @param relativePosition
	 *            Utils.RELATIVEPOSITION -> la posici�n se devuelve respecto a
	 *            la posici�n indicada por fromIndex. Utils.ABSOLUTEPOSITION ->
	 *            lse devuelve la posicion absoluta dentro del vector.
	 * @return Devuelve la posici�n del m�ximo absoluto dentro del vector
	 */
	public static int findAbsMaxPosition(float[] array, int fromIndex,
			int toIndex, boolean relativePosition) {
		float max = Math.abs(array[fromIndex]);
		int maxPos = fromIndex;
		float aux;

		for (int i = fromIndex + 1; i <= toIndex; i++) {
			aux = Math.abs(array[i]);
			if (aux > max) {
				max = aux;
				maxPos = i;
			}
		}

		if (relativePosition) {
			maxPos = maxPos - fromIndex;
		}
		return maxPos;
	}

	/**
	 * Encuentra la posici�n del m�ximo en el vector dado
	 * 
	 * @param array
	 *            Vector donde buscar el m�ximo
	 * @return Devuelve la posici�n del m�ximo dentro del vector
	 */
	public static int findMaxPosition(float[] array) {
		return GenericMath.findMaxPosition(array, 0, array.length - 1, false);
	}

	/**
	 * Encuentra la posici�n del m�ximo en el vector dado
	 * 
	 * @param array
	 *            Vector donde buscar el m�ximo
	 * @param fromIndex
	 *            Apartir de que indice en el vector busca el m�ximo
	 * @param toIndex
	 *            �tima posici�n donde ya se busca el m�ximo.
	 * @param relativePosition
	 *            Utils.RELATIVEPOSITION -> la posici�n se devuelve respecto a
	 *            la posici�n indicada por fromIndex. Utils.ABSOLUTEPOSITION ->
	 *            lse devuelve la posicion absoluta dentro del vector.
	 * @return Devuelve la posici�n del m�ximo dentro del vector
	 */
	public static int findMaxPosition(float[] array, int fromIndex,
			int toIndex, boolean relativePosition) {
		float max = array[fromIndex];
		int maxPos = fromIndex;

		for (int i = fromIndex + 1; i <= toIndex; i++) {
			if (array[i] > max) {
				max = array[i];
				maxPos = i;
			}
		}

		if (relativePosition) {
			maxPos = maxPos - fromIndex;
		}
		return maxPos;
	}
	
	public static void logF(float[] array) {
		logF(array, 0, 1, array.length - 1);
	}

	/**
	 * Calcula el logaritmo natural de cada posici�n. Equivale a la funci�n de
	 * matlab: array = log(array(fromPos:step:toPos));
	 * 
	 * @param array
	 *            Vector a calcular la logaritmo
	 * @param fromPos
	 *            A partir de que posici�n en el vector (incluida) se utiliza
	 * @param step
	 *            Posiciones que se saltan
	 * @param toPos
	 *            Hasta que posici�n en el vector 8incluida) se utiliza
	 */
	public static void logF(float[] array, int fromPos, int step, int toPos) {
		for (int i = fromPos; i <= toPos; i = i + step) {
			array[i] = (float) Math.log(array[i]);
		}
	}
	
	public static float maxF(float[] x){
		float max = x[0];		
		for(int i = 1; i < x.length; i++)
			if (max < x[i])
				max = x[i];
		return max;
	}
	
	public static void maxFF(float[] x, float[] maxValue, float[] posValue ){
		float max = x[0];		
		for(int i = 1; i < x.length; i++)
			if (max < x[i])
				max = x[i];
//		return max;
	}

	/**
	 * Calcula la media de los valores en el array
	 * 
	 * @param array
	 *            Vector a calcular la media
	 * @return Media del vector
	 */
	public static float mean(float[] array) {
		return mean(array, 0, 1, array.length - 1);
	}

	/**
	 * Calcula la media de los valores en el array Equivale a la funci�n de
	 * matlab: mean(array(fromPos:step:toPos));
	 * 
	 * @param array
	 *            Vector a calcular la media
	 * @param fromPos
	 *            A partir de que posici�n en el vector (incluida) se utiliza
	 * @param step
	 *            Posiciones que se saltan
	 * @param toPos
	 *            Hasta que posici�n en el vector 8incluida) se utiliza
	 * @return Devuelve la media calculada.
	 */
	public static float mean(float[] array, int fromPos, int step, int toPos) {
		float mean = 0;
		for (int i = fromPos; i <= toPos; i = i + step) {
			mean += array[i];
		}
		int length = ((toPos - fromPos) / step) + 1;
		return mean / length;
	}
	
	/**
	 * Calcula la mediana del vector x. En caso que la longitud de x sea par, se 
	 * calcula entonces el valor medio de los dos valores centrales. En caso que el tipo
	 * del vector x no permita decimales, se hace un floor a su valor m�s pr�ximo
	 * 
	 * @param x
	 * @return Valor de la mediana
	 */
	public static float medianF(float[] x) {
		return medianF(x,0,x.length-1);
	}
	
	public static float medianF(float[] x, int fromPos, int toPos){
		if (x == null)
			throw new IllegalArgumentException("x es null");
		
		if (x.length == 0)
			throw new IllegalArgumentException("x est� vacio"); 
		
		if (fromPos < 0)
			throw new IllegalArgumentException("fromPos es negativo"); 

		if (toPos >= x.length)
			throw new IllegalArgumentException("toPos excede el tama�o del vector"); 
		
		if (fromPos > toPos)
			throw new IllegalArgumentException("fromPos es mayor que toPos"); 

		
		float[] xSorted = Arrays.copyOfRange(x, fromPos, toPos+1);
		Arrays.sort(xSorted);
		
		if (xSorted.length % 2 == 0) {
			// El vector tiene longitud par
			float x1 = xSorted[(int) (xSorted.length / 2) - 1];
			float x2 = xSorted[(int) (xSorted.length / 2)];			
			return (x2 + x1) / 2f; 
		} else
			return xSorted[(int) (xSorted.length / 2)];		
	}

	/**
	 * Multiplica un vector por una constantw
	 * 
	 * @param array
	 *            Vector a multiplicar
	 * @param scale
	 *            Constante multiplicativa
	 * @return Devuelve el nuevo vector
	 */
	public static float[] multiply(float[] array, float scale) {
		int length = array.length;
		float[] result = new float[length];
		for (int i = 0; i < length; i++) {
			result[i] = array[i] * scale;
		}
		return result;
	}
	
	/**
	 * Multiplica un vector por una constantw
	 * 
	 * @param array
	 *            Vector a multiplicar
	 * @param scale
	 *            Constante multiplicativa
	 * @param result Array donde se guarda el resultado. Puede ser el mismo que el par�metro array
	 */
	public static void multiply(float[] array, float scale, float[] result) {
		if (array == null)
			throw new NullPointerException("El argumento array es null");
		
		if (result == null)
			throw new NullPointerException("El argumento result es null");
		
		if (array.length != result.length)
			throw new IllegalArgumentException("La longitud de los array no coincide");
		
		int length = array.length;
		for (int i = 0; i < length; i++) {
			result[i] = array[i] * scale;
		}
	}

	/**
	 * Calcula el percentil indicado
	 * 
	 * @param array
	 *            Array donde calcuar el percentil. No necesita que est�
	 *            ordenado.
	 * @param pctl
	 *            Percentil requerido. Debe estar entre 0 y 100.
	 * @return Valor del percentil indicado.
	 */
	public static float percentil(float[] array, int pctl) {
		int k, size = array.length;
		float result;
		float[] tmpArray = new float[size];
		for (k = 0; k < size; k++)
			tmpArray[k] = array[k]; // Make a temporal copy of the array
		// to sort
		sort(tmpArray); // Sort array
		k = (int) (pctl * size / 100);
		result = tmpArray[k];

		return result; // Return amplitude value that has %pctl of array samples
						// below
	}

	/**
	 * Eleva al cuadrado los campos del vector
	 * 
	 * @param array
	 */
	public static void pow2Vector(float[] array) {
		int length = array.length;
		for (int i = 0; i < length; i++) {
			array[i] *= array[i];
		}
	}

	/**
	 * Escala los valores del vector por la constante dada
	 * 
	 * @param array
	 *            Vector a escalar
	 * @param scale
	 *            Valor del escalado
	 */
	public static void scale(double[] array, double scale) {
		int length = array.length;
		for (int i = 0; i < length; i++) {
			array[i] *= scale;
		}
	}

	/**
	 * Escala los valores del vector por la constante dada
	 * 
	 * @param array
	 *            Vector a escalar
	 * @param scale
	 *            Valor del escalado
	 */
	public static void scale(float[] array, float scale) {
		int length = array.length;
		for (int i = 0; i < length; i++) {
			array[i] *= scale;
		}
	}

	/**
	 * Es un set pero en un vector
	 * 
	 * @param value
	 *            Valor
	 * @param x
	 *            Vector
	 * @param fromPos
	 *            Desde que posici�n se iguala el campo del vector a value.
	 * @param toPos
	 *            Hasta que posici�n (incluida) se iguala el campo del vector a
	 *            value.
	 */
	public static void set(float value, float[] x, int fromPos, int toPos) {
		for (int i = fromPos; i <= toPos; i++) {
			x[i] = value;
		}
	}

	/**
	 * Ordena el array de menor a mayor que se le pasa como par�metro.
	 * 
	 * @param array
	 *            Array a ordenar.
	 */
	public static void sort(float[] array) {
		int i, j;
		float index;
		int array_size = array.length;
		for (i = 1; i < array_size; ++i) {
			index = array[i];
			for (j = i; j > 0 && array[j - 1] > index; j--)
				array[j] = array[j - 1];

			array[j] = index;
		}
	}

	/**
	 * std(X) = sqrt( 1/(N-1) * sum((Xi - mean(X))^2) ) == sqrt(var(X))
	 * 
	 * @param x
	 * @return Devuelve la desviaci�n estandar del vector de datos x
	 */
	public static float std(float[] x) {
		return (float) Math.sqrt(GenericMath.var(x));
	}

	/**
	 * Calcula la desviaci�n t�pica insesgada y normalizada del vector. std(X) =
	 * sqrt( 1/(N-1) * sum((Xi - mean(X))^2) ) == sqrt(var(X))
	 * 
	 * @param x
	 *            Vector
	 * @param fromPos
	 *            Posici�n de inicio del vector donde se busca.
	 * @param toPos
	 *            Posici�n final (incluida) del vector donde se busca.
	 * @param step
	 *            Posici�n consecutiva que se consulta en la busqueda. Equivale
	 *            en matlab a vector(fromPos:step:toPos)
	 * @return Devuelve la desviaci�n t�pica calculada.
	 */
	public static float std(float[] x, int fromPos, int step, int toPos) {
		return (float) Math.sqrt(GenericMath.var(x, fromPos, step, toPos));
	}

	/**
	 * Aplica la operaci�n array1 = array1 - array2
	 * Resta array2 a array1. El resultado se guarda en array1. Los dos vectores
	 * DEBEN ser de la misma longitud
	 * 
	 * @param array1
	 *            Vector restado
	 * @param array2
	 *            Vector que resta
	 */
	public static void substraction(float[] array1, float[] array2) {
		int length = array1.length;
		for (int i = 0; i < length; i++) {
			array1[i] -= array2[i];
		}
	}

	/**
	 * Suma todos los campos del vector. Equivale a la instrucci�n de matlab
	 * sum()
	 * 
	 * @param array
	 *            Vector a sumar
	 * @return Devuelve la suma de todos los campos
	 */
	public static float sum(float[] array) {
		return GenericMath.sum(array, 0, array.length - 1);
	}

	/**
	 * Suma todos los campos del vector. Equivale a la instrucci�n de matlab
	 * sum(x(fromPos:toPos))
	 * 
	 * @param x
	 *            Vector a sumar
	 * @param fromPos
	 *            Desde que posici�n se empieza a sumar
	 * @param toPos
	 *            Hasta que posici�n (incluida) se suma
	 * @return Devuelve la suma de todos los campos
	 */
	public static float sum(float[] x, int fromPos, int toPos) {
		float sum = 0;
		for (int i = fromPos; i <= toPos; i++) {
			sum += x[i];
		}
		return sum;
	}

	/**
	 * Suma todos los campos del vector. Equivale a la instrucci�n de matlab
	 * sum()
	 * 
	 * @param array
	 *            Vector a sumar
	 * @return Devuelve la suma de todos los campos
	 */
	public static int sum(int[] array) {
		int sum = 0;
		int length = array.length;
		for (int i = 0; i < length; i++) {
			sum += array[i];
		}
		return sum;
	}

	/**
	 * Suma una constante a array1
	 * 
	 * @param array
	 *            Vector sumado
	 * @param constant
	 *            Constante que se suma a todo el vector.
	 */
	public static void sum(float[] array, float constant) {
		int length = array.length;
		for (int i = 0; i < length; i++) {
			array[i] += constant;
		}
	}

	/**
	 * Suma array2 a array1. Los dos vectores DEBEN ser de la misma longitud
	 * 
	 * @param array1
	 *            Vector sumado
	 * @param array2
	 *            Vector que suma
	 */
	public static void sum(float[] array1, float[] array2) {
		int length = array1.length;
		for (int i = 0; i < length; i++) {
			array1[i] += array2[i];
		}
	}

	/**
	 * Calcula la varianza insesgada y normalizada del vector. var(X) = 1/(N-1)
	 * * sum((Xi - mean(X))^2)
	 * 
	 * @param x
	 *            Vector
	 * @return La varianza calculada
	 */
	public static float var(float[] x) {
		return GenericMath.var(x, 0, 1, x.length - 1);
	}

	/**
	 * Calcula la varianza insesgada y normalizada del vector. var(X) = 1/(N-1)
	 * * sum((Xi - mean(X))^2)
	 * 
	 * @param x
	 *            Vector
	 * @param fromPos
	 *            Posici�n de inicio del vector donde se busca.
	 * @param toPos
	 *            Posici�n final (incluida) del vector donde se busca.
	 * @param step
	 *            Posici�n consecutiva que se consulta en la busqueda. Equivale
	 *            en matlab a vector(fromPos:step:toPos)
	 * @return La varianza calculada
	 */
	public static float var(float[] x, int fromPos, int step, int toPos) {
		float mean = GenericMath.mean(x, fromPos, step, toPos);
		float var = 0;
		float tmp;

		for (int i = fromPos; i <= toPos; i = i + step) {
			tmp = x[i] - mean;
			var += tmp * tmp;
		}

		// Normalizamos la varianza:
		int length = ((toPos - fromPos) / step) + 1;
		return var / (length - 1);

	}

	/**
	 * Calcula la autocorrelaci�n NO normalizada de dos se�ales REALES. El
	 * vector devuelto tiene tama�o 2N-1, donde la posici�n N-1 contiene el
	 * valor de la autocorrelaci�n en cero: Rx(0), la posici�n N es Rx(1), N-2
	 * es Rx(-1), ... Funciona igual que la funci�n xcorr de matlab.
	 * 
	 * @param x
	 *            El vector a aplicar la autocorrelaci�n
	 * @return Devuelve el vector de la autocorrelaci�n del se�al
	 */
	public static float[] xcorr(float[] x) {
		// Create float vector with 0 value in all fields
		int rLength = 2 * x.length - 1;
		float[] result = new float[rLength];

		// Rxx(m) = E[x_n * (y_n+m)* ] = E[x_n+m * (y_n)* ]
		// Propiedad: Rx(m) = Rx(-m)*
		int length = x.length;
		for (int m = 0; m < length; m++) {
			float aux = 0;
			for (int i = 0; i + m < length; i++) {
				// Es un 20% m�s r�p�da si se utiliza la instrucci�n comentada
				// pero despu�s se
				// deben de reordenar todos los valores (no est� hecho). Sino la
				// posicion 0 equivale a Rx(0),
				// la pos. 1 == Rx(1),...
				// result[m+length-1] += x[i]*x[i+m];
				aux += x[i] * x[i + m];
				// result[m] += x[i]*x[i+m];
			}
			result[m + length - 1] = aux;
		}

		for (int m = 0; m < length - 1; m++) {
			result[m] = result[rLength - m - 1];
		}
		return result;
	}

}
