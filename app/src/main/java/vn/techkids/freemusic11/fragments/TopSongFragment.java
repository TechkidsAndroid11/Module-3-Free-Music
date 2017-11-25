package vn.techkids.freemusic11.fragments;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.techkids.freemusic11.R;
import vn.techkids.freemusic11.databases.MusicTypeModel;
import vn.techkids.freemusic11.events.OnClickMusicTypeEvent;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopSongFragment extends Fragment {
    private static final String TAG = "TopSongFragment";

    @BindView(R.id.tv_music_type)
    TextView tvMusicType;
    @BindView(R.id.iv_favourite)
    ImageView ivFav;
    @BindView(R.id.iv_music_type)
    ImageView ivMusicType;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.rv_top_songs)
    RecyclerView rvTopSongs;
    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;

    public MusicTypeModel musicTypeModel;

    public TopSongFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_top_song, container, false);

        EventBus.getDefault().register(this);

        setupUI(view);

        return view;
    }

    @Subscribe(sticky = true)
    public void onReceivedOnMusicTypeClicked(OnClickMusicTypeEvent onClickMusicTypeEvent) {
        musicTypeModel = onClickMusicTypeEvent.musicTypeModel;
    }

    private void setupUI(View view) {
        ButterKnife.bind(this, view);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        tvMusicType.setText(musicTypeModel.key);
        Picasso.with(getContext()).load(musicTypeModel.imageID).into(ivMusicType);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.d(TAG, "onOffsetChanged: " + verticalOffset);

                if (verticalOffset == 0) {
                    toolbar.setBackground(getResources().getDrawable
                            (R.drawable.custom_gradient_text_bot_to_top));
                } else {
                    toolbar.setBackground(null);
                }
            }
        });
    }

}
