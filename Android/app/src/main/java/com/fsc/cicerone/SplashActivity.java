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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.model.UserType;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final Class activityToOpenIfLogged = MainActivity.class;
        final Class activityToOpenIfNotLogged = LoginActivity.class;
        final Class activityToOpenIfLoggedAdmin = AdminMainActivity.class;

        SharedPreferences preferences = getSharedPreferences("com.fsc.cicerone", Context.MODE_PRIVATE);
        String latestLoggedUserCredentials = preferences.getString("session", "");

        if (latestLoggedUserCredentials != null && !latestLoggedUserCredentials.equals("")) {
            try {
                JSONObject currentLoggedUser = new JSONObject(latestLoggedUserCredentials);
                if (!currentLoggedUser.getString("username").equals("") && !currentLoggedUser.getString("password").equals("")) {
                    AccountManager.attemptLogin(this, currentLoggedUser.getString("username"), currentLoggedUser.getString("password"), () -> {
                        // Do nothing
                    }, (result, success) -> {
                        Intent intent = new Intent(SplashActivity.this,
                                (success) ?
                                        (AccountManager.getCurrentLoggedUser().getUserType() == UserType.ADMIN) ?
                                                activityToOpenIfLoggedAdmin :
                                                activityToOpenIfLogged :
                                        activityToOpenIfNotLogged);
                        startActivity(intent);
                        finish();
                    });
                }
            } catch (JSONException e) {
                startActivity(new Intent(SplashActivity.this, activityToOpenIfNotLogged));
                finish();
            }
        } else {
            startActivity(new Intent(SplashActivity.this, activityToOpenIfNotLogged));
            finish();
        }


    }
}
