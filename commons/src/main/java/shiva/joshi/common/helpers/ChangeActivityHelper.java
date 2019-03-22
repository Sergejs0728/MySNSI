package shiva.joshi.common.helpers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;

public class ChangeActivityHelper implements View.OnClickListener {

    private Activity mSource;
    private Class<?> mDestination;
    private boolean mFinishActivity;

    public ChangeActivityHelper(Activity source, Class<?> destination) {
        super();
        mSource = source;
        mDestination = destination;
        mFinishActivity = false;
    }

    public ChangeActivityHelper(Activity source, Class<?> destination, boolean finishActivity) {
        this(source, destination);
        mFinishActivity = finishActivity;
    }

    @Override
    public void onClick(View v) {
        changeActivity(mSource, mDestination, mFinishActivity);
    }

    public static void changeActivity(Activity source, Class<?> destination) {
        changeActivity(source, destination, false);
    }




    public static void changeActivityClearStack(@NonNull Activity source, @NonNull Class<?> destination, boolean wantToClearStack) {
        Intent intent = new Intent(source, destination);
        if (wantToClearStack) {
            source.finish();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }

        source.startActivity(intent);
    }
    /**
     * call when you need to transit form one activity to another
     *
     * @param shouldFinishContext: true if you want to finish context of the current activity, false otherwise
     */
    public static void changeActivity(@NonNull Activity source, @NonNull Class<?> destination, Boolean shouldFinishContext) {
        if (shouldFinishContext) {
            source.finish();
        }
        Intent intent = new Intent(source, destination);
        source.startActivity(intent);
    }


    // Intent with bundle
    public static void changeActivity(@NonNull Activity source, @NonNull Class<?> destination, boolean shouldFinishContext, @NonNull Bundle bundle) {
        if (shouldFinishContext) {
            source.finish();
        }
        Intent intent = new Intent(source, destination);

        if (bundle != null)
            intent.putExtras(bundle);
        source.startActivity(intent);
    }

    // Intent with bundle
    public static void changeActivityForResult(@NonNull Activity source, @NonNull Class<?> destination, int requestCode, @NonNull Bundle bundle) {
        Intent intent = new Intent(source, destination);
        intent.putExtras(bundle);
        source.startActivityForResult(intent, requestCode);
    }

    // Intent with bundle
    public static void changeActivityForResultByFragment(@NonNull Fragment source, @NonNull Class<?> destination, int requestCode, @NonNull Bundle bundle) {
        Intent intent = new Intent(source.getActivity(), destination);
        if (bundle != null)
            intent.putExtras(bundle);
        source.startActivityForResult(intent, requestCode);
    }


    //
    public static void openWebByFragment(@NonNull Fragment source, @NonNull String url) {
        Uri uri = Uri.parse(url); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        source.startActivity(intent);
    }
}