package com.interaxon.test.libmuse;

import android.util.Log;

import java.util.ArrayList;
import java.util.Queue;

/**
 * Created by Martin on 20/02/2016.
 */
public class Signal {
    ArrayList<Double> signal;
    private int FREQUENCY = 220; //Hz
    private int TIME = 24; //seg
    private int MAX_DATA = FREQUENCY * TIME;
    private String SEPARATOR = "\t";

    Signal() {
        signal = new ArrayList<>();
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
