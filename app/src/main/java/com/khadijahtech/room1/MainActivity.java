package com.khadijahtech.room1;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getName();
    private NoteViewModel mViewModel;

    private RecyclerView mRecyclerView;
    private NoteAdapter mAdapter;
    //private List<Note> mNotesList = new ArrayList<>();

    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //views- floating button
        FloatingActionButton floatingActionButton = findViewById(R.id.button_add_note);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,AddEditNoteActivity.class);
                startActivityForResult(i, ADD_NOTE_REQUEST);
            }
        });

        //Recycler view & adapter
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));//, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new NoteAdapter();
        mRecyclerView.setAdapter(mAdapter);//untill now adapter is empty


        // set view model
        mViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        mViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                //this method is triggered every time the data is changed in LIVE DATA
                //OK.. everytime the LiveData changed,
                //SO, update the UI ..>>RecyclerView here :)

                //Refresh dataset
                mAdapter.setNotes(notes);
            }
        });

        //for swipe
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
                //not used here
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //for swiping

                //get position from view Holder
                Note currentNote = mAdapter.getNoteAt(viewHolder.getAdapterPosition());
                mViewModel.delete(currentNote);
                Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(mRecyclerView);
        //end of swipe

        //for click item in RV
        mAdapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                //open edit mode
                Intent i = new Intent(MainActivity.this,AddEditNoteActivity.class);
                //send the values of the current note, to edit them
                i.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.getTitle());
                i.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, note.getDescription());
                i.putExtra(AddEditNoteActivity.EXTRA_PRIORITY, note.getPriority());
                //room need id to know which note to edit it
                i.putExtra(AddEditNoteActivity.EXTRA_ID, note.getId());

                startActivityForResult(i, EDIT_NOTE_REQUEST);
            }
        });

    }

    //Read the coming Intent

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK)
        {
            //ADD NEW NOTE MODE
            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1);

            Note note = new Note(title, description, priority);
            mViewModel.insert(note);

            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
        } else if(requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK)
        {
            //UPDATE NOTE MODE
            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1);
            int id = data.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1);

            if(id == -1)
            {
                //something wrong
                Toast.makeText(this, "Note can not updated", Toast.LENGTH_SHORT).show();
                return;
            }

            //update now
            Note note = new Note(title, description, priority);
            note.setId(id);//important line needed for room
            mViewModel.update(note);
            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show();
        }


    }

    //tell the system use my menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    //handle the click of the menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.remove_all_notes_menu:
                mViewModel.deleteAllNotes();
                Toast.makeText(this, "All note deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
