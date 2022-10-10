package com.labrace.torontoso.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.labrace.torontoso.R;
import com.labrace.torontoso.activity.MainNavActivity;
import com.labrace.torontoso.common.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PatientPhotoDetailFragment extends Fragment {

    @BindView(R.id.patientImage)
    ImageView patientImage;

    private MainNavActivity activity;

    PhotoViewAttacher mAttacher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_image_detail, null);
        ButterKnife.bind(this, root);

        activity = (MainNavActivity) getActivity();
        activity.invalidateToolbarForOtherPage();

        setUpData();

        return root;
    }

    private void setUpData() {
        Bundle bundle = getArguments();
        Bitmap image = bundle.getParcelable(Constants.IMAGE);

        //Load patient image
        patientImage.setImageBitmap(image);

        // Attach a PhotoViewAttacher, which takes care of all of the zooming functionality.
        // (not needed unless you are going to change the drawable later)
        mAttacher = new PhotoViewAttacher(patientImage);

        activity.getSupportActionBar().setTitle("Viewer");
    }
}
