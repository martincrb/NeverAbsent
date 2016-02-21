package com.interaxon.test.libmuse;

import android.support.v4.util.CircularArray;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Martin on 20/02/2016.
 */
public class Signal {
    private LinkedList<Float> signal;
    private int FREQUENCY = 220; //Hz
    private float TIME = 23.6f; //seg
    private int MAX_DATA = Math.round(FREQUENCY * TIME);
    private float ANALYZE_TIME = 23.6f; // seg
    private int WAIT_ELEMS = Math.round(FREQUENCY * ANALYZE_TIME);
    private String SEPARATOR = "\t";
    private int current_counter;
    boolean analyzable;

    Signal() {
        signal = new LinkedList<Float>();
        current_counter = 0;
        analyzable = false;
    }

    Signal(Signal s) {
        this.signal = new LinkedList<Float>(s.signal);
        this.FREQUENCY = FREQUENCY;
        this.TIME = TIME;
        this.MAX_DATA = MAX_DATA;
        this.ANALYZE_TIME = ANALYZE_TIME;
        this.WAIT_ELEMS = WAIT_ELEMS;
        this.SEPARATOR = SEPARATOR;
        this.current_counter = current_counter;
        this.analyzable = analyzable;
    }

    float[] toFloatArray() {
        float[] f = new float[signal.size()];
        int i=0;
        for (float data : signal) {
            f[i] = data; i++;
        }
        return f;
    }
    void update(Double value) {
        signal.add(value.floatValue());
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
        for (Float v : signal) {
            str += v.toString() + SEPARATOR;
        }
        return str;
    }

}
