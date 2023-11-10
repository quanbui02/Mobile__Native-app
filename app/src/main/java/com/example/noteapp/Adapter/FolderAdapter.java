package com.example.noteapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;


import com.example.noteapp.Model.Folder;
import com.example.noteapp.R;

import java.util.ArrayList;
public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder>{
    private Context fContext;
    private ArrayList<Folder> listFolder;

    public FolderAdapter(Context fContext, ArrayList<Folder> listFolder) {
        this.fContext = fContext;
        this.listFolder = listFolder;
    }

    public class FolderViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        public FolderViewHolder(@NonNull View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.nameFolder);
        }
    }
    //3 phuong thuc quan trong cua adapter

    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_item_view, parent, false);
        return new FolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {
        Folder folder = this.listFolder.get(position);
        holder.name.setText(folder.getName());
    }
    @Override
    public int getItemCount() {
        if (this.listFolder != null && this.listFolder.size() > 0)
            return this.listFolder.size();
        else
            return 0;
    }
}
