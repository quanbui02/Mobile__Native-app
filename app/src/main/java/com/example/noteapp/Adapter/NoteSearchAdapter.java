package com.example.noteapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp.Model.Note;
import com.example.noteapp.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NoteSearchAdapter extends RecyclerView.Adapter<NoteSearchAdapter.NoteSearchViewHolder> {
    public NoteSearchAdapter(List<Note> lstNoteSearch, Context context) {
        this.lstNoteSearch = lstNoteSearch;
        this.context = context;
    }

    private List<Note> lstNoteSearch;
    private Context context;
    @NonNull
    @Override
    public NoteSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_note_search, parent, false);
        return new NoteSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteSearchViewHolder holder, int position) {
        Note note = lstNoteSearch.get(position);
        if(note == null) return;
//        holder.imgNote.setIm(note.getImagePath());
    }

    @Override
    public int getItemCount() {
        if(lstNoteSearch != null) {
            return lstNoteSearch.size();
        }
        return 0;
    }

    public class NoteSearchViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView imgNote;
        private TextView titleNote;
        private TextView dateNote;

        public NoteSearchViewHolder(@NonNull View itemView) {
            super(itemView);
            imgNote = itemView.findViewById(R.id.img_note);
            titleNote = itemView.findViewById(R.id.title_note);
            dateNote = itemView.findViewById(R.id.dateNote_search);

        }
    }
}
