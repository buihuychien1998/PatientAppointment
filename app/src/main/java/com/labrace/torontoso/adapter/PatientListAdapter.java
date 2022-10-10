package com.labrace.torontoso.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.labrace.torontoso.R;
import com.labrace.torontoso.activity.MainNavActivity;
import com.labrace.torontoso.bean.Patient;
import com.labrace.torontoso.common.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dung on 4/14/2016.
 */
public class PatientListAdapter extends RecyclerView.Adapter<PatientListAdapter.PatientViewHolder> implements View.OnClickListener {
    private static final int HEADER_TYPE = 0;
    private static final int ROW_TYPE = 1;
    private List<Patient> patientList;
    private List<Integer> headersPosition;
    private Context mContext;

    public PatientListAdapter(Context context, List<Patient> patientList, List<Integer> headersPosition) {
        this.patientList = patientList;
        this.headersPosition = headersPosition;
        this.mContext = context;
    }

    @Override
    public PatientViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = null;
        if(i == HEADER_TYPE) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.patient_list_header, viewGroup, false);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.patient_list_item, viewGroup, false);
        }
        PatientViewHolder viewHolder = new PatientViewHolder(view, i);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PatientViewHolder customViewHolder, int i) {
        Patient patient = patientList.get(i);

        //Setting patient information
        if(customViewHolder.viewType == HEADER_TYPE) {
            customViewHolder.patientName.setText(patient.getFullName());
        } else {
            customViewHolder.patientId.setText(String.valueOf(patient.getId()));
            customViewHolder.patientName.setText(patient.getFullName());
            customViewHolder.patientDob.setText(patient.getDob());
            customViewHolder.patientGender.setText(patient.getSex());
            customViewHolder.patientBraClass.setText("BraClass: " + patient.getBraClass());
            customViewHolder.itemView.setTag(i);
            customViewHolder.itemView.setOnClickListener(this);
        }
    }

    @Override
    public int getItemCount() {
        return (null != patientList ? patientList.size() : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if(headersPosition.contains(position)) {
            return HEADER_TYPE;
        } else {
            return ROW_TYPE;
        }
    }

    @Override
    public void onClick(View view) {
        MainNavActivity activity = (MainNavActivity) mContext;
        int position = (int) view.getTag();
        Patient patient = patientList.get(position);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.PATIENT, patient);
        activity.openPage(Constants.SCREEN_PHOTO_LIST, bundle);
    }

    public class PatientViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.patientName)
        protected TextView patientName;

        @Nullable
        @BindView(R.id.patientId)
        protected TextView patientId;

        @Nullable
        @BindView(R.id.patientDob)
        protected TextView patientDob;

        @Nullable
        @BindView(R.id.patientGender)
        protected TextView patientGender;

        @Nullable
        @BindView(R.id.patientBraClass)
        protected TextView patientBraClass;

        protected int viewType;

        public PatientViewHolder(View view, int viewType) {
            super(view);
            this.viewType = viewType;
            ButterKnife.bind(this, itemView);
        }
    }
}
