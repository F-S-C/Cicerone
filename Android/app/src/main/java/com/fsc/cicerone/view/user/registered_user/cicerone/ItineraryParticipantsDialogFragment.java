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

package com.fsc.cicerone.view.user.registered_user.cicerone;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fsc.cicerone.R;
import com.fsc.cicerone.adapter.UserListAdapter;
import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.manager.ReservationManager;
import com.fsc.cicerone.model.Itinerary;
import com.fsc.cicerone.model.Reservation;
import com.fsc.cicerone.model.User;
import com.fsc.cicerone.model.UserType;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * A class that displays the participants list of an Itinerary as a dialog.
 */
public class ItineraryParticipantsDialogFragment extends DialogFragment {

    /**
     * @see androidx.fragment.app.DialogFragment#onCreateDialog(Bundle)
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Context context = Objects.requireNonNull(getActivity());
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        RecyclerView recyclerView;
        Itinerary itinerary;

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_users_list, null);
        Bundle bundle = getArguments();

        recyclerView = view.findViewById(R.id.user_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        if (AccountManager.getCurrentLoggedUser().getUserType() == UserType.CICERONE) {
            String s = Objects.requireNonNull(bundle).getString("itinerary");
            itinerary = new Itinerary(s);
            ReservationManager.getReservations(getActivity(), itinerary, list -> {
                List<User> participants = new LinkedList<>();
                for (Reservation reservation : list) {
                    if (reservation.isConfirmed())
                        participants.add(reservation.getClient());
                }
                if (participants.isEmpty()) {
                    TextView message = new TextView(getActivity());
                    message.setText(getActivity().getString(R.string.no_participants));
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    int margin = (int) getActivity().getResources().getDimension(R.dimen.material_card_spacing);
                    params.setMargins(0, margin, 0, margin);
                    message.setLayoutParams(params);
                    LinearLayout linearLayout = view.findViewById(R.id.user_list_container);
                    linearLayout.addView(message);
                }
                UserListAdapter adapter = new UserListAdapter(getActivity(), participants);
                recyclerView.setAdapter(adapter);
            });
        }
        builder.setView(view);

        builder.setTitle(Objects.requireNonNull(getActivity()).getString(R.string.participants_list));
        builder.setNeutralButton(getActivity().getString(R.string.ok), null);

        return builder.create();
    }

    /**
     * Empty Constructor.
     */
    public ItineraryParticipantsDialogFragment() {
        // Required empty constructor
    }

    /**
     * A function that creates a new Instance of the Fragment, setting the Itinerary as a Bundle.
     *
     * @param itinerary The current Itinerary.
     * @return The Fragment with the Itinerary set in a Bundle.
     */
    public static ItineraryParticipantsDialogFragment newInstance(Itinerary itinerary) {

        Bundle args = new Bundle();
        args.putString("itinerary", itinerary.toString());

        ItineraryParticipantsDialogFragment fragment = new ItineraryParticipantsDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
