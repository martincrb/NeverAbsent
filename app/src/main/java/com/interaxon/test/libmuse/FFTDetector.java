package com.interaxon.test.libmuse;

import java.util.Arrays;

import org.jtransforms.fft.FloatFFT_1D;

public final class FFTDetector {
	private int mFs;
	private int mLengthEEGSignal;
	private FloatFFT_1D mFFT;
	
	public FFTDetector(int fs, int lengthEEGSignal){
		mFs = fs;
		mLengthEEGSignal = lengthEEGSignal;
		mFFT = new FloatFFT_1D(lengthEEGSignal);
	}
	
	public float computeF(float[] eeg) {
		if (eeg.length != mLengthEEGSignal){
			System.out.println("La longitud del ecg no es la misma que la inicial!!");
			return -1;
		}
		
		float[] x = Arrays.copyOf(eeg, eeg.length);
		mFFT.realForward(x);
		
		int posF0_5 = (int) Math.ceil((double) (0.5 * eeg.length) / mFs);
		int posF1_5 = (int) Math.ceil((double) (1.5 * eeg.length) / mFs);
		int posF5 = (int) Math.ceil((double) (5 * eeg.length)  / mFs);
		
		float lowFrecPow = 0, highFrecPow = 0;
		
		// Buscamos la potencia en el rango 0.5 - 1.5 Hz
		for (int i=2*posF0_5; i < 2*posF1_5;i+=2)
			lowFrecPow += x[i] * x[i] + x[i+1]*x[i+1];
		
		// Buscamos la potencia en el rango 1.5 - 5 Hz
		for (int i=2*posF1_5; i < 2*posF5;i+=2)
			highFrecPow += x[i] * x[i] + x[i+1]*x[i+1];
		
		return lowFrecPow / highFrecPow;
	}	
}
