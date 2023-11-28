package com.example.noteapp;

import android.content.Intent;
import android.os.Bundle;

import com.example.noteapp.Adapter.NoteDeleteAdapter;
import com.example.noteapp.Database.DatabaseForApp;
import com.example.noteapp.Model.Note;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp.databinding.ActivityDeleteNoteListBinding;

import java.util.ArrayList;

public class DeleteNoteListActivity extends AppCompatActivity {
    private TextView headerNoteDText;
    private Button backNoteDBtn;
    private RecyclerView rcNoteDelete;
    private EditText searchNoteDText;
    private ArrayList<Note> listNoteD;
    private NoteDeleteAdapter noteDeleteAdapter;
    private ActivityResultLauncher<Intent> rsLaucherForRestore;
    private DatabaseForApp db;
    private int idFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_note_list);
        this.db = new DatabaseForApp(this);
        this.intRS();
        try {
            Intent i = getIntent();
            this.idFolder=i.getIntExtra("idFolder",1);
        }
        catch(Exception ex){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage(ex.getMessage());
            alert.show();
        }
        this.getAllNoteDelete();

        this.headerNoteDText = findViewById(R.id.headerNoteDeleteText);
        this.backNoteDBtn = findViewById(R.id.backNoteDeleteBtn);
        this.searchNoteDText = findViewById(R.id.searchNoteDeleteText);

        this.noteDeleteAdapter = new NoteDeleteAdapter(this, this.listNoteD, new NoteDeleteAdapter.ClickListeners() {
            @Override
            public void onItemClick(int position, View v) {

            }
        });

        this.rcNoteDelete = findViewById(R.id.recycleNoteDelete);
        this.rcNoteDelete.setAdapter(this.noteDeleteAdapter);
        this.rcNoteDelete.addItemDecoration(new DividerItemDecoration(this.rcNoteDelete.getContext(), DividerItemDecoration.VERTICAL));
        this.rcNoteDelete.setLayoutManager(new LinearLayoutManager(this));
    }

    public void getAllNoteDelete(){
            this.listNoteD = new ArrayList<Note>();
            ArrayList<Note> allNote = new ArrayList<>();
            allNote = this.db.getAllNote();
            this.listNoteD = allNote;
            for(int i=0;i<allNote.size();i++){
                // continute
            }
    }
    public void intRS(){
        try{
            this.rsLaucherForRestore = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),rs->{

            });
        }
        catch(Exception ex){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage(ex.getMessage());
            alert.show();
        }
    }
}