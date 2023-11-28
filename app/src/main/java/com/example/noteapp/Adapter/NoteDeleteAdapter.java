package com.example.noteapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp.Model.Note;
import com.example.noteapp.R;

import java.util.ArrayList;

public class NoteDeleteAdapter extends RecyclerView.Adapter<NoteDeleteAdapter.NoteViewHolder> {
    private Context context;

    private ArrayList<Note> listNoteD;
    private ClickListeners clickListeners;

    public NoteDeleteAdapter(Context context, ArrayList<Note> listNoteD,ClickListeners cl) {
        this.context = context;
        this.listNoteD = listNoteD;
        this.clickListeners = cl;
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView titleNoteText,dateNoteText;
        ImageView imageViewNote;
        public NoteViewHolder(@NonNull View itemView){
            super(itemView);
            titleNoteText = itemView.findViewById(R.id.titleNoteText);
            dateNoteText = itemView.findViewById(R.id.dateNoteText);
            imageViewNote = itemView.findViewById(R.id.imageViewNote);

            itemView.setOnClickListener(this);
//            itemView.setOnLongClickListener(this);
        }
        @Override
        public void onClick(View v) {
            clickListeners.onItemClick(getAdapterPosition(),v);
        }


    }
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item_view, parent, false);
        return new NoteViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull NoteDeleteAdapter.NoteViewHolder holder, int position) {
        Note note = this.listNoteD.get(position);
        holder.titleNoteText.setText(note.getTitle());
        holder.dateNoteText.setText(note.getCreateTime());
//        holder.imageViewNote.setImageURI();
    }
    @Override
    public int getItemCount() {
        if (this.listNoteD != null && this.listNoteD.size() > 0)
            return this.listNoteD.size();
        else
            return 0;
    }
    public interface ClickListeners{
        void onItemClick(int position, View v);
    }
}