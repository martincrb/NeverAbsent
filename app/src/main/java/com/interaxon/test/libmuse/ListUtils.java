package com.interaxon.test.libmuse;

import java.util.Iterator;
import java.util.List;

public class ListUtils {
	public static <T extends Number> float[] toFloatArray(List<T> list){
		// Se utiliza iteradores por si la lista es un linkedList
	    float[] ret = new float[list.size()];
	    Iterator<T> iterator = list.iterator();
	    for (int i = 0; i < ret.length; i++)
	        ret[i] = iterator.next().floatValue();
	    return ret;
	}
}
