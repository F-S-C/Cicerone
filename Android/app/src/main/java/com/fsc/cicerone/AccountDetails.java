package com.fsc.cicerone;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import app_connector.ConnectorConstants;

/**
 * Class that specifying the account detail page
 */
public class AccountDetails extends Fragment {

    private static final String ERROR_TAG = "ERROR IN " + AccountDetails.class.getName();

    private TabLayout tabLayout;
    private Fragment fragment = null;

    private Activity context;

    private View holderView;

    private static final int PERMISSION_REQUEST_CODE = 357;

    AccountDetails() {
        // required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        holderView = inflater.inflate(R.layout.activity_account_details, container, false);

        context = Objects.requireNonNull(getActivity());

        fragment = new ProfileFragment();
        FragmentManager fragmentManager = Objects.requireNonNull(getFragmentManager());
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
        tabLayout = holderView.findViewById(R.id.tabs);

        /* Set TextView username */
        TextView usernameText = holderView.findViewById(R.id.username);
        User currentLoggedUser = AccountManager.getCurrentLoggedUser();
        String username = "@" + currentLoggedUser.getUsername();
        usernameText.setText(username);
        setNameSurname(currentLoggedUser); //Require data from server and set Name Surname

        /* TabLayout */
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new ProfileFragment();
                        break;
                    case 1:
                        fragment = new ReportFragment();
                        break;
                    case 2:
                        fragment = new ReviewFragment();
                        break;
                    case 3:
                        fragment = new ItineraryFragment();
                        break;
                    default:
                        break;
                }
                FragmentManager fm = Objects.requireNonNull(getFragmentManager());
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frame, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Do nothing
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Do nothing
            }

        });

        return holderView;
    }

    private void setNameSurname(User currentLoggedUser) {
        String nameSurname = currentLoggedUser.getName() + " " + currentLoggedUser.getSurname();
        TextView nameSurnameTextView = holderView.findViewById(R.id.name_surname);
        nameSurnameTextView.setText(nameSurname);
        if (currentLoggedUser.getUserType() == UserType.GLOBETROTTER)
            tabLayout.removeTabAt(3);
    }

    public void requestUserData(View view) {
        checkPermission();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // show an alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(getString(R.string.external_storage_permission_required_message));
                    builder.setTitle(getString(R.string.please_grant_permission));
                    builder.setPositiveButton(getString(R.string.ok), (dialogInterface, i) -> ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE));
                    builder.setNeutralButton(getString(R.string.cancel), null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    // Request permission
                    ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                }
            } else {
                downloadUserData();
            }
        } else {
            downloadUserData();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadUserData();
            } else {
                Toast.makeText(context, getString(R.string.storage_permission_required), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void downloadUserData() {
        final WebView webView = new WebView(context);
        webView.setWebViewClient(new WebViewClient());

        User currentLoggedUser = AccountManager.getCurrentLoggedUser();

        Uri uri = Uri.parse(ConnectorConstants.DOWNLOAD_USER_DATA)
                .buildUpon()
                .appendQueryParameter("username", currentLoggedUser.getUsername())
                .appendQueryParameter("password", currentLoggedUser.getPassword())
                .build();

        webView.loadUrl(uri.toString());
        RelativeLayout rootLayout = holderView.findViewById(R.id.accountDetailsRoot);
        rootLayout.addView(webView);

        webView.setDownloadListener((url, userAgent, contentDescription, mimetype, contentLength) -> {
            // The download request
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.allowScanningByMediaScanner();

            // Set the notification visibility
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            // Set the destination on the device
            String fileName = URLUtil.guessFileName(url, contentDescription, mimetype);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

            // Enqueue the download
            DownloadManager dManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            dManager.enqueue(request);
            webView.destroy();
        });

    }
}
