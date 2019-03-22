package com.scubasnsi.mysnsi.controllers.logbook.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.scubasnsi.R;
import com.scubasnsi.mysnsi.app.MyApplication;
import com.scubasnsi.mysnsi.app.listeners.UpdateLogbookHeader;
import com.scubasnsi.mysnsi.app.utilities.GenericDialogs;
import com.scubasnsi.mysnsi.model.data_models.Logbook;
import com.scubasnsi.mysnsi.model.sessions.UserSession;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import shiva.joshi.common.logger.AppLogger;
import shiva.joshi.common.utilities.AppDirectoryImpl;
import shiva.joshi.common.utilities.CommonGenericDialogs;
import shiva.joshi.common.utilities.FileUtilities;

import static android.app.Activity.RESULT_OK;
import static shiva.joshi.common.CommonConstants.BUNDLE_SERIALIZED_OBJECT;
import static shiva.joshi.common.utilities.CommonGenericDialogs.REQUEST_IMAGE_CAPTURE;
import static shiva.joshi.common.utilities.CommonGenericDialogs.RESULT_LOAD_IMAGE;


@RuntimePermissions
public class DiveSitePictureFragment extends Fragment {

    public static final String TAG = "com.snsi.controllers.logbbook.fragments.DiveSitePictureFragment";

    private Context mContext;
    private UserSession mUserSession;
    private AppDirectoryImpl mAppDirectory;

    @BindString(R.string.app_name)
    protected String mAppName;

    @BindString(R.string.dive_site_image_title)
    protected String mTitle;

    @BindView(R.id.logback_dive_site_image)
    ImageView mImVDiveSiteImage;

    private String mCameraImagePath;
    private UpdateLogbookHeader mUpdateLogbookHeader;
    private BottomSheetDialog mBottomSheetDialog;
    private Logbook mLogbook;
    private ImageLoader mImageLoader;

    public static DiveSitePictureFragment newInstance(Logbook logbook) {
        DiveSitePictureFragment fragment = new DiveSitePictureFragment();
        Bundle args = new Bundle();
        args.putSerializable(BUNDLE_SERIALIZED_OBJECT, logbook);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UpdateLogbookHeader) {
            mUpdateLogbookHeader = (UpdateLogbookHeader) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement UpdateHomeHeader");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();

        mUserSession = MyApplication.getApplicationInstance().getUserSession();
        mImageLoader = MyApplication.getApplicationInstance().getImageLoaderInstance(mContext, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_dive_site_image, container, false);

        ButterKnife.bind(this, view);
        if (getArguments() != null) {
            mLogbook = (Logbook) getArguments().getSerializable(BUNDLE_SERIALIZED_OBJECT);
        }
        mUpdateLogbookHeader.OnUpdateHeader(true, mTitle);
        return view;

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!mLogbook.getDiveCenterLogo().isEmpty())
            mImageLoader.displayImage(mLogbook.getDiveCenterLogo(), mImVDiveSiteImage);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mUpdateLogbookHeader = null;
        if (mBottomSheetDialog != null) {
            if (mBottomSheetDialog.isShowing())
                mBottomSheetDialog.dismiss();
            mBottomSheetDialog = null;
        }

    }


    //Requesting permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        DiveSitePictureFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }


    protected void takePicture() {

        String filePath = CommonGenericDialogs.takeAPicture(mContext, DiveSitePictureFragment.this);
        if (filePath == null) {
            GenericDialogs.showInformativeDialog(CommonGenericDialogs.CAMERA_ERROR, mContext);
            return;
        }
        removeFile();//if previous file is not required  //// TODO: 18-04-2017  if this operation required or not
        setCameraImagePath(filePath);
    }

    @OnClick(R.id.logback_take_picture_btn)
    protected void getImagePicker() {
        DiveSitePictureFragmentPermissionsDispatcher.showImagePickerWithCheck(DiveSitePictureFragment.this, true);
    }

    /**
     * Image Upload process :
     * STEP 1 :  Ask for permissions  : user the external library to ask permissions , it automatically handled the process
     * STEP 2 : Get PicPiker -> getPicPiker , Static method from the Utility Class with options to .
     * 1. TAKE PICTURE FROM DEFAULT CAMERA : -> takeAPicture
     */
    @SuppressWarnings("all")
    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
    protected void showImagePicker(boolean isPickerOpen) {
        mAppDirectory = null;
        String[] directoryFolders = getResources().getStringArray(R.array.directory_sub_folders);
        mAppDirectory = new AppDirectoryImpl(mContext, AppDirectoryImpl.MODE_PUBLIC, mAppName, directoryFolders);
        if (isPickerOpen) {
            mBottomSheetDialog = getBottomSheetDialog();
            mBottomSheetDialog.show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            if (!(requestCode == REQUEST_IMAGE_CAPTURE && getCameraImagePath() != null && !getCameraImagePath().isEmpty())) {
                AppLogger.Logger.error(TAG, "Intent is null");
                return;
            }
        }
        if (resultCode != RESULT_OK) {
            setCameraImagePath(null);
            AppLogger.Logger.error(TAG, "Error in result");
            return;
        }

        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                getImage(null);
                break;
            case RESULT_LOAD_IMAGE:
                Uri selectedImage = data.getData();
                getImage(selectedImage);
                break;
        }
    }

    //Get image from Camera && Gallery
    private void getImage(Uri uri) {
        try {
            String picturePath;
            if (uri != null) // Gallery Image
                picturePath = FileUtilities.getGalleryImagePathFromUri(uri, mContext);
            else    // Take camera image
                picturePath = getCameraImagePath();

            if (picturePath == null || picturePath.isEmpty())
                throw new Exception("Invalid image path" + picturePath);

            imageHandlingFromGalleryAndCamera(picturePath);
        } catch (Exception ex) {
            AppLogger.Logger.error(TAG, "OnActivity Result : " + ex.getMessage(), ex);
            GenericDialogs.showInformativeDialog(CommonGenericDialogs.IMAGE_UPLOAD_ERROR + " - " + ex.getMessage(), mContext);
        }
    }

    // saving image to local system
    private void imageHandlingFromGalleryAndCamera(String tempPath) throws Exception {

        Bitmap bitmap = FileUtilities.getRequiredBitmap(mContext, tempPath);
        if (bitmap == null) {
            throw new Exception("Unable to upload this image. ");
        }
        // Saving image to local directory
        String fileName = FileUtilities.getUniqueFileName(FileUtilities.IMAGE_FILE_PREFIX);
        // save image in logbook folder
        String filePath = FileUtilities.saveBitmapAsFileToExternalCard(bitmap, fileName, mAppDirectory.getPath(getString(R.string.directory_sub_folders_images_logbook)));

        if (filePath == null) {
            throw new Exception("Invalid imagePath " + filePath);
        }
        setCameraImagePath(filePath);
        Bitmap originalBitmap = FileUtilities.getBitmapImages(filePath, mImVDiveSiteImage.getWidth(), mImVDiveSiteImage.getHeight());
        mImVDiveSiteImage.setImageBitmap(originalBitmap);

        removeFile(mLogbook.getDiveCenterLogo()); // remove already add file if existed
        //removeFile();
        //setCameraImagePath(null);
        //  mImVImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    // Remove Image from local
    private void removeFile() {
        if (getCameraImagePath() != null) {
            File file = new File(getCameraImagePath());
            file.delete();
        }
    }

    private void removeFile(String path) {
        File file = null;
        if (!path.isEmpty()) {
            try {
                file = new File(URI.create(path));
                if (file.exists())
                    file.delete();
            } catch (IllegalArgumentException ex) {
                AppLogger.Logger.error(TAG, ex.getMessage());
            }
        }
    }

    public File next() {
        File file = null;
        try {
            if (getCameraImagePath() == null || getCameraImagePath().isEmpty())
                return null;

            file = new File(getCameraImagePath());
           /* file = getImageFromImageView();*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }


    //Creating image picker bottom sheet
    private BottomSheetDialog getBottomSheetDialog() {
        if (mBottomSheetDialog == null) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.bottom_sheet_imaeg_picker, null);
            mBottomSheetDialog = new BottomSheetDialog(mContext, R.style.ios_image_picker);
            mBottomSheetDialog.setContentView(view);

            mBottomSheetDialog.setCanceledOnTouchOutside(true);
            mBottomSheetDialog.setCancelable(true);

            view.findViewById(R.id.bottom_sheet_image_picker_photo_camera).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mBottomSheetDialog.dismiss();
                    takePicture();
                }
            });
            view.findViewById(R.id.bottom_sheet_image_picker_photo_gallery).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mBottomSheetDialog.dismiss();
                    CommonGenericDialogs.getImageFromGalleryByFragment(DiveSitePictureFragment.this);
                }
            });
            view.findViewById(R.id.bottom_sheet_image_picker_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mBottomSheetDialog.dismiss();
                }
            });

        }
        return mBottomSheetDialog;

    }

    public String getCameraImagePath() {
        return mCameraImagePath;
    }

    public void setCameraImagePath(String cameraImagePath) {
        mCameraImagePath = cameraImagePath;
    }

    private File getImageFromImageView() throws Exception {

        String fileName = FileUtilities.getUniqueFileName(FileUtilities.IMAGE_FILE_PREFIX);
        File file = new File(mContext.getCacheDir(), fileName);
        file.createNewFile();

//Convert bitmap to byte array
        Bitmap bitmap = ((BitmapDrawable) mImVDiveSiteImage.getDrawable()).getBitmap();
        if (bitmap == null)
            return null;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80 /*ignored for PNG*/, bos);
        byte[] bitMapData = bos.toByteArray();

//write the bytes in file
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bitMapData);
        fos.flush();
        fos.close();
        return file;

    }
}
