package com.interaxon.test.libmuse;

import android.content.Context;

/**
 * Created by Martin on 20/02/2016.
 */
public class State {
    public String STATUS_TEXT_NORMAL = "Normal";
    public String STATUS_TEXT_ATTACK = "Seizure";

    public String CURRENT_STATE = "Normal";

    private Context context;
    private boolean update;
    private SignalAnalyzer sanalyzer;

    private ServerNotifier snotifier;

    State(Context c) {
        context = c;
        sanalyzer = new SignalAnalyzer();
    }

    int getCurrentStateColor() {
        if (CURRENT_STATE == STATUS_TEXT_ATTACK) {
            return context.getResources().getColor(R.color.red);
        }
        else if (CURRENT_STATE == STATUS_TEXT_NORMAL) {
            return context.getResources().getColor(R.color.green);
        }
        return context.getResources().getColor(R.color.green);
    }
    String getCurrentStateName() {
        return CURRENT_STATE;
    }

    void updateState(Signal signal) {
        if (signal != null) {
            sanalyzer.analyze(signal);
            if (Math.random() < 0.01) {
                if (CURRENT_STATE == STATUS_TEXT_ATTACK) {
                    CURRENT_STATE = STATUS_TEXT_NORMAL;
                }
                else if (CURRENT_STATE == STATUS_TEXT_NORMAL) {
                    CURRENT_STATE = STATUS_TEXT_ATTACK;
                    //Attack Starts
                    //Get actual time

                    //Prepare JSON

                    //Send to server
                }
            }
        }
    }

}
