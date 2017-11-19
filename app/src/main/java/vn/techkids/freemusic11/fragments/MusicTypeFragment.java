package vn.techkids.freemusic11.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.techkids.freemusic11.R;
import vn.techkids.freemusic11.activities.MainActivity;
import vn.techkids.freemusic11.adapters.MusicTypeAdapter;
import vn.techkids.freemusic11.databases.MusicTypeModel;
import vn.techkids.freemusic11.networks.MusicTypesInterface;
import vn.techkids.freemusic11.networks.MusicTypesResponseJSON;
import vn.techkids.freemusic11.networks.RetrofitInstance;

/**
 * A simple {@link Fragment} subclass.
 */
public class MusicTypeFragment extends Fragment {
    @BindView(R.id.rv_music_types)
    RecyclerView rvMusicType;

    private List<MusicTypeModel> musicTypeModelList = new ArrayList<>();
    private Context context;
    private MusicTypeAdapter musicTypeAdapter;

    public MusicTypeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music_type, container, false);
        ButterKnife.bind(this, view);
        context = getContext();

        musicTypeAdapter = new MusicTypeAdapter(musicTypeModelList);
        rvMusicType.setAdapter(musicTypeAdapter);

        rvMusicType.setLayoutManager(new LinearLayoutManager(context));

        loadData();

        return view;
    }

    private void loadData() {

        MusicTypesInterface musicTypesInterface =
                RetrofitInstance.getInstance().create(MusicTypesInterface.class);
        musicTypesInterface.getMusicTypes().enqueue(new Callback<MusicTypesResponseJSON>() {
            @Override
            public void onResponse(Call<MusicTypesResponseJSON> call, Response<MusicTypesResponseJSON> response) {
                List<MusicTypesResponseJSON.SubObjectJSON> list
                        = response.body().subgenres;
                for (MusicTypesResponseJSON.SubObjectJSON subObjectJSON : list) {
                    MusicTypeModel musicTypeModel = new MusicTypeModel();
                    musicTypeModel.id = subObjectJSON.id;
                    musicTypeModel.key = subObjectJSON.translation_key;
                    musicTypeModel.imageID = context.getResources().getIdentifier(
                            "genre_x2_" + musicTypeModel.id,
                            "raw",
                            context.getPackageName()
                    );
                    musicTypeModelList.add(musicTypeModel);
                }
                musicTypeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MusicTypesResponseJSON> call, Throwable t) {
                Toast.makeText(context, "No connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
