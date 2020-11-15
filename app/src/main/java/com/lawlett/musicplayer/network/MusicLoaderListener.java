/*
 * This is the source code of DMAudioStreaming for Android v. 1.0.0.
 * You should have received a copy of the license in this archive (see LICENSE).
 * Copyright @Dibakar_Mistry(dibakar.ece@gmail.com), 2017.
 */
package com.lawlett.musicplayer.network;

import com.lawlett.musicplayer.models.SongModel;

import java.util.List;

import dm.audiostreamer.MediaMetaData;

public interface MusicLoaderListener {

    void onLoadSuccess(List<MediaMetaData> listMusic);

    void onLoadFailed();

    void onLoadError();
}
