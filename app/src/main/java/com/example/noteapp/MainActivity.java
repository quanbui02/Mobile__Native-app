package com.example.noteapp;

import android.content.Intent;
import android.os.Bundle;

import com.example.noteapp.Adapter.FolderAdapter;
import com.example.noteapp.Model.Folder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Folder> listFolder;
    private RecyclerView rcView;
    private FolderAdapter folderAdapter;
    private TextView textFolder;
    private EditText searchText;
    private FloatingActionButton addFolderBtn;
    private ActivityResultLauncher<Intent> rsLauncherForAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getAllMovie();
        this.initResultLauncher();

        this.textFolder = findViewById(R.id.TextFolder);
        this.searchText = findViewById(R.id.searchText);
        this.addFolderBtn = findViewById(R.id.addFolderButton);

        this.folderAdapter = new FolderAdapter(this,this.listFolder);
        this.rcView = findViewById(R.id.recycleFolder);
        this.rcView.setAdapter(this.folderAdapter);
        this.rcView.addItemDecoration(new DividerItemDecoration(this.rcView.getContext(), DividerItemDecoration.VERTICAL));
        this.rcView.setLayoutManager(new LinearLayoutManager(this));

        this.addFolderBtn.setOnClickListener(v->{
            Intent i = new Intent(MainActivity.this,AddFolderActivity.class);
            i.putExtra("flag","add");
            this.rsLauncherForAdd.launch(i);
        });
}
    public void getAllMovie(){
        try {
            this.listFolder= new ArrayList<Folder>();
            Folder f1 = new Folder(1,"Folder1","active");
            this.listFolder.add(f1);
            Folder f2 = new Folder(1,"Folder2","active");
            this.listFolder.add(f2);
            Folder f3 = new Folder(1,"Folder3","active");
            this.listFolder.add(f3);
        }
        catch (Exception ex){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage(ex.getMessage());
            alert.show();
        }
    }
    public void initResultLauncher(){
        try{
            this.rsLauncherForAdd = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),rs->{
                if(rs != null && rs.getResultCode() == RESULT_OK){
                    Folder f = (Folder) rs.getData().getSerializableExtra("folder");
                    this.listFolder.add(f);
                    this.folderAdapter.notifyDataSetChanged();
                }
            });
        }
        catch(Exception ex){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage(ex.getMessage());
            alert.show();
        }
    }
}