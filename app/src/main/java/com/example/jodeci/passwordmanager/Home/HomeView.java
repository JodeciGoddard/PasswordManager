package com.example.jodeci.passwordmanager.Home;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jodeci.passwordmanager.Profile.AddProfileView;
import com.example.jodeci.passwordmanager.Generator.GeneratorView;
import com.example.jodeci.passwordmanager.Profile.ProfileView;
import com.example.jodeci.passwordmanager.R;
import com.example.jodeci.passwordmanager.Util.Entry;
import com.example.jodeci.passwordmanager.Util.FileManager;
import com.example.jodeci.passwordmanager.Util.Preferences;
import com.example.jodeci.passwordmanager.Util.Util;
import com.example.jodeci.passwordmanager.database.DataViewModel;
import com.example.jodeci.passwordmanager.database.Items;
import com.example.jodeci.passwordmanager.database.Profiles;

import java.io.FileNotFoundException;
import java.util.List;

public class HomeView extends AppCompatActivity {

    private int currentProfileID;
    private Profiles currentProfile;
    private String username;
    private List<String> profileList;
    private RecyclerView view;
    private HomeAdapter adapter;
    private DrawerLayout mDrawerLayout;
    private FileManager fm;
    private FloatingActionButton mFab;
    private NavigationView navigationView;
    private Button profileButton;
    private TextView profileText;

    private DataViewModel mViewModel;

    private static final int READ_REQUEST_CODE = 42;
    private static final int ADD_PROFILE_REQ = 23;
    private static final int EDIT_PROFILE_REQ = 24;
    private static final  int WRITE_PERMISSION_REQ_CODE = 112;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_view);
        mViewModel = ViewModelProviders.of(this).get(DataViewModel.class);
        username = mViewModel.getUser().username;
        currentProfileID = Preferences.getCurrentProfileID(this);
        currentProfile = mViewModel.getProfileByID(currentProfileID);
        setTitle("Showing: All");

        //ToolBar setup
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        toolbar.setTitleTextColor(getResources().getColor(R.color.TextOnLight));
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mFab = findViewById(R.id.btnFloat2);
        fm = new FileManager(getBaseContext());

        mDrawerLayout = findViewById(R.id.drawer_layout2);
        navigationView = findViewById(R.id.nav_view2);


        initialiseListView();
        setupFloatingButton();
        setupNavigationView();

        //May be some double ups
        loadProfileChanges();

    }



    private void setupFloatingButton(){
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuiler = new AlertDialog.Builder(HomeView.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_add_item, null);
                final EditText newApp = (EditText) mView.findViewById(R.id.txtAppName);
                final EditText newPass = (EditText) mView.findViewById(R.id.txtAppPass);
                final EditText newUser = (EditText) mView.findViewById(R.id.txtAppUser);
                final Spinner spnProfile = mView.findViewById(R.id.spnProfile);
                final Button add = (Button) mView.findViewById(R.id.btnAddApp);

                mBuiler.setView(mView);
                final AlertDialog diag = mBuiler.create();

                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(HomeView.this, android.R.layout.simple_spinner_item, profileList);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnProfile.setAdapter(spinnerAdapter);

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String appname, user, pass, profile;
                        appname = newApp.getText().toString();
                        user = newUser.getText().toString();
                        pass = newPass.getText().toString();
                        profile = spnProfile.getSelectedItem().toString();

                        if ( !appname.trim().equals("") && !user.trim().equals("")&& !pass.trim().equals("")){
                            final Items item = new Items(appname,user, pass, profile);

                            mViewModel.insert(item);
                            adapter.insert(item);

                            diag.cancel();
                            setDefaultText();
                            Toast.makeText(HomeView.this,"Addition successful",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(HomeView.this,"Please fill all fields",Toast.LENGTH_SHORT).show();
                        }


                    }
                });



                diag.show();

            }
        });
    }

    private void setupNavigationView(){

        setProfile();

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        if (menuItem.getItemId() == R.id.nav_export) {

                            try {
                                requestAppPermissions();
                                String data = convertEntriestoFileFormat(adapter.getList());
                                fm.write(data, "data.txt");
                                Toast.makeText(HomeView.this, "File Export successful", Toast.LENGTH_SHORT).show();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                            //Log.i("Profile: ", "Menu item import selected");
                        } else if (menuItem.getItemId() == R.id.nav_import) {
                            performFileSearch();

                        } else if (menuItem.getItemId() == R.id.nav_passwordGen) {
                            Intent intent = new Intent(HomeView.this, GeneratorView.class);
                            startActivity(intent);
                        }else if(menuItem.getItemId() == R.id.nav_add_profile){
                            Intent intent =  new Intent(HomeView.this, AddProfileView.class);
                            startActivityForResult(intent, ADD_PROFILE_REQ);
                        } else if (menuItem.getItemId() == R.id.nav_exit) {
                            finish();
                        }

                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeView.this, ProfileView.class);
                startActivityForResult(intent, EDIT_PROFILE_REQ);
            }
        });

    }

    public void initialiseListView(){
        view = findViewById(R.id.recyclerList);
        adapter = new HomeAdapter(HomeView.this,  this, mViewModel);
        view.setAdapter(adapter);
        view.setLayoutManager(new LinearLayoutManager(this));
        setDefaultText();
        profileList = mViewModel.getProfilesAsString();
    }

    public void setDefaultText(){
        TextView text = findViewById(R.id.lblEmpty2);
        if (adapter.getItemCount()  > 0){
            text.setVisibility(View.GONE);
        } else {
            text.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Fires an intent to spin up the "file chooser" UI and select a file.
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
            Uri intentUri;
            if (resultData != null) {
                intentUri = resultData.getData();

                final String result;

                result = fm.read(intentUri);

                if(result == null){
                     Toast.makeText(getBaseContext(),"File Read failed", Toast.LENGTH_LONG).show();
                    Log.i("Read/Write: " , "Result is null");
                } else {
                    Toast.makeText(getBaseContext(),"Reading Files", Toast.LENGTH_SHORT).show();
                    Log.i("Read/Write: " , result);
                    addImportsToDatabase(result);
                }
            }
        } else if( requestCode == ADD_PROFILE_REQ || requestCode == EDIT_PROFILE_REQ ){
            loadProfileChanges();
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
    private String convertEntriestoFileFormat(List<Items> list){
        String data = "";

        String header = username + "." + list.size() + ".";

        data = data + header + "\n";

        for (Items e : list){
            data = data + "{\n";
            data = data + "" + e.appname + "\n";
            data = data + "" + e.username + "\n";
            data = data + "" + e.password + "\n";
            data = data + "}\n";
        }


        return data;
    }

    private Boolean addImportsToDatabase(String data){
        String[] lines = data.split("\\r?\\n");
        String[] firstline = lines[0].split("\\.");

        //make sure its a valid data file
        if (!username.equals(firstline[0])){
            Toast.makeText(HomeView.this,"Reading Files Failed",Toast.LENGTH_SHORT).show();
            return  false;
        }


        String appname = "";
        String appuser = "";
        String appPass = "";
        for (int i = 1; i < lines.length; i++){

            //check for open bracket
            //TODO: remove Entry calls to uncleanse
            if (lines[i].equals("{")) {
                appname = Entry.uncleanseInput(lines[i + 1]) ;
                appuser = Entry.uncleanseInput(lines[i + 2]) ;
                appPass = Entry.uncleanseInput(lines[i + 3]) ;
                continue;
            }

            //check for close braket
            if(lines[i].equals("}")){
                Items item = new Items(appname,appuser,appPass,currentProfile.name);
                mViewModel.insert(item);
                adapter.insert(item);
            }

        }

        setDefaultText();
        Toast.makeText(HomeView.this,"Reading Files Completed",Toast.LENGTH_SHORT).show();
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


    private void setProfile(){
        // Set Button color and Profile Text
        View header = navigationView.getHeaderView(0);
        profileButton = header.findViewById(R.id.btnNavProfile);
        profileText = header.findViewById(R.id.txtNavProfile);
        profileButton.setBackground(Util.createCircle(currentProfile.color));
        profileButton.setText(currentProfile.firstLetter());
        profileText.setText(currentProfile.name);
    }

    public void loadProfileChanges(){
        currentProfileID = Preferences.getCurrentProfileID(HomeView.this);
        currentProfile = mViewModel.getProfileByID(currentProfileID);
        setProfile();
        profileList = mViewModel.getProfilesAsString();
        adapter.filterList(currentProfile);

        //Set the title
        if(currentProfile.name.equals("Default")){
            setTitle("Showing: All");
        } else {
            setTitle("Showing: " + currentProfile.name);
        }
    }

}
