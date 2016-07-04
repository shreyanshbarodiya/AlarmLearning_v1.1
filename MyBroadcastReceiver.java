package shreyansh.com.alarmlearning;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {
    public static MediaPlayer mp;
    @Override
    public void onReceive(Context context, Intent intent) {
        if(mp==null){
            mp = MediaPlayer.create(context, R.raw.song);
            mp.start();
        }else if(!mp.isPlaying()){
            mp = MediaPlayer.create(context, R.raw.song);
            mp.start();
        }
        Log.i("myTag","Alarm baj gaya be...");
        Toast.makeText(context, "Alarm....", Toast.LENGTH_LONG).show();
    }
}