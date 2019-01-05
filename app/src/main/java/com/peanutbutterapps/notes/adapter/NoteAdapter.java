package com.peanutbutterapps.notes.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.peanutbutterapps.notes.R;
import com.peanutbutterapps.notes.model.Note;

public class NoteAdapter extends ListAdapter<Note, NoteAdapter.NoteHolder> {

    private OnItemClickListener clickListener;
    private OnItemLongClickListener longClickListener;
    private Context context;


    public NoteAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getKey() == newItem.getKey();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getColor() == newItem.getColor();
        }
    };

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note currentNote = getItem(position);

        holder.title.setText(currentNote.getTitle());
        holder.description.setText(currentNote.getDescription());
        holder.cardView.setCardBackgroundColor(context.getResources().getColor(currentNote.getColor()));

    }

    public Note getNoteAt(int position) {
        return getItem(position);
    }

    class NoteHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView description;
        private CardView cardView;

        public NoteHolder(@NonNull final View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.note_title);
            description = itemView.findViewById(R.id.note_description);
            cardView = itemView.findViewById(R.id.note_cardView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (clickListener != null && position != RecyclerView.NO_POSITION) {
                        clickListener.onItemClick(getItem(position), itemView);
                    }
                }
            });

//            itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//
//                    int position = getAdapterPosition();
//                    if (longClickListener != null && position != RecyclerView.NO_POSITION) {
//                        longClickListener.onItemLongClick(getItem(position));
//                    }
//                    return true;
//                }
//            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Note note, View view);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(Note note);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }
}