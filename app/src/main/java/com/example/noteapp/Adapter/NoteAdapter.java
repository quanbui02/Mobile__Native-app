package com.example.noteapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp.Model.Folder;
import com.example.noteapp.Model.Note;
import com.example.noteapp.R;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private Context context;

    private ArrayList<Note> listNote;

    public NoteAdapter(Context context, ArrayList<Note> listNote) {
        this.context = context;
        this.listNote = listNote;
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView titleNoteText,dateNoteText;
        ImageView imageViewNote;
        public NoteViewHolder(@NonNull View itemView){
            super(itemView);
            titleNoteText = itemView.findViewById(R.id.titleNoteText);
            dateNoteText = itemView.findViewById(R.id.dateNoteText);
            imageViewNote = itemView.findViewById(R.id.imageViewNote);
        }
    }
    @NonNull
    @Override
    public NoteAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item_view, parent, false);
        return new NoteAdapter.NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.NoteViewHolder holder, int position) {
        Note note = this.listNote.get(position);
        holder.titleNoteText.setText(note.getTitle());
        holder.dateNoteText.setText(note.getCreateTime());
//        holder.imageViewNote.setImageURI();
    }
    @Override
    public int getItemCount() {
        if (this.listNote != null && this.listNote.size() > 0)
            return this.listNote.size();
        else
            return 0;
    }
}
