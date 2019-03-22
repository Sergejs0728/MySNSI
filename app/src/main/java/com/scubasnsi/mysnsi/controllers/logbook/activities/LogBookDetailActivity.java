package com.scubasnsi.mysnsi.controllers.logbook.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scubasnsi.R;
import com.scubasnsi.mysnsi.app.MyApplication;
import com.scubasnsi.mysnsi.app.listeners.UpdateLogbookHeader;
import com.scubasnsi.mysnsi.app.utilities.GenericDialogs;
import com.scubasnsi.mysnsi.controllers.logbook.fragments.DiveSitePictureFragment;
import com.scubasnsi.mysnsi.controllers.logbook.fragments.LogbookDetailFragment;
import com.scubasnsi.mysnsi.model.data_models.Logbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import shiva.joshi.common.CommonConstants;
import shiva.joshi.common.callbacks.GenericInformativeDialogBoxCallback;
import shiva.joshi.common.logger.AppLogger;
import shiva.joshi.common.receivers.ConnectivityReceiver;
import shiva.joshi.common.utilities.AppDirectoryImpl;
import shiva.joshi.common.utilities.CommonGenericDialogs;


@RuntimePermissions
public class LogBookDetailActivity extends AppCompatActivity implements UpdateLogbookHeader, ConnectivityReceiver.ConnectivityReceiverListener {


    private final String TAG = LogBookDetailActivity.class.getName();
    private Context mContext;
    //Header

    @BindView(R.id.toolbar_logbook_left_cancel_image_id)
    ImageView mLeftCancel;

    @BindView(R.id.toolbar_logbook_right_share_image_id)
    Button mLeftshare;


    @BindView(R.id.toolbar_logbook_title_id)
    TextView mTVTitle;

    @BindView(R.id.toolbar_logbook_back_layout_id)
    LinearLayout mLLBack;


    /* Logbook fragments */
    private LogbookDetailFragment mLogbookDetailFragment;

    private Fragment mCurrentFragment;
    private String mCurrentFragmentTag;
    private Logbook mLogbook;
    private boolean isForEdit;
    @BindView(R.id.container)
     LinearLayout container;
    private AppDirectoryImpl mAppDirectory;
    @BindString(R.string.app_name)
    protected String mAppName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logbook_detail);

        mContext = this;
        ButterKnife.bind(this);
        MyApplication.getApplicationInstance().setConnectivityListener(this);
        if (getIntent() == null || getIntent().getExtras() == null || getIntent().getExtras().getSerializable(CommonConstants.BUNDLE_SERIALIZED_OBJECT) == null) {
            GenericDialogs.getGenericInformativeDialogBoxWithSingleButton(mContext, "", "Can't show dive detail", CommonGenericDialogs.OK, false, new GenericInformativeDialogBoxCallback() {
                @Override
                public void PositiveMethod(DialogInterface dialog, int id) {
                    finish();
                }
            });

            return;
        }
        mLogbook = (Logbook) getIntent().getExtras().getSerializable(CommonConstants.BUNDLE_SERIALIZED_OBJECT);
        initializeView();

    }

    private void initializeView() {
        mLogbookDetailFragment = LogbookDetailFragment.newInstance(mLogbook);
        setFragment(mLogbookDetailFragment, mLogbookDetailFragment.TAG, false);
    }


    /* Handling of fragment stack */
    @Override
    public void onBackPressed() {
       super.onBackPressed();

    }


    @Override
    public void OnUpdateHeader(boolean isBackShown, String title) {
        mLeftCancel.setVisibility(View.GONE);
        mLeftshare.setVisibility(View.GONE);
        mLLBack.setVisibility(View.GONE);
        if (isBackShown) {
            mLLBack.setVisibility(View.VISIBLE);
        } else {
            mLeftCancel.setVisibility(View.VISIBLE);
            mLeftshare.setVisibility(View.VISIBLE);
        }
        mTVTitle.setText(title);
    }



    @OnClick(R.id.toolbar_logbook_left_cancel_image_id)
    protected void cancelOrFinish(View v) {
        onBackPressed();
    }

    @OnClick(R.id.toolbar_logbook_right_share_image_id)
    protected void share(View v) {

        LogBookDetailActivityPermissionsDispatcher.showImagePickerWithCheck(LogBookDetailActivity.this, true);


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
            takeScrennShotAndShare();
        }

    }

    private void setFragment(Fragment fragment, String tag, boolean addToStack) {
        try {
            if (fragment != null) {
                mCurrentFragment = fragment;
                mCurrentFragmentTag = tag;
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.common_content, fragment, tag);
                if (addToStack)
                    ft.addToBackStack(tag);
                ft.commit();
            }
        } catch (Exception ex) {
            AppLogger.Logger.error(TAG, ex.getMessage(), ex);
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected)
            GenericDialogs.showInformativeDialog("", R.string.network_error, mContext);
    }

    public Bitmap getBitmap(LinearLayout view){
        view.measure(0,0);
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
//            Bitmap bmp = Bitmap.createBitmap(layout.getDrawingCache());
//            layout.setDrawingCacheEnabled(false);

        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.draw(canvas);
    return bitmap;
    }

    //ProgressDialog mProgressDialog;
    public void takeScrennShotAndShare(){

       // Bitmap b =getBitmap(container);//container.getDrawingCache();

        new AsyncTask<String, Void, File>(){
            Bitmap bitmap;
            File imageFile=null;
            String mPath;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
               // mProgressDialog=new ProgressDialog(LogBookDetailActivity.this);
               // mProgressDialog.show();
                container.setDrawingCacheEnabled(true);

                bitmap = getBitmap(container);
                container.setDrawingCacheEnabled(false);

            }

            @Override
            protected File doInBackground(String... params) {
                Date now = new Date();
                android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

                try {
                    // image naming and path  to include sd card  appending name you choose for file
                    mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";
                    imageFile = new File(mPath);
                    FileOutputStream outputStream = new FileOutputStream(imageFile);
                    int quality = 100;
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                    outputStream.flush();
                    outputStream.close();

                } catch (Throwable e) {
                    // Several error may come out with file handling or DOM
                    e.printStackTrace();
                }

                return imageFile;
            }

            @Override
            protected void onPostExecute(File file) {
                super.onPostExecute(file);
                //mProgressDialog.dismiss();

                if (imageFile!=null) {

                    Uri uri = Uri.parse("file://"+mPath);
                    Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT,  "");
                    shareIntent.putExtra(Intent.EXTRA_STREAM,uri);
                    shareIntent.setType("image/png");
                    startActivity(Intent.createChooser(shareIntent, "Share Image"));
                }

            }
        }.execute();
    }




}


