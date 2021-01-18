package com.example.adk_recorder.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.adk_recorder.R;
import com.example.adk_recorder.Services.RecordingService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecordFragment extends Fragment
{
    @BindView(R.id.chronometer) Chronometer chronometer;
    @BindView(R.id.recording_status_txt) TextView recording_status_txt;
    @BindView(R.id.btnRecord) FloatingActionButton btnRecord;
    @BindView(R.id.btnPause) Button btnPause;
    @BindView(R.id.Json) Button json;

    private boolean mStartRecording = true;
    private boolean mPauseRecording = true;
    long timeWhenPaused = 0;
    @Override
    public void onCreate(@Nullable Bundle savedInstacneState){
        super.onCreate(savedInstacneState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View recordView = inflater.inflate(R.layout.fragment_record, container, false);
        ButterKnife.bind(this, recordView);
        return recordView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnPause.setVisibility(View.GONE);
        btnRecord.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

    }
    @OnClick(R.id.Json)
    public void sendRequest()
    {
        //File file = new File(textFileName);
        //HttpPost post = new HttpPost("http://echo.200please.com");
        //FileBody fileBody = new FileBody(file, ContentType.DEFAULT_BINARY);
        //StringBody stringBody1 = new StringBody("Message 1", ContentType.MULTIPART_FORM_DATA);
        //StringBody stringBody2 = new StringBody("Message 2", ContentType.MULTIPART_FORM_DATA);
//
        //MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        //builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        //builder.addPart("upfile", fileBody);
        //builder.addPart("text1", stringBody1);
        //builder.addPart("text2", stringBody2);
        //HttpEntity entity = builder.build();
//
        //post.setEntity(entity);
        //HttpResponse response = client.execute(post);
    }

    @OnClick(R.id.btnRecord)
    public void recordAudio()
    {
        onRecord(mStartRecording);
        mStartRecording = !mStartRecording;
    }

    private void onRecord(boolean start)
    {
        Intent intent = new Intent(getActivity(), RecordingService.class);

        if(start)
        {
            btnRecord.setImageResource(R.drawable.ic_media_stop);
            Toast.makeText(getContext(), "Recodring started", Toast.LENGTH_LONG).show();

                File folder = new File(Environment.getExternalStorageDirectory() + "/.My_sound_Record");

            if (!folder.exists()) {
                folder.mkdir();
            }

            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();

            getActivity().startService(intent);
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            recording_status_txt.setText("Recording");

        }
        else
        {
            btnRecord.setImageResource(R.drawable.ic_mic_white);
            chronometer.stop();
            chronometer.setBase(SystemClock.elapsedRealtime());
            timeWhenPaused = 0;
            recording_status_txt.setText("Tap button to start recording");

            getActivity().stopService(intent);
        }
    }
}
