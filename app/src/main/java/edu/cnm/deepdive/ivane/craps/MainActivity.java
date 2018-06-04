package edu.cnm.deepdive.ivane.craps;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import edu.cnm.deepdive.craps.Game;
import edu.cnm.deepdive.craps.Roll;
import edu.cnm.deepdive.craps.State;
import java.security.SecureRandom;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    final TextView status = findViewById(R.id.game_status);
    Button play = findViewById(R.id.play_game);
    final Game game = new Game();
    final Random rng = new SecureRandom();
    play.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        game.reset();
        game.play(rng);
        int totalRolls = game.getRolls().length;
        StringBuilder builder = new StringBuilder();
        for (Roll roll : game.getRolls()) {
          builder.append(roll);
          builder.append(System.getProperty("line.separator"));
        }
        builder.append(game.getState());
/*
        String message = (totalRolls > 1) ?
            getString(R.string.status_pattern,
                game.getRolls()[0],
                game.getRolls()[totalRolls - 1],
                totalRolls,
                game.getState()) :
            getString(R.string.simple_status_pattern,
                game.getRolls()[0],
                game.getState());
*/
        status.setText(builder.toString());
        if (game.getState() == State.WIN) {
          status.setBackgroundColor(getResources().getColor(R.color.win_background));
        } else {
          status.setBackgroundColor(getResources().getColor(R.color.loss_background));
        }
      }
    });
  }
}
