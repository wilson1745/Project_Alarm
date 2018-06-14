package e.wilso.project_alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

   final String TAG = "Alarm_Receiver";

   @Override
   public void onReceive(Context context, Intent intent) {
      //fetch data from the intent
      String state = intent.getExtras().getString("extra");
      Log.e(TAG, "In the receiver with " + state);

      //create intent to the rigntoneservice
      Intent serviceIntent = new Intent(context,RingtonePlayingService.class);

      //put the extra string from Mainactivity to the Ringtoneservice
      serviceIntent.putExtra("extra", state);

      //start the ringtoneservice
      context.startService(serviceIntent);
   }
}
