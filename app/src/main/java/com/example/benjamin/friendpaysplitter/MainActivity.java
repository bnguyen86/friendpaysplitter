package com.example.benjamin.friendpaysplitter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.app.ActionBar;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.Manager;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.couchbase.lite.android.AndroidContext;

import org.w3c.dom.Text;


public class MainActivity extends Activity implements ActionBar.TabListener, EventsFragment.OnFragmentInteractionListener, QuickSplit.OnFragmentInteractionListener {
    public static final String DB_NAME = "couchbaseevents";
    public static final String TAG = "couchbaseevents";
    public Manager manager;
    public Database database;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

        setupUI(findViewById(R.id.pager));
        helloCBL();
        //createAllEventsView();

    }


    private void helloCBL() {
        try {
            this.manager = new Manager(new AndroidContext(this), Manager.DEFAULT_OPTIONS);
            this.database = manager.getDatabase(DB_NAME);
        } catch (Exception e) {
            Log.e(TAG, "Error getting database", e);
        }
    }


    public String createDocument(Map<String, Object> map) {
        // Create a new document and add data
        Document document = database.createDocument();
        String documentId = document.getId();

        try {
            // Save the properties to the document
            document.putProperties(map);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }
        return documentId;
    }

    public void createEvent(View view){

        Map<String, Object> event = new HashMap<String,Object>();

        EditText eventNameField = (EditText) findViewById(R.id.eventName);
        EditText eventDateField = (EditText) findViewById(R.id.eventDate);
        EditText eventDescriptionField = (EditText) findViewById(R.id.eventDescription);

        DateFormat df = DateFormat.getDateInstance();

        String eventName = eventNameField.getText().toString();
        Date eventdate;
            try{
                eventdate = df.parse(eventDateField.getText().toString());
            } catch(ParseException p){
                eventdate = new Date();
            }
        String eventDescription = eventDescriptionField.getText().toString();

        List<String> participants = new ArrayList<String>();
        LinearLayout layout = (LinearLayout) findViewById(R.id.eventParticipants);
        for(int i = 0 ; i < layout.getChildCount() ; i++){
            View v = layout.getChildAt(i);
            if(v.getTag() == "participantField"){
                participants.add(((EditText) v).getText().toString());
            }
        }

        event.put("docType","event");
        event.put("name",eventName);
        event.put("description",eventDescription);
        event.put("date",eventdate);
        event.put("participants", participants);

        createDocument(event);

        List<Map<String,Object>> allEvents = getAllEvents();
        LinearLayout eventsContainer = (LinearLayout)findViewById(R.id.allEvents);

        for(Map<String,Object> m : allEvents){
            TextView t = new TextView(this);
            t.setText((String)m.get("name"));
            eventsContainer.addView(t);
        }

    }

    public List<Map<String,Object>> getAllEvents(){
        List<Map<String,Object>> allEvents = new ArrayList<Map<String, Object>>();
        Query query = database.createAllDocumentsQuery();
        try {
            QueryEnumerator results = query.run();
       /* Iterate through the rows to get the document ids */
            for (Iterator<QueryRow> it = results; it.hasNext();) {
                QueryRow row = it.next();
                String docId = row.getValue().toString();
                Log.i("DOC_ID",docId);
                Document retrievedDocument = database.getDocument(docId);
                allEvents.add(retrievedDocument.getProperties());

            }
        } catch (CouchbaseLiteException e) {
            Log.e("Error querying view.", e.toString());
        }

        return allEvents;
    }

    public Database getDatabaseInstance() throws CouchbaseLiteException {
        if ((this.database == null) & (this.manager != null)) {
            this.database = manager.getDatabase(DB_NAME);
        }
        return database;
    }

    public Manager getManagerInstance() throws IOException {
        if (manager == null) {
            manager = new Manager(new AndroidContext(this), Manager.DEFAULT_OPTIONS);
        }
        return manager;
    }

    public void createAllEventsView() {
        com.couchbase.lite.View allEventsView = this.getView("allEvents");
            allEventsView.setMap(new Mapper(){
                        @Override
                        public void map(Map<String, Object> document, Emitter emitter) {
                            List<String> events = (List) document.get("docType");
                            for (String event : events) {
                                emitter.emit(event, document.get("name"));
                            }
                        }
                    }, "1" /* The version number of the mapper... */
            );
    }

    private com.couchbase.lite.View getView(String name) {
        com.couchbase.lite.View view = null;
        try {
            view = this.getDatabaseInstance().getView(name);
        }
        catch (CouchbaseLiteException cble) {
            cble.printStackTrace();
        }
        return view;
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onFragmentInteraction(String id) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            // TODO Auto-generated method stub
            switch (position) {
                case 0:
                    return CreateEventFragment.newInstance(position);

                case 1:
                    return EventsFragment.newInstance(position);

                case 2:
                    return QuickSplit.newInstance(position);
            }

            return null;

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }


    public void showDatePickerDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");

    }

    private void addParticipant() {
        int participantCount = 0;
        LinearLayout layout = (LinearLayout) findViewById(R.id.eventParticipants);
        for(int i = 0 ; i < layout.getChildCount() ; i++){
            View v = layout.getChildAt(i);
            if(v.getTag() == "participantField"){
                participantCount++;
            }
        }
        EditText participant = new EditText(this);
        participant.setTag("participantField");
        participant.setHint("Friend #"+(participantCount+1));
        participant.setMaxLines(1);

        layout.addView(participant);
        participant.requestFocus();
    }

    public void addParticipant(View view){
        addParticipant();
    }

    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(MainActivity.this);
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView);
            }
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
