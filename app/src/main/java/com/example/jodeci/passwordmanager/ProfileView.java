package com.example.jodeci.passwordmanager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.entries;
import static android.R.attr.userVisible;

public class ProfileView extends AppCompatActivity {

    private String username;
    private MyDBHandler dbHandler;
    private ListView view;
    private CustomAdapter adapter;
    private ArrayList<Entry> myList;
    private DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        Bundle loginData = getIntent().getExtras();
        username = loginData.getString("username");
        setTitle("Home: " + username);

        dbHandler = new MyDBHandler(this,null, null,1);
        FloatingActionButton mFab = (FloatingActionButton) findViewById(R.id.btnFloat);

        //get entries
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                myList = dbHandler.getEntries(username);
            }
        });
        thread.run();
        initialiseListView();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuiler = new AlertDialog.Builder(ProfileView.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_application_input, null);
                final EditText newApp = (EditText) mView.findViewById(R.id.txtAppName);
                final EditText newPass = (EditText) mView.findViewById(R.id.txtAppPass);
                final EditText newUser = (EditText) mView.findViewById(R.id.txtAppUser);
                final Button add = (Button) mView.findViewById(R.id.btnAddApp);

                mBuiler.setView(mView);
                final AlertDialog diag = mBuiler.create();

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String appname, user, pass;
                        appname = newApp.getText().toString();
                        user = newUser.getText().toString();
                        pass = newPass.getText().toString();
                        if ( !appname.trim().equals("") && !user.trim().equals("")&& !pass.trim().equals("")){
                            final Entry entry = new Entry(appname,user, pass);

                            int id = Preferences.getNewID(username, ProfileView.this);
                            Preferences.incremnetID(username, ProfileView.this);

                            entry.set_id(id);
                            entry.cleanseInput();

                            dbHandler.addEntry(entry, username);
                            dbHandler.printdblogEntries(username);

                            adapter.add(entry);
                            diag.cancel();
                            setDefaultText();
                            adapter.notifyDataSetChanged();
                            Toast.makeText(ProfileView.this,"Addition successful",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ProfileView.this,"Please fill all fields",Toast.LENGTH_SHORT).show();
                        }


                    }
                });


                diag.show();

            }
        });
    }

    public void initialiseListView(){
        view = (ListView) findViewById(R.id.Entries);
        adapter = new CustomAdapter(ProfileView.this, myList);
        view.setAdapter(adapter);
        setDefaultText();
    }

    public void setDefaultText(){
        TextView text = (TextView) findViewById(R.id.lblEmpty);
        if (myList.size() > 0){
            text.setVisibility(View.GONE);
        } else {
            text.setVisibility(View.VISIBLE);
        }
    }

    public void deleteEntry(final View v){

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        final Entry e = (Entry) v.getTag();
                        adapter.remove(e);
                        setDefaultText();

                        dbHandler.deleteEntry(e.get_id(), username);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure, you wish to delete?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

}
