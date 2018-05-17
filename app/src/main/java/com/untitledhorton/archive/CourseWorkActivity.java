package com.untitledhorton.archive;

import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.classroom.ClassroomScopes;
import com.google.api.services.classroom.model.Announcement;
import com.google.api.services.classroom.model.CourseWork;
import com.google.api.services.classroom.model.ListAnnouncementsResponse;
import com.google.api.services.classroom.model.ListCourseWorkResponse;
import com.untitledhorton.archive.Model.CourseAnnouncement;
import com.untitledhorton.archive.Model.CourseWorks;
import com.untitledhorton.archive.Utility.AnnouncementRecyclerAdapter;
import com.untitledhorton.archive.Utility.CourseWorkRecyclerAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class CourseWorkActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private RecyclerView recyclerView;
    private CourseWorkRecyclerAdapter adapter;
    private ProgressBar progressBar;
    private TextView lblClass, tvEmpty;

    GoogleAccountCredential mCredential;

    ArrayList<CourseWorks> courseWorks;
    private String courseId, className, photoURL, courseTeacher;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    private static final String PREF_ACCOUNT_NAME = "accountName";

    String[] SCOPES = {ClassroomScopes.CLASSROOM_COURSES_READONLY, ClassroomScopes.CLASSROOM_COURSEWORK_ME_READONLY,
            ClassroomScopes.CLASSROOM_COURSEWORK_ME, ClassroomScopes.CLASSROOM_COURSEWORK_STUDENTS };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);
        recyclerView = findViewById(R.id.subjectRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = findViewById(R.id.progressBar);
        tvEmpty = findViewById(R.id.tvEmpty);
        lblClass = findViewById(R.id.lblClass);

        Bundle extras = getIntent().getExtras();
        courseId = extras.getString("courseId");

        className = extras.getString("courseName", className);
        photoURL = extras.getString("photoURL", photoURL);
        courseTeacher = extras.getString("courseTeacher", courseTeacher);

        lblClass.setText(className);
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        System.out.println("ID: " + courseId);

        getResultsFromApi();
    }

    private void getResultsFromApi() {
        if (! isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (! isDeviceOnline()) {
            Toast.makeText(CourseWorkActivity.this, "No network connection available.", Toast.LENGTH_LONG);
        } else {
            new MakeRequestTask(mCredential).execute();
        }
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                this, android.Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    android.Manifest.permission.GET_ACCOUNTS);
        }
    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    Toast.makeText(CourseWorkActivity.this, "This app requires Google Play Services. " +
                            "Please install Google Play Services on your device and relaunch this app.", Toast.LENGTH_LONG);
                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
    }

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                CourseWorkActivity.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
        private com.google.api.services.classroom.Classroom mService = null;
        private Exception mLastError = null;

        MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.classroom.Classroom.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Classroom API Android Quickstart")
                    .build();
        }

        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        private List<String> getDataFromApi() throws IOException {
            List<String> names = new ArrayList<String>();

            ListCourseWorkResponse announcementsResponse = mService.courses().courseWork()
                    .list(courseId)
                    .execute();

            List<CourseWork> works = announcementsResponse.getCourseWork();
            courseWorks = new ArrayList<>();

            if (works != null) {
                for (CourseWork work : works) {
                    names.add(work.getDescription());

                    String newDate =
                            work.getCreationTime().substring(0,work.getCreationTime().lastIndexOf("T"));

                    courseWorks.add(new CourseWorks(work.getTitle(), work.getDescription(), newDate, courseTeacher, photoURL));

                }
            }

            return names;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(List<String> output) {
            progressBar.setVisibility(View.INVISIBLE);
            if (output == null || output.size() == 0) {
                progressBar.setVisibility(View.INVISIBLE);
                tvEmpty.setVisibility(View.VISIBLE);
                tvEmpty.setText("No Course Works");
            } else {
                adapter = new CourseWorkRecyclerAdapter(courseWorks, CourseWorkActivity.this);
                recyclerView.setAdapter(adapter);
            }
        }

        @Override
        protected void onCancelled() {
            progressBar.setVisibility(View.INVISIBLE);
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            CourseWorkActivity.REQUEST_AUTHORIZATION);
                } else {
                    Toast.makeText(CourseWorkActivity.this, "The following error occurred: "+ mLastError.getMessage(), Toast.LENGTH_LONG);
                }
            } else {
                Toast.makeText(CourseWorkActivity.this, "Request cancelled.", Toast.LENGTH_LONG);
            }
        }
    }

}
