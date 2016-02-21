package com.interaxon.test.libmuse;

import android.content.Context;
import android.util.Log;

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

    void updateState(Signal signal1, Signal signal2, Signal signal3, Signal signal4) {
        if (signal1 != null && signal2 != null && signal3 != null && signal4 != null) {
            Log.d("Analyzer", "Analyzing");
            signal1.analyzable = false;
            sanalyzer.analyze(signal1);
            float res1 = sanalyzer.last_result;

            signal2.analyzable = false;
            sanalyzer.analyze(signal2);
            float res2 = sanalyzer.last_result;

            signal3.analyzable = false;
            sanalyzer.analyze(signal3);
            float res3 = sanalyzer.last_result;

            signal4.analyzable = false;
            sanalyzer.analyze(signal4);
            float res4 = sanalyzer.last_result;

            if (res1 < 0 || res2 < 0 || res3 < 0 || res4 < 0) {
                if (CURRENT_STATE == STATUS_TEXT_NORMAL) {
                    CURRENT_STATE = STATUS_TEXT_ATTACK;
                    //Attack Starts
                    //Get actual time
                    //Prepare JSON
                    snotifier.pushDataToServer(createJson(signal1, signal2, signal3, signal4));
                    //Send to server
                }
            }
            else {
                if (CURRENT_STATE == STATUS_TEXT_ATTACK) {
                    CURRENT_STATE = STATUS_TEXT_NORMAL;
                }
            }
           /* if (Math.random() < 0.00) {
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
            */
        }
    }

    private String createJson(Signal signal1, Signal signal2, Signal signal3, Signal signal4){
        return "{ patient : " + "Espartano" +
                " fecha : " + System.currentTimeMillis() +
                " "
    }

}
