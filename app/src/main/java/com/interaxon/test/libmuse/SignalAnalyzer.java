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
        int fs = 220;
		int xLen = (int) Math.round(fs * 23.6);
        fftd = new FFTDetector(fs, xLen);

    }

    void analyze(Signal signal) {
        new ComputeTask().execute(new Signal(signal));
    }

    private class ComputeTask extends AsyncTask<Signal, Void, Float> {
        protected Float doInBackground(Signal... params) {
            long time= System.nanoTime(), from;
            float[] s = new float[params.length];
            float[] si = params[0].toFloatArray();

            float value = 0;
            Log.d("compute", "Computed");
            from = System.nanoTime();
            float apen = ApEn.computeF(si);

            Log.d("RESULT and TIME", Float.toString(apen) + " " + Long.toString((System.nanoTime()-from)/1000000));

            //Compute sampEn
            Log.d("APEN", "Computed");
            from = System.nanoTime();
            float sampEn = SampEn.computeF(si);

            Log.d("RESULT and TIME", Float.toString(sampEn) + " " + Long.toString((System.nanoTime()-from)/1000000));
            //Compute fft
            Log.d("SAMPEN", "Computed");
            from = System.nanoTime();
            float fft = fftd.computeF(si);

            Log.d("RESULT and TIME", Float.toString(fft) + " " + Long.toString((System.nanoTime()-from)/1000000));
            Log.d("FFT", "Computed");
            value = coef0 + FFTWeight*fft + ApEnWeight*apen + sampEnWeight*sampEn;
            long time2= System.nanoTime();
            Log.d("RESULT and TIME", Float.toString(value) + " " + Long.toString((time2-time)/1000000));
            return value;
        }

        protected void onPostExecute(Float result) {
            last_result = result;
        }
    }
}
