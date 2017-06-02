package com.android.popularmoviesstagetwo;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.popularmoviesstagetwo.fragments.FavoritesFragment;
import com.android.popularmoviesstagetwo.fragments.HighestRatedFragment;
import com.android.popularmoviesstagetwo.fragments.MostPopularFragment;
import com.android.popularmoviesstagetwo.sync.MovieSyncUtils;

public class MainPosterActivity extends AppCompatActivity {

    public static final int INDEX_MOVIE_ID = 0;
    public static final int INDEX_MOVIE_TITLE = 1;
    public static final int INDEX_MOVIE_POSTER = 2;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_most_popular:
                    selectedFragment = MostPopularFragment.newInstance();
                    break;
                case R.id.navigation_highest_rated:
                    selectedFragment = HighestRatedFragment.newInstance();
                    break;
                case R.id.navigation_favorites:
                    selectedFragment = FavoritesFragment.newInstance();
                    break;
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commit();
            return true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_poster);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, MostPopularFragment.newInstance());
        transaction.commit();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
}
