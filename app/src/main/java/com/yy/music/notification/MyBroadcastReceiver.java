package com.yy.music.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.RemoteInput;

public class MyBroadcastReceiver extends BroadcastReceiver {
    String KEY_TEXT_REPLY = "key_text_reply";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("TAG", "onReceive: 开始");
        String str = getMessageText(intent).toString();
        Log.d("TAG", "onReceive: "+str);
    }

    private CharSequence getMessageText(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            return remoteInput.getCharSequence(KEY_TEXT_REPLY);
        }
        return null;
    }
}
