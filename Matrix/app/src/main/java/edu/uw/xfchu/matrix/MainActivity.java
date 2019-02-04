package edu.uw.xfchu.matrix;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements EventFragment.OnItemSelectListener, CommentFragment.OnItemSelectedListener {

    private EventFragment mListFragment;
    private CommentFragment mGridFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        if (findViewById(R.id.fragment_container) != null) {
////            Fragment fragment = isTablet() ? new CommentFragment() : new EventFragment();
////            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
////        }

        //add ListView
        mListFragment = new EventFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.event_container, mListFragment).commit();


        //add GridView
        if (isTablet()) {
            mGridFragment = new CommentFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.comment_container, mGridFragment).commit();
        }
    }

    private boolean isTablet() {
        return (getApplicationContext().getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    @Override
    public void onItemSelected(int position) {
        if (!isTablet()) {
            Intent intent = new Intent(this, EventGridActivity.class);
            intent.putExtra("position", position);
            startActivity(intent);
        } else {
            mGridFragment.onItemSelected(position);
        }
    }

    @Override
    public void onCommentSelected(int position) {
        mListFragment.onItemSelected(position);
    }
}


