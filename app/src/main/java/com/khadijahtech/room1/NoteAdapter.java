package com.khadijahtech.room1;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    public List<Note> mNoteList = new ArrayList<>();


    private OnItemClickListener mListener;

  /*  //constructor
    public NoteAdapter(ArrayList<Note> wordList) {

        mNoteList = wordList;
    }*/

    public class NoteViewHolder extends RecyclerView.ViewHolder {

        public TextView mText1;
        public TextView mText2;
        public TextView mPriority;

        public NoteViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mText1 = itemView.findViewById(R.id.text_view_title);
            mText2 = itemView.findViewById(R.id.text_view_description);
            mPriority = itemView.findViewById(R.id.text_view_priority);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(mNoteList.get(position));
                        }
                    }
                }
            });

         /*   mDeleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });*/
        }
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row, viewGroup, false);
        //call custom item and connect it with holder
        NoteViewHolder v = new NoteViewHolder(view, mListener);
        return v;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int position) {
        //pass the values into the views
        Note currentNote = mNoteList.get(position);
        noteViewHolder.mText1.setText(currentNote.getTitle());
        noteViewHolder.mText2.setText(currentNote.getDescription());
        noteViewHolder.mPriority.setText(String.valueOf(currentNote.getPriority()));
    }

    @Override
    public int getItemCount() {
        //count the number items in the RV
        return mNoteList.size();
    }

    public void setNotes(List<Note> notes) {
        mNoteList = notes;
        notifyDataSetChanged();
    }

    //for swiping
    public Note getNoteAt(int position) {
        return mNoteList.get(position);
    }

    //for click each item event
    public interface OnItemClickListener {
        void onItemClick(Note note);
    }

    //for click each item event
    //we call it from activity
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
