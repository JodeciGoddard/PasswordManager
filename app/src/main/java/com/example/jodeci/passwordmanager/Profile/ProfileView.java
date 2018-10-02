package com.example.jodeci.passwordmanager.Profile;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.LinearLayout;

import com.example.jodeci.passwordmanager.Home.HomeAdapter;
import com.example.jodeci.passwordmanager.Home.HomeView;
import com.example.jodeci.passwordmanager.R;
import com.example.jodeci.passwordmanager.database.DataViewModel;

public class ProfileView extends AppCompatActivity {

    private DataViewModel mViewModel;
    private RecyclerView view;
    private ProfileAdapter adapter;

    ItemTouchHelper.SimpleCallback itemTouchHelperCallBack;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        mViewModel = ViewModelProviders.of(this).get(DataViewModel.class);

        //ToolBar setup
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        toolbar.setTitleTextColor(getResources().getColor(R.color.TextOnLight));
        toolbar.setTitle("Manage Profiles");
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);

        initialiseListView();
        setUpItemHelper();
        new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(view);

    }

    @Override
    public boolean onSupportNavigateUp(){
        //onBackPressed();
        finish();
        return true;
    }

    public void initialiseListView(){
        view = findViewById(R.id.profileRecyclerView);
        view.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration itemDeco = new DividerItemDecoration(view.getContext(), DividerItemDecoration.HORIZONTAL);
        view.addItemDecoration(itemDeco);
        adapter = new ProfileAdapter(ProfileView.this, mViewModel);
        view.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == ProfileAdapter.EDIT_PROFILE_REQ ) {
            adapter.notifyItemChanged();
        }
    }

    public void setUpItemHelper(){
        itemTouchHelperCallBack = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){


            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                if(viewHolder.getAdapterPosition() == 0) return 0;
                return super.getMovementFlags(recyclerView, viewHolder);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.removeAt(viewHolder.getAdapterPosition());
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                // view the background view
                View itemView = viewHolder.itemView;
                int itemHeight = itemView.getBottom() - itemView.getTop();

                //Draw red background;
                ColorDrawable bg = new ColorDrawable();
                bg.setColor(getResources().getColor(R.color.colorSecondary));
                bg.setBounds(itemView.getRight() + (int)dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                bg.draw(c);

                // Calculate position of delete icon
                Drawable deleteIcon = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_delete_white);
                int intrinsicWidth = deleteIcon.getIntrinsicWidth();
                int intrinsicHeight = deleteIcon.getIntrinsicHeight();
                int deleteIconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                int deleteIconMargin = (itemHeight - intrinsicHeight) / 2;
                int deleteIconLeft = itemView.getRight() - deleteIconMargin - intrinsicWidth;
                int deleteIconRight = itemView.getRight() - deleteIconMargin;
                int deleteIconBottom = deleteIconTop + intrinsicHeight;

                // Draw the delete icon
                deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
                deleteIcon.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
    }
}
