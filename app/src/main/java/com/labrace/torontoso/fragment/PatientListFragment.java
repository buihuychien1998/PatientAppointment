package com.labrace.torontoso.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.labrace.torontoso.R;
import com.labrace.torontoso.activity.MainNavActivity;
import com.labrace.torontoso.adapter.PatientListAdapter;
import com.labrace.torontoso.bean.Patient;
import com.labrace.torontoso.common.Constants;
import com.labrace.torontoso.common.SortType;
import com.labrace.torontoso.service.TorsoCADService;
import com.labrace.torontoso.utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;

public class PatientListFragment extends Fragment {
    public static final int ADD_NEW_PATIENT = 999;

    @BindView(R.id.addPatient)
    FloatingActionButton addPatient;

    @BindView(R.id.patientList)
    RecyclerView patientListView;

    @BindView(R.id.patientScroller)
    VerticalRecyclerViewFastScroller patientScroller;

    private MainNavActivity activity;

    private List<Integer> headersPosition;

    private List<Patient> mPatientListWithHeaders;

    private List<Patient> mPatientListWithoutHeaders;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_patient_list, null);
        ButterKnife.bind(this, root);

        activity = (MainNavActivity) getActivity();

        setUpPatientList();

        setHasOptionsMenu(true);

        return root;
    }

    private void setUpPatientList() {
        // Set layoutManager for list
        patientListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Connect the recycler to the scroller (to let the scroller scroll the list)
        patientScroller.setRecyclerView(patientListView);

        // Connect the scroller to the recycler (to let the recycler scroll the scroller's handle)
        patientListView.setOnScrollListener(patientScroller.getOnScrollListener());

        new LoadPatientsTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        activity.getSupportActionBar().setTitle("Patients");
    }

    @OnClick(R.id.addPatient)
    public void onClickAddPatient() {
        activity.openPage(Constants.SCREEN_NEW_PATIENT);
    }

    private List<Patient> orderPatients(List<Patient> patientList) {
        Collections.sort(patientList, new Comparator<Patient>() {
            public int compare(Patient v1, Patient v2) {
                return v1.getFullName().toUpperCase().compareTo(v2.getFullName().toUpperCase());
            }
        });
        List<Patient> result = new ArrayList<>();
        headersPosition = new ArrayList<>();
        if(patientList.size() > 0) {
            Character firstChar = patientList.get(0).getFullName().charAt(0);
            Patient header = new Patient();
            header.setFirstName(firstChar.toString().toUpperCase());
            result.add(header);
            headersPosition.add(0);
        }
        for (int i = 0; i < patientList.size(); i++) {
            result.add(patientList.get(i));
            if(i != patientList.size() - 1) {
                Character firstChar = patientList.get(i).getFullName().charAt(0);
                Character nextFirstChar = patientList.get(i + 1).getFullName().charAt(0);
                if(!firstChar.toString().equalsIgnoreCase(nextFirstChar.toString())) {
                    Patient header = new Patient();
                    header.setFirstName(nextFirstChar.toString().toUpperCase());
                    result.add(header);
                    headersPosition.add(result.size() - 1);
                }
            }
        }
        return result;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_patient_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_sign_out:
                activity.signOut();
                activity.finish();
                break;
            case R.id.action_sort_id:
                sortPatients(SortType.ID);
                break;
            case R.id.action_sort_first_name:
                sortPatients(SortType.FIRST_NAME);
                break;
            case R.id.action_sort_last_name:
                sortPatients(SortType.LAST_NAME);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sortPatients(SortType sortType) {
        if(mPatientListWithHeaders != null && mPatientListWithHeaders.size() > 0) {
            PatientListAdapter patientListAdapter = null;
            switch (sortType) {
                case ID:
                    Collections.sort(mPatientListWithoutHeaders, new Comparator<Patient>() {
                        public int compare(Patient v1, Patient v2) {
                            return v2.getId() - v1.getId();
                        }
                    });
                    patientListAdapter = new PatientListAdapter(getActivity(), mPatientListWithoutHeaders, new ArrayList<Integer>());
                    break;
                case FIRST_NAME:
                    Collections.sort(mPatientListWithHeaders, new Comparator<Patient>() {
                        public int compare(Patient v1, Patient v2) {
                            return v1.getFullName().toUpperCase().compareTo(v2.getFullName().toUpperCase());
                        }
                    });
                    patientListAdapter = new PatientListAdapter(getActivity(), mPatientListWithHeaders, headersPosition);
                    break;
                case LAST_NAME:
                    Collections.sort(mPatientListWithoutHeaders, new Comparator<Patient>() {
                        public int compare(Patient v1, Patient v2) {
                            return v1.getLastName().toUpperCase().compareTo(v2.getLastName().toUpperCase());
                        }
                    });
                    patientListAdapter = new PatientListAdapter(getActivity(), mPatientListWithoutHeaders, new ArrayList<Integer>());
                    break;
            }
            patientListView.setAdapter(patientListAdapter);
        }
    }

    private class LoadPatientsTask extends AsyncTask<String, Void, List<Patient>> {
        @Override
        protected void onPreExecute() {
            activity.showProgress(R.string.loading_patients);
        }

        @Override
        protected List<Patient> doInBackground(String... params) {
            // Load patient images from Database
            String userId = PreferencesUtils.loadData(getActivity(), Constants.USER_ID);
            List<Patient> patientList = null;
            if(userId != null){
                patientList = TorsoCADService.loadPatientsForUser(Integer.valueOf(userId));
            }

            if(patientList != null) {
                mPatientListWithoutHeaders = patientList;
                List<Patient> patientListWithHeader = orderPatients(patientList);
                mPatientListWithHeaders = patientListWithHeader;
                return patientListWithHeader;
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Patient> patientList) {
            if(patientList != null) {
                PatientListAdapter patientListAdapter = new PatientListAdapter(getActivity(), patientList, headersPosition);
                patientListView.setAdapter(patientListAdapter);
            }
            activity.hideProgress();
        }
    }
}
