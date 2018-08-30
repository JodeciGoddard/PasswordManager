package com.example.jodeci.passwordmanager;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ProfileView extends AppCompatActivity {

    public String username;
    public  MyDBHandler dbHandler;
    private RecyclerView view;
    private ProfileAdapter adapter;
    private ArrayList<Entry> myList;
    private DrawerLayout mDrawerLayout;
    private FileManager fm;

    private static final int READ_REQUEST_CODE = 42;
    private static final  int WRITE_PERMISSION_REQ_CODE = 112;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        Bundle loginData = getIntent().getExtras();
        username = loginData.getString("username");
        setTitle("Home: " + username);

        //ToolBar setup
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        dbHandler = new MyDBHandler(this,null, null,1);
        FloatingActionButton mFab = (FloatingActionButton) findViewById(R.id.btnFloat2);

        fm = new FileManager(getBaseContext());

        //get entries
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                myList = dbHandler.getEntries(username);
            }
        });
        thread.run();
        initialiseListView();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout2);



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view2);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        if (menuItem.getItemId() == R.id.nav_export) {

                            try {
                                requestAppPermissions();
                                String data = convertEntriestoFileFormat(myList);
                                fm.write(data, "data.txt");
                                Toast.makeText(ProfileView.this,"File Export successful",Toast.LENGTH_SHORT).show();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                            //Log.i("Profile: ", "Menu item import selected");
                        } else if(menuItem.getItemId() == R.id.nav_import){
                            performFileSearch();

                        } else if (menuItem.getItemId() == R.id.nav_passwordGen){
                            Intent intent = new Intent(ProfileView.this, GeneratorView.class);
                            startActivity(intent);
                        }

                        //Log.i("Profile: ", "Something was tapped");
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

                            // int id = Preferences.getNewID(username, ProfileView.this);
                            // Preferences.incremnetID(username, ProfileView.this);

                            long id = dbHandler.addEntry(entry, username);
                            dbHandler.printdblogEntries(username);

                            entry.set_id((int) id);

                            adapter.add(entry);
                            diag.cancel();
                            setDefaultText();
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
        view = (RecyclerView) findViewById(R.id.recyclerList);
        adapter = new ProfileAdapter(ProfileView.this, myList, this);
        view.setAdapter(adapter);
        view.setLayoutManager(new LinearLayoutManager(this));
        setDefaultText();
    }

    public void setDefaultText(){
        TextView text = (TextView) findViewById(R.id.lblEmpty2);
        if (myList.size() > 0){
            text.setVisibility(View.GONE);
        } else {
            text.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Fires an intent to spin up the "file chooser" UI and select an image.
     */
    public void performFileSearch() {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".
        intent.setType("*/*");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK ) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri intentUri = null;
            if (resultData != null) {
                intentUri = resultData.getData();

                final String result;

                result = fm.read(intentUri);

                if(result == null){
                    Toast toast = Toast.makeText(getBaseContext(),"File Read failed", Toast.LENGTH_LONG);
                    Log.i("Read/Write: " , "Result is null");
                } else {
                    Toast toast = Toast.makeText(getBaseContext(),"Reading Files", Toast.LENGTH_SHORT);
                    Log.i("Read/Write: " , result);
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            addImportsToDatabase(result);
                        }
                    });
                    thread.run();
                }
            }
        }
    }

    //Permissions
    private void requestAppPermissions() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        if (hasReadPermissions() && hasWritePermissions()) {
            return;
        }

        ActivityCompat.requestPermissions(this,
                new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, WRITE_PERMISSION_REQ_CODE); // your request code
    }

    private boolean hasReadPermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasWritePermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    //Converts entries to string format to save in a file
    private String convertEntriestoFileFormat(ArrayList<Entry> list){
        String data = "";

        String header = username + "." + list.size() + ".";

        data = data + header + "\n";

        for (Entry e : list){
            data = data + "{\n";
            data = data + "" + e.getUncleanAppName() + "\n";
            data = data + "" + e.getUncleanUsername() + "\n";
            data = data + "" + e.getUncleanPass() + "\n";
            data = data + "}\n";
        }


        return data;
    }

    private Boolean addImportsToDatabase(String data){
        String[] lines = data.split("\\r?\\n");
        String[] firstline = lines[0].split("\\.");

        //make sure its a valid data file
        if (!username.equals(firstline[0])){
            Toast.makeText(ProfileView.this,"Reading Files Failed",Toast.LENGTH_SHORT).show();
            return  false;
        }


        String appname = "";
        String appuser = "";
        String appPass = "";
        for (int i = 1; i < lines.length; i++){

            //check for open bracket
            if (lines[i].equals("{")) {
                appname = Entry.uncleanseInput(lines[i + 1]) ;
                appuser = Entry.uncleanseInput(lines[i + 2]) ;
                appPass = Entry.uncleanseInput(lines[i + 3]) ;
                continue;
            }

            //check for close braket
            if(lines[i].equals("}")){
                Entry e = new Entry(appname,appuser,appPass);
                long id = dbHandler.addEntry(e, username);
                e.set_id((int) id);
                adapter.add(e);
                continue;
            }

        }

        setDefaultText();
        Toast.makeText(ProfileView.this,"Reading Files Completed",Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
