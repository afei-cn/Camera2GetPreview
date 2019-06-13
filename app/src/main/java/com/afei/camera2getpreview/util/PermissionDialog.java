package com.afei.camera2getpreview.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;

import com.afei.camera2getpreview.R;

public class PermissionDialog extends DialogFragment implements DialogInterface.OnClickListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.permission_tip);
        builder.setMessage(R.string.permission_msg);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.permission_btn_setting, this);
        builder.setNegativeButton(R.string.permission_btn_quit, this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        dismiss();
        if (which == DialogInterface.BUTTON_POSITIVE) {
            //setting detail intent
            final Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        } else {
            getActivity().finish();
        }
    }
}
