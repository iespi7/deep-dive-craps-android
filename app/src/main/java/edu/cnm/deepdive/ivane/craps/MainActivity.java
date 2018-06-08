package edu.cnm.deepdive.ivane.craps;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;
import edu.cnm.deepdive.craps.Game;
import edu.cnm.deepdive.craps.Roll;
import edu.cnm.deepdive.craps.State;
import java.security.SecureRandom;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

  private Game game;
  private Button play;
  private Button reset;
  private ToggleButton run;
  private ListView rolls;
  private TextView tally;
  private Random rng;
  private RollsAdapter rollsAdapter;
  private long plays = 0;
  private long wins = 0;
  private boolean running = false;
  private Worker worker = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    play = findViewById(R.id.play_game);
    reset = findViewById(R.id.reset_game);
    run = findViewById(R.id.play_continuously);
    rolls = findViewById(R.id.game_rolls);
    tally = findViewById(R.id.tally);
    game = new Game();
    rng = new SecureRandom();
    rollsAdapter = new RollsAdapter(this, R.layout.item_roll);
    rolls.setAdapter(rollsAdapter);
    reset();

    play.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        play();
        updateDisplay(game.getState(), game.getRolls(), plays, wins);
      }
    });
    reset.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        reset();
      }
    });

    run.setOnCheckedChangeListener(new OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        toggleRun(isChecked);
      }
    });
  }

  private void toggleRun(boolean isChecked) {
    if (isChecked) {
      play.setEnabled(false);
      reset.setEnabled(false);
      worker = new Worker();
      running = true;
      worker.start();
    } else {
      running = false;
      worker = null;
      reset.setEnabled(true);
      play.setEnabled(true);
    }
  }

  private void reset() {
        plays = 0;
        wins = 0;
        updateDisplay(game.getState(), null, plays, wins);
      }


  private void play() {
    game.reset();
    game.play(rng);
    plays++;
    if (game.getState() == State.WIN) {
      wins++;
    }
  }

  private void updateDisplay(State state, Roll[] rolls, long plays, long wins) {
    double percentage = (plays > 0) ? (100d * wins / plays) : 0;
    tally.setText(getString(R.string.tally_pattern, wins, plays, percentage));
    rollsAdapter.clear();
    rollsAdapter.setState(state);
    if (rolls != null) {
      rollsAdapter.addAll(rolls);
    }
  }

  private class Worker extends Thread {

  private edu.cnm.deepdive.craps.State state = game.getState();
  private Roll[] rolls;
  private long threadPlays;
  private long threadWins;

  @Override
    public void run() {
      while (running) {
        do {
          play();
        } while (plays % 100 != 0);
        state = game.getState();
        rolls = game.getRolls();
        threadPlays = plays;
        threadWins = wins;
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            updateDisplay(state, rolls, threadPlays, threadWins);
          }
        });
      }
    }
  }
}
