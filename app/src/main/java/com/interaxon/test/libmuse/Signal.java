package com.interaxon.test.libmuse;

import android.util.Log;

import java.util.ArrayList;
import java.util.Queue;

/**
 * Created by Martin on 20/02/2016.
 */
public class Signal {
    private ArrayList<Double> signal;
    private int FREQUENCY = 220; //Hz
    private float TIME = 23.6f; //seg
    private int MAX_DATA = Math.round(FREQUENCY * TIME);
    private String SEPARATOR = "\t";

    Signal() {
        signal = new ArrayList<>();
    }

    float[] toFloatArray() {
        float[] f = new float[signal.size()];
        for (int i = 0; i < f.length; ++i) {
             f[i] = signal.get(i).floatValue();
        }
        return f;
    }
    void update(Double value) {
        signal.add(value);
        if (signal.size() > MAX_DATA) {
            Log.d("Update Signal", "Valid range");
            signal.remove(0);
        }
    }

    String getString() {
        String str = "";
        for (Double v : signal) {
            str += v.toString() + SEPARATOR;
        }
        return str;
    }

}
