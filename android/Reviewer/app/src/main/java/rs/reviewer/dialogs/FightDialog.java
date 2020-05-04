package rs.reviewer.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;

import android.util.Log;
import model.PokeBoss;
import rs.reviewer.MainActivity;
import rs.reviewer.R;
import rs.reviewer.activities.FightBossActivity;
import rs.reviewer.activities.ProfileActivity;

public class FightDialog extends AlertDialog.Builder {

    PokeBoss boss;

    public FightDialog(Context context) {

        super(context);

        setUpDialog();
    }

    private void setUpDialog(){
        setTitle(R.string.near_fight);
        setMessage(R.string.wanna_catch);
        setCancelable(false);

        setPositiveButton(R.string.catchBtn, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent fightIntent = new Intent(getContext(), FightBossActivity.class);
                fightIntent.putExtra("bossId", boss.getId());
                getContext().startActivity(fightIntent);
                dialog.dismiss();
            }
        });

        setNegativeButton(R.string.ignoreBtn, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

    }

    public AlertDialog prepareDialog(PokeBoss boss){
        setTitle("You're near pokemon: " + boss.getPokemon().getName() + "(lvl. " + boss.getLevel() + ")");

        AlertDialog dialog = create();
        dialog.setCanceledOnTouchOutside(false);
        this.boss = boss;


        return dialog;
    }
}