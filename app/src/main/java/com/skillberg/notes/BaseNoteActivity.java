package com.skillberg.notes;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.skillberg.notes.db.NotesContract;
import com.skillberg.notes.ui.NoteImagesAdapter;


public abstract class BaseNoteActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    protected static final int LOADER_NOTE = 0;
    protected static final int LOADER_IMAGES = 1;

    protected long noteId = -1;

    protected NoteImagesAdapter noteImagesAdapter;

    protected void initNoteLoader() {
        getLoaderManager().initLoader(
                LOADER_NOTE,
                null,
                this
        );
    }


    protected void initImagesLoader() {
        getLoaderManager().initLoader(
                LOADER_IMAGES,
                null,
                this
        );
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_NOTE) {
            return new CursorLoader(
                    this,
                    ContentUris.withAppendedId(NotesContract.Notes.URI, noteId),
                    NotesContract.Notes.SINGLE_PROJECTION,
                    null,
                    null,
                    null
            );
        } else {
            return new CursorLoader(
                    this,
                    NotesContract.Images.URI,
                    NotesContract.Images.PROJECTION,
                    NotesContract.Images.COLUMN_NOTE_ID + " = ?",
                    new String[]{String.valueOf(noteId)},
                    null
            );
        }
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (loader.getId() == LOADER_NOTE) {
            cursor.setNotificationUri(getContentResolver(), NotesContract.Notes.URI);

            displayNote(cursor);
        } else {
            cursor.setNotificationUri(getContentResolver(), NotesContract.Images.URI);

            noteImagesAdapter.swapCursor(cursor);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    protected abstract void displayNote(Cursor cursor);


    public abstract void onBindViewHolder(@NonNull CreateNoteActivity.ViewHolder holder, int position);
}
