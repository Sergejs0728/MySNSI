package com.scubasnsi.mysnsi.controllers.home.fragment;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.scubasnsi.R;
import com.scubasnsi.mysnsi.app.AppGlobalConstant;
import com.scubasnsi.mysnsi.app.MyApplication;
import com.scubasnsi.mysnsi.app.listeners.UpdateHomeHeader;
import com.scubasnsi.mysnsi.app.utilities.CustomProgressBar;
import com.scubasnsi.mysnsi.app.utilities.GenericDialogs;
import com.scubasnsi.mysnsi.model.dao.CardListDao;
import com.scubasnsi.mysnsi.model.dao.impl.CardDaoImpl;
import com.scubasnsi.mysnsi.model.data_models.C_Cards;
import com.scubasnsi.mysnsi.model.dto.C_CardsPDFDto;
import com.scubasnsi.mysnsi.model.services.HomeService;
import com.scubasnsi.mysnsi.model.services.impl.HomeServicesImpl;
import com.scubasnsi.mysnsi.model.sessions.UserSession;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import shiva.joshi.common.callbacks.GenericInformativeDialogBoxCallback;
import shiva.joshi.common.callbacks.ResponseCallBackHandler;
import shiva.joshi.common.data_models.ResponseHandler;
import shiva.joshi.common.java.JavaUtility;
import shiva.joshi.common.logger.AppLogger;
import shiva.joshi.common.utilities.AppDirectoryImpl;
import shiva.joshi.common.utilities.CommonGenericDialogs;

import static shiva.joshi.common.CommonConstants.BUNDLE_SERIALIZED_OBJECT;


public class ShowCardDetailFragment extends Fragment {

    public static final String TAG = ShowCardDetailFragment.class.getName();
    @BindView(R.id.web_view)
    protected WebView mWebView;


    @BindString(R.string.app_name)
    protected String mAppName;


    AppDirectoryImpl mAppDirectory;

    private CustomProgressBar mCustomProgressBar;
    private HomeService mHomeService;
    private UserSession mUserSession;
    private C_Cards mC_cards;
    private CardListDao mCardListDao;
    private Context mContext;
    private UpdateHomeHeader mUpdateHomeHeader;
    private Callback mCallback;


    public static ShowCardDetailFragment newInstance(C_Cards c_cards) {
        ShowCardDetailFragment fragment = new ShowCardDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(BUNDLE_SERIALIZED_OBJECT, c_cards);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UpdateHomeHeader) {
            mUpdateHomeHeader = (UpdateHomeHeader) context;
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
        mHomeService = new HomeServicesImpl();
        mCustomProgressBar = new CustomProgressBar(mContext);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_show_card_detail, container, false);

        ButterKnife.bind(this, view);
        String[] directoryFolders = getResources().getStringArray(R.array.directory_sub_folders);
        mAppDirectory = new AppDirectoryImpl(mContext,AppDirectoryImpl.MODE_PUBLIC, mAppName, directoryFolders);
        mCardListDao = new CardDaoImpl();

        if (getArguments() == null || getArguments().getSerializable(BUNDLE_SERIALIZED_OBJECT) == null) {
            GenericDialogs.getGenericInformativeDialogBoxWithSingleButton(mContext, null, getString(R.string.something_went_wrong), CommonGenericDialogs.OK, false, new GenericInformativeDialogBoxCallback() {
                @Override
                public void PositiveMethod(DialogInterface dialog, int id) {
                    ((Activity) mContext).onBackPressed();
                }
            });
            return null;
        }

        mC_cards = (C_Cards) getArguments().getSerializable(BUNDLE_SERIALIZED_OBJECT);
        mUserSession = MyApplication.getApplicationInstance().getUserSession();
        mCallback = new Callback();
        mWebView.setWebViewClient(mCallback);
        mUpdateHomeHeader.OnUpdateHeader(0, mC_cards.getCourseNickName(), R.drawable.ic_refresh,0);
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.setAllowFileAccessFromFileURLs(true);
            settings.setAllowUniversalAccessFromFileURLs(true);
        }

        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        getPdf(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUpdateHomeHeader = null;
    }


    private void getPdf(boolean isRefresh) {
        if (!isRefresh) {
            String savedFile = mCardListDao.getPDFFileName(mC_cards.getCardId());
            if (savedFile != null && !savedFile.isEmpty()) {
                if (decideToWhichFunctionalityWorks(savedFile))
                    return;
            }
        }
        //fetch data from server ! no corresponding file is available local .
        fetchPdfFromServer();

    }

    //fetch pdf path from server
    private void fetchPdfFromServer() {
        C_CardsPDFDto c_cardsPDFDto = new C_CardsPDFDto(mUserSession.getUserID(), mC_cards.getProfessionalQualificationId(), mC_cards.getCertificationId());
        mCustomProgressBar.showHideProgressBar(true, getString(R.string.loading_downloading));
        mHomeService.getPDFUrl(new ResponseCallBackHandler() {
            @Override
            public void returnResponse(ResponseHandler responseHandler) {
                mCustomProgressBar.showHideProgressBar(false, null);
                if (responseHandler.isExecuted()) {
                    downloadPdfFromServer(responseHandler.getValue().toString());
                    return;
                }
                GenericDialogs.showInformativeDialog(
                        (responseHandler.getMessage().equalsIgnoreCase(AppGlobalConstant.SOMETHING_WENT_WRONG) ? getString(R.string.verify_cant_download_card) : responseHandler.getMessage())
                        , mContext);
            }
        }, c_cardsPDFDto);
    }


    //Download PDF
    private void downloadPdfFromServer(final String path) {
        try {
            mCustomProgressBar.showHideProgressBar(true, getString(R.string.loading_downloading));
            mHomeService.downloadFile(new ResponseCallBackHandler() {
                @Override
                public void returnResponse(ResponseHandler responseHandler) {
                    mCustomProgressBar.showHideProgressBar(false, null);
                    if (responseHandler.isExecuted()) {
                        savePdfToLocal((ResponseBody) responseHandler.getValue(), path);
                        return;
                    }
                    GenericDialogs.showInformativeDialog(responseHandler.getMessage(), mContext);
                }

            }, path);

        } catch (Exception e) {
            AppLogger.Logger.error(TAG, e.getMessage(), e);
        }
    }

    //Save Local
    private void savePdfToLocal(ResponseBody responseBody, String url) {
        String fileName = null;
        try {
            fileName = JavaUtility.getFileNameFromUrl(url);
            boolean isSavedLocal = writeResponseBodyToDisk(responseBody, fileName);
            if (isSavedLocal) {
                mCardListDao.saveOrUpdatePDF(mC_cards.getCardId(), fileName);
                if (!decideToWhichFunctionalityWorks(fileName)) {
                    AppLogger.showToast(TAG, getString(R.string.verify_cant_download_card));
                }
            }
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            //show error here while downloading
            AppLogger.Logger.error(TAG, e.getMessage(), e);
        }

    }

    public void refresh() {

        getPdf(true);
    }


    private class Callback extends WebViewClient {

        String mFilePath;
        private boolean isLoaded;

        public Callback() {

        }

        public void setPath(String filePath, boolean isLoaded) {
            this.mFilePath = filePath;
            this.isLoaded = isLoaded;
        }

        @Override
        public boolean shouldOverrideUrlLoading(
                WebView view, String url) {
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (!isLoaded) {
                mCustomProgressBar.showHideProgressBar(true, getString(R.string.loading_downloading));
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (!isLoaded) {
                mCustomProgressBar.showHideProgressBar(false, null);
                isLoaded = true;
                view.loadUrl("javascript:load_pdf_java('" + mFilePath + "')");
            }
        }
    }

    //Save ti external device
    private boolean writeResponseBodyToDisk(ResponseBody body, String fileName) {
        try {
            File futureStudioIconFile = new File(mAppDirectory.getPath(getString(R.string.directory_sub_folders_pdf_cards)) + File.separator + fileName);
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    AppLogger.Logger.debug(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                AppLogger.Logger.error(TAG, e.getMessage(), e);
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (Exception e) {
            AppLogger.Logger.error(TAG, e.getMessage(), e);
            return false;
        }
    }

    //Check if rending functionality available or not
    private boolean decideToWhichFunctionalityWorks(String fileName) {
        return openPdfWebView(fileName);
    }

    // Open file using external App.
    private boolean openPDFFromLocal(String savedFile) {
        File file = new File(mAppDirectory.getPath(getString(R.string.directory_sub_folders_pdf_cards)) + savedFile);
        if (file.exists()) {
            Uri path = Uri.fromFile(file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(path, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            try {
                startActivity(intent);
                return true;
            } catch (ActivityNotFoundException e) {
                AppLogger.showToast(TAG, "No Application Available to View PDF");
            }
        }
        return false;
    }

    private boolean openPdfWebView(String filePath) {
        File file = new File(mAppDirectory.getPath(getString(R.string.directory_sub_folders_pdf_cards)) + filePath);
        if (file.exists()) {
            mCallback.setPath(file.getAbsolutePath(), false);
            mWebView.loadUrl("file:///android_asset/pdftest/web/viewer.html");
            return true;
        } else {
            Log.d("File not found", "");
        }
        return false;
    }
}
