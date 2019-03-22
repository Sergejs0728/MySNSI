package shiva.joshi.common.callbacks;

import android.content.DialogInterface;

/**
 * created by jugalkishorjoshi on
 */

public interface GenericImagePickerCallback {

    void pickFromCamera(DialogInterface dialog, int id);

    void pickPromGallery(DialogInterface dialog, int id);

    void removePic(DialogInterface dialog, int id);
}