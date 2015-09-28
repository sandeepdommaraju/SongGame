package games.songgame;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class EndActivity extends Activity {
	
	TextView templateView;
	TextView gameStatsView;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end_layout);
        
        Bundle bundle = getIntent().getExtras();
        int finalScore = bundle.getInt("totalScore");
        int corrects = bundle.getInt("corrects");
        long startTime  = bundle.getLong("startTime");
        
        long currentTime = System.currentTimeMillis();
        
        int timeTaken = Math.round((currentTime - startTime)/1000);
        int mins = timeTaken / 60;
        int secs = timeTaken % 60;
        
        templateView  = (TextView) findViewById(R.id.textView1);
        gameStatsView = (TextView) findViewById(R.id.textView2);
        
        String data = "";
        
        data += "Score: " + finalScore +" " + "\n";
        data += "CorrectGuesses: " + corrects + "\n";
        if(mins > 0) data += "Time taken: " + mins + "mins " + secs + " secs";
        else data += "Time taken: " + timeTaken +" secs";
        
        
        templateView.setText("gg..Good Game!!");
        gameStatsView.setText(data);
        
	}

}
