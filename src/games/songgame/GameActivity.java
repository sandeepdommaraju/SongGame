package games.songgame;


import java.io.*;
import java.util.*;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author Sandeep
 * This activiy is the GameInstance Screen!!
 *
 */
public class GameActivity extends Activity {
	
	//Button playButton;
	//Button pauseButton;
	MediaPlayer mMediaPlayer;
	
	List<String> songs;
	List<String> songsPlayed;
	List<String> addrList;
	
	CountDownTimer countdown;
	
	RadioButton rb1;
	RadioButton rb2;
	RadioButton rb3;
	RadioButton rb4;
	
	TextView scoreView;
	TextView timerView;
	
	Button submitButton;
	
	int totalScore;
	int selectedOption;
	int ansIndex;
	
	String randOpt1;
	String randOpt2;
	String randOpt3;	
	String guessSong;
	
	String songAddr;
	
	int corrects;
	long startTime;
	
	boolean mediaReset = false;
	boolean playFirstTime = true;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);
        
        Bundle bundle = getIntent().getExtras();
        String songFile = bundle.getString("songFile");
        String addrFile = bundle.getString("addrFile");
        
        buildSongList(songFile);
        buildAddrList(addrFile);
        
        timerView  = (TextView) findViewById(R.id.textView1);
        scoreView  = (TextView) findViewById(R.id.textView2);
        
        totalScore = 0;
        scoreView.setText(totalScore+"");
        
        rb1 = (RadioButton) findViewById(R.id.radioButton1);
        rb2 = (RadioButton) findViewById(R.id.radioButton2);
        rb3 = (RadioButton) findViewById(R.id.radioButton3);
        rb4 = (RadioButton) findViewById(R.id.radioButton4);        
        submitButton = (Button) findViewById(R.id.button1);
       // playButton = (Button) findViewById(R.id.playbutton);
       // pauseButton = (Button) findViewById(R.id.button2);
        
        songsPlayed = new ArrayList<String>();
        corrects = 0;
        startTime = System.currentTimeMillis();
        
        reset();
        init(); 
        
		submitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				selectedOption = -1;
				if(rb1.isChecked()){
					selectedOption = 1;
				}else if(rb2.isChecked()){
					selectedOption = 2;
				}else if(rb3.isChecked()){
					selectedOption = 3;
				}else if(rb4.isChecked()){
					selectedOption = 4;
				}
				
				// TODO Auto-generated method stub
				if(selectedOption <1 || selectedOption >4){
					Toast.makeText(getApplicationContext(), "Guess the song!", Toast.LENGTH_SHORT).show();
				}else{
					if(selectedOption == ansIndex){
						totalScore += 5;
						corrects++;
						countdown.cancel();
						scoreView.setText("Score: "+totalScore);
						repeat();
					}else{
						countdown.cancel();
						Intent intent = new Intent(getApplicationContext(),EndActivity.class);
						intent.putExtra("totalScore", totalScore);
						intent.putExtra("corrects", corrects);
						intent.putExtra("startTime", startTime);
						reset();
						startActivity(intent);
						//Toast.makeText(getApplicationContext(), "GameOver!! Your Score: "+totalScore, Toast.LENGTH_LONG).show();
					}
				}
				
			}
		});
		
		/*pauseButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!mediaReset){
					mMediaPlayer.stop();
				}
				
			}
		});*/
        
    }
	
	void reset(){
		rb1.setChecked(false);
		rb2.setChecked(false);
		rb3.setChecked(false);
		rb4.setChecked(false);
		
		if(mMediaPlayer != null && !mediaReset){
			mMediaPlayer.stop();
			mMediaPlayer.release();
			mediaReset = true;
		}
	}
	
	void init(){
		generateRandomOptions();
		setOptions();
		countdown = new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                timerView.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                timerView.setText("times up!");
                Intent intent = new Intent(getApplicationContext(),EndActivity.class);
				intent.putExtra("totalScore", totalScore);
				intent.putExtra("corrects", corrects);
				intent.putExtra("startTime", startTime);
				reset();
				startActivity(intent);
            }
         }.start();
         updateSongAddress();
         attachSongListener();
         playFirstTime = true;
	}
	
	void repeat(){
		reset();
		init();		
	}
	
	void setOptions(){
		ansIndex = randInt(1, 4);
		String parsed_guessSong = parse(guessSong);
		String parsed_randOpt1 =  parse(randOpt1);
		String parsed_randOpt2 = parse(randOpt2);
		String parsed_randOpt3 = parse(randOpt3);
		switch(ansIndex) {
	        case 1:
	        	rb1.setText(guessSong);
	        	rb2.setText(randOpt1);
	        	rb3.setText(randOpt2);
	        	rb4.setText(randOpt3);
	        	break;
	        case 2:
	        	rb1.setText(randOpt1);
	        	rb2.setText(guessSong);
	        	rb3.setText(randOpt2);
	        	rb4.setText(randOpt3);
	        	break;
	        case 3:
	        	rb1.setText(randOpt1);
	        	rb2.setText(randOpt2);
	        	rb3.setText(guessSong);
	        	rb4.setText(randOpt3);
	        	break;
	        case 4:
	        	rb1.setText(randOpt1);
	        	rb2.setText(randOpt2);
	        	rb3.setText(randOpt3);
	        	rb4.setText(guessSong);
	        	break;	        	
	    }
		
	}
	
	void generateRandomOptions(){
		List<Integer> chosenRands = new ArrayList<Integer>();
		int guessSongInt = randInt(0, songs.size() -1);
		while(songsPlayed.contains(songs.get(guessSongInt))){
			guessSongInt = randInt(0, songs.size() -1);
		}
		
		int rand1 = randInt(0, songs.size()-1);
		while(chosenRands.contains(rand1)){
			rand1 = randInt(0, songs.size()-1);
		}
		chosenRands.add(rand1);
		
		int rand2 = randInt(0, songs.size()-1);
		while(chosenRands.contains(rand2)){
			rand2 = randInt(0, songs.size()-1);
		}
		chosenRands.add(rand2);
		
		int rand3 = randInt(0, songs.size()-1);
		while(chosenRands.contains(rand3)){
			rand3 = randInt(0, songs.size()-1);
		}
		
		randOpt1 = songs.get(rand1);
		randOpt2 = songs.get(rand2);
		randOpt3 = songs.get(rand3);
		guessSong = songs.get(guessSongInt);
		songsPlayed.add(guessSong);
	}
	
	
	void updateSongAddress(){
		
		if(guessSong == null){
			System.out.println("Something Screwed up - need to generate guessSong before updatingSongAddress!");
			return;
		}
		
		int songIndex = songs.indexOf(guessSong);
		if(songIndex == -1){
			System.out.println("Something Screwed up - not able to find song: "+ guessSong +" in songsList!");
			return;
		}
		
		songAddr = addrList.get(songIndex) + "/" + guessSong;
		if(songAddr == null){
			System.out.println("Something Screwed up - not able to find songAddr: "+ songAddr +" in songAddrList for song: "+ guessSong );
			return;
		}
	
	}
	
	
	void attachSongListener(){
		
		//playButton.setOnClickListener(new OnClickListener() {
			
			//@SuppressLint("NewApi")
			//@Override
			//public void onClick(View v) {
				//mMediaPlayer.start();
				
				try{		
					
					/*System.out.println(songpath);
					System.out.println(Environment.isExternalStorageEmulated());
					System.out.println(getApplicationContext().checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE"));*/
					
					if(playFirstTime){
						mMediaPlayer = new  MediaPlayer();
						mMediaPlayer.setDataSource(songAddr);
						mMediaPlayer.prepare();   
						mMediaPlayer.start();
						mMediaPlayer.seekTo(randInt(0, mMediaPlayer.getDuration()));
						mediaReset = false;
						playFirstTime = false;
					}
					
				}catch(Exception e){
					System.out.println("Exception!! "+e);
				}

			//}
		//});
	}
	
	
	int randInt(int min, int max){
		Random r = new Random();
		int rand = r.nextInt((max-min) +1) + min;
		return rand;
	}
	
	void buildSongList(String songFile){
		
		try {
			
			songs = new ArrayList<String>();
	        InputStream inputStream = openFileInput(songFile);

	        if ( inputStream != null ) {
	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	            String receiveString = "";
	            //StringBuilder stringBuilder = new StringBuilder();

	            while ( (receiveString = bufferedReader.readLine()) != null ) {
	                songs.add(receiveString);
	            }

	            inputStream.close();
	           
	        }
	    }
	    catch (FileNotFoundException e) {
	        System.out.println("File not found: " + e.toString());
	    } catch (IOException e) {
	        System.out.println("Can not read file: " + e.toString());
	    }finally{
	    	System.out.println("readFile() Success!");
	    }
		
		System.out.println("#### Built songs from songs.txt songs-count: "+songs.size());
		/*for(String song: songs){
			System.out.println(song);
		}*/
		
	}
	
	void buildAddrList(String addrFile){
		try {
			
				addrList = new ArrayList<String>();
		        InputStream inputStream = openFileInput(addrFile);
	
		        if ( inputStream != null ) {
		            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		            String receiveString = "";
		            //StringBuilder stringBuilder = new StringBuilder();
	
		            while ( (receiveString = bufferedReader.readLine()) != null ) {
		            	addrList.add(receiveString);
		            }
	
		            inputStream.close();
		           
		        }
	    }
	    catch (FileNotFoundException e) {
	        System.out.println("File not found: " + e.toString());
	    } catch (IOException e) {
	        System.out.println("Can not read file: " + e.toString());
	    }finally{
	    	System.out.println("readFile() Success!");
	    }
		
		System.out.println("#### Built songs from songs.txt songAddrList-count: "+addrList.size());
		/*for(String sAddr: addrList){
			System.out.println(sAddr);
		}*/
	}
	
	String parse(String in){
		String parsed = in;
		parsed = parsed.replaceAll("[^\\p{L}\\p{Nd}]+", "");
		return parsed;
	}
}
