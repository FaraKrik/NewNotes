package com.skillberg.notes.ui;

import android.database.Cursor;


import androidx.recyclerview.widget.RecyclerView;

import com.skillberg.notes.db.NotesContract;

import org.jetbrains.annotations.Nullable;


public abstract class CursorRecyclerAdapter<ViewHolder extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<ViewHolder> {

    protected Cursor cursor;
    protected boolean isDataValid;
    protected int idColumnIndex;

    public CursorRecyclerAdapter(Cursor cursor) {
        super();

        this.cursor = cursor;


        isDataValid = cursor != null;


        idColumnIndex = cursor != null
                ? cursor.getColumnIndexOrThrow(NotesContract.Notes._ID)
                : -1;


        setHasStableIds(true);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        if (!isDataValid) {
            throw new IllegalStateException("Cursor is not valid!");
        }


        if (!cursor.moveToPosition(position)) {
            throw new IllegalStateException("Can not move to position " + position);
        }


        onBindViewHolder(viewHolder, cursor);
    }

    public abstract void onBindViewHolder(ViewHolder viewHolder, Cursor cursor);

    @Override
    public int getItemCount() {
        if (isDataValid && cursor != null) {
            return cursor.getCount();
        } else {
            return 0;
        }
    }

    @Override
    public long getItemId(int position) {


        if (isDataValid && cursor != null) {


            if (cursor.moveToPosition(position)) {


                return cursor.getLong(idColumnIndex);
            }
        }


        return RecyclerView.NO_ID;
    }

    @Nullable
    public Cursor swapCursor(Cursor newCursor) {
        // Если курсор не изменился — ничего не заменяем
        if (newCursor == this.cursor) {
            return null;
        }

        Cursor oldCursor = this.cursor;
        this.cursor = newCursor;

        if (newCursor != null) {
            idColumnIndex = newCursor.getColumnIndexOrThrow(NotesContract.Notes._ID);
            isDataValid = true;

            notifyDataSetChanged();
        } else {
            idColumnIndex = -1;
            isDataValid = false;

            notifyItemRangeRemoved(0, getItemCount());
        }

        return oldCursor;
    }


}
