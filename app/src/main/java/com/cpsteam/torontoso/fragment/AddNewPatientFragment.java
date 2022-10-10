package com.cpsteam.torontoso.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.cpsteam.torontoso.R;
import com.cpsteam.torontoso.activity.MainNavActivity;
import com.cpsteam.torontoso.bean.Patient;
import com.cpsteam.torontoso.common.Constants;
import com.cpsteam.torontoso.common.HyphoKyphoType;
import com.cpsteam.torontoso.common.RaceType;
import com.cpsteam.torontoso.common.ServiceType;
import com.cpsteam.torontoso.common.SexType;
import com.cpsteam.torontoso.service.TorsoCADService;
import com.cpsteam.torontoso.utils.PreferencesUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTouch;
import fr.ganfra.materialspinner.MaterialSpinner;

public class AddNewPatientFragment extends Fragment {

    @BindView(R.id.firstName)
    EditText firstName;

    @BindView(R.id.lastName)
    EditText lastName;

    @BindView(R.id.dob)
    MaterialSpinner dob;

    @BindView(R.id.sex)
    MaterialSpinner sex;

    @BindView(R.id.race)
    MaterialSpinner race;

    @BindView(R.id.service)
    MaterialSpinner service;

    @BindView(R.id.braceClass)
    MaterialSpinner braceClass;

    @BindView(R.id.hyphoKypho)
    MaterialSpinner hyphoKypho;

    @BindView(R.id.cancel)
    Button cancel;

    @BindView(R.id.add)
    Button add;

    private MainNavActivity activity;

    private boolean isValid = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_add_new_patient, null);
        ButterKnife.bind(this, root);

        activity = (MainNavActivity) getActivity();
        activity.invalidateToolbarForOtherPage();

        setUpInputFields();
        return root;
    }

    private void setUpInputFields() {
        String[] dobGroup = getResources().getStringArray(R.array.dob_group);
        String[] sexGroup = getResources().getStringArray(R.array.sex_group);
        String[] raceGroup = getResources().getStringArray(R.array.race_group);
        String[] serviceGroup = getResources().getStringArray(R.array.service_group);
        String[] braceGroup = getResources().getStringArray(R.array.brace_group);
        String[] hkGroup = getResources().getStringArray(R.array.hk_group);
        setUpSpinner(dobGroup, dob);
        setUpSpinner(sexGroup, sex);
        setUpSpinner(raceGroup, race);
        setUpSpinner(serviceGroup, service);
        setUpSpinner(braceGroup, braceClass);
        setUpSpinner(hkGroup, hyphoKypho);

        // Set default value for all field
        dob.setSelection(1);
        sex.setSelection(1);
        race.setSelection(1);
        service.setSelection(1);
        braceClass.setSelection(1);
        hyphoKypho.setSelection(1);

        activity.getSupportActionBar().setTitle("Add Patient");
    }

    private void setUpSpinner(String[] items, MaterialSpinner spinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @OnItemSelected(R.id.dob)
    public void onItemSelectedDob(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        // We have bug, spinner will be invalidated after calling setSelection
        // So if we setError come along with calling setSelection, spinner will lost the error state
        // This will be a hack for this case
        if(!isValid) {
            dob.setError(R.string.date_field_invalid);
        }
    }

    @OnTouch(R.id.dob)
    public boolean onTouchDob(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            DatePickerDialog datePickerDialog = new DatePickerDialog();

            datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

                    int month = monthOfYear + 1;

                    String selectedMonth;
                    String selectedDay;

                    if (month < 10) {
                        selectedMonth = "0" + month;
                    } else {
                        selectedMonth = String.valueOf(month);
                    }

                    if (dayOfMonth < 10) {
                        selectedDay = "0" + dayOfMonth;
                    } else {
                        selectedDay = String.valueOf(dayOfMonth);

                    }

                    String date = selectedMonth + "/" + selectedDay + "/" + year;

                    Date now = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    String nowStr = dateFormat.format(now);
                    try {
                        String[] dobGroup = {date};
                        setUpSpinner(dobGroup, dob);
                        dob.setSelection(1);
                        if (dateFormat.parse(nowStr).compareTo(dateFormat.parse(date)) < 0) {
                            dob.requestFocus();
                            dob.setError(R.string.date_field_invalid);
                            isValid = false;
                        } else {
                            dob.clearFocus();
                            dob.setError(null);
                            isValid = true;
                        }
                    } catch (ParseException e) {
                        dob.requestFocus();
                        dob.setError(R.string.date_field_invalid);
                    }
                }
            });
            datePickerDialog.show(getActivity().getFragmentManager(), "Datepickerdialog");
        }
        return true;
    }

    @OnClick(R.id.cancel)
    public void onClickCancel() {
        activity.onBackPressed();
    }

    @OnClick(R.id.add)
    public void onClickAdd() {
        Patient patient = new Patient();
        int valid = validateInput();
        if(valid == 0) {
            patient.setFirstName(firstName.getText().toString());
            patient.setLastName(lastName.getText().toString());
            patient.setDob(dob.getSelectedItem().toString());
            patient.setSex(SexType.valueOf(sex.getSelectedItem().toString()).getValue());
            String raceStr = race.getSelectedItem().toString();
            patient.setRace(RaceType.valueOf(raceStr.substring(4, raceStr.length())).getValue());
            patient.setUserId(Integer.valueOf(PreferencesUtils.loadData(getActivity(), Constants.USER_ID)));
            patient.setService(ServiceType.valueOf(service.getSelectedItem().toString()).getValue());
            patient.setBraClass(braceClass.getSelectedItem().toString());
            patient.setHyphoKypho(HyphoKyphoType.valueOf(hyphoKypho.getSelectedItem().toString()).getValue());

            new AddPatientTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, patient);
        }
    }

    private int validateInput() {
        int valid = 0;
        String emptyError = getString(R.string.patient_error_empty);
        if(TextUtils.isEmpty(firstName.getText().toString().trim())) {
            valid++;
            firstName.setError(String.format(emptyError, getString(R.string.patient_first_name)));
        }
        if(TextUtils.isEmpty(lastName.getText().toString().trim())) {
            valid++;
            lastName.setError(String.format(emptyError, getString(R.string.patient_last_name)));
        }

        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String nowStr = dateFormat.format(now);
        String dobStr = dob.getSelectedItem().toString().trim();
        try {
            if (dateFormat.parse(nowStr).compareTo(dateFormat.parse(dobStr)) < 0) {
                dob.setError(R.string.date_field_invalid);
                valid++;
            }
        } catch (ParseException e) {
        }
        if(TextUtils.isEmpty(dob.getSelectedItem().toString().trim()) || dob.getSelectedItemPosition() == 0) {
            valid++;
            dob.setError(String.format(emptyError, getString(R.string.patient_dob)));
        }
        if(TextUtils.isEmpty(sex.getSelectedItem().toString()) || sex.getSelectedItemPosition() == 0) {
            valid++;
            sex.setError(String.format(emptyError, getString(R.string.patient_sex)));
        }
        if(TextUtils.isEmpty(race.getSelectedItem().toString().trim()) || race.getSelectedItemPosition() == 0) {
            valid++;
            race.setError(String.format(emptyError, getString(R.string.patient_race)));
        }
        if(TextUtils.isEmpty(service.getSelectedItem().toString().trim()) || service.getSelectedItemPosition() == 0) {
            valid++;
            service.setError(String.format(emptyError, getString(R.string.patient_service)));
        }
        if(TextUtils.isEmpty(braceClass.getSelectedItem().toString().trim()) || braceClass.getSelectedItemPosition() == 0) {
            valid++;
            braceClass.setError(String.format(emptyError, getString(R.string.patient_brace_class)));
        }
        if(TextUtils.isEmpty(hyphoKypho.getSelectedItem().toString().trim()) || hyphoKypho.getSelectedItemPosition() == 0) {
            valid++;
            hyphoKypho.setError(String.format(emptyError, getString(R.string.patient_hypho_kypho)));
        }
        return valid;
    }

    private class AddPatientTask extends AsyncTask<Patient, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            activity.showProgress(R.string.adding);
        }

        @Override
        protected Boolean doInBackground(Patient... params) {
            Patient patient = params[0];

            // Load patient images from Database
            boolean result = TorsoCADService.addNewPatient(patient);
            if(result) {
                result = TorsoCADService.addNewMeasurement(patient);
                return result;
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result) {
                Toast.makeText(getActivity(), "Add new patient success!", Toast.LENGTH_SHORT).show();
                activity.openPage(Constants.SCREEN_PATIENT_LIST);
            } else {
                Toast.makeText(getActivity(), "Add new patient failed!", Toast.LENGTH_SHORT).show();
            }
            activity.hideProgress();
        }
    }
}
