package com.yy.music.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

import com.yy.music.MainActivity;
import com.yy.music.R;
import com.yy.music.activity.MusicActivity;
import com.yy.music.data.GlobalVariable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static android.app.PendingIntent.FLAG_ONE_SHOT;

public class NotifyBar {
    Context context;
    String channelID = "0Xaa";
    private NotificationManager manager;

    public NotifyBar(Context context) {
        super();
        this.context = context;
        init();
    }

    public void init() {
        String id = "0Xaa";
        String KEY_TEXT_REPLY = "key_text_reply";
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher);
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent intent = PendingIntent.getActivity(context, 0XFF, new Intent(context, MusicActivity.class), FLAG_ONE_SHOT);


        Intent intent1 = new Intent();
        intent1.setAction(GlobalVariable.getInstance().SEND_REPLAY);

        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                .setLabel("回复操作")
                .build();

        PendingIntent replyPendingIntent =
                PendingIntent.getBroadcast(context, 0X01, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(R.drawable.ic_launcher_background,
                        "回复操作", replyPendingIntent)
                        .addRemoteInput(remoteInput)
                        .build();
        //通道
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelID, channelID, importance);
            channel.setDescription("你好");
            manager.createNotificationChannel(channel);
        }


        Notification notification = new NotificationCompat.Builder(context, channelID)
                .setContentTitle("指定通知的标题内容")
                .setContentText("指定通知的正文内容")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(intent)
                //.addAction(R.mipmap.ic_launcher,"你好1",intent)
                //.addAction(R.mipmap.ic_launcher,"你好2",intent)
                //.addAction(R.mipmap.ic_launcher,"你好3",intent)
                //.addAction(action)
                //.setStyle(new NotificationCompat.BigPictureStyle().bigLargeIcon(bitmap))
                .setAutoCancel(true)
                .build();
        manager.notify(1, notification);
    }

    public void bit(){
        try {
            FileInputStream in = new FileInputStream("/sdcard/Download/sample.png");
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            Bitmap bm = BitmapFactory.decodeFile(""); // 间接调用 BitmapFactory.decodeStream
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }





        //Bitmap bm = BitmapFactory.decodeByteArray(myByte,0,myByte.length);
    }
}
