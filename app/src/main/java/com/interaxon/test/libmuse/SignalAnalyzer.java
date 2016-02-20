package com.interaxon.test.libmuse;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by Martin on 20/02/2016.
 */
public class SignalAnalyzer {
    private float coef0 = 0f;
    private float FFTWeight = 0.5212189f;
    private float ApEnWeight = 0.535182f;
    private float sampEnWeight = 0.3826242f;

    SignalAnalyzer() {}

    void analyze(Signal signal) {
        new ComputeTask().execute(signal);
    }

    private class ComputeTask extends AsyncTask<Signal, Void, Float> {
        protected Float doInBackground(Signal... params) {
            float[] s = new float[params.length];
            float[] si = params[0].toFloatArray();

            float value = 0;
            //Compute ApEn
            float apen = 0;
            //Compute sampEn
            float sampEn = 0;
            //Compute fft
            float fft = 0;
            return coef0 + FFTWeight*fft + ApEnWeight*apen + sampEnWeight*sampEn;
        }

        protected void onPostExecute(Float result) {
            Log.d("Computed ",result.toString());
        }
    }
}
