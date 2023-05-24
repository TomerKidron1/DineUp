package com.example.myapp;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;

public class NetReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
       if(!Common.isConnectedToInternet(context)){
           AlertDialog.Builder builder = new AlertDialog.Builder(context);
           builder.setTitle("No Internet Connection");
           builder.setMessage("Please check your internet connection\nwould you like to try again?");
           builder.setCancelable(false);
           builder.setPositiveButton("Retry", (new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialogInterface, int i) {
                   dialogInterface.dismiss();
                   context.startActivity(new Intent(context,context.getClass()));

               }
           }));
           builder.setNegativeButton("Cancel", (new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialogInterface, int i) {
                   Intent intent = new Intent(Intent.ACTION_MAIN);
                   intent.addCategory(Intent.CATEGORY_HOME);
                   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                   context.startActivity(intent);
               }
           }));
           builder.show();

        }
    }

    private int networkSpeed(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
        int downSpeed = nc.getLinkDownstreamBandwidthKbps();
        return downSpeed;
    }
}

