package com.lawlett.musicplayer.ui.list;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lawlett.musicplayer.App;
import com.lawlett.musicplayer.R;
import com.lawlett.musicplayer.adapters.adapter.AdapterMusic;
import com.lawlett.musicplayer.data.network.MusicBrowser;
import com.lawlett.musicplayer.data.network.MusicLoaderListener;
import com.lawlett.musicplayer.data.network.local.Prefs;
import com.lawlett.musicplayer.room.FavoriteModel;
import com.lawlett.musicplayer.ui.history.HistoryFragment;
import com.lawlett.musicplayer.ui.music.MusicActivity;
import com.lawlett.musicplayer.ui.years.YearsFragment;
import com.lawlett.musicplayer.widgets.LineProgress;
import com.lawlett.musicplayer.widgets.PlayPauseView;
import com.lawlett.musicplayer.widgets.Slider;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import dm.audiostreamer.AudioStreamingManager;
import dm.audiostreamer.CurrentSessionCallback;
import dm.audiostreamer.Logger;
import dm.audiostreamer.MediaMetaData;

public class ListFragment extends Fragment implements CurrentSessionCallback, View.OnClickListener, Slider.OnValueChangedListener {
    HistoryFragment historyFragment;
    ImageView favoriteBtn;
    FavoriteModel favoriteModel;
    List<FavoriteModel> list;
    FavoriteModel favoriteModelFromHistory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            year = getArguments().getString(YearsFragment.YEAR, "2020");
            if (getArguments().getSerializable(HistoryFragment.MEDIA) != null) {
                favoriteModelFromHistory = (FavoriteModel) getArguments().getSerializable(HistoryFragment.MEDIA);
            }
            Log.d("TAG", year);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    private String year = "2020";

    private Context context;
    private ListView musicList;
    private AdapterMusic adapterMusic;

    private PlayPauseView btn_play;
    private ImageView image_songAlbumArt;
    private ImageView img_bottom_albArt;
    private ImageView image_songAlbumArtBlur;
    private TextView time_progress_slide;
    private TextView time_total_slide;
    private TextView time_progress_bottom;
    private TextView time_total_bottom;
    private RelativeLayout pgPlayPauseLayout;
    private LineProgress lineProgress;
    private Slider audioPg;
    private ImageView btn_backward;
    private ImageView btn_forward;
    private TextView text_songName;
    private TextView text_songAlb;
    private TextView txt_bottom_SongName;
    private TextView txt_bottom_SongAlb;

    private SlidingUpPanelLayout mLayout;
    private RelativeLayout slideBottomView;
    private boolean isExpand = false;

    private DisplayImageOptions options;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private AudioStreamingManager streamingManager;
    private MediaMetaData currentSong;
    private List<MediaMetaData> listOfSongs = new ArrayList<MediaMetaData>();

    public static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingStarted(String imageUri, View view) {
            progressEvent(view, false);
        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            progressEvent(view, true);
        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 1000);
                    displayedImages.add(imageUri);
                }
            }
            progressEvent(view, true);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            if (streamingManager != null) {
                streamingManager.subscribesCallBack(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        try {
            if (streamingManager != null) {
                streamingManager.unSubscribeCallBack();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        try {
            if (streamingManager != null) {
                streamingManager.unSubscribeCallBack();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    public void saveFavoriteSong() {

        favoriteModel = new FavoriteModel(currentSong.getMediaTitle(), currentSong.getMediaArt(), currentSong.getMediaArtist(), currentSong.getMediaUrl(), new Prefs(getContext()).getYear(), currentSong.getMediaId());
        App.getDataBase().favoriteDao().insert(favoriteModel);
        Toast.makeText(context, currentSong.getMediaTitle(), Toast.LENGTH_SHORT).show();

        Log.e("favorite", "sendFavoriteSong: " + currentSong.getMediaTitle());
    }

    @Override
    public void updatePlaybackState(int state) {
        Logger.e("updatePlaybackState: ", "" + state);
        switch (state) {
            case PlaybackStateCompat.STATE_PLAYING:
                pgPlayPauseLayout.setVisibility(View.INVISIBLE);
                btn_play.Play();
                if (currentSong != null) {
                    currentSong.setPlayState(PlaybackStateCompat.STATE_PLAYING);
                    notifyAdapter(currentSong);

                }
                break;
            case PlaybackStateCompat.STATE_PAUSED:
                pgPlayPauseLayout.setVisibility(View.INVISIBLE);
                btn_play.Pause();
                if (currentSong != null) {
                    currentSong.setPlayState(PlaybackStateCompat.STATE_PAUSED);
                    notifyAdapter(currentSong);
                }
                break;
            case PlaybackStateCompat.STATE_NONE:
                currentSong.setPlayState(PlaybackStateCompat.STATE_NONE);
                notifyAdapter(currentSong);
                break;
            case PlaybackStateCompat.STATE_STOPPED:
                pgPlayPauseLayout.setVisibility(View.INVISIBLE);
                btn_play.Pause();
                audioPg.setValue(0);
                if (currentSong != null) {
                    currentSong.setPlayState(PlaybackStateCompat.STATE_NONE);
                    notifyAdapter(currentSong);
                }
                break;
            case PlaybackStateCompat.STATE_BUFFERING:
                pgPlayPauseLayout.setVisibility(View.VISIBLE);
                if (currentSong != null) {
                    currentSong.setPlayState(PlaybackStateCompat.STATE_NONE);
                    notifyAdapter(currentSong);
                }
                break;
        }
    }

    @Override
    public void playSongComplete() {
        String timeString = "00.00";
        time_total_bottom.setText(timeString);
        time_total_slide.setText(timeString);
        time_progress_bottom.setText(timeString);
        time_progress_slide.setText(timeString);
        lineProgress.setLineProgress(0);
        audioPg.setValue(0);
    }

    @Override
    public void currentSeekBarPosition(int progress) {
        audioPg.setValue(progress);
        setPGTime(progress);
    }

    @Override
    public void playCurrent(int indexP, MediaMetaData currentAudio) {
        showMediaInfo(currentAudio);
        notifyAdapter(currentAudio);
    }

    @Override
    public void playNext(int indexP, MediaMetaData CurrentAudio) {
        showMediaInfo(CurrentAudio);
    }

    @Override
    public void playPrevious(int indexP, MediaMetaData currentAudio) {
        showMediaInfo(currentAudio);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_forward:
                streamingManager.onSkipToNext();
                break;
            case R.id.btn_backward:
                streamingManager.onSkipToPrevious();
                break;
            case R.id.btn_play:
                if (currentSong != null) {
                    playPauseEvent(view);
                }
                break;
        }
    }

    @Override
    public void onValueChanged(int value) {
        streamingManager.onSeekTo(value);
        streamingManager.scheduleSeekBarUpdate();
    }

    private void notifyAdapter(MediaMetaData media) {
        adapterMusic.notifyPlayState(media);
    }

    private void playPauseEvent(View v) {
        if (streamingManager.isPlaying()) {
            streamingManager.onPause();
            ((PlayPauseView) v).Pause();
        } else {
            streamingManager.onPlay(currentSong);
            ((PlayPauseView) v).Play();
        }
    }

    private void playSong(MediaMetaData media) {
        if (streamingManager != null) {
            streamingManager.onPlay(media);
            showMediaInfo(media);
        }
    }

    private void showMediaInfo(MediaMetaData media) {
        currentSong = media;
        audioPg.setValue(0);
        audioPg.setMin(0);
        audioPg.setMax(Integer.valueOf(media.getMediaDuration()) * 1000);
        setPGTime(0);
        setMaxTime();
        loadSongDetails(media);
    }

    private void configAudioStreamer() {
        streamingManager = AudioStreamingManager.getInstance(context);
        streamingManager.setPlayMultiple(true);
        if (!streamingManager.isMediaListEmpty() && !listOfSongs.equals(new Prefs(getContext()).getYear())) {
            streamingManager.clearList();
            Log.d("TAG", "yUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU");
        }
        streamingManager.setMediaList(listOfSongs);
        streamingManager.setShowPlayerNotification(true);
        streamingManager.setPendingIntentAct(getNotificationPendingIntent());
        if (favoriteModelFromHistory != null) {
            MediaMetaData mediaMetaData = new MediaMetaData();
            mediaMetaData.setMediaArtist(favoriteModelFromHistory.getMediaArtist());
            mediaMetaData.setMediaTitle(favoriteModelFromHistory.getTitle());
            mediaMetaData.setMediaArt(favoriteModelFromHistory.getMediaArt());
            mediaMetaData.setMediaAlbum("album");
            mediaMetaData.setMediaComposer("composer");
            mediaMetaData.setMediaId(favoriteModelFromHistory.getIdSong());
            mediaMetaData.setMediaDuration("210");
            mediaMetaData.setMediaUrl(favoriteModelFromHistory.getSongUrl());
            playSong(mediaMetaData);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.context = getContext();
        uiInitialization(view);
        configAudioStreamer();
        loadMusicData();
    }

    private void uiInitialization(View view) {

//        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        toolbar.setTitle(getString(R.string.app_name));

        btn_play = (PlayPauseView) view.findViewById(R.id.btn_play);
        image_songAlbumArtBlur = (ImageView) view.findViewById(R.id.image_songAlbumArtBlur);
        image_songAlbumArt = (ImageView) view.findViewById(R.id.image_songAlbumArt);
        img_bottom_albArt = (ImageView) view.findViewById(R.id.img_bottom_albArt);
        btn_backward = (ImageView) view.findViewById(R.id.btn_backward);
        btn_forward = (ImageView) view.findViewById(R.id.btn_forward);
        audioPg = (Slider) view.findViewById(R.id.audio_progress_control);
        pgPlayPauseLayout = (RelativeLayout) view.findViewById(R.id.pgPlayPauseLayout);
        lineProgress = (LineProgress) view.findViewById(R.id.lineProgress);
        time_progress_slide = (TextView) view.findViewById(R.id.slidepanel_time_progress);
        time_total_slide = (TextView) view.findViewById(R.id.slidepanel_time_total);
        time_progress_bottom = (TextView) view.findViewById(R.id.slidepanel_time_progress_bottom);
        time_total_bottom = (TextView) view.findViewById(R.id.slidepanel_time_total_bottom);

        btn_backward.setOnClickListener(this);
        btn_forward.setOnClickListener(this);
        btn_play.setOnClickListener(this);
        favoriteBtn = view.findViewById(R.id.favorite_btn);
        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveFavoriteSong();
            }
        });

        pgPlayPauseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                return;
            }
        });

        btn_play.Pause();

        changeButtonColor(btn_backward);
        changeButtonColor(btn_forward);

        text_songName = (TextView) view.findViewById(R.id.text_songName);
        text_songAlb = (TextView) view.findViewById(R.id.text_songAlb);
        txt_bottom_SongName = (TextView) view.findViewById(R.id.txt_bottom_SongName);
        txt_bottom_SongAlb = (TextView) view.findViewById(R.id.txt_bottom_SongAlb);

        mLayout = (SlidingUpPanelLayout) view.findViewById(R.id.sliding_layout);

        slideBottomView = (RelativeLayout) view.findViewById(R.id.slideBottomView);
        slideBottomView.setVisibility(View.VISIBLE);
        slideBottomView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            }
        });

        audioPg.setMax(0);
        audioPg.setOnValueChangedListener(this);

        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float slideOffset) {
                if (slideOffset == 0.0f) {
                    isExpand = false;
                    slideBottomView.setVisibility(View.VISIBLE);
                    //slideBottomView.getBackground().setAlpha(0);
                } else if (slideOffset > 0.0f && slideOffset < 1.0f) {
                    //slideBottomView.getBackground().setAlpha((int) slideOffset * 255);
                } else {
                    //slideBottomView.getBackground().setAlpha(100);
                    isExpand = true;
                    slideBottomView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPanelStateChanged(View view, SlidingUpPanelLayout.PanelState panelState, SlidingUpPanelLayout.PanelState panelState1) {
                switch (panelState) {
                    case EXPANDED:
                        isExpand = true;
                        break;
                    case COLLAPSED:
                        isExpand = false;
                        break;
                    default:
                        isExpand = false;
                        break;
                }

            }


        });

        musicList = (ListView) view.findViewById(R.id.musicList);
        adapterMusic = new AdapterMusic(context, new ArrayList<MediaMetaData>());
        adapterMusic.setListItemListener(new AdapterMusic.ListItemListener() {
            @Override
            public void onItemClickListener(MediaMetaData media) {
                playSong(media);
            }
        });
        musicList.setAdapter(adapterMusic);

        this.options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.bg_default_album_art)
                .showImageForEmptyUri(R.drawable.bg_default_album_art)
                .showImageOnFail(R.drawable.bg_default_album_art).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

    }

    private void loadMusicData() {
        MusicBrowser.loadMusic(new Prefs(getContext()).getYear(), context, new MusicLoaderListener() {
            @Override
            public void onLoadSuccess(List<MediaMetaData> listMusic) {
                if (!listOfSongs.isEmpty()) {
                    listOfSongs.clear();
                    Log.d("TAG", "cleaned");
                }
                listOfSongs = listMusic;
                adapterMusic.refresh(listOfSongs);
                configAudioStreamer();
                checkAlreadyPlaying();
            }

            @Override
            public void onLoadFailed() {
                //TODO SHOW FAILED REASON
            }

            @Override
            public void onLoadError() {
                //TODO SHOW ERROR
            }
        });
    }

    private void checkAlreadyPlaying() {
        if (streamingManager.isPlaying()) {
            currentSong = streamingManager.getCurrentAudio();
            if (currentSong != null) {
                currentSong.setPlayState(streamingManager.mLastPlaybackState);
                showMediaInfo(currentSong);
                notifyAdapter(currentSong);
            }
        }
    }

    private void loadSongDetails(MediaMetaData metaData) {
        text_songName.setText(metaData.getMediaTitle());
        text_songAlb.setText(metaData.getMediaArtist());
        txt_bottom_SongName.setText(metaData.getMediaTitle());
        txt_bottom_SongAlb.setText(metaData.getMediaArtist());

        imageLoader.displayImage(metaData.getMediaArt(), image_songAlbumArtBlur, options, animateFirstListener);
        imageLoader.displayImage(metaData.getMediaArt(), image_songAlbumArt, options, animateFirstListener);
        imageLoader.displayImage(metaData.getMediaArt(), img_bottom_albArt, options, animateFirstListener);
    }


    private static void progressEvent(View v, boolean isShowing) {
        try {
            View parent = (View) ((ImageView) v).getParent();
            ProgressBar pg = (ProgressBar) parent.findViewById(R.id.pg);
            if (pg != null)
                pg.setVisibility(isShowing ? View.GONE : View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setPGTime(int progress) {
        try {
            String timeString = "00.00";
            int linePG = 0;
            currentSong = streamingManager.getCurrentAudio();
            if (currentSong != null && progress != Long.parseLong(currentSong.getMediaDuration())) {
                timeString = DateUtils.formatElapsedTime(progress / 1000);
                Long audioDuration = Long.parseLong(currentSong.getMediaDuration());
                linePG = (int) (((progress / 1000) * 100) / audioDuration);
            }
            time_progress_bottom.setText(timeString);
            time_progress_slide.setText(timeString);
            lineProgress.setLineProgress(linePG);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void setMaxTime() {
        try {
            String timeString = DateUtils.formatElapsedTime(Long.parseLong(currentSong.getMediaDuration()));
            time_total_bottom.setText(timeString);
            time_total_slide.setText(timeString);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void changeButtonColor(ImageView imageView) {
        try {
            int color = Color.BLACK;
            imageView.setColorFilter(color);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PendingIntent getNotificationPendingIntent() {
        Intent intent = new Intent(context, MusicActivity.class);
        intent.setAction("openplayer");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent mPendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        return mPendingIntent;
    }


}
