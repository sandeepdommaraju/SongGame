package games.songgame.game;

import java.util.List;

import android.app.Application;

public class GameDTO extends Application{
	
	public enum GAME_STATE{
		NOT_STARTED, IN_PROGRESS, GAME_OVER
	};
	
	private int totalscore;
	private List<String> playedSongs;
	private GAME_STATE gState;	
	
	
	public GAME_STATE getgState() {
		return gState;
	}
	public void setgState(GAME_STATE gState) {
		this.gState = gState;
	}
	public int getTotalscore() {
		return totalscore;
	}
	public void setTotalscore(int totalscore) {
		this.totalscore = totalscore;
	}
	public List<String> getPlayedSongs() {
		return playedSongs;
	}
	public void setPlayedSongs(List<String> playedSongs) {
		this.playedSongs = playedSongs;
	}
	
}
