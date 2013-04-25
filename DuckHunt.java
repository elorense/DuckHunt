/* Author:  Edric Orense
 * File:    DuckHuntjava
 * Purpose: This is a simple 2D game, similar to whack-a-mole.
 * 			Difficulty can be set by the number picker.
 */



import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;

public class DuckHunt extends Activity implements OnClickListener, OnValueChangeListener{
	
	TextView scoreCount;
	ImageButton duck;
	MediaPlayer laser;
	int score = 0;
	NumberPicker np;
	Timer t;
	Task task;
	ImageView winDog;
	TextView winText;
	Button retryButton;
	
	static int speed;
	
	String[] nums = new String[6];
    @SuppressLint({ "NewApi", "NewApi" })
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_media);
        
        //Populate a string array of numbers 0-6 for the number picker
        for(int i = 0; i < 6; i++){
        	nums[i] = Integer.toString(i);
        }
        
        /* Loading components */
        retryButton = (Button) findViewById(R.id.button1);
        winDog = (ImageView) findViewById(R.id.imageView1);
        winText = (TextView) findViewById(R.id.textView2);
        laser = MediaPlayer.create(getApplicationContext(), R.raw.laser);
        scoreCount = (TextView) findViewById(R.id.textView1);
        np = (NumberPicker) findViewById(R.id.numberPicker1);
        duck = (ImageButton) findViewById(R.id.imageButton1);
        /* Loading components */
        
        duck.setOnClickListener(this);
        
        /* Set value for number picker */
        np.setMaxValue(5);
        np.setMinValue(0);
        np.setDisplayedValues(nums);
        np.setOnValueChangedListener(this);
        /* Set value for number picker */
        
        doOnCreate();
        
        /* Set a listener for the retry button */
		retryButton.setOnClickListener(new View.OnClickListener() {

	            public void onClick(View v) {
	            	doOnCreate();
	            	onRetry();
	            }
	        });
    }
    
    /* Makes the number picker and score text visible on retry */
    public void onRetry(){
    	np.setVisibility(0);
    	scoreCount.setVisibility(0);
    }

    /* Reinitializes all the values for score and makes the win text and picture invisible
     * while making the number picker's value set to 0.
     * Also restarts the timer for the ducks*/
    public void doOnCreate(){
    	score = 0;
        retryButton.setVisibility(4);
        winDog.setVisibility(4);
        winText.setVisibility(4);
        
        
		scoreCount.setText("Score: " + Integer.toString(score));
		np.setValue(0);
		speed = np.getValue();
		t = new Timer();
		task = new Task();
		t.schedule(task, 2000 - (speed * 250), 2000 - (speed * 250));
    }
    
    @SuppressLint("NewApi")
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_test_media, menu);
        
        return true;
    }

    /* Handles onClick events */
	@SuppressLint({ "NewApi", "NewApi" })
	public void onClick(View v) {
		/* sets duck invisible and moves it to a random location*/
		duck.setVisibility(4);
		Random r = new Random();
		duck.setX(r.nextInt(300));
		duck.setY(r.nextInt(350));			
		/* sets duck invisible and moves it to a random location*/
		
		score++;
		
		//If score is 10, make duck invisible and initialize win sequence
		if(score == 10){
			duck.setVisibility(4);
			t.cancel();
			scoreCount.setVisibility(4);
			np.setVisibility(4);
			winDog.setVisibility(0);
			winText.setText("You win!");
			retryButton.setText("Play Again?");
			winText.setVisibility(0);
			retryButton.setVisibility(0);
		}
		
		scoreCount.setText("Score: " + Integer.toString(score));
		laser.start(); //plays a laser sound when ducks are hit
		
	}
	
	//Task to facilitate duck random movement 
	private class Task extends TimerTask{

		@SuppressLint({ "NewApi", "NewApi" })
		@Override
		public void run() {
			//Create thread that can edit the UI
			DuckHunt.this.runOnUiThread(new Runnable() {
				public void run() {
					//randomize duck's position
					Random r = new Random();

					duck.setX(r.nextInt(300));
					duck.setY(r.nextInt(350));		
					duck.setVisibility(0);
					
				}
			});
		}
	}

	//Listens for numberpicker changes and sets the difficulty accordingly
	public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
		t.cancel();
		
		t = new Timer();
		task = new Task();
		speed = np.getValue();
		t.schedule(task, 2000 - (speed * 250), 2000 - (speed * 250));

	}


		
	
}
