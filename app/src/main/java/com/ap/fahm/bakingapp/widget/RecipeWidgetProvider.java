package com.ap.fahm.bakingapp.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.ap.fahm.bakingapp.R;


public class RecipeWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews remoteViews = getRecipeGridRemoteView(context, appWidgetId);
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    public static RemoteViews getRecipeGridRemoteView(Context context, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        views.setImageViewResource(R.id.iv_no_recipe, R.drawable.ic_dish);

        Intent chooserIntent = new Intent(context, RecipeChoiceActivity.class);
        chooserIntent.putExtra("widgetId", appWidgetId);
        PendingIntent chooserPendingIntent = PendingIntent.getActivity(
                context, appWidgetId, chooserIntent, 0);
        views.setOnClickPendingIntent(R.id.bt_add_recipe, chooserPendingIntent);

        return views;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        updateAppWidget(context, appWidgetManager, appWidgetId);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}

