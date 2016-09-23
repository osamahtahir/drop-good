package com.tba.dropgood.util;

public class Delayer {
	
	private float delay;
	private long start;

	public Delayer(float seconds) {
		delay = seconds * 1000000000;
		start = System.nanoTime();
	}
	
	public boolean isDone() {
		return System.nanoTime() - start >= delay;
	}
}
