package com.evwwa.evtracker.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.evwwa.evtracker.ConnectionHandler.ServerRequestHandler;
import com.evwwa.evtracker.Interfaces.AlertDialogInterface;
import com.evwwa.evtracker.Interfaces.ServerInterface;
import com.evwwa.evtracker.R;

/**
 * Created by aiubian on 2/8/18.
 */

public class ViewUtils {

    public static void NavigateToFragmentClearPopUp(Context context, Fragment fragment){
        try {
            FragmentManager supportFragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); //clear all fragments from back stack
            supportFragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
        }catch (Exception e){}
    }

    public static void navigateToActivity(Context context, Class cls){
        Intent intent = new Intent(context,cls);
        context.startActivity(intent);
    }

    public static void exitApp(Context context){
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startMain);
    }

    public static void showAlertDialog(Context context, String title, String message, boolean showNegativeButton, boolean cancelable, String positiveText, String negativeText, final AlertDialogInterface alertDialogInterface){
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(cancelable);

            if(!TaskUtils.isEmpty(title))
                builder.setTitle(title);

            builder.setMessage(message);

            builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    alertDialogInterface.OnPositiveButtonClick();
                }
            });

            if(showNegativeButton){
                builder.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        alertDialogInterface.OnNegativeButtonClick();
                    }
                });
            }

            AlertDialog dialog = builder.create();
            dialog.show();
        }catch (Exception e){}
    }

    public static void show_autostart_alert(final Context context, final int brand){
        /* 1 = xiaomi
        2 = letv
        3 = huawie
         */
        String msg ="";
        if(brand == 1)
            msg = context.getString(R.string.autostart_msg_xiaomi);
        else if(brand == 2)
            msg = context.getString(R.string.autostart_msg_letv);
        else if(brand == 3)
            msg = context.getString(R.string.autostart_msg_huawei);

        showAlertDialog(context, "", msg, false, false, context.getString(R.string.ok), "", new AlertDialogInterface() {
            @Override
            public void OnPositiveButtonClick() {
                TaskUtils.start_autostart_activity(context,brand);
            }

            @Override
            public void OnNegativeButtonClick() {
            }
        });
    }

    public static void show_track_email_alert_dialog(final Activity activity, final ServerInterface serverInterface){
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setCancelable(true);

            builder.setTitle(activity.getString(R.string.track_title));

            LayoutInflater inflater = activity.getLayoutInflater();
            View view = inflater.inflate(R.layout.custom_email_address_alertdialog,null);
            final EditText et = view.findViewById(R.id.et_email);

            builder.setView(view);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    ServerRequestHandler.TrackRequest(activity,et.getText().toString(),serverInterface);
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }catch (Exception e){}
    }

    public static void changeStatusBarColor(Activity activity){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
            window.setStatusBarColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
        }
    }

    public static void openPrivacyPolicyLink(Context context){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.PRIVACY_POLICY_URL));
        context.startActivity(browserIntent);
    }


}
