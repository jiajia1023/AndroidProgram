package com.mylibrary.dialog;


import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;

/**
 * Created by work on 2017/3/21.
 *
 * @author chris zou
 * @mail chrisSpringSmell@gmail.com
 */

public interface IDialog {

    void show(Activity activity);
    void dismiss();
    void initDialog(Activity context);
    AlertDialog getDialog();
}
