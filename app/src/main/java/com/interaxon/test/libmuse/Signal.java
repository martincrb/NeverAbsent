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
    private float ANALYZE_TIME = 2.0f; // seg
    private int WAIT_ELEMS = Math.round(FREQUENCY * ANALYZE_TIME);
    private String SEPARATOR = "\t";
    private int current_counter;
    boolean analyzable;

    Signal() {
        signal = new ArrayList<>();
        current_counter = 0;
        analyzable = false;
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
        ++current_counter;
        if (signal.size() > MAX_DATA) {
            Log.d("Update Signal", "Valid range");
            signal.remove(0);
        }
        if (current_counter >= WAIT_ELEMS) {
            current_counter = 0;
            analyzable = true;
        }
        if (current_counter == 1) {
            analyzable = false;
        }
    }

    boolean isAnalyzable() {
        if (analyzable) {
            analyzable = false;
            return true;
        }
        else {
            analyzable = false;
            return false;
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
