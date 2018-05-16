package com.untitledhorton.archive.Fragment;

import android.accounts.Account;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.classroom.ClassroomScopes;
import com.google.api.services.classroom.model.Course;
import com.google.api.services.classroom.model.ListCoursesResponse;
import com.google.api.services.classroom.model.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.untitledhorton.archive.Model.ClassCourse;
import com.untitledhorton.archive.R;
import com.untitledhorton.archive.Utility.ClassRecyclerAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import yalantis.com.sidemenu.interfaces.ScreenShotable;

/**
 * Created by Greg on 10/03/2018.
 */

public class ClassFragment extends Fragment implements ScreenShotable {

    private View Fragmentone_view;
    private Bitmap bitmap;
    GoogleAccountCredential mCredential;

    private static final String[] SCOPES = { ClassroomScopes.CLASSROOM_COURSES_READONLY};

    ArrayList<ClassCourse> classCourses;
    private RecyclerView recyclerView;
    private ClassRecyclerAdapter adapter;
    private ProgressBar progressBar;

    public static ClassFragment newInstance() {
        ClassFragment classFrag = new ClassFragment();
        return classFrag;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_class, container, false);

        recyclerView = rootView.findViewById(R.id.subjectRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        progressBar = rootView.findViewById(R.id.progressBar);

        mCredential = GoogleAccountCredential.usingOAuth2(
                getActivity(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        getResultsFromApi();

        return rootView;
    }



    private void getResultsFromApi() {
        mCredential.setSelectedAccount(new Account(FirebaseAuth.getInstance().getCurrentUser().getEmail(), "com.untitledhorton.archive2"));

        if (! isDeviceOnline()) {
            progressBar.setVisibility(View.INVISIBLE);
        } else {
            new ClassFragment.MakeRequestTask(mCredential).execute();
        }
    }

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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
            ListCoursesResponse response = mService.courses().list()
                    .execute();

            List<Course> courses = response.getCourses();
            List<String> names = new ArrayList<String>();
            classCourses = new ArrayList<>();

            if (courses != null) {
                for (Course course : courses) {
                    names.add(course.getName());

                    if (course.getCourseState().equals("ACTIVE")) {
                        classCourses.add(new ClassCourse(course.getId(), course.getName(),"teacher name"));
                    }
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
                adapter = new ClassRecyclerAdapter(classCourses, getActivity());
                recyclerView.setAdapter(adapter);
            }
        }

    }

    @Override
    public void takeScreenShot() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Bitmap bitmap = Bitmap.createBitmap(Fragmentone_view.getWidth(),
                        Fragmentone_view.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                Fragmentone_view.draw(canvas);
                ClassFragment.this.bitmap = bitmap;
            }
        };

        thread.start();

    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.Fragmentone_view = view.findViewById(R.id.container);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
