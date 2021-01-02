package com.ynov.todolist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Recupération elements
        FloatingActionButton addButton = (FloatingActionButton)  findViewById(R.id.floAdd);
        LinearLayout tachelayout = (LinearLayout)  findViewById(R.id.tache);
        RecyclerView matache;
        ArrayList<Taches> list;
        final TachesAdapter[] tachesAdapter = new TachesAdapter[1];
        matache = findViewById(R.id.ourdoes);
        matache.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<Taches>();


        //LECTURE FIREBASE
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Taches");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Taches t = dataSnapshot1.getValue(Taches.class);
                    list.add(t);
                }
                tachesAdapter[0] = new TachesAdapter(MainActivity.this, list);
                matache.setAdapter(tachesAdapter[0]);
                tachesAdapter[0].notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("taches ", "Failed to read value.", error.toException());
            }
        });


        //LISTENER
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addDialog();
            }
        });

    } //FIN ONCREATE

    int selectedYear = 2000;
    int selectedMonth = 5;
    int selectedDayOfMonth = 10;

    // Date Select Listener.
    private void addDialogDatePicker( EditText here){
    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year,
                              int monthOfYear, int dayOfMonth) {


            here.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
        }
    };

    // Create DatePickerDialog (Spinner Mode):
    DatePickerDialog datePickerDialog = new DatePickerDialog(this,
            android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
            dateSetListener, selectedYear, selectedMonth, selectedDayOfMonth);

    // Show
    datePickerDialog.show();
    }

    //FONCTIONS
    private void addDialog(){
        Integer numtache = new Random().nextInt();
        String idtache = Integer.toString(numtache);

        //Création d'une boite de dialogue
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Ajouter une tache");
        alert.setMessage("Donne moi des détails sur ta tâche :)");

        // Création des deux inputs
        final EditText name = new EditText (this);
        name.setHint("Nom");

        final EditText text = new EditText(this);
        text.setHint("Tache");

       // DatePicker date = (DatePicker) findViewById(R.id.datep);

        final EditText date = new EditText(this);
        date.setHint("Dead line");

        date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addDialogDatePicker(date);
            }
        });


       //DatePicker date = new DatePicker(this);
        //date.setSpinnersShown(true);
       // date.setCalendarViewShown(false);

       //date.setBackgroundColor(Color.RED);
        //Affichage des deux inputs
        Context context = getApplicationContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(70, 0, 70, 0);

        layout.addView(name, layoutParams);
        layout.addView(text, layoutParams);
        layout.addView(date, layoutParams);



        alert.setView(layout);

        alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                if(name.length() > 0 || text.length() > 0) {

                    //ECRITURE DANS FIREBASE
                  FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference().child("Taches").child("tache"+ numtache);
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            dataSnapshot.getRef().child("name").setValue(name.getText().toString());
                            dataSnapshot.getRef().child("message").setValue(text.getText().toString());
                            dataSnapshot.getRef().child("date").setValue(text.getText().toString());
                            //dataSnapshot.getRef().child("date").setValue(date.getDayOfMonth()+"/"+ (date.getMonth() + 1)+"/"+date.getYear());
                            dataSnapshot.getRef().child("idtache").setValue(idtache);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    finish();
                    startActivity(getIntent());
                }
            }
        });

        alert.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
    }
    //Sur de vouloir quitter l'app?
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing Activity")
                .setMessage("Are you sure you want to close this activity?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
}
