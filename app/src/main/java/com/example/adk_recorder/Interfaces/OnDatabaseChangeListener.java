package com.example.adk_recorder.Interfaces;

import com.example.adk_recorder.Models.RecordingItem;

public interface OnDatabaseChangeListener {

    void onNewDatabaseEntryAdded(RecordingItem recordingItem);

}
