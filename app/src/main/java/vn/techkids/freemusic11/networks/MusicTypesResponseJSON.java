package vn.techkids.freemusic11.networks;

import java.util.List;

/**
 * Created by LapTop on 11/15/2017.
 */

public class MusicTypesResponseJSON {
    public List<SubObjectJSON> subgenres;

    public class SubObjectJSON {
        public String id;
        public String translation_key;
    }
}
