package shiva.joshi.common.callbacks; /**
 * created by jugalkishorjoshi on
 */

import android.content.DialogInterface;

public interface GenericInputDialogBoxCallback {

    void PositiveMethod(DialogInterface dialog, int id, String value);

    void NegativeMethod(DialogInterface dialog, int id);
}