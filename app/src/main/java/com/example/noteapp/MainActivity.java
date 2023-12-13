package com.example.noteapp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.noteapp.Adapter.FolderAdapter;
import com.example.noteapp.Database.DatabaseForApp;
import com.example.noteapp.Model.Folder;
import com.example.noteapp.Model.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.SearchView;

import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Folder> listFolder;
    private RecyclerView rcView;
    private FolderAdapter folderAdapter;
    private TextView textFolder;
    private SearchView searchText;
    private FloatingActionButton addFolderBtn;
    private ActivityResultLauncher<Intent> rsLauncherForAdd;
    private ActivityResultLauncher<Intent> rsLaucherForUpdate;
    private ActivityResultLauncher<Intent> rsLaucherForNote;
    private int pos;
    private RelativeLayout folder_content;
    private DatabaseForApp db;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.db = new DatabaseForApp(this);
        this.getAllFolder();
        this.initResultLauncher();

        this.textFolder = findViewById(R.id.TextFolder);
        this.searchText = findViewById(R.id.searchText);
        this.addFolderBtn = findViewById(R.id.addFolderButton);
        this.folder_content = findViewById(R.id.f_content);
        searchText.clearFocus();
        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        this.folderAdapter = new FolderAdapter(this, this.listFolder, new FolderAdapter.ClickListeners() {
            @Override
            public void onItemLongClick(int position, View v) {
                pos = position;
                Folder f = listFolder.get(pos);
                Snackbar mySnackbar = Snackbar.make(folder_content, "Chọn thao tác ?", Snackbar.LENGTH_SHORT);
                mySnackbar.setAction("Sửa", view -> {
                    Intent i = new Intent(MainActivity.this, AddFolderActivity.class);
                    i.putExtra("flag", "update");
                    i.putExtra("folderE", f);
                    rsLaucherForUpdate.launch(i);
                });
                mySnackbar.show();
            }

            @Override
            public void onItemClick(int position, View v) {
                pos = position;
                Folder fol = listFolder.get(pos);
                Intent i = new Intent(MainActivity.this, NoteListActivity.class);
                i.putExtra("idFolder", fol.getId());
                rsLaucherForNote.launch(i);
            }
        });
        this.rcView = findViewById(R.id.recycleFolder);
        this.rcView.setAdapter(this.folderAdapter);
        this.rcView.addItemDecoration(new DividerItemDecoration(this.rcView.getContext(), DividerItemDecoration.VERTICAL));
        this.rcView.setLayoutManager(new LinearLayoutManager(this));

        this.addFolderBtn.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, AddFolderActivity.class);
            i.putExtra("flag", "add");
            this.rsLauncherForAdd.launch(i);
        });
    }

    private void filterList(String text) {
        ArrayList<Folder> filteredListFolder = new ArrayList<>();
        for (Folder folder : listFolder) {
            if (folder.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredListFolder.add(folder);
            }
        }
        if (filteredListFolder.isEmpty()) {
            Toast.makeText(this, "khong tim thay", Toast.LENGTH_LONG).show();
        } else {
            this.folderAdapter.setFilteredFolder(filteredListFolder);
        }
    }

    public void getAllFolder() {
        try {
//            //Lay ra tat ca cac folder tu db
            this.listFolder = new ArrayList<Folder>();
//            Folder f1 = new Folder(1, "Folder1", "active");
//            this.listFolder.add(f1);
//            Folder f2 = new Folder(2, "Folder2", "active");
//            this.listFolder.add(f2);
//            Folder f3 = new Folder(3, "Folder3", "active");
//            this.listFolder.add(f3);
            this.listFolder = this.db.getAllFolder();
        } catch (Exception ex) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage(ex.getMessage());
            alert.show();
        }
    }

    public void initResultLauncher() {
        try {
            this.rsLauncherForAdd = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), rs -> {
                if (rs != null && rs.getResultCode() == RESULT_OK) {
                    Folder f = (Folder) rs.getData().getSerializableExtra("folder");
                    this.listFolder.add(f);
                    this.folderAdapter.notifyDataSetChanged();
                }
            });
            this.rsLaucherForUpdate = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), rs -> {
                if (rs != null && rs.getResultCode() == RESULT_OK) {
                    Folder fAE = (Folder) rs.getData().getSerializableExtra("fAE");
                    this.listFolder.set(this.pos, fAE);
                    this.folderAdapter.notifyItemChanged(this.pos);
                }
            });
            this.rsLaucherForNote = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), rs -> {
            });
        } catch (Exception ex) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage(ex.getMessage());
            alert.show();
        }
    }
}