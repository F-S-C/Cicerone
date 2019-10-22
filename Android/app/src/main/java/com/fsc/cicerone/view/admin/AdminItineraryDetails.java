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

package com.fsc.cicerone.view.admin;

import android.os.Bundle;
import android.view.View;

import com.fsc.cicerone.R;
import com.fsc.cicerone.view.itinerary.ItineraryActivity;

/**
 * A class that displays the information of an Itinerary Admin-side.
 */
public class AdminItineraryDetails extends ItineraryActivity {

    /**
     * Empty Constructor.
     */
    public AdminItineraryDetails() {
        super();
        this.layout = R.layout.activity_admin_itinerary_details;
    }

    /**
     * A Constructor that takes a Layout as a parameter.
     * @param contentLayoutId The layout to set.
     */
    public AdminItineraryDetails(int contentLayoutId) {
        super(contentLayoutId);
        this.layout = R.layout.activity_admin_itinerary_details;
    }

    /**
     * A function that allows the Admin to visit the author the itinerary profile.
     * @param view The current View.
     */
    @Override
    public void goToAuthor(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("user", itinerary.getCicerone().toJSONObject().toString());
        startActivityWithData(AdminUserProfile.class, bundle);
    }

}
