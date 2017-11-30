package vn.techkids.freemusic11.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import hybridmediaplayer.HybridMediaPlayer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.techkids.freemusic11.databases.TopSongModel;
import vn.techkids.freemusic11.networks.MusicInterface;
import vn.techkids.freemusic11.networks.RetrofitInstance;
import vn.techkids.freemusic11.networks.SearchResponseJSON;

/**
 * Created by qklahpita on 11/29/17.
 */

public class MusicHandler {
    private static final String TAG = "MusicHandler";
    private static HybridMediaPlayer hybridMediaPlayer;

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
    }


}
