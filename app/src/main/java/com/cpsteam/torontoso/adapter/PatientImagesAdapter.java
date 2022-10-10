package com.cpsteam.torontoso.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.SkuDetails;
import com.cpsteam.torontoso.R;
import com.cpsteam.torontoso.activity.MainNavActivity;
import com.cpsteam.torontoso.bean.Image;
import com.cpsteam.torontoso.billing.PurchaseManager;
import com.cpsteam.torontoso.common.Constants;
import com.cpsteam.torontoso.service.FTPService;
import com.cpsteam.torontoso.service.TorsoCADService;
import com.cpsteam.torontoso.utils.CommonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dung on 4/14/2016.
 */
public class PatientImagesAdapter extends RecyclerView.Adapter<PatientImagesAdapter.PatientViewHolder> implements View.OnClickListener {
    private List<Image> mImages;
    private Context mContext;
    private boolean isDeleteMode;
    private TextView mTextEmpty;
    private MainNavActivity mActivity;
    private int mTorsoId;
    private ArrayList<LoadImageTask> loadImageTasks = new ArrayList<>();
    private List<SkuDetails> skuDetailsList;
    private BillingClient billingClient;
    private String imageID;

    public PatientImagesAdapter(Context context, List<Image> images, TextView textEmpty, int mTorsoId,
                                List<SkuDetails> skuDetailsList, BillingClient billingClient) {
        this.mContext = context;
        Image image = new Image();
        Bitmap bm = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.beachs);
        image.setBitmap(bm);
        Image image1 = new Image();
        Bitmap bm1 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.autumn);
        image1.setBitmap(bm1);
        Image image2 = new Image();
        Bitmap bm2 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.forest);
        image2.setBitmap(bm2);
        Image image3 = new Image();
        Bitmap bm3 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.sunset);
        image3.setBitmap(bm3);
        images = new ArrayList<>();
        images.add(image);
        images.add(image1);
        images.add(image2);
        images.add(image3);
        this.mImages = images;

        this.mTextEmpty = textEmpty;
        this.mTorsoId = mTorsoId;
        this.billingClient = billingClient;
        this.skuDetailsList = skuDetailsList;



    }

    public List<Image> getImages() {
        return mImages;
    }

    @Override
    public PatientViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_list_item, null);
        PatientViewHolder viewHolder = new PatientViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PatientViewHolder customViewHolder, int i) {
        String imageName = mImages.get(i).getImagePath();

        Bitmap loadedBitmap = mImages.get(i).getBitmap();
        if(loadedBitmap == null) {
            Bitmap bm = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.loading);
            customViewHolder.patientImage.setImageBitmap(bm);
            customViewHolder.patientImage.setTag(mImages.get(i).getImageId());
            LoadImageTask loadImageTask = new LoadImageTask(customViewHolder.patientImage, mTorsoId, mImages.get(i));
            loadImageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, imageName);
            loadImageTasks.add(loadImageTask);
        } else {
            customViewHolder.patientImage.setImageBitmap(loadedBitmap);
        }

        customViewHolder.itemView.setTag(i);
        customViewHolder.itemView.setOnClickListener(this);
        customViewHolder.delete.setTag(mImages.get(i));
        customViewHolder.delete.setOnClickListener(this);

        if(isDeleteMode) {
            customViewHolder.mask.setVisibility(View.VISIBLE);
            customViewHolder.delete.setVisibility(View.VISIBLE);
        } else {
            customViewHolder.mask.setVisibility(View.GONE);
            customViewHolder.delete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return (null != mImages ? mImages.size() : 0);
    }

    @Override
    public void onClick(View view) {
        List<String> productIds = new ArrayList<>();
        productIds.add("beachs");
        productIds.add("autumn");
        productIds.add("forest");
        productIds.add("sunset");
        mActivity = (MainNavActivity) mContext;
        if (skuDetailsList.isEmpty()) {
            Toast.makeText(mActivity.getApplicationContext(), "This feature is coming soon", Toast.LENGTH_SHORT).show();
            return;
        }
//        Log.d(ActivityBuySignatureWorkoutEventHandler.class.getSimpleName(), "buy: " + GorillaApp.currentWorkout);
        int skuIndex = -1;
        for (int index = 0; index < skuDetailsList.size(); index++) {
            if (skuDetailsList.get(index).getSku().equals(PurchaseManager.productIds.get(productIds))) {
                skuIndex = index;
                break;
            }
        }
        if (skuIndex == -1) {
            Toast.makeText(mActivity.getApplicationContext(), "Price not found for this item!", Toast.LENGTH_SHORT).show();
            return;
        }
        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(skuDetailsList.get(skuIndex))
                .build();

        BillingResult result = billingClient.launchBillingFlow(mActivity, billingFlowParams);
        if(view.getId() == R.id.delete) {
            Image image = (Image) view.getTag();
            new DeleteImageTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, image);
        } else {
            ImageView patientImage = (ImageView) view.findViewById(R.id.patientImage);
            BitmapDrawable bitmapDrawable = (BitmapDrawable) patientImage.getDrawable();
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.IMAGE, bitmapDrawable.getBitmap());
            mActivity.openPage(Constants.SCREEN_PHOTO_DETAIL, bundle);
        }
    }

    public void removeAt(int position) {
        mImages.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mImages.size());
    }

    public class PatientViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.patientImage)
        protected ImageView patientImage;

        @Nullable
        @BindView(R.id.mask)
        protected View mask;

        @Nullable
        @BindView(R.id.delete)
        protected ImageView delete;

        public PatientViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setIsDeleteMode(boolean isDeleteMode) {
        this.isDeleteMode = isDeleteMode;
    }

    private class DeleteImageTask extends AsyncTask<Image, Void, Boolean> {
        private Image image;

        @Override
        protected void onPreExecute() {
            mActivity.showProgress(R.string.deleting);
        }

        @Override
        protected Boolean doInBackground(Image... params) {
            image = params[0];
            try {
                boolean isDeleted = new FTPService(String.valueOf(mTorsoId)).deleteImage(image.getImagePath());
                if(isDeleted) {
                    isDeleted = TorsoCADService.deleteImage(image);
                    if(isDeleted) {
                        return true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result) {
                removeAt(mImages.indexOf(image));
                if(mImages.size() == 0 && mTextEmpty != null) {
                    mTextEmpty.setVisibility(View.VISIBLE);
                }
                Toast.makeText(mContext, "Image is deleted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "Delete image failed. Image is not existed on server!", Toast.LENGTH_SHORT).show();
            }
            mActivity.hideProgress();
        }
    }

    public class LoadImageTask extends AsyncTask<String, Void, Bitmap> {

        private ImageView imageView;

        private int mTorsoId;

        private Image mImage;

        public LoadImageTask(ImageView imageView, int torsoId, Image image) {
            this.imageView = imageView;
            this.mTorsoId = torsoId;
            this.mImage = image;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            if(!isCancelled()) {
                String imageName = params[0];
                Bitmap bitmap = null;
                try {
                    bitmap = new FTPService(String.valueOf(mTorsoId)).loadImage(imageName);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

                bitmap = CommonUtils.resize(bitmap, Constants.IMAGE_MAX_SIZE, Constants.IMAGE_MAX_SIZE);

                return bitmap;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if(result != null) {
                int tag = (int) imageView.getTag();
                if(tag == mImage.getImageId()) {
                    imageView.setImageBitmap(result);
                    mImage.setBitmap(result);
                }
            }
        }
    }

    public ArrayList<LoadImageTask> getLoadImageTasks() {
        return loadImageTasks;
    }
}
