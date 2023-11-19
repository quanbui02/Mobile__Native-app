package com.example.noteapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.noteapp.Model.Note;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.noteapp.databinding.ActivityAddNoteBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddNoteActivity extends AppCompatActivity {
    private Button backNoteList;
    private Button editNoteBtn;
    private EditText titleNoteText;
    private EditText contentNoteText;
    private ImageView imageNote;
    private ImageButton addImageBtn;

    private int idFolder;
    private String flag;
    private Note nE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        this.backNoteList = findViewById(R.id.backNoteListBtn);
        this.editNoteBtn = findViewById(R.id.editNoteBtn);
        this.titleNoteText = findViewById(R.id.noteTitleText);
        this.contentNoteText = findViewById(R.id.noteContentText);
        this.imageNote = findViewById(R.id.imageNoteView);
        this.addImageBtn = findViewById(R.id.addImageBtn);
        Intent i = getIntent();

        try{
            this.idFolder = i.getIntExtra("idFolderAdd",1);
            this.flag = i.getStringExtra("flag");
            this.nE = (Note) i.getSerializableExtra("noteE");
        }
        catch (Exception ex){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage(ex.getMessage());
            alert.show();
        }

        if(this.flag.equals("edit_note")){
            this.titleNoteText.setText(this.nE.getTitle());
            this.contentNoteText.setText(this.nE.getContent());
            Uri uri = Uri.parse(this.nE.getImagePath());
            this.imageNote.setImageURI(uri);
        }

        this.backNoteList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        this.editNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag.equals("add_note")){
                    String title = titleNoteText.getText().toString();
                    String content = contentNoteText.getText().toString();
                    String createTime = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());

                    Note note = new Note(1,title,content,createTime,"active","",idFolder);
                    //call addNoteToDatabase() and get id from function then set newID for note
                    Intent i = new Intent();
                    i.putExtra("new_note",note);
                    setResult(RESULT_OK,i);
                    finish();
                }else{
                    //To do update....
                    String title = titleNoteText.getText().toString();
                    String content = contentNoteText.getText().toString();

                    Note newNote = new Note(nE.getId(),title,content,nE.getCreateTime(),nE.getStatusN(),nE.getImagePath(),nE.getIdFolder());
                    // call updateNoteDatabase
                    Intent i = new Intent();
                    i.putExtra("noteAE",newNote);
                    setResult(RESULT_OK,i);
                    finish();
                }
            }
        });
    }

}