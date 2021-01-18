package com.example.adk_recorder.Services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.telecom.Call;
import android.text.format.DateUtils;
import android.widget.Toast;

import com.example.adk_recorder.Database.DBHelper;
import com.example.adk_recorder.Models.RecordingItem;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

public class RecordingService extends Service {

    MediaRecorder mediaRecorder;
    long mStartingTimeMillis=0;
    long mElapsedMillis = 0;

    File file;

    String fileName;

    DBHelper dbHelper;

    @Override
    public void onCreate(){
        super.onCreate();
        dbHelper= new DBHelper(getApplicationContext());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startRecording();
        return START_STICKY;
    }

    private void startRecording() {
        Long tsLong= System.currentTimeMillis()/1000;
        Calendar now= Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        Long hours = tsLong/3600;
        Long minutes = tsLong/60 - hours*60;
        Long secodns = tsLong - minutes*60 - hours *3600;
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int thisday = calendar.get(Calendar.DAY_OF_MONTH);
        int thismonth = calendar.get(Calendar.MONTH)+1;
        int thisYear = calendar.get(Calendar.YEAR);
        String da = String.valueOf(thisday);
        String mo = String.valueOf(thismonth);
        String ye = String.valueOf(thisYear);
        String ts = secodns.toString();
        String th = String.valueOf(hour);
        String tm = minutes.toString();


        fileName= "User x "+ da+"_"+mo+"_"+ye+"   "+ th+"_"+tm+"_"+ts;

        file=   new File(Environment.getExternalStorageDirectory() + "/.My_sound_Record/"+fileName+".wav");//wav

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setOutputFormat(AudioFormat.ENCODING_PCM_16BIT);
        mediaRecorder.setOutputFile(file.getAbsolutePath());
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setAudioChannels(1);
        mediaRecorder.setAudioEncodingBitRate(128000);
        mediaRecorder.setAudioSamplingRate(48000);

        try
        {
            mediaRecorder.prepare();
            mediaRecorder.start();

            mStartingTimeMillis= System.currentTimeMillis();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    private void stopRecording()
    {
        mediaRecorder.stop();
        mElapsedMillis = (System.currentTimeMillis()- mStartingTimeMillis);
        mediaRecorder.release();
        Toast.makeText(getApplicationContext(), "Recording saved" +file.getAbsolutePath(), Toast.LENGTH_LONG).show();

        // add2 database

        RecordingItem recordingItem = new RecordingItem(fileName,file.getAbsolutePath(),mElapsedMillis,System.currentTimeMillis());

        dbHelper.addRecording(recordingItem);

    }

    @Override
    public void onDestroy() {
        if (mediaRecorder!=null)
        {
            stopRecording();
        }
        super.onDestroy();
    }
}
