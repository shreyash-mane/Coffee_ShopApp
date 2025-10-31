package uk.ac.hw.group20.order.dto;

public class ThreadTimerDto {
	int producerTimer;
	int server1Timer;
	int server2Timer;
	int server3Timer;
	int baristaTimer;
	
	
	
	public ThreadTimerDto() {
		// Default thread timer
		this.producerTimer = 1000;
		this.server1Timer = 2000;
		this.server2Timer = 2000;
		this.server3Timer = 2000;
		this.baristaTimer = 2000;
	}

	public ThreadTimerDto(int producerTimer, int server1Timer, int server2Timer, int server3Timer, int baristaTimer) {
		this.producerTimer = producerTimer;
		this.server1Timer = server1Timer;
		this.server2Timer = server2Timer;
		this.server3Timer = server3Timer;
		this.baristaTimer = baristaTimer;
	}
	
	public int getProducerTimer() {
		return producerTimer;
	}
	public void setProducerTimer(int producerTimer) {
		this.producerTimer = producerTimer;
	}
	public int getServer1Timer() {
		return server1Timer;
	}
	public void setServer1Timer(int server1Timer) {
		this.server1Timer = server1Timer;
	}
	public int getServer2Timer() {
		return server2Timer;
	}
	public void setServer2Timer(int server2Timer) {
		this.server2Timer = server2Timer;
	}
	public int getServer3Timer() {
		return server3Timer;
	}
	public void setServer3Timer(int server3Timer) {
		this.server3Timer = server3Timer;
	}
	public int getBaristaTimer() {
		return baristaTimer;
	}
	public void setBaristaTimer(int baristaTimer) {
		this.baristaTimer = baristaTimer;
	}

	@Override
	public String toString() {
		return "Thread Timers [producerTimer=" + producerTimer + ", server1Timer=" + server1Timer + ", server2Timer="
				+ server2Timer + ", server3Timer=" + server3Timer + ", baristaTimer=" + baristaTimer + "]";
	}
	
}
