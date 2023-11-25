package com.example.noteapp;

import android.app.InvalidForegroundServiceTypeException;
import android.content.Intent;
import android.os.Bundle;

import com.example.noteapp.Adapter.NoteAdapter;
import com.example.noteapp.Database.DatabaseForApp;
import com.example.noteapp.Model.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoteListActivity extends AppCompatActivity {
    private FloatingActionButton addNoteBtn;
    private TextView headerNoteText;
    private EditText searchNoteText;
    private Button backNoteBtn;
    private RecyclerView rcNoteView;
    private NoteAdapter noteAdapter;
    private ArrayList<Note> listNote;
    private ActivityResultLauncher<Intent> rsLaucherForAdd;
    private ActivityResultLauncher<Intent> rsLaucherForUpdate;
    private int idFolder;
    private int pos;
    private RelativeLayout main_content;
    private DatabaseForApp db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        this.db = new DatabaseForApp(this);
        this.initRsLauncher();

        Intent i = getIntent();
        try {
            this.idFolder=i.getIntExtra("idFolder",1);
            this.getAllNote(this.idFolder);
        }
        catch(Exception ex){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage(ex.getMessage());
            alert.show();
        }


        this.headerNoteText = findViewById(R.id.headerNoteText);
        this.searchNoteText = findViewById(R.id.searchNoteText);
        this.backNoteBtn = findViewById(R.id.backNoteBtn);
        this.addNoteBtn = findViewById(R.id.addNoteBtn);
        this.main_content = findViewById(R.id.note_list_content);

        this.noteAdapter = new NoteAdapter(this, this.listNote, new NoteAdapter.ClickListeners() {
            @Override
            public void onItemClick(int position, View v) {
                pos = position;
                Note note = listNote.get(pos);
                Intent i = new Intent(NoteListActivity.this,AddNoteActivity.class);
                i.putExtra("flag","edit_note");
                i .putExtra("noteE",note);
                rsLaucherForUpdate.launch(i);
            }

            @Override
            public void onItemLongClick(int position, View v) {
                Snackbar mySnackbar = Snackbar.make(main_content, "Bạn có muốn xoá ghi chú này ?", Snackbar.LENGTH_SHORT);
                mySnackbar.setAction("Đồng ý", view-> {
                    pos = position;
                    Note note = listNote.get(pos);
                    //Call updateNote(id) to delete
                    note.setStatusN("not active");
                    db.updateNote(note);
                    listNote.remove(pos);
                    noteAdapter.notifyItemRemoved(pos);
                });
                mySnackbar.show();
            }
        });
        this.rcNoteView = findViewById(R.id.recycleNote);
        this.rcNoteView.setAdapter(this.noteAdapter);
        this.rcNoteView.addItemDecoration(new DividerItemDecoration(this.rcNoteView.getContext(), DividerItemDecoration.VERTICAL));
        this.rcNoteView.setLayoutManager(new LinearLayoutManager(this));

        this.backNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        this.addNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NoteListActivity.this,AddNoteActivity.class);
                i.putExtra("flag","add_note");
                i.putExtra("idFolderAdd",idFolder);
                rsLaucherForAdd.launch(i);
            }
        });

    }
    public void getAllNote(int id){
        this.listNote = new ArrayList<Note>();

        //Lay ve tat ca ca note trong db
        ArrayList<Note> allNote = new ArrayList<Note>();
//        Note n1 = new Note(1,"Lau nha","Hom nay lau nha.....","2002/22/02","active","/../../..",1);
//        Note n2 = new Note(2,"Lau nha","Hom nay lau nha.....","2002/22/02","active","/../../..",2);
//        Note n3 = new Note(3,"Lau nha","Hom nay lau nha.....","2002/22/02","active","/../../..",2);
//        Note n4 = new Note(4,"Lau nha","Hom nay lau nha.....","2002/22/02","active","/../../..",3);
//        Note n5 = new Note(5,"Lau nha","Hom nay lau nha.....","2002/22/02","active","/../../..",3);
//        Note n6 = new Note(6,"Lau nha","Hom nay lau nha.....","2002/22/02","active","/../../..",3);
//        allNote.add(n1);
//        allNote.add(n2);
//        allNote.add(n3);
//        allNote.add(n4);
//        allNote.add(n5);
//        allNote.add(n6);
        //lay ra cac note co idFolder phu hop
        allNote = this.db.getAllNote();
        for(int i=0;i<allNote.size();i++){
            if(allNote.get(i).getIdFolder() == id){
                this.listNote.add(allNote.get(i));
            }
        }
    }
    public void initRsLauncher(){
        try{
            this.rsLaucherForAdd = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),rs->{
                if(rs != null && rs.getResultCode() == RESULT_OK){
                    Note n = (Note) rs.getData().getSerializableExtra("new_note");
                    this.listNote.add(n);
                    this.noteAdapter.notifyDataSetChanged();
                }
            });
            this.rsLaucherForUpdate = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),rs->{
                if(rs  != null && rs.getResultCode() == RESULT_OK){
                    Note nAE = (Note) rs.getData().getSerializableExtra("noteAE");
                    this.listNote.set(this.pos,nAE);
                    this.noteAdapter.notifyItemChanged(this.pos);
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