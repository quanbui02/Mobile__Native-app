package com.example.noteapp;

import android.content.Intent;
import android.os.Bundle;

import com.example.noteapp.Adapter.NoteAdapter;
import com.example.noteapp.Database.DatabaseForApp;
import com.example.noteapp.Model.Folder;
import com.example.noteapp.Model.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoteListActivity extends AppCompatActivity {
    private FloatingActionButton addNoteBtn;
    private TextView headerNoteText;
    private SearchView searchNoteText;
    private Button backNoteBtn;
    private RecyclerView rcNoteView;
    private NoteAdapter noteAdapter;
    private ArrayList<Note> listNote;
    private FloatingActionButton garbageNoteBtn;
    private ActivityResultLauncher<Intent> rsLaucherForAdd;
    private ActivityResultLauncher<Intent> rsLaucherForUpdate;
    private ActivityResultLauncher<Intent> rsLaucherForGarbageNote;
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


        this.headerNoteText = findViewById(R.id.headerNoteDeleteText);
        this.searchNoteText = findViewById(R.id.searchNoteDeleteText);
        this.backNoteBtn = findViewById(R.id.backNoteDeleteBtn);
        this.addNoteBtn = findViewById(R.id.addNoteBtn);
        this.garbageNoteBtn = findViewById(R.id.deleteNoteListBtn);
        this.main_content = findViewById(R.id.note_list_content);

        searchNoteText.clearFocus();
        searchNoteText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });
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
        this.rcNoteView = findViewById(R.id.recycleNoteDelete);
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

        this.garbageNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NoteListActivity.this,DeleteNoteListActivity.class);
                i.putExtra("idFolder",idFolder);
                rsLaucherForGarbageNote.launch(i);
            }
        });
    }

    private void filterList(String text) {
        ArrayList<Note> filteredListNote = new ArrayList<>();
        for (Note nte : listNote) {
            if (nte.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredListNote.add(nte);
            }
        }
        if (filteredListNote.isEmpty()) {
            this.noteAdapter.setFilteredNote(filteredListNote);
            Toast toast = Toast.makeText(this, "Không tìm thấy", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 100);
            toast.show();

        } else {
            this.noteAdapter.setFilteredNote(filteredListNote);
        }
    }

    public void getAllNote(int id){
        this.listNote = new ArrayList<Note>();

        //Lay ve tat ca ca note trong db
        ArrayList<Note> allNote = new ArrayList<Note>();
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
            this.rsLaucherForGarbageNote = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),rs->{
                if(rs != null && rs.getResultCode() == RESULT_OK){
                    Note n = (Note) rs.getData().getSerializableExtra("noteRestoreToNoteList");
                    this.listNote.add(n);
                    this.noteAdapter.notifyDataSetChanged();
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