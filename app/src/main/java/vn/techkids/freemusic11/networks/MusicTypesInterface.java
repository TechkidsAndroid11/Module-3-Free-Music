package vn.techkids.freemusic11.networks;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by LapTop on 11/15/2017.
 */

public interface MusicTypesInterface {
    @GET("api")
    Call<MusicTypesResponseJSON> getMusicTypes();
}
