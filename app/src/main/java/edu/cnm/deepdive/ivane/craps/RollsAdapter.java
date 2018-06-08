package edu.cnm.deepdive.ivane.craps;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import edu.cnm.deepdive.craps.Game;
import edu.cnm.deepdive.craps.Roll;
import edu.cnm.deepdive.craps.State;

public class RollsAdapter extends ArrayAdapter<Roll> {

  private State state;
  private Drawable[] faces;


  public RollsAdapter(@NonNull Context context, int resource) {
    super(context, resource);
    Resources res = context.getResources();
    faces = new Drawable[6];
    faces[0] = res.getDrawable(R.drawable.face_1, null);
    faces[1] = res.getDrawable(R.drawable.face_2, null);
    faces[2] = res.getDrawable(R.drawable.face_3, null);
    faces[3] = res.getDrawable(R.drawable.face_4, null);
    faces[4] = res.getDrawable(R.drawable.face_5, null);
    faces[5] = res.getDrawable(R.drawable.face_6, null);

  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    Roll roll = getItem(position);
    LayoutInflater inflater = LayoutInflater.from(getContext());
    ViewGroup rollView = (ViewGroup) inflater.inflate(R.layout.item_roll, null);
    ImageView die1 = rollView.findViewById(R.id.die_1);
    ImageView die2 = rollView.findViewById(R.id.die_2);
    TextView diceSum = rollView.findViewById(R.id.dice_sum);
    die1.setImageDrawable(faces[roll.getDice()[0] - 1]);
    die2.setImageDrawable(faces[roll.getDice()[1] - 1]);
    diceSum.setText(getContext().getString(R.string.dice_sum_pattern, roll.getSum()));
    if (position == getCount() - 1) {
      if (state == State.WIN) {
        rollView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.win_background));
      } else {
        rollView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.loss_background));
      }
    }
    return rollView;
  }

  public void setState(State state) {
    this.state = state;
  }
}
