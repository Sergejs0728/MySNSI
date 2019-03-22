package shiva.joshi.common.utilities;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.io.File;

import static android.content.Context.MODE_PRIVATE;

/**
 * Author - J.K.Joshi
 * Date -  30-11-2016.
 */

public class AppDirectoryImpl {

    public static final int MODE_PUBLIC = 1;
    public static final int MODE_CACHE = 2;
    private String APPLICATION_FOLDER_NAME = "";
    private String parentDirectoryPath;
    private String[] subFolders = {"images", "profile"};

    public AppDirectoryImpl(Context context, String appName) {
        if (appName == null || appName.isEmpty())
            throw new RuntimeException(" Please specify the application folder name here  ");
        APPLICATION_FOLDER_NAME = appName;

        createExternalStorageDirectory(context, MODE_PUBLIC);
    }

    public AppDirectoryImpl(Context context, int MODE, String appName, String[] subFolders) {
        if (appName == null || appName.isEmpty())
            throw new RuntimeException(" Please specify the application folder name here  ");
        APPLICATION_FOLDER_NAME = appName;
        this.subFolders = subFolders;
        createExternalStorageDirectory(context, MODE);
    }

    public boolean createExternalStorageDirectory(Context context, int Mode) {
        if (!FileUtilities.isExternalStorageReadable()) {
            Toast.makeText(context, "Error : unable to create directory. /n You don't have external card.", Toast.LENGTH_LONG).show();
            return false;
        }

        File myDirectory;
        switch (Mode) {
            case MODE_CACHE:
                myDirectory = new File(context.getCacheDir(), APPLICATION_FOLDER_NAME);
                break;
            case MODE_PRIVATE:
                myDirectory = new File(context.getExternalFilesDir(null), APPLICATION_FOLDER_NAME);
                break;
            case MODE_PUBLIC:
            default:
                myDirectory = new File(Environment.getExternalStorageDirectory(), APPLICATION_FOLDER_NAME);
        }

        if (!myDirectory.exists()) {
            if (!myDirectory.mkdirs()) {
                Toast.makeText(context, "Error : unable to create directory. /n Internal errror.", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        setParentDirectoryPath(myDirectory.getAbsolutePath());
        for (String subfolderName : subFolders) {
            createDirectory(getParentDirectoryPath(), subfolderName);
        }
        return true;
    }

    @NonNull
    private String createDirectory(String parentDirectoryPath, String folderName) {
        // Profile Image folder
        String absolutePathDic = parentDirectoryPath + "/" + folderName;
        File mProfileImageDirectory = new File(absolutePathDic);
        if (!mProfileImageDirectory.exists())
            mProfileImageDirectory.mkdirs();
        return mProfileImageDirectory.getAbsolutePath();
    }


    public String getParentDirectoryPath() {
        return parentDirectoryPath;
    }

    private void setParentDirectoryPath(String parentDirectoryPath) {
        this.parentDirectoryPath = parentDirectoryPath;
    }
/*
    public String getPathImageFolder() {
        return mPathImageFolder + "/";
    }

    public String getPathProfileImageFolder() {
        return mPathProfileImageFolder + "/";
    }*/

    public String getPath(String subFolderName) {
        if (subFolderName == null || subFolderName.isEmpty()) {
            return getParentDirectoryPath(); // no sub folder directory
        }
        return new File(getParentDirectoryPath() + "/" + subFolderName).getAbsolutePath() + "/"; // else return full sub folder path
    }
}
