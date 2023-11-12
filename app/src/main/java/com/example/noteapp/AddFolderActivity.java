package com.example.noteapp;

import android.content.Intent;
import android.os.Bundle;

import com.example.noteapp.Model.Folder;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class AddFolderActivity extends AppCompatActivity {

    private TextView headerText;
    private EditText nameFolderText;
    private Button editFolderBtn;
    private Button backButton;
    private String flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_folder);

        this.headerText = findViewById(R.id.headerAddFolder);
        this.nameFolderText = findViewById(R.id.editNameFolder);
        this.editFolderBtn = findViewById(R.id.editFolderBtn);
        this.backButton= findViewById(R.id.backOnEditFolder);

        Intent i = getIntent();
        this.flag = i.getStringExtra("flag");
        if(this.flag.equals("update")) {
            //To do update...
            this.headerText.setText("Cập nhật thư mục");
            this.editFolderBtn.setText("Lưu lại");

            Folder folderE = (Folder) i.getSerializableExtra("folderE");
            this.nameFolderText.setText(folderE.getName());
        }else {
            this.headerText.setText("Tạo mới thư mục");
            this.editFolderBtn.setText("Tạo mới");
        }

        this.editFolderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag.equals("add")) {
                    try {
                        Folder f = new Folder(1,nameFolderText.getText().toString(),"active");

                        Intent i = new Intent();
                        i.putExtra("folder",f);
                        setResult(RESULT_OK,i);
                        finish();
                    }
                    catch (Exception ex){
                        AlertDialog.Builder alert = new AlertDialog.Builder(AddFolderActivity.this);
                        alert.setMessage(ex.getMessage());
                        alert.show();
                    }
                }else{
                    // To do update...
                    try{
                        Folder fE = new Folder(1,nameFolderText.getText().toString(),"active");
                        Intent i = new Intent();
                        i.putExtra("fE",fE);
                        setResult(RESULT_OK,i);
                        finish();
                    }
                    catch(Exception ex){
                        AlertDialog.Builder alert = new AlertDialog.Builder(AddFolderActivity.this);
                        alert.setMessage(ex.getMessage());
                        alert.show();
                    }
                }
            }
        });
        this.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

}