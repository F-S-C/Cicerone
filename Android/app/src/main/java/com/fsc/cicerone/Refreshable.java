package com.fsc.cicerone;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public interface Refreshable {
    default void refresh(){
        refresh(null);
    }
    void refresh(@Nullable SwipeRefreshLayout swipeRefreshLayout);
}
