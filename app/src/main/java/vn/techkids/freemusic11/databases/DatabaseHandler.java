package vn.techkids.freemusic11.databases;

import java.util.List;

import io.realm.Realm;

/**
 * Created by qklahpita on 12/16/17.
 */

public class DatabaseHandler {
    private static Realm realm = Realm.getDefaultInstance();

    public static void addMusicType(MusicTypeModel musicTypeModel) {
        realm.beginTransaction();
        realm.copyToRealm(musicTypeModel);
        realm.commitTransaction();
    }

    public static List<MusicTypeModel> getMusicTypes() {
        return realm.where(MusicTypeModel.class).findAll();
    }

    public static void updateFavourite(MusicTypeModel musicTypeModel) {
        realm.beginTransaction();
        musicTypeModel.isFavourite = !musicTypeModel.isFavourite;
        realm.commitTransaction();
    }

    public static List<MusicTypeModel> getFavourites() {
        return realm.where(MusicTypeModel.class)
                .equalTo("isFavourite", true).findAll();
    }
}
