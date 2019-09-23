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
import android.app.DatePickerDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fsc.cicerone.adapter.ItineraryAdapter;
import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.Itinerary;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import app_connector.ConnectorConstants;
import app_connector.SendInPostConnector;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverFragment extends Fragment implements Refreshable {
    /**
     * The context (the host activity) of the fragment.
     */
    private Activity context;

    private RecyclerView recyclerView;
    private Button setEndingDateBtn;
    private Button setBeginningDateBtn;

    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    private SearchView.OnCloseListener onCloseListener;

    private Date beginningDate = null;
    private Date endingDate = null;
    private String location = null;
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public DiscoverFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        context = Objects.requireNonNull(getActivity());


        recyclerView = view.findViewById(R.id.itinerary_list);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));

        setBeginningDateBtn = view.findViewById(R.id.itinerary_search_beginning_date_btn);
        setBeginningDateBtn.setOnClickListener(this::setBeginningDate);

        setEndingDateBtn = view.findViewById(R.id.itinerary_search_ending_date_btn);
        setEndingDateBtn.setOnClickListener(this::setEndingDate);
        setEndingDateBtn.setEnabled(false);

        Button clearBtn = view.findViewById(R.id.itinerary_search_clear_btn);
        clearBtn.setOnClickListener(this::clearAllFields);

        refresh();

        return view;
    }

    /**
     * Clear all the search parameters' fields (except the location search bar)
     *
     * @param view See {@link View.OnClickListener#onClick(View)}.
     */
    @SuppressWarnings("WeakerAccess")
    public void clearAllFields(@SuppressWarnings("unused") View view) {
        setButtonStyle(setBeginningDateBtn, R.style.Widget_MaterialComponents_Button_OutlinedButton);
        setBeginningDateBtn.setText(getString(R.string.beginning_date));
        beginningDate = null;

        setButtonStyle(setEndingDateBtn, R.style.Widget_MaterialComponents_Button_OutlinedButton);
        setEndingDateBtn.setText(getString(R.string.ending_date));
        endingDate = null;

        refresh();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.discover_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) context.getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(context.getComponentName()));
            searchView.setIconifiedByDefault(true);
            searchView.findViewById(R.id.search_close_btn)
                    .setOnClickListener(v -> {
                        searchView.setQuery("", false);
                        searchView.setIconified(true);
                    });
            EditText searchEditText = searchView.findViewById(R.id.search_src_text);
            searchEditText.setHint(getString(R.string.search_location));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    // Do nothing
                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    location = !query.isEmpty() ? query : null;
                    refresh();
                    return true;
                }
            };

            onCloseListener = () -> {
                location = null;
                DiscoverFragment.this.refresh();
                return false;
            };

            searchView.setOnQueryTextListener(queryTextListener);
            searchView.setOnCloseListener(onCloseListener);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            // Not implemented here
            return false;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        searchView.setOnCloseListener(onCloseListener);
        return super.onOptionsItemSelected(item);
    }

    /**
     * Set the beginning date.
     *
     * @param view See {@link View.OnClickListener#onClick(View)}.
     */
    @SuppressWarnings("WeakerAccess")
    public void setBeginningDate(@SuppressWarnings("unused") View view) {
        showDatePicker(setBeginningDateBtn, context.getString(R.string.beginning_date), false);
    }

    /**
     * Set the ending date.
     *
     * @param view See {@link View.OnClickListener#onClick(View)}.
     */
    @SuppressWarnings("WeakerAccess")
    public void setEndingDate(@SuppressWarnings("unused") View view) {
        showDatePicker(setEndingDateBtn, context.getString(R.string.ending_date), true);
    }

    private void showDatePicker(Button button, String defaultLabel, boolean isEnding) {
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener onDateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            setButtonStyle(button, R.style.Widget_MaterialComponents_Button);
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
            button.setText(sdf.format(myCalendar.getTime()));

            if (isEnding)
                endingDate = myCalendar.getTime();
            else {
                setEndingDateBtn.setEnabled(true);
                beginningDate = myCalendar.getTime();
                if (endingDate != null && endingDate.before(beginningDate)) {
                    endingDate = beginningDate;
                    setEndingDateBtn.setText(sdf.format(endingDate));
                }
            }
        };

        DatePickerDialog picker = new DatePickerDialog(context, onDateSetListener, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        picker.setOnCancelListener(dialog -> {
            setButtonStyle(button, R.style.Widget_MaterialComponents_Button_OutlinedButton);
            button.setText(defaultLabel);


            endingDate = null;
            if (!isEnding) {
                beginningDate = null;
                setEndingDateBtn.setEnabled(false);
                setButtonStyle(setEndingDateBtn, R.style.Widget_MaterialComponents_Button_OutlinedButton);
                setEndingDateBtn.setText(getString(R.string.ending_date));
            }
        });

        picker.setOnDismissListener(dialog -> refresh());

        if (!isEnding || beginningDate == null)
            picker.getDatePicker().setMinDate(new Date().getTime());
        else
            picker.getDatePicker().setMinDate(beginningDate.getTime());

        picker.show();
    }

    private void setButtonStyle(Button button, int style) {
        if (style == R.style.Widget_MaterialComponents_Button) {
            button.setBackgroundColor(ContextCompat.getColor(context, button.isEnabled() ? R.color.colorPrimary : android.R.color.darker_gray));
            button.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        } else if (style == R.style.Widget_MaterialComponents_Button_OutlinedButton) {
            button.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
            button.setTextColor(ContextCompat.getColor(context, button.isEnabled() ? R.color.colorPrimary : android.R.color.darker_gray));
        }
    }

    @Override
    public void refresh(@Nullable SwipeRefreshLayout swipeRefreshLayout) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        Map<String, Object> object = new HashMap<>(3);
        if (beginningDate != null) object.put("beginning_date", sdf.format(beginningDate));
        if (endingDate != null) object.put("beginning_date", sdf.format(endingDate));
        if (location != null) object.put("location", location);
        SendInPostConnector<Itinerary> connector = new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_ITINERARY, BusinessEntityBuilder.getFactory(Itinerary.class))
                .setContext(context)
                .setOnStartConnectionListener(() -> {
                    if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(true);
                })
                .setOnEndConnectionListener(list -> {
                    if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
                    List<Itinerary> filteredList = new LinkedList<>();
                    for (Itinerary itinerary : list) {
                        if (!itinerary.getCicerone().equals(AccountManager.getCurrentLoggedUser()))
                            filteredList.add(itinerary);
                    }
                    ItineraryAdapter adapter = new ItineraryAdapter(getActivity(), filteredList, this);

                    recyclerView.setAdapter(adapter);
                })
                .setObjectToSend(object)
                .build();
        connector.execute();
    }
}
