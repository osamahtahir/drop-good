package com.tba.dropgood.util;

public class PSTimer {
	
	public final long interval;
	public boolean running;
	private long prev;

	public PSTimer(int divisor) {
		interval = 1000000000/divisor;
	}
	
	public void start() {
		prev = System.nanoTime();
		running = true;
	}
	
	public void stop() {
		running = false;
	}
	
	public boolean isReady() {
		if(System.nanoTime() - prev >= interval) {
			prev = System.nanoTime();
			return true;
		}
		return false;
	}
}
