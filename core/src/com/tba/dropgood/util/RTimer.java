package com.tba.dropgood.util;

import java.util.Random;

public class RTimer {
	
	private int max, min;
	private long delay;
	private long prev;

	public RTimer(int minInMillis, int maxInMillis) {
		this.max = maxInMillis;
		this.min = minInMillis;
		delay = ((long) new Random().nextInt(max-min) + min) * 1000000;
	}
	
	public void start() {
		prev = System.nanoTime();
	}
	
	public boolean isReady() {
		if(System.nanoTime() - prev >= delay) {
			prev = System.nanoTime();
			delay = getRDelay();
			return true;
		}
		return false;
	}
	
	public long getRDelay() {
		return ((long) new Random().nextInt(max - min) + min) * 1000000;
	}
}
