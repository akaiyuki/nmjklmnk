package com.darsh.multipleimageselect.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;

/**
 * Created by ksuresh on 7/29/2016.
 */
public class AlertDialogManager {


    /**
     * Function to display simple Alert Dialog
     * @param context - application context
     * @param title - alert dialog title
     * @param message - alert message
     * @param status - success/failure (used to set icon)
     *               - pass null if you don't want icon
     * */
    @SuppressWarnings("deprecation")
    public void showAlertDialog(final Activity context, String title, String message,
                                Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        if(status != null)

            alertDialog.setButton(Dialog.BUTTON_POSITIVE,"Upgrade to Premium", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    Intent intent = new Intent();
                    intent.putExtra(Constants.INTENT_EXTRA_IMAGES, "upgrade");
                    context.setResult(context.RESULT_CANCELED);
                    context.finish();

                }
            });
           alertDialog.setButton( Dialog.BUTTON_NEGATIVE,"Ok", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int whichButton) {


               }
           });


            // Showing Alert Message
            alertDialog.show();
        }

    }
