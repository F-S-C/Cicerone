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

package com.fsc.cicerone;

import android.os.Bundle;
import android.view.View;

public class AdminItineraryDetails extends ItineraryActivity {

    public AdminItineraryDetails() {
        super();
        this.layout = R.layout.activity_admin_itinerary_details;
    }

    public AdminItineraryDetails(int contentLayoutId) {
        super(contentLayoutId);
        this.layout = R.layout.activity_admin_itinerary_details;
    }

    @Override
    public void goToAuthor(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("user", itinerary.getCicerone().toJSONObject().toString());
        startActivityWithData(AdminUserProfile.class, bundle);
    }

}
