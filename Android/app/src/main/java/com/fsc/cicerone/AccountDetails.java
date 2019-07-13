package com.fsc.cicerone;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import app_connector.ConnectorConstants;

/**
 * Class that specifying the account detail page
 */
public class AccountDetails extends AppCompatActivity {

    private static final String ERROR_TAG = "ERROR IN " + AccountDetails.class.getName();

    TabLayout tabLayout;
    FrameLayout frameLayout;
    Fragment fragment = null;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private static final int PERMISSION_REQUEST_CODE = 357;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        frameLayout = findViewById(R.id.frame);
        fragment = new ProfileFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
        tabLayout = findViewById(R.id.tabs);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.app_bar_account);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.app_bar_home:
                case R.id.app_bar_discover:
                case R.id.app_bar_wishlist:
                    Toast.makeText(AccountDetails.this, "Sorry, Work in Progress", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;

            }
            return false;
        });

        /* Set TextView username */
        TextView usernameText = findViewById(R.id.username);
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
                FragmentManager fm = getSupportFragmentManager();
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

    }

    private void setNameSurname(User currentLoggedUser) {
        String nameSurname = currentLoggedUser.getName() + " " + currentLoggedUser.getSurname();
        TextView nameSurnameTextView = findViewById(R.id.name_surname);
        nameSurnameTextView.setText(nameSurname);
        if (currentLoggedUser.getUserType() == UserType.GLOBETROTTER)
            tabLayout.removeTabAt(3);
    }

    public void requestUserData(View view) {
        checkPermission();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // show an alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(AccountDetails.this);
                    builder.setMessage(getString(R.string.external_storage_permission_required_message));
                    builder.setTitle(getString(R.string.please_grant_permission));
                    builder.setPositiveButton(getString(R.string.ok), (dialogInterface, i) -> ActivityCompat.requestPermissions(AccountDetails.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE));
                    builder.setNeutralButton(getString(R.string.cancel), null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    // Request permission
                    ActivityCompat.requestPermissions(AccountDetails.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
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
                Toast.makeText(this, getString(R.string.storage_permission_required), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void downloadUserData() {
        final WebView webView = new WebView(AccountDetails.this);
        webView.setWebViewClient(new WebViewClient());

        User currentLoggedUser = AccountManager.getCurrentLoggedUser();

        Uri uri = Uri.parse(ConnectorConstants.DOWNLOAD_USER_DATA)
                .buildUpon()
                .appendQueryParameter("username", currentLoggedUser.getUsername())
                .appendQueryParameter("password", currentLoggedUser.getPassword())
                .build();

        webView.loadUrl(uri.toString());
        RelativeLayout rootLayout = findViewById(R.id.accountDetailsRoot);
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
            DownloadManager dManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            dManager.enqueue(request);
            webView.destroy();
        });

    }
}
