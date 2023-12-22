package com.example.noteapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.noteapp.Database.DatabaseForApp;
import com.example.noteapp.Model.Note;

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
    private String imagePath;
    private int idFolder;
    private String flag;
    private Note nE;
    private Note nR;
    private String  check;
    private ActivityResultLauncher<String> rsAddImage;
    private DatabaseForApp db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        this.db = new DatabaseForApp(this);
        check = "false";
        this.initRS();

        this.backNoteList = findViewById(R.id.backNoteListBtn);
        this.editNoteBtn = findViewById(R.id.editNoteBtn);
        this.titleNoteText = findViewById(R.id.noteTitleText);
        this.contentNoteText = findViewById(R.id.noteContentText);
        this.addImageBtn = findViewById(R.id.addImageBtn);
        this.imageNote = findViewById(R.id.imageNoteView);
        Intent i = getIntent();

        try{
            this.idFolder = i.getIntExtra("idFolderAdd",1);
            this.flag = i.getStringExtra("flag");
        }
        catch (Exception ex){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage(ex.getMessage());
            alert.show();
        }

        if(this.flag.equals("edit_note")){
            this.nE = (Note) i.getSerializableExtra("noteE");
            this.titleNoteText.setText(this.nE.getTitle());
            this.contentNoteText.setText(this.nE.getContent());
            try {
                String img = this.nE.getImagePath();
                Uri imageUri = Uri.parse(img);
                Glide.with(this.imageNote)
                        .load(imageUri)
//                        .placeholder(R.drawable.img3)
                        .into(this.imageNote);
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setMessage("Them anh thanh cong");
                alert.show();
            }
            catch (Exception ex){
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setMessage(ex.getMessage());
                alert.show();
            }
        }else {
            if (this.flag.equals("restore")) {
                this.nR = (Note) i.getSerializableExtra("noteR");
                this.titleNoteText.setText(this.nR.getTitle());
                this.contentNoteText.setText(this.nR.getContent());
//                this.imageNote.setImageURI(Uri.parse(this.nR.getImagePath()));
                this.editNoteBtn.setText("Khôi phục");
            }
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
                    try {
                        String title = titleNoteText.getText().toString();
                        String content = contentNoteText.getText().toString();
                        String createTime = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());

                        Note note = new Note(1, title, content, createTime, "active", "", idFolder);
                        if (check.equals("true")) {
                            note.setImagePath(imagePath);
                        }
                        //call addNote() and get id from function then set newID for note
                        long newIDN = db.addNote(note);
                        note.setId((int) newIDN);
                        Intent i = new Intent();
                        i.putExtra("new_note", note);
                        setResult(RESULT_OK, i);
                        finish();
                    }
                    catch (Exception ex){
                        AlertDialog.Builder alert = new AlertDialog.Builder(AddNoteActivity.this);
                        alert.setMessage(ex.getMessage());
                        alert.show();
                    }
                }else{
                    if(flag.equals("edit_note")) {
                        //To do update....
                        String title = titleNoteText.getText().toString();
                        String content = contentNoteText.getText().toString();
                        Note newNote = new Note(nE.getId(), title, content, nE.getCreateTime(), nE.getStatusN(), nE.getImagePath(), nE.getIdFolder());
                        if (check.equals("true")) {
                            newNote.setImagePath(imagePath);
                        }
                        // call updateNoteDatabase
                        db.updateNote(newNote);
                        Intent i = new Intent();
                        i.putExtra("noteAE", newNote);
                        setResult(RESULT_OK, i);
                        finish();
                    }else{
                        Note noteRestore = new Note(nR.getId(),nR.getTitle(),nR.getContent(), nR.getCreateTime(),"active", nR.getImagePath(), nR.getIdFolder());
                        db.updateNote(noteRestore);
                        Intent i = new Intent();
                        i.putExtra("noteAR",noteRestore);
                        setResult(RESULT_OK,i);
                        finish();
                    }
                }
            }
        });
        this.addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rsAddImage.launch("image/*");
            }
        });
    }
    public void initRS(){
        try {
            this.rsAddImage = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri o) {
                    imageNote.setImageURI(o);
                    imagePath = o.toString();
                    AlertDialog.Builder alert = new AlertDialog.Builder(AddNoteActivity.this);
                    alert.setMessage("Thêm thành công");
                    alert.show();
                    check = "true";
                }
            });
        }
        catch (Exception ex){
            AlertDialog.Builder alert = new AlertDialog.Builder(AddNoteActivity.this);
            alert.setMessage(ex.getMessage());
            alert.show();
        }
    }
}