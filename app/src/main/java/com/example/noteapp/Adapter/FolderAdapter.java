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
import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder> {
    private Context fContext;
    private ArrayList<Folder> listFolder;
    private ClickListeners clickListeners;

    public FolderAdapter(Context fContext, ArrayList<Folder> listFolder,ClickListeners clickListeners) {
        this.fContext = fContext;
        this.listFolder = listFolder;
        this.clickListeners = clickListeners;
    }

    public class FolderViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener,View.OnClickListener{
        TextView name;
        public FolderViewHolder(@NonNull View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.nameFolder);

            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListeners.onItemLongClick(getAdapterPosition(),v);
            return true;
        }
        @Override
        public void onClick(View v) {
            clickListeners.onItemClick(getAdapterPosition(),v);
        }
    }
    //3 phuong thuc quan trong cua adapter
    public void setFilteredFolder(ArrayList<Folder> filteredFolder) {
        this.listFolder = filteredFolder;
        notifyDataSetChanged();
    }
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
    public interface ClickListeners{
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }
}
