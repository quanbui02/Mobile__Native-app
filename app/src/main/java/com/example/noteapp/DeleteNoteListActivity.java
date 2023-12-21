package com.example.noteapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp.Adapter.NoteDeleteAdapter;
import com.example.noteapp.Database.DatabaseForApp;
import com.example.noteapp.Model.Note;

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
    private int pos;
    private String checkRestore;
    private Note nR;

    @SuppressLint("MissingInflatedId")
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
        this.getAllNoteDelete(this.idFolder);

        this.headerNoteDText = findViewById(R.id.headerNoteDeleteText);
        this.backNoteDBtn = findViewById(R.id.backNoteDeleteBtn);
        this.searchNoteDText = findViewById(R.id.searchNoteDeleteText);
        this.checkRestore = "false";
        this.nR = new Note();

        this.noteDeleteAdapter = new NoteDeleteAdapter(this, this.listNoteD, new NoteDeleteAdapter.ClickListeners() {
            @Override
            public void onItemClick(int position, View v) {
                pos = position;
                Note note = listNoteD.get(pos);
                Intent i = new Intent(DeleteNoteListActivity.this,AddNoteActivity.class);
                i.putExtra("flag","restore");
                i.putExtra("noteR",note);
                rsLaucherForRestore.launch(i);
            }
        });

        this.rcNoteDelete = findViewById(R.id.recycleNoteDelete);
        this.rcNoteDelete.setAdapter(this.noteDeleteAdapter);
        this.rcNoteDelete.addItemDecoration(new DividerItemDecoration(this.rcNoteDelete.getContext(), DividerItemDecoration.VERTICAL));
        this.rcNoteDelete.setLayoutManager(new LinearLayoutManager(this));

        this.backNoteDBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkRestore.equals("true")){
                    Intent i = new Intent();
                    i.putExtra("noteRestoreToNoteList",nR);
                    setResult(RESULT_OK,i);
                }
                finish();
            }
        });
    }

    public void getAllNoteDelete(int id){
            this.listNoteD = new ArrayList<Note>();
            ArrayList<Note> allNote = new ArrayList<Note>();
                allNote = this.db.getAllNoteDelete();
                for(int i=0;i<allNote.size();i++){
                    if(allNote.get(i).getIdFolder() == id){
                        this.listNoteD.add(allNote.get(i));
                    }
                }
    }
    public void intRS(){
        try{
            this.rsLaucherForRestore = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),rs->{
                if(rs != null && rs.getResultCode() == RESULT_OK){
                    this.checkRestore = "true";
                    int positionR = 0;
                    Note nAR = (Note) rs.getData().getSerializableExtra("noteAR");
                    this.nR = nAR;
                    for(int i =0;i<this.listNoteD.size();i++){
                        if(this.listNoteD.get(i).getId() == nAR.getId()){
                            positionR = i;
                            this.listNoteD.remove(i);
                            break;
                        }
                    }
                    this.noteDeleteAdapter.notifyItemRemoved(positionR);
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