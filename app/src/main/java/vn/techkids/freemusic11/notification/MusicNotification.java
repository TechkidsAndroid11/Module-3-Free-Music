package vn.techkids.freemusic11.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import vn.techkids.freemusic11.R;
import vn.techkids.freemusic11.activities.MainActivity;
import vn.techkids.freemusic11.databases.TopSongModel;
import vn.techkids.freemusic11.utils.MusicHandler;

/**
 * Created by qklahpita on 12/9/17.
 */
//TODO: check noti when swipe app
public class MusicNotification {
    private static final String TAG = "MusicNotification";

    private static RemoteViews remoteViews;
    public static NotificationCompat.Builder builder;
    public static NotificationManager notificationManager;

    private static final int NOTIFICATION_ID = 1;

    public static void setupNotification(Context context, TopSongModel topSongModel) {

        remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.notification_layout);
        remoteViews.setTextViewText(R.id.tv_song, topSongModel.song);
        remoteViews.setTextViewText(R.id.tv_singer, topSongModel.singer);
        remoteViews.setImageViewResource(R.id.iv_play, R.drawable.ic_pause_black_24dp);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.ic_music_note_black_24dp)
                .setContent(remoteViews)
                .setContentIntent(pendingIntent)
                .setOngoing(true);

        notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (topSongModel.smallImage != null) {
            Picasso.with(context).load(topSongModel.smallImage).transform(new CropCircleTransformation())
                    .into(remoteViews, R.id.iv_song, NOTIFICATION_ID, builder.build());
        } else {
            Picasso.with(context).load(topSongModel.offlineImage).transform(new CropCircleTransformation())
                    .into(remoteViews, R.id.iv_song, NOTIFICATION_ID, builder.build());
        }
        setOnClickPlayPause(context);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public static void updateNotification() {
        if (!MusicHandler.hybridMediaPlayer.isPlaying()) {
            remoteViews.setImageViewResource(R.id.iv_play, R.drawable.ic_play_arrow_black_24dp);
            builder.setOngoing(false);
        } else {
            remoteViews.setImageViewResource(R.id.iv_play, R.drawable.ic_pause_black_24dp);
            builder.setOngoing(true);
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private static void setOnClickPlayPause(Context context) {

        Intent intent = new Intent(context, MusicService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

        remoteViews.setOnClickPendingIntent(R.id.iv_play, pendingIntent);
    }
}
