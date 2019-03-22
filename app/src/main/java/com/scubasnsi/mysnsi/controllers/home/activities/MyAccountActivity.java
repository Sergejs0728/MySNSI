package com.scubasnsi.mysnsi.controllers.home.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.scubasnsi.R;
import com.scubasnsi.mysnsi.app.MyApplication;
import com.scubasnsi.mysnsi.app.listeners.UpdateHomeHeader;
import com.scubasnsi.mysnsi.app.utilities.CustomProgressBar;
import com.scubasnsi.mysnsi.app.utilities.GenericDialogs;
import com.scubasnsi.mysnsi.controllers.logbook.fragments.DiveSitePictureFragment;
import com.scubasnsi.mysnsi.controllers.login.activities.WelcomeSlidesActivity;
import com.scubasnsi.mysnsi.model.data_models.Logbook;
import com.scubasnsi.mysnsi.model.data_models.User;
import com.scubasnsi.mysnsi.model.dto.LogCountDto;
import com.scubasnsi.mysnsi.model.dto.LoginDto;
import com.scubasnsi.mysnsi.model.dto.ProfileDto;
import com.scubasnsi.mysnsi.model.services.HomeService;
import com.scubasnsi.mysnsi.model.services.ProfileService;
import com.scubasnsi.mysnsi.model.services.ProfileUpdateService;
import com.scubasnsi.mysnsi.model.services.impl.HomeServicesImpl;
import com.scubasnsi.mysnsi.model.services.impl.ProfileServicesImpl;
import com.scubasnsi.mysnsi.model.services.impl.ProfileUpdateServiceImp;
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
import shiva.joshi.common.callbacks.GenericConfirmationDialogBoxCallback;
import shiva.joshi.common.callbacks.GenericInformativeDialogBoxCallback;
import shiva.joshi.common.callbacks.ResponseCallBackHandler;
import shiva.joshi.common.customs.Screen;
import shiva.joshi.common.data_models.ResponseHandler;
import shiva.joshi.common.helpers.ChangeActivityHelper;
import shiva.joshi.common.logger.AppLogger;
import shiva.joshi.common.utilities.AppDirectoryImpl;
import shiva.joshi.common.utilities.CommonGenericDialogs;
import shiva.joshi.common.utilities.FileUtilities;

import static shiva.joshi.common.utilities.CommonGenericDialogs.REQUEST_IMAGE_CAPTURE;
import static shiva.joshi.common.utilities.CommonGenericDialogs.RESULT_LOAD_IMAGE;

@RuntimePermissions
public class MyAccountActivity extends AppCompatActivity implements UpdateHomeHeader {

    private final String TAG = MyAccountActivity.class.getName();

    private Context mContext;
    private UserSession mUserSession;
    private User mUser;
    private ImageLoader mImageLoader;
    private HomeService mHomeService;
    private ProfileUpdateService profileUpdateService;
    private ProfileService profileService;

    //Header
    @BindString(R.string.profile)
    protected String mHeaderTitle;

    @BindView(R.id.toolbar_home_left_image_id)
    ImageView mLeftIcon;
    @BindView(R.id.toolbar_home_left_new_image_id)
    ImageView mLeftIconNew;

    @BindView(R.id.toolbar_home_title_id)
    TextView mTVTitle;

    @BindView(R.id.toolbar_home_right_image_id)
    ImageView mRightIcon;

    @BindView(R.id.my_account_profile_pic)
    protected ImageView mImVProfilePic;
    @BindView(R.id.my_account_username)
    protected EditText mUserName;
    @BindView(R.id.my_account_email)
    protected EditText mUserEmail;
    @BindView(R.id.my_account_logged_dive_score)
    protected TextView mDiveScore;

    private String DEFAULT_LOG_COUNT = "0";

    private AppDirectoryImpl mAppDirectory;
    private BottomSheetDialog mBottomSheetDialog;
    @BindString(R.string.app_name)
    protected String mAppName;
    private String mCameraImagePath;
    private CustomProgressBar mCustomProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);
        mContext = this;
        mUserSession = MyApplication.getApplicationInstance().getUserSession();
        mImageLoader = MyApplication.getApplicationInstance().getImageLoaderInstance(mContext, null);
        mHomeService = new HomeServicesImpl();
        profileUpdateService=new ProfileUpdateServiceImp();
        profileService=new ProfileServicesImpl();
        ButterKnife.bind(this);
        mCustomProgressBar = new CustomProgressBar(mContext);
        initializeView();
    }

    private void initializeView() {

        OnUpdateHeader(R.drawable.ic_cancel, mHeaderTitle, R.drawable.ic_setting,0);
        setUserData();

        if (mUserSession.getLoggedUserData().getmPassportImg() != null && !mUserSession.getLoggedUserData().getmPassportImg().isEmpty()) {

        }
        else
            Log.e("jhdb",">>>>>>>>>>");
        //show logged dives
        getLoggedDivesCount();
        getProfileData();


    }

    private void getProfileData() {

        ProfileDto profileDto = new ProfileDto(String.valueOf(mUserSession.getUserID()));
       // mCustomProgressBar.showHideProgressBar(true, getString(R.string.loading_login));
        profileService.ProfileService(new ResponseCallBackHandler() {
            @Override
            public void returnResponse(ResponseHandler responseHandler) {
               // mCustomProgressBar.showHideProgressBar(false, null);
                if (responseHandler.isExecuted()) {
                    mUserSession.login((User) responseHandler.getValue());
                    return;
                }
                else {
                    //Wrong username password
                    GenericDialogs.showInformativeDialog(responseHandler.getMessage(), mContext);
                }

            }
        },profileDto);

    }

    @Override
    public void OnUpdateHeader(int leftIcon, String title, int rightIcon, int leftIconNew) {
        /*mLeftIcon.setVisibility(View.INVISIBLE);
        mRightIcon.setVisibility(View.INVISIBLE);
        mLeftIconNew.setVisibility(View.INVISIBLE);*/
        mLeftIcon.setVisibility(View.GONE);
        mRightIcon.setVisibility(View.GONE);
        mLeftIconNew.setVisibility(View.GONE);
        if (leftIcon != 0) {
            mLeftIcon.setVisibility(View.VISIBLE);
            mLeftIcon.setImageResource(leftIcon);
        }

        if (leftIconNew != 0) {
            mLeftIconNew.setVisibility(View.VISIBLE);
            mLeftIconNew.setImageResource(leftIconNew);
        }
        if (rightIcon != 0) {
            mRightIcon.setVisibility(View.VISIBLE);
            mRightIcon.setImageResource(rightIcon);
        }
        mTVTitle.setText(title);
    }

    @OnClick(R.id.toolbar_home_left_image_id)
    protected void leftIconClick(View v) {
        onBackPressed();
    }

    @OnClick(R.id.toolbar_home_right_image_id)
    protected void rightIconClick(View v) {
        ChangeActivityHelper.changeActivity(MyAccountActivity.this, SettingActivity.class, false);
    }

    @OnClick(R.id.my_account_view_image_btn)
    protected void showFullImage() {
        if (mUserSession.getLoggedUserData().getmPassportImg() != null && !mUserSession.getLoggedUserData().getmPassportImg().isEmpty()) {
            showImage(mContext, mUserSession.getLoggedUserData().getmPassportImg(), false, new GenericInformativeDialogBoxCallback() {
                @Override
                public void PositiveMethod(DialogInterface dialog, int id) {

                }
            });
        }
        else
            Toast.makeText(this,"You have not uploaded any passport yet",Toast.LENGTH_LONG).show();
    }
    @OnClick(R.id.my_account_logout_btn)
    protected void logout(View v) {
        mUserSession.logout();
        ChangeActivityHelper.changeActivityClearStack(MyAccountActivity.this, WelcomeSlidesActivity.class, true);
    }
    protected void takePicture() {

        String filePath = CommonGenericDialogs.takeAPictureforActivity(mContext, MyAccountActivity.this);
        if (filePath == null) {
            GenericDialogs.showInformativeDialog(CommonGenericDialogs.CAMERA_ERROR, mContext);
            return;
        }
        removeFile();//if previous file is not required  //// TODO: 18-04-2017  if this operation required or not
        setCameraImagePath(filePath);
    }


    @OnClick(R.id.my_account_upload_image_btn)
    protected void clickPick(View v) {
        MyAccountActivityPermissionsDispatcher.showImagePickerWithCheck(MyAccountActivity.this, true);
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



    public void showImage(Context mContext, String imageUrl, boolean isCancelable, final GenericInformativeDialogBoxCallback dialogBoxInterface) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        String positiveBtnCaption = "DONE";


        Screen screen = new Screen(MyAccountActivity.this);


        //Input field
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.alert_show_full_image, null, false);
        view.setMinimumHeight((int) (screen.getHeightPixels() * 0.9f));

        final ImageView imageView = ButterKnife.findById(view, R.id.alert_image_view_id);
        mImageLoader.displayImage(imageUrl, imageView);
        builder.setCancelable(false)
                .setPositiveButton(positiveBtnCaption, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        final AlertDialog alert = builder.create();
        alert.setCancelable(isCancelable);
        alert.setView(view);

        alert.show();


        // retrieve display dimensions
        if (isCancelable) {
            alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    dialog.dismiss();
                }
            });
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
       /* Bitmap originalBitmap = FileUtilities.getBitmapImages(filePath, mImVDiveSiteImage.getWidth(), mImVDiveSiteImage.getHeight());
        mImVDiveSiteImage.setImageBitmap(originalBitmap);*/

        //removeFile(mLogbook.getDiveCenterLogo()); // remove already add file if existed
        //removeFile();
        //setCameraImagePath(null);
        //  mImVImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        savePasswordImage();
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



    private void savePasswordImage() {
        File file = next();
       /* if(file ==null &&logbook.getDiveCenterLogo()!=null&&!logbook.getDiveCenterLogo().isEmpty())
            return;*/
        if (file != null && file.exists()) { // logbook.setDiveCenterLogo(Uri.fromFile(file).toString());
            Log.e("file_in_string", ">>>" + Uri.fromFile(file).toString());
        }


      /*  if (isForEdit) {
            delete(logbook, file);
        } else {*/
        uplaodPassport(file);
        // }
    }

    private void uplaodPassport(final File file) {
        String.valueOf(mUserSession.getLoggedUserData().getUserId());

        Logbook logbook =new Logbook("upload_passport");
        logbook.setUserId(mUserSession.getUserID());
        mCustomProgressBar.showHideProgressBar(true, getString(R.string.uploading_passport));
        profileUpdateService.uloadPassportImage(new ResponseCallBackHandler() {
            @Override
            public void returnResponse(ResponseHandler responseHandler) {
                mCustomProgressBar.showHideProgressBar(false, null);
                if (responseHandler.isExecuted() && responseHandler.getValue() != null) {
                    //Delete local file if exist in folder
                   // File file = null;
                    try {
                       // file = new File(URI.create(logbook.getDiveCenterLogo()));
                        if (file.exists())
                            file.delete();
                    } catch (IllegalArgumentException | NullPointerException ex) {
                        AppLogger.Logger.error(TAG, ex.getMessage());
                    }
                    mUserSession.login((User) responseHandler.getValue());

                    return;
                }


            }
        },logbook , file);
    }


    //Creating image picker bottom sheet
    private BottomSheetDialog getBottomSheetDialog() {
        if (mBottomSheetDialog == null) {
            View view = MyAccountActivity.this.getLayoutInflater().inflate(R.layout.bottom_sheet_imaeg_picker, null);
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
                    CommonGenericDialogs.getImageFromGalleryByActivity(MyAccountActivity.this);
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

   /* private File getImageFromImageView() throws Exception {

        String fileName = FileUtilities.getUniqueFileName(FileUtilities.IMAGE_FILE_PREFIX);
        File file = new File(mContext.getCacheDir(), fileName);
        file.createNewFile();

//Convert bitmap to byte array
        Bitmap bitmap = ((BitmapDrawable) mImVDiveSiteImage.getDrawable()).getBitmap();
        if (bitmap == null)
            return null;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80 *//*ignored for PNG*//*, bos);
        byte[] bitMapData = bos.toByteArray();

//write the bytes in file
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bitMapData);
        fos.flush();
        fos.close();
        return file;

    }
    */


    private void setUserData() {
        mUser = mUserSession.getLoggedUserData();

        mUserName.setText(mUser.getUserDisplayName());
        mUserEmail.setText(mUser.getEmail());
        if (mUser.getProfileImage() == null || mUser.getProfileImage().isEmpty())
            return;
        mImageLoader.displayImage(mUser.getProfileImage(), mImVProfilePic);
    }


    //Update log count
    private void updateLoggedDive(String text) {
        mDiveScore.setText(text);
    }

    //Fetch from server
    private void getLoggedDivesCount() {
        LogCountDto logCountDto = new LogCountDto(mUser.getUserId());
        mHomeService.getLogCounts(new ResponseCallBackHandler() {
            @Override
            public void returnResponse(ResponseHandler responseHandler) {
                if (responseHandler.isExecuted()) {
                    updateLoggedDive((String) responseHandler.getValue());
                    return;
                }
                updateLoggedDive(DEFAULT_LOG_COUNT);
            }
        }, logCountDto);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
