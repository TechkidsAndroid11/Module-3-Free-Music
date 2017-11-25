package vn.techkids.freemusic11.networks;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by LapTop on 11/15/2017.
 */

public interface MusicInterface {
    @GET("api")
    Call<MusicTypesResponseJSON> getMusicTypes();

    @GET("https://itunes.apple.com/us/rss/topsongs/limit=50/genre={id}/explicit=true/json")
    Call<TopSongsResponseJSON> getTopSongs(@Path("id") String id);
}
