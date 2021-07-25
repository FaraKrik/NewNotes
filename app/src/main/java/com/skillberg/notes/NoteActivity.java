package com.skillberg.notes;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newnotes.R;
import com.skillberg.notes.BaseNoteActivity;
import com.skillberg.notes.db.NotesContract;
import com.skillberg.notes.ui.NoteImagesAdapter;


public class NoteActivity extends BaseNoteActivity {

    public static final String EXTRA_NOTE_ID = "note_id";

    private TextView noteTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_note);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        noteTv = toolbar.findViewById();

        RecyclerView recyclerView = findViewById(R.id.images_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        noteImagesAdapter = new NoteImagesAdapter(null, null);
        recyclerView.setAdapter(noteImagesAdapter);

        noteId = getIntent().getLongExtra(EXTRA_NOTE_ID, -1);
        if (noteId != -1) {
            initNoteLoader();
            initImagesLoader();
        } else {
            finish();
        }
    }



    @Override
    protected void displayNote(Cursor cursor) {
        if (!cursor.moveToFirst()) {


            finish();
            return;
        }

        String title = cursor.getString(cursor.getColumnIndexOrThrow(NotesContract.Notes.COLUMN_TITLE));
        String noteText = cursor.getString(cursor.getColumnIndexOrThrow(NotesContract.Notes.COLUMN_NOTE));

        setTitle(title);
        noteTv.setText(noteText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.view_note, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();

                return true;

            case R.id.action_edit:
                editNote();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void editNote() {
        Intent intent = new Intent(this, CreateNoteActivity.class);
        intent.putExtra(CreateNoteActivity.EXTRA_NOTE_ID, noteId);

        startActivity(intent);
    }


}
