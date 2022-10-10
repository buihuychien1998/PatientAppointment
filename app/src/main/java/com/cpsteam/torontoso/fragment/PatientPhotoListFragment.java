package com.cpsteam.torontoso.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.cpsteam.torontoso.R;
import com.cpsteam.torontoso.activity.MainNavActivity;
import com.cpsteam.torontoso.adapter.PatientImagesAdapter;
import com.cpsteam.torontoso.bean.Image;
import com.cpsteam.torontoso.bean.Patient;
import com.cpsteam.torontoso.common.Constants;
import com.cpsteam.torontoso.service.FTPService;
import com.cpsteam.torontoso.service.TorsoCADService;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cpsteam.torontoso.billing.PurchaseManager.productIds;

public class PatientPhotoListFragment extends Fragment {

    private static final int CAMERA_SELECT_PICTURE = 0;

    private static final int GALLERY_SELECT_PICTURE = 1;

    @BindView(R.id.imageList)
    RecyclerView imageListView;

    @BindView(R.id.textEmpty)
    TextView textEmpty;

    private MainNavActivity activity;

    private String mCurrentPhotoPath;

    private int torsoId;

    private Patient patient;

    private LoadImagesTask loadImagesTask;

    private UploadImageTask uploadImageTask;

    private LoadTorsoIdTask loadTorsoIdTask;

    private BillingClient billingClient;

    final List<SkuDetails> skuDetailsList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_patient_images, null);
        ButterKnife.bind(this, root);

        activity = (MainNavActivity) getActivity();
        activity.invalidateToolbarForOtherPage();


        billingClient = BillingClient.newBuilder(requireContext())
                .enablePendingPurchases()
                .setListener((billingResult, list) -> {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
                        for (Purchase purchase : list) {
                            if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                                handlePurchase(purchase);
                                Toast.makeText(activity.getApplicationContext(), activity.getResources().getString(R.string.purchase_successful), Toast.LENGTH_SHORT).show();
                                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                            }
                        }
                        // Query for existing user purchases
                        // Query for products for sale
                        Log.d(PatientPhotoListFragment.class.getSimpleName(), "Billing client successfully set up!");
                    }
                    Log.d(PatientPhotoListFragment.class.getSimpleName(), "billingResult: " + billingResult.getResponseCode());
                })
                .build();

        //TODO: Connect ứng dụng của bạn với Google Billing
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                //TODO: Sau khi connect thành công, thử lấy thông tin các sản phẩm
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    Log.d(PatientPhotoListFragment.class.getSimpleName(), "Billing client successfully set up!");
                    // Query for existing user purchases
                    // Query for products for sale
                }
                queryProducts();
            }

            @Override
            public void onBillingServiceDisconnected() {
                //TODO: Connect Google Play not success
                Log.d(PatientPhotoListFragment.class.getSimpleName(), "Billing service disconnected");
            }

        });

//    }
//        iapConnector.addPurchaseListener(new PurchaseServiceListener() {
//            public void onPricesUpdated(@NotNull Map iapKeyPrices) {
//                Log.d("TAG", "onPricesUpdated: " + iapKeyPrices);
//            }
//
//            public void onProductPurchased(@NonNull DataWrappers.PurchaseInfo purchaseInfo) {
//
//            }
//
//            public void onProductRestored(@NonNull DataWrappers.PurchaseInfo purchaseInfo) {
//                Log.d("TAG", "onProductRestored: " + purchaseInfo);
//            }
//        });


//        setHasOptionsMenu(true);
        setUpData();
        return root;
    }

    private void queryProducts() {
        if (!billingClient.isReady()) {
            Log.d(PatientPhotoListFragment.class.getSimpleName(), "queryPurchases: BillingClient is not ready");
        }
        // TODO: tạo list các product id (chính là product id bạn đã nhập ở bước trước) để lấy thông tin
//        List<String> productIds = new ArrayList<>();
//        productIds.add("thanh_hoa");
//        productIds.add("nghe_an");
//        productIds.add("ha_tinh");
//        productIds.add("quang_binh");
//        productIds.add("quang_tri");
//        productIds.add("thua_thien_hue");
        List<String> skusList = new ArrayList<>(productIds.values());
        SkuDetailsParams skuDetailsParams = SkuDetailsParams.newBuilder()
                .setSkusList(skusList)
                .setType(BillingClient.SkuType.INAPP)
                //TODO: Sử dụng INAPP với one-time product và SUBS với các gói subscriptions.
                .build();

        // TODO: Thực hiện query
        // Query for existing in app products that have been purchased. This does NOT include subscriptions.

        billingClient.querySkuDetailsAsync(skuDetailsParams, (billingResult, list) -> {
            Log.d(PatientPhotoListFragment.class.getSimpleName(), skusList.size() + " skusList");
            if (list != null && !list.isEmpty()) {
                Log.d(PatientPhotoListFragment.class.getSimpleName(), list.size() + "");
                //Đã lấy được thông tin các sản phẩm đang bán theo các product id ở trên
                skuDetailsList.clear();
                skuDetailsList.addAll(list);
//                for (SkuDetails skuDetails : list) {
//                    // matches the last text surrounded by parentheses at the end of the SKU title
//                    items.get(getItemIndexByTitle(skuDetails.getSku())).setCost(skuDetails.getPrice());
//                }
//                adapter.notifyDataSetChanged();
            } else {
                Log.d(PatientPhotoListFragment.class.getSimpleName(), "No existing in app purchases found.");
            }
        });
    }

    void handlePurchase(Purchase purchase) {
        // Purchase retrieved from BillingClient#queryPurchasesAsync or your PurchasesUpdatedListener.
//        Purchase purchase = ...;

        // Verify the purchase.
        // Ensure entitlement was not already granted for this purchaseToken.
        // Grant entitlement to the user.

        ConsumeParams consumeParams =
                ConsumeParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        .build();

        ConsumeResponseListener listener = (billingResult, purchaseToken) -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                // Handle the success of the consume operation.
            }
        };

        billingClient.consumeAsync(consumeParams, listener);
    }



    private void setUpData() {
        Bundle bundle = getArguments();
        patient = bundle.getParcelable(Constants.PATIENT);
        activity.getSupportActionBar().setTitle(patient.getFullName());

        // Set layoutManager for list
        imageListView.setLayoutManager(new LinearLayoutManager(getActivity()));

//        loadImagesTask = new LoadImagesTask();
//        loadImagesTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        PatientImagesAdapter patientImagesAdapter = new PatientImagesAdapter(getActivity(), patient.getImages(), textEmpty,
                torsoId, skuDetailsList, billingClient);
        imageListView.setAdapter(patientImagesAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_patient_image, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_add_photo:
                showPhotoSourcePopup(activity.findViewById(R.id.action_settings));
                break;
            case R.id.action_delete_photo:
//                activateDeleteMode();
                Toast.makeText(activity.getApplicationContext(), "This feature is coming soon", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void activateDeleteMode() {
        PatientImagesAdapter patientImagesAdapter = (PatientImagesAdapter) imageListView.getAdapter();
        if (patientImagesAdapter != null) {
            patientImagesAdapter.setIsDeleteMode(true);
            imageListView.getAdapter().notifyDataSetChanged();
            imageListView.invalidate();
        }
    }

    private void showPhotoSourcePopup(View anchor) {
        PopupMenu popup = new PopupMenu(getActivity(), anchor);
        popup.getMenuInflater().inflate(R.menu.menu_add_photo, popup.getMenu());
        makePopForceShowIcon(popup);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_camera:
                        openCamera();
                        break;
                    case R.id.action_gallery:
                        openGallery();
                        break;
                }
                return true;
            }
        });
        popup.show();
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(getActivity(), getString(R.string.error_camera_open), Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                takePictureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                startActivityForResult(takePictureIntent, CAMERA_SELECT_PICTURE);
            }
        } else {
            Toast.makeText(getActivity(), getString(R.string.error_camera_open), Toast.LENGTH_SHORT).show();
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), GALLERY_SELECT_PICTURE);
    }

    private void makePopForceShowIcon(PopupMenu popupMenu) {
        try {
            Field mFieldPopup = popupMenu.getClass().getDeclaredField("mPopup");
            mFieldPopup.setAccessible(true);
            MenuPopupHelper mPopup = (MenuPopupHelper) mFieldPopup.get(popupMenu);
            mPopup.setForceShowIcon(true);
        } catch (Exception e) {
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CAMERA_SELECT_PICTURE:
                    if (mCurrentPhotoPath != null) {
                        Toast.makeText(getActivity(), "Selected image : " + mCurrentPhotoPath, Toast.LENGTH_SHORT).show();
                        uploadImage(mCurrentPhotoPath);
                    }
                    break;
                case GALLERY_SELECT_PICTURE:
                    Uri selectedImageUri = data.getData();
                    String selectedImagePath = getImagePath(selectedImageUri);
                    if (selectedImagePath != null) {
                        Toast.makeText(getActivity(), "Selected image : " + selectedImagePath, Toast.LENGTH_SHORT).show();
                        uploadImage(selectedImagePath);
                    }
                    break;
            }
        }
    }

    private void uploadImage(String imagePath) {
        uploadImageTask = new UploadImageTask();
        uploadImageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, imagePath);
    }

    public String getImagePath(Uri uri) {
        Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String documentId = cursor.getString(0);
        documentId = documentId.substring(documentId.lastIndexOf(":") + 1);
        cursor.close();

        cursor = activity.getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{documentId}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private class UploadImageTask extends AsyncTask<String, Void, Boolean> {
        private Image image;

        @Override
        protected void onPreExecute() {
            Toast.makeText(getActivity(), getString(R.string.uploading), Toast.LENGTH_LONG).show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            final String imagePath = params[0];
            try {
                boolean isUploaded = new FTPService(String.valueOf(torsoId)).uploadImage(imagePath);
                if (isUploaded) {
                    image = new Image();
                    image.setTorsoId(torsoId);
                    File file = new File(imagePath);
                    image.setImagePath(file.getName());
                    boolean isAdded = TorsoCADService.addNewImage(image);
                    if (isAdded) {
                        return true;
                    }
                }
            } catch (IOException e) {
                Toast.makeText(getActivity(), getString(R.string.ftp_service_error), Toast.LENGTH_LONG).show();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                PatientImagesAdapter patientImagesAdapter = (PatientImagesAdapter) imageListView.getAdapter();
                patientImagesAdapter.getImages().add(0, image);
                patient.setImages(patientImagesAdapter.getImages());
                patientImagesAdapter.notifyItemInserted(0);
                ((LinearLayoutManager)imageListView.getLayoutManager()).scrollToPositionWithOffset(0, 0);
                textEmpty.setVisibility(View.GONE);
            } else {
                Toast.makeText(getActivity(), "Upload image failed!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class LoadImagesTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            activity.showProgress(R.string.loading_images);
        }

        @Override
        protected String doInBackground(String... params) {
            // Load patient images from Database
            TorsoCADService.loadImagesForPatient(patient);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // If no images yet, show empty message
            if (patient.getImages() != null && patient.getImages().size() == 0) {
                textEmpty.setVisibility(View.VISIBLE);
                activity.hideProgress();
            } else {
                textEmpty.setVisibility(View.GONE);
            }
            loadTorsoIdTask = new LoadTorsoIdTask();
            loadTorsoIdTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    private class LoadTorsoIdTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Integer doInBackground(String... params) {
            // Load image from FTP server
            torsoId = TorsoCADService.getTorsoIdByPatientId(patient.getId());
            if (torsoId == -1) {
                return -1;
            } else {
                return torsoId;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result == -1) {
                Toast.makeText(getActivity(), "This patient does not have TorsoCAD Measurements.\nImages could not be uploaded", Toast.LENGTH_SHORT).show();
            } else if (result == -2) {
                Toast.makeText(getActivity(), "FTP processing failed!", Toast.LENGTH_SHORT).show();
            } else {
                if (patient.getImages() != null) {
                    new ProcessImageTask().execute();
                }
            }
        }
    }

    private class ProcessImageTask extends AsyncTask<String, Void, List<String>> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected List<String> doInBackground(String... params) {
            // Load image from FTP server
            try {
                // Get list of image from FTP
                List<String> images = new FTPService(String.valueOf(torsoId)).getImageLst();
                return images;
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<String> ftpImages) {
            if (ftpImages != null) {
                List<String> sqlImages = new ArrayList<>();
                for (int i = 0; i < patient.getImages().size(); i++) {
                    sqlImages.add(patient.getImages().get(i).getImagePath());
                }

                // Delete images on FTP if they are not exist on SQL server
                for (int i = 0; i < ftpImages.size(); i++) {
                    String imageName = ftpImages.get(i);
                    if (!sqlImages.contains(imageName)) {
                        // Try to delete the image which is not existing
                        new DeleteImageTask().execute(imageName);
                    }
                }

                // Delete images on SQL server if they are not exist on FTP server
                for (int i = 0; i < sqlImages.size(); i++) {
                    String imageName = sqlImages.get(i);
                    if (!ftpImages.contains(imageName)) {
                        // Try to delete the image which is not existing
                        for (Image image: patient.getImages()) {
                            if(image.getImagePath().equals(imageName)) {
                                TorsoCADService.deleteImage(image);
                                patient.getImages().remove(image);
                                break;
                            }
                        }
                    }
                }
            }
            PatientImagesAdapter patientImagesAdapter = new PatientImagesAdapter(getActivity(), patient.getImages(), textEmpty, torsoId,
                    skuDetailsList, billingClient);
            imageListView.setAdapter(patientImagesAdapter);
            activity.hideProgress();
        }
    }

    private class DeleteImageTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(String... params) {
            String imagePath = params[0];
            try {
                return new FTPService(String.valueOf(torsoId)).deleteImage(imagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        activity.hideProgress();
        if (loadImagesTask != null) {
            loadImagesTask.cancel(true);
        }
        if (loadTorsoIdTask != null) {
            loadTorsoIdTask.cancel(true);
        }
        PatientImagesAdapter patientImagesAdapter = (PatientImagesAdapter) imageListView.getAdapter();
        if (patientImagesAdapter != null && patientImagesAdapter.getLoadImageTasks() != null) {
            for (PatientImagesAdapter.LoadImageTask loadImageTask : patientImagesAdapter.getLoadImageTasks()) {
                loadImageTask.cancel(true);
            }
        }
    }
}
