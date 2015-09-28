package games.songgame.game;

import java.util.*;

public class GameInstance {
	
	private int timer = 0;
	private int instanceScore = 0;
	
	private String song;
	private List<String> options;
	
	
	public int getTimer() {
		return timer;
	}
	public void setTimer(int timer) {
		this.timer = timer;
	}
	public int getInstanceScore() {
		return instanceScore;
	}
	public void setInstanceScore(int instanceScore) {
		this.instanceScore = instanceScore;
	}
	public String getSong() {
		return song;
	}
	public void setSong(String song) {
		this.song = song;
	}
	public List<String> getOptions() {
		return options;
	}
	public void setOptions(List<String> options) {
		this.options = options;
	}
	
	

}
