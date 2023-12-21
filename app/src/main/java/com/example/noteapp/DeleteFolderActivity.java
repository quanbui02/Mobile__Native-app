package com.example.noteapp;

import android.content.Intent;
import android.os.Bundle;

import com.example.noteapp.Adapter.FolderAdapter;
import com.example.noteapp.Database.DatabaseForApp;
import com.example.noteapp.Model.Folder;
import com.example.noteapp.Model.Note;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.view.WindowCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;


public class DeleteFolderActivity extends AppCompatActivity {

    private ArrayList<Folder> listDF;
    private FolderAdapter fad;
    private RecyclerView rcDF;
    private TextView header;
    private Button backBtn;
    private DatabaseForApp db;
    private String checkRestore;
    private RelativeLayout f_content;
    private Folder fRestore;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_folder);
        this.db = new DatabaseForApp(this);
        this.getDF();
        this.checkRestore = "false";
        this.header = findViewById(R.id.folder_g_header);
        this.backBtn = findViewById(R.id.backFolderListbtn);
        this.f_content = findViewById(R.id.f_content);

        this.fad = new FolderAdapter(this, this.listDF, new FolderAdapter.ClickListeners() {
            @Override
            public void onItemClick(int position, View v) {

            }

            @Override
            public void onItemLongClick(int position, View v) {
                Snackbar mySnackbar = Snackbar.make(f_content, "Ban muon khoi phuc thu mua nay ?", Snackbar.LENGTH_SHORT);
                mySnackbar.setAction("Đồng ý", view -> {
                    pos = position;
                    Folder f = listDF.get(pos);
                    fRestore = f;
                    f.setStatusG("active");
                    db.updateFolder(f);
                    checkRestore = "true";
                    listDF.remove(pos);
                    fad.notifyItemRemoved(pos);
                });
                mySnackbar.show();
            }
        });
        this.rcDF = findViewById(R.id.rs_g_folder);
        this.rcDF.setAdapter(this.fad);
        this.rcDF.addItemDecoration(new DividerItemDecoration(this.rcDF.getContext(), DividerItemDecoration.VERTICAL));
        this.rcDF.setLayoutManager(new LinearLayoutManager(this));

        this.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkRestore.equals("true")) {
                    Intent i = new Intent();
                    i.putExtra("fRestore", fRestore);
                    setResult(RESULT_OK, i);
                }
                finish();

            }
        });
    }

    public void getDF() {
        try {
            this.listDF = new ArrayList<Folder>();
            this.listDF = this.db.getAllFolderDelete();
        } catch (Exception ex) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage(ex.getMessage());
            alert.show();
        }
    }
}