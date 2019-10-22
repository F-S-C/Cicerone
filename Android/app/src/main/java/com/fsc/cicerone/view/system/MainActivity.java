/*
 * Copyright 2019 FSC - Five Students of Computer Science
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fsc.cicerone.view.system;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.fsc.cicerone.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

/**
 * The Main Activity used in the system Cicerone.
 */
public abstract class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    /**
     * The currently displayed fragment.
     */
    protected Fragment activeFragment;

    /**
     * The bottom navigation.
     */
    protected BottomNavigationView navView;

    protected FragmentPagerAdapter fragmentPagerAdapter;
    protected ViewPager viewPager;
    protected MenuItem prevMenuItem;

    protected SwipeRefreshLayout swipeRefreshLayout;

    protected int layout;

    /**
     * Empty Constructor.
     */
    public MainActivity() {
        super();
    }

    /**
     * A Constructor that set a Layout taken as a Parameter.
     *
     * @param contentLayoutId The Layout to set.
     */
    public MainActivity(int contentLayoutId) {
        super(contentLayoutId);
    }

    /**
     * @see android.app.Activity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout);

        swipeRefreshLayout = findViewById(R.id.main_swipe_refresh);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (activeFragment instanceof Refreshable) {
                ((Refreshable) activeFragment).refresh(swipeRefreshLayout);
            } else {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        navView = findViewById(R.id.main_bottom_navigation);
        navView.setOnNavigationItemSelectedListener(this);

        setUpViewPager();
    }

    /**
     * A function that sets the View Pager.
     */
    @SuppressLint("ClickableViewAccessibility")
    private void setUpViewPager() {
        viewPager = findViewById(R.id.main_container);

        setupFragmentAdapter();

        activeFragment = fragmentPagerAdapter.getItem(0);
        viewPager.setAdapter(fragmentPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            /**
             * This method will be invoked when the current page is scrolled, either as part
             * of a programmatically initiated smooth scroll or a user initiated touch scroll.
             *
             * @param position Position index of the first page currently being displayed.
             *                 Page position+1 will be visible if positionOffset is nonzero.
             * @param positionOffset Value from [0, 1) indicating the offset from the page at position.
             * @param positionOffsetPixels Value in pixels indicating the offset from position.
             */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Do nothing
            }

            /**
             * This method will be invoked when a new page becomes selected. Animation is not
             * necessarily complete.
             *
             * @param position Position index of the new selected page.
             */
            @Override
            public void onPageSelected(int position) {

                ActionBar supportActionBar = Objects.requireNonNull(getSupportActionBar());
                supportActionBar.setDisplayHomeAsUpEnabled(position != 0);
                supportActionBar.setDisplayShowHomeEnabled(position != 0);
                supportActionBar.setTitle(fragmentPagerAdapter.getPageTitle(position));

                activeFragment = fragmentPagerAdapter.getItem(position);

                if (prevMenuItem != null)
                    prevMenuItem.setChecked(false);
                else
                    navView.getMenu().getItem(0).setChecked(false);

                navView.getMenu().getItem(position < 2 ? position : position + 1).setChecked(true);
                prevMenuItem = navView.getMenu().getItem(position < 2 ? position : position + 1);
            }

            /**
             * Called when the scroll state changes. Useful for discovering when the user
             * begins dragging, when the pager is automatically settling to the current page,
             * or when it is fully stopped/idle.
             *
             * @param state The new scroll state.
             * @see ViewPager#SCROLL_STATE_IDLE
             * @see ViewPager#SCROLL_STATE_DRAGGING
             * @see ViewPager#SCROLL_STATE_SETTLING
             */
            @Override
            public void onPageScrollStateChanged(int state) {
                // Do nothing
            }
        });

        // fix to make the ViewPager work with SwipeRefreshLayout
        viewPager.setOnTouchListener((v, event) -> {
            swipeRefreshLayout.setEnabled(false);
            if (event.getAction() == MotionEvent.ACTION_UP) {
                swipeRefreshLayout.setEnabled(true);
            }
            return false;
        });
    }

    /**
     * A function that sets the Fragment Adapter.
     */
    protected abstract void setupFragmentAdapter();

    /**
     * @see AppCompatActivity#onSupportNavigateUp()
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * @see Activity#onBackPressed()
     */
    @Override
    public void onBackPressed() {
        if (activeFragment == fragmentPagerAdapter.getItem(0)) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle(getString(R.string.are_you_sure))
                    .setMessage(getString(R.string.sure_to_exit))
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, (arg0, arg1) -> super.onBackPressed())
                    .create()
                    .show();
        } else {
            navView.setSelectedItemId(R.id.navigation_home);
        }
    }
}
