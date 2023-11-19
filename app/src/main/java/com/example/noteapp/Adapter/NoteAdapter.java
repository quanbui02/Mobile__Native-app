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
    private ClickListeners clickListeners;

    public NoteAdapter(Context context, ArrayList<Note> listNote,ClickListeners cl) {
        this.context = context;
        this.listNote = listNote;
        this.clickListeners = cl;
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleNoteText,dateNoteText;
        ImageView imageViewNote;
        public NoteViewHolder(@NonNull View itemView){
            super(itemView);
            titleNoteText = itemView.findViewById(R.id.titleNoteText);
            dateNoteText = itemView.findViewById(R.id.dateNoteText);
            imageViewNote = itemView.findViewById(R.id.imageViewNote);

            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            clickListeners.onItemClick(getAdapterPosition(),v);
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
    public interface ClickListeners{
        void onItemClick(int position, View v);
    }
}
