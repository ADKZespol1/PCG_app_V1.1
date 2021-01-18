package com.example.adk_recorder.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adk_recorder.Adatpers.FileViewerAdapter;
import com.example.adk_recorder.Database.DBHelper;
import com.example.adk_recorder.Models.RecordingItem;
import com.example.adk_recorder.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FileViewerFragmet extends Fragment {


    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    DBHelper dbHelper;
    private FileViewerAdapter fileViewerAdapter;

    ArrayList<RecordingItem> arrayListAudios;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file_viewer,container,false);
        ButterKnife.bind(this,view);
        return view;
        }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper=new DBHelper(getContext());
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());

        llm.setOrientation(LinearLayoutManager.VERTICAL);

        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);

        arrayListAudios = dbHelper.getAllAudiios();

        if(arrayListAudios==null){
            Toast.makeText(getContext(), "No audios found", Toast.LENGTH_SHORT).show();
        }
        else
        {
            fileViewerAdapter= new FileViewerAdapter(getActivity(),arrayListAudios,llm);
            recyclerView.setAdapter(fileViewerAdapter);
        }

    }
}
