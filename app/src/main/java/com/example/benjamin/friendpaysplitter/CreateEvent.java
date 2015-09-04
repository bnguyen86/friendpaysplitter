package com.example.benjamin.friendpaysplitter;

import android.app.Activity;
import android.app.DialogFragment;


import android.app.Fragment;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;


public class CreateEvent extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showDatePickerDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");

    }

    public void addParticipant(View view){
        int participantCount = 0;
        LinearLayout layout = (LinearLayout) findViewById(R.id.newEventForm);
        for(int i = 0 ; i < layout.getChildCount() ; i++){
            View v = layout.getChildAt(i);
            if(v.getTag() == "participantField"){
                participantCount++;
            }
        }
        EditText participant = new EditText(this);
            participant.setTag("participantField");
            participant.setHint("Friend #"+(participantCount+1));

        layout.addView(participant);

    }
}
