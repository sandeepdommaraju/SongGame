package games.songgame;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
	String[] extensions = {".mp3"};
	List<String> albums = new ArrayList<String>();
	List<String> songs = new ArrayList<String>();					
	List<String> addrList = new ArrayList<String>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if(savedInstanceState == null) {
			System.out.println("ERROR!");
		}
		
		String MusicDirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC) + "";		
		initializeMusicData(MusicDirPath);
		
		writeSongsToFile();
		writeAddrToFile();
		
		//PrintInfo();
		
		System.out.println("songs-count: "+songs.size());
		System.out.println("addrs-count: "+addrList.size());
		
		System.out.println("song[10]: "+songs.get(10));
		System.out.println("addrs[10]: "+addrList.get(10));
		
		
		/*Object o = getApplicationContext();
		gDTO = (GameDTO) getApplicationContext();
		gDTO.setTotalscore(0);
		gDTO.setPlayedSongs(new ArrayList<String>());
		gDTO.setgState(GAME_STATE.NOT_STARTED);*/
		
		//start game
		Button startButton = (Button) findViewById(R.id.startbutton);
		startButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//gDTO.setgState(GAME_STATE.IN_PROGRESS);
				Intent intent = new Intent(getApplicationContext(),GameActivity.class);
				intent.putExtra("songFile", "songs.txt");
				intent.putExtra("addrFile", "addr.txt");
				startActivity(intent);
				
			}
		});
		
				
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
void initializeMusicData(String musicDirPath){
		
		File f = new File(musicDirPath);
		
		if(f.listFiles() == null){
			if(f.getName().indexOf(extensions[0]) > 0)
				songs.add(f.getName());
				addrList.add(f.getAbsolutePath());
				
		}else{
			build(f);
		}	
		
	}

	public void writeSongsToFile() {
	    try {
	    	String data = "";
	        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("songs.txt", getApplicationContext().MODE_PRIVATE));
	        for(String song: songs){
	        	data += song;
	        	data += "\n";
	        }
	        outputStreamWriter.write(data);
	        outputStreamWriter.close();
	    }
	    catch (IOException e) {
	        System.out.println("Exception: File write failed: " + e.toString());
	    }finally{
	    	System.out.println("writeToFile() Success!");
	    }
	}
	
	public void writeAddrToFile(){
		
		try {
	    	String data = "";
	        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("addr.txt", getApplicationContext().MODE_PRIVATE));
	        for(String addr: addrList){
	        	data += addr;
	        	data += "\n";
	        }
	        outputStreamWriter.write(data);
	        outputStreamWriter.close();
	    }
	    catch (IOException e) {
	        System.out.println("Exception: File write failed: " + e.toString());
	    }finally{
	    	System.out.println("writeToFile() Success!");
	    }
		
	}
	
	void build (File f){
		for(File inFile: f.listFiles()){
			if(inFile.isDirectory()){
				albums.add(inFile.getName());
				build(inFile);
			}else{
				if(inFile.getName().indexOf(extensions[0]) >0)
					songs.add(inFile.getName());
					addrList.add(f.getAbsolutePath());
			}
		}
	}
	
	public String cleanString(String input){
		String s = input;
		return s;
	}
	
	void PrintInfo(){
		System.out.println("##### ADDRESS #####" +" count: "+addrList.size());
		
		for(String addr: addrList){
			System.out.println(addr);
		}
		
		System.out.println("##### SONGS #####" +" count: "+songs.size());
		for(String song: songs){
			System.out.println(song);
		}
	}
	
	int randInt(int min, int max){
		Random r = new Random();
		int rand = r.nextInt((max-min) +1) + min;
		return rand;
	}
	

}
