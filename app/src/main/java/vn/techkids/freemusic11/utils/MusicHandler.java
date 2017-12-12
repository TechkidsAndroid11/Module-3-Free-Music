package vn.techkids.freemusic11.utils;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.logging.Handler;

import hybridmediaplayer.HybridMediaPlayer;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.techkids.freemusic11.R;
import vn.techkids.freemusic11.databases.TopSongModel;
import vn.techkids.freemusic11.networks.MusicInterface;
import vn.techkids.freemusic11.networks.RetrofitInstance;
import vn.techkids.freemusic11.networks.SearchResponseJSON;
import vn.techkids.freemusic11.notification.MusicNotification;

/**
 * Created by qklahpita on 11/29/17.
 */

public class MusicHandler {
    private static final String TAG = "MusicHandler";
    public static HybridMediaPlayer hybridMediaPlayer;
    private static boolean keepUpdating = true;

    public static void getSearchSong(final TopSongModel topSongModel, final Context context) {
        MusicInterface musicInterface = RetrofitInstance.getInstance()
                .create(MusicInterface.class);
        musicInterface.getSearchSong(topSongModel.song + " " + topSongModel.singer)
                .enqueue(new Callback<SearchResponseJSON>() {
                    @Override
                    public void onResponse(Call<SearchResponseJSON> call, Response<SearchResponseJSON> response) {
                        Log.d(TAG, "onResponse: " + response.code());
                        if (response.code() == 200) {
                            topSongModel.url = response.body().data.url;
                            topSongModel.largeImage = response.body().data.thumbnail;

                            playMusic(context, topSongModel);
                            MusicNotification.setupNotification(context, topSongModel);
                        } else if (response.code() == 500) {
                            Toast.makeText(context, "Not found!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchResponseJSON> call, Throwable t) {
                        Toast.makeText(context, "No connection!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private static void playMusic(Context context, TopSongModel topSongModel) {

        if (hybridMediaPlayer != null) {
            hybridMediaPlayer.pause();
            hybridMediaPlayer.release();
        }

        hybridMediaPlayer = HybridMediaPlayer.getInstance(context);
        hybridMediaPlayer.setDataSource(topSongModel.url);
        hybridMediaPlayer.prepare();
        hybridMediaPlayer.setOnPreparedListener(new HybridMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(HybridMediaPlayer hybridMediaPlayer) {
                hybridMediaPlayer.play();
            }
        });
    }

    public static void playPause() {
        if (hybridMediaPlayer.isPlaying()) {
            hybridMediaPlayer.pause();
        } else {
            hybridMediaPlayer.play();
        }
        MusicNotification.updateNotification();
    }

    public static void updateUIRealtime(final SeekBar seekBar,
                                        final FloatingActionButton floatingActionButton,
                                        final ImageView imageView,
                                        final TextView tvCurrent,
                                        final TextView tvDuration) {
        final android.os.Handler handler = new android.os.Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //update UI
                if (keepUpdating && (hybridMediaPlayer != null)) {
                    seekBar.setMax(hybridMediaPlayer.getDuration());
                    seekBar.setProgress(hybridMediaPlayer.getCurrentPosition());

                    if (hybridMediaPlayer.isPlaying()) {
                        floatingActionButton.setImageResource(R.drawable.ic_pause_black_24dp);
                    } else {
                        floatingActionButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                    }

                    Utils.rotateImage(imageView, hybridMediaPlayer.isPlaying());

                    if (tvCurrent != null) {
                        tvCurrent.setText(Utils.convertTime(hybridMediaPlayer.getCurrentPosition()));
                        tvDuration.setText(Utils.convertTime(hybridMediaPlayer.getDuration()));
                    }
                }

                handler.postDelayed(this, 100);
            }
        };
        runnable.run();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                keepUpdating = false;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                hybridMediaPlayer.seekTo(seekBar.getProgress());
                keepUpdating = true;
            }
        });
    }
}
