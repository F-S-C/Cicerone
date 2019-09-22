package com.fsc.cicerone;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public interface Refreshable {
    default void requireData(){
        requireData(null);
    }
    void requireData(@Nullable SwipeRefreshLayout swipeRefreshLayout);
}
