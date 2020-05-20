package com.yy.music.activity;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;


import com.yy.music.R;

import java.util.Objects;

public class NewAppWidget extends AppWidgetProvider {
    private static final String CLICK_ACTION = "com.taohuahua.action.click";

    static void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager, final int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.big_notification_upgrade);
        Intent anIntent = new Intent(context, LocalDocActivity.class);
        PendingIntent anPendingIntent = PendingIntent.getActivity(context, 0, anIntent, 0);
        views.setOnClickPendingIntent(R.id.image_notification, anPendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.big_notification_upgrade);
        if (Objects.equals(intent.getAction(), CLICK_ACTION)) {
            Toast.makeText(context, "hello world", Toast.LENGTH_SHORT).show();
            //获得appwidget管理实例，用于管理appwidget以便进行更新操作
            AppWidgetManager manger = AppWidgetManager.getInstance(context);
            // 相当于获得所有本程序创建的appwidget
            ComponentName thisName = new ComponentName(context, NewAppWidget.class);
            //更新widget
            manger.updateAppWidget(thisName, views);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }
}
/*
<appwidget-provider xmlns:android="http://schemas.android.com/apk/res/android"
        android:minWidth="40dp"
        android:minHeight="40dp"
        android:updatePeriodMillis="86400000"
        android:previewImage="@drawable/preview"
        android:initialLayout="@layout/example_appwidget"
        android:configure="com.example.android.ExampleAppWidgetConfigure"
        android:resizeMode="horizontal|vertical"
        android:widgetCategory="home_screen">
</appwidget-provider>

minWidth 和 minHeight ：默认情况下应用程序小部件的最小空间
        updatePeriodMillis：定义App Widget框架调用update（）方法的频率
        android:initialLayout：指向定义应用程序小部件布局的布局资源
        android:previewImage：指定应用程序小部件添加时的预览图片
        android:widgetCategory：配置应用程序窗口小部件是否可以显示在主屏幕（Home Sub屏幕）、锁定屏幕（KEGHARID）上
        resizeMode ：指定可调整小部件的调整规则（水平或竖直）*/
