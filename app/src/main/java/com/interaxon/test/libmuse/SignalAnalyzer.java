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

    public float last_result;
    private FFTDetector fftd;


    SignalAnalyzer() {
        fftd = new FFTDetector(10, 10);

    }

    void analyze(Signal signal) {
        new ComputeTask().execute(signal);
    }

    private class ComputeTask extends AsyncTask<Signal, Void, Float> {
        protected Float doInBackground(Signal... params) {
            float[] s = new float[params.length];
            float[] si = params[0].toFloatArray();

            float value = 0;
            Log.d("compute", "Computed");
            float apen = ApEn.computeF(si);
            //Compute sampEn
            Log.d("APEN", "Computed");
            float sampEn = SampEn.computeF(si);
            //Compute fft
            Log.d("SAMPEN", "Computed");
            float fft = fftd.computeF(si);
            Log.d("FFT", "Computed");
            value = coef0 + FFTWeight*fft + ApEnWeight*apen + sampEnWeight*sampEn;
            Log.d("RESULT", Float.toString(value));
            return value;
        }

        protected void onPostExecute(Float result) {
            last_result = result;
        }
    }
}
