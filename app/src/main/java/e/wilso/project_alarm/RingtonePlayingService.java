package e.wilso.project_alarm;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Random;

public class RingtonePlayingService extends Service {

   private final String TAG = "RingtonePlayingService";
   private boolean isRunning;
   private Context context;
   MediaPlayer mMediaPlayer;
   private int startId;

   @Nullable
   @Override
   public IBinder onBind(Intent intent) {
      Log.e("MyActivity", "In the Richard service");
      return null;
   }

   @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
   public int onStartCommand(Intent intent, int flags, int startId) {
      //notification, set up the notification service
      final NotificationManager notify_manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

      //set up intent goes to MainActivity
      Intent intent_main = new Intent(this.getApplicationContext(), MainActivity.class);

      PendingIntent pending_main = PendingIntent.getActivity(this, 0, intent_main, 0);

      Notification notification_popup  = new Notification.Builder(this)
              .setContentTitle("An alarm is going off!")
              .setContentText("Click me!")
              .setSmallIcon(R.drawable.ic_action_call)
              .setContentIntent(pending_main)
              .setAutoCancel(true)
              .build();



      String state = intent.getExtras().getString("extra");
      Log.e(TAG, state);

      assert state != null;
      switch (state) {
         case "no":
            startId = 0;
            break;
         case "yes":
            startId = 1;
            break;
         default:
            startId = 0;
            break;
      }


      if(!this.isRunning && startId == 1) {
         Log.e(TAG, "if there was not sound, and you want start");

         mMediaPlayer = MediaPlayer.create(this, R.raw.win);
         mMediaPlayer.start();

         //set up notification start command
         notify_manager.notify(0, notification_popup);

         this.isRunning = true;
         this.startId = 0;

      }
      else if (!this.isRunning && startId == 0){
         Log.e(TAG, "if there was not sound, and you want end");

         this.isRunning = false;
         this.startId = 0;

      }
      else if (this.isRunning && startId == 1){
         Log.e(TAG, "if there is sound, and you want start");

         this.isRunning = true;
         this.startId = 0;

      }
      else {
         Log.e(TAG, "if there is sound, and you want end");

         mMediaPlayer.stop();
         mMediaPlayer.reset();

         this.isRunning = false;
         this.startId = 0;
      }
      Log.e(TAG, "MyActivity In the service");

      return START_NOT_STICKY;
   }


   @Override
   public void onDestroy() {
      Log.e("JSLog", "on destroy called");
      super.onDestroy();

      this.isRunning = false;
   }
}
