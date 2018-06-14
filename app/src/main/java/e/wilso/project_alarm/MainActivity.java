package e.wilso.project_alarm;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

   AlarmManager alarmManager;
   private PendingIntent pending_intent;
   private TimePicker alarmTimePicker;
   private TextView alarmTextView;
   private AlarmReceiver alarm;
   MainActivity inst;
   Context context;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      this.context = this;

      //alarm = new AlarmReceiver();
      alarmTextView = findViewById(R.id.alarmText);

      final Intent myIntent = new Intent(this.context, AlarmReceiver.class);

      // Get the alarm manager service
      alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

      // set the alarm to the time that you picked
      final Calendar calendar = Calendar.getInstance();

      alarmTimePicker = findViewById(R.id.alarmTimePicker);

      Button start_alarm = findViewById(R.id.start_alarm);

      start_alarm.setOnClickListener(new View.OnClickListener() {
         @TargetApi(Build.VERSION_CODES.M)
         @Override
         public void onClick(View v) {
            //settting calendar instance with the hour and minute that we picked on the time picker
            calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getHour());
            calendar.set(Calendar.MINUTE, alarmTimePicker.getMinute());

            String hour_str;
            String minute_str;

            int hour = alarmTimePicker.getCurrentHour();
            int minute = alarmTimePicker.getCurrentMinute();;

            //conver 24 hour format
            if(hour > 12) hour_str = String.valueOf(hour - 12);
            else hour_str = String.valueOf(hour);
            if(minute < 10) minute_str = "0" + String.valueOf(minute);
            else minute_str = String.valueOf(minute);

            //method that changes the update text Textbox
            setAlarmText("Alarm set to: " + hour_str + ":" + minute_str);

            //put extra string into my_intent, tell the clock that you pressed the "alarm on" button
            myIntent.putExtra("extra", "yes");

            //create a pendingIntent that delays the intent until the specified calendar time
            pending_intent = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            //set the alarm manager
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);
         }
      });

      Button stop_alarm = findViewById(R.id.stop_alarm);
      stop_alarm.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            myIntent.putExtra("extra", "no");
            sendBroadcast(myIntent);

            alarmManager.cancel(pending_intent);
            setAlarmText("Alarm canceled");
         }
      });
   }

   private void setAlarmText(String alarmText) {

      alarmTextView.setText(alarmText);
   }

   @Override
   public void onStart() {
      super.onStart();
      inst = this;
   }

   @Override
   public void onDestroy() {
      super.onDestroy();

      Log.e("MyActivity", "on Destroy");
   }
}
