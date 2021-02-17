package engine.client.util;

public class TimeManager {
	public static final long APPLICATION_START_TIME = System.nanoTime();
	public static final long SECOND = 1000000000;
	public static final long MILLISECOND = 1000000;
	public static final long MICROSECOND = 1000;
	
	private long timerStartTime = 0l;
	
	public void setStartTime() {
		this.timerStartTime = System.nanoTime();
	}
	
	public void setStartTime(long time) {
		this.timerStartTime = time;
	}
	
	public long getElapsedTime() {
		return System.nanoTime() - this.timerStartTime;
	}
	
	public boolean hasElapsed(long nanoseconds) {
		return System.nanoTime() - timerStartTime >= nanoseconds ? true : false;
	}
}
