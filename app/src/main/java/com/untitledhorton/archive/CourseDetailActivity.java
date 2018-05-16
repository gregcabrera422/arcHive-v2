package com.untitledhorton.archive;

import android.accounts.Account;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.classroom.ClassroomScopes;
import com.google.api.services.classroom.model.Announcement;
import com.google.api.services.classroom.model.ListAnnouncementsResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.untitledhorton.archive.Model.CourseAnnouncement;
import com.untitledhorton.archive.Utility.AnnouncementRecyclerAdapter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CourseDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AnnouncementRecyclerAdapter adapter;
    private ProgressBar progressBar;

    GoogleAccountCredential mCredential;

    private static final String[] SCOPES = {ClassroomScopes.CLASSROOM_COURSES_READONLY, ClassroomScopes.CLASSROOM_ANNOUNCEMENTS_READONLY,
                    ClassroomScopes.CLASSROOM_ANNOUNCEMENTS};
    ArrayList<CourseAnnouncement> courseAnnouncements;
    private String courseId;
    SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);
        recyclerView = findViewById(R.id.subjectRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = findViewById(R.id.progressBar);

        Bundle extras = getIntent().getExtras();
        courseId = extras.getString("courseId");

        mCredential = GoogleAccountCredential.usingOAuth2(
                CourseDetailActivity.this, Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        System.out.println("ID: " + courseId);

        getResultsFromApi();
    }

    private void getResultsFromApi() {
        mCredential.setSelectedAccount(new Account(FirebaseAuth.getInstance().getCurrentUser().getEmail(), "com.untitledhorton.archive2"));

        if (! isDeviceOnline()) {
            progressBar.setVisibility(View.INVISIBLE);
        } else {
            new CourseDetailActivity.MakeRequestTask(mCredential).execute();
        }
    }

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
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
            ListAnnouncementsResponse announcementsResponse = mService.courses().announcements()
                    .list(courseId)
                    .execute();

            List<Announcement> announcements = announcementsResponse.getAnnouncements();
            List<String> names = new ArrayList<String>();

            if (announcements != null) {
                for (Announcement announcement : announcements) {
                    names.add(announcement.getText());
                        //System.out.println("announcement results: " + announcement.getId());
                    courseAnnouncements.add(new CourseAnnouncement(announcement.getText(), announcement.getCreationTime()));
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
//                tvResult.setText("No results returned.");
            } else {
                adapter = new AnnouncementRecyclerAdapter(courseAnnouncements, CourseDetailActivity.this);
                recyclerView.setAdapter(adapter);
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
