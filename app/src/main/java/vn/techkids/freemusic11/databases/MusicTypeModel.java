package vn.techkids.freemusic11.databases;

import io.realm.RealmObject;

/**
 * Created by qklahpita on 11/18/17.
 */

public class MusicTypeModel extends RealmObject{
    public String key;
    public String id;
    public int imageID;

    public boolean isFavourite;
}
