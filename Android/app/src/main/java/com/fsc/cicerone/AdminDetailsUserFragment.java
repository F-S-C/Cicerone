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

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.model.User;
import com.fsc.cicerone.model.UserType;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;


/**
 * Class that contains the details of the user to whom the administrator is interested.
 */
public class AdminDetailsUserFragment extends Fragment {

    private TextView documentNumber;
    private TextView documentType;
    private TextView documentExpiryDate;
    private DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    private Button removeUser;
    private Activity context;
    private User user;
    private TextView name;
    private TextView surname;
    private TextView email;
    private TextView cellphone;
    private TextView birthDate;
    private TextView avgEarn;
    private TextView sex;

    /**
     * Empty constructor
     */
    public AdminDetailsUserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = Objects.requireNonNull(getActivity());

        View view = inflater.inflate(R.layout.activity_admin_details_user_fragment, container, false);
        name = view.findViewById(R.id.name_users_admin);
        surname = view.findViewById(R.id.surname_user_admin);
        email = view.findViewById(R.id.email_user_admin);
        cellphone = view.findViewById(R.id.cellphone_user_admin);
        birthDate = view.findViewById(R.id.bday_user_admin);
        documentNumber = view.findViewById(R.id.nrDoc_user_admin);
        documentType = view.findViewById(R.id.typeDoc_user_admin);
        documentExpiryDate = view.findViewById(R.id.dateEx_user_admin);
        avgEarn = view.findViewById(R.id.avg_earn);
        sex = view.findViewById(R.id.sex_user_admin);
        removeUser = view.findViewById(R.id.remove_user_admin);
        Bundle bundle = getArguments();

        user = new User(Objects.requireNonNull(bundle).getString("user"));

        getDataUser(user);

        removeUser.setOnClickListener(v -> deleteAccount(user));

        return view;
    }

    private void getDataUser(User user) {
        if (user.getUserType() == UserType.CICERONE) {
            avgEarn.setVisibility(View.VISIBLE);
            AccountManager.userAvgEarnings(context, user.getUsername(), avgEarn);
        }

        String data = "Name: " + user.getName();
        name.setText(data);
        data = "Surname: " + user.getSurname();
        surname.setText(data);
        data = "Email: " + user.getEmail();
        email.setText(data);
        data = "Cellphone: " + user.getCellphone();
        cellphone.setText(data);
        data = "Birth Date: " + outputFormat.format(user.getBirthDate());
        birthDate.setText(data);
        data = "Sex: " + user.getSex().toString();
        sex.setText(data);
        data = "Document Number: " + user.getDocument().getNumber();
        documentNumber.setText(data);
        data = "Document Type: " + user.getDocument().getType();
        documentType.setText(data);
        data = "Expiry Date: " + outputFormat.format(user.getDocument().getExpirationDate());
        documentExpiryDate.setText(data);
    }

    private void deleteAccount(User user) {
        DialogInterface.OnClickListener positiveClickListener = (dialog, which) -> {
            AccountManager.deleteAccount(context, user , success -> Toast.makeText(context,
                    context.getString(R.string.removed_user), Toast.LENGTH_SHORT)
                    .show());
            removeUser.setEnabled(false);
        };
        new MaterialAlertDialogBuilder(context)
                .setTitle(context.getString(R.string.are_you_sure))
                .setMessage(context.getString(R.string.sure_to_delete_account))
                .setPositiveButton(context.getString(R.string.yes), positiveClickListener)
                .setNegativeButton(context.getString(R.string.no), null)
                .show();
    }
}
