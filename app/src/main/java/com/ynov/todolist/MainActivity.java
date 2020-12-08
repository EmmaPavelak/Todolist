package com.ynov.todolist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView mTextStatus;


    //Context context = getApplicationContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* mTweets = generateData();
        refreshList();*/

        // Recupération elements
        TextView mTextStatus= (TextView) findViewById(R.id.list_status);

        Button addButton = (Button)  findViewById(R.id.addButton);
        Button clearButton = (Button) findViewById(R.id.clearButton);
        ListView list = (ListView) findViewById(R.id.list);


        //LECTURE FIREBASE
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        ValueEventListener valueEventListener = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("message", "Value is: " + value);

                ArrayAdapter<String> tableau = new ArrayAdapter<String>(list.getContext(), R.layout.text_field, R.id.textView);
                for (int i=0; i<10; i++) {
                    tableau.add(value); }
                list.setAdapter(tableau);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("message  ", "Failed to read value.", error.toException());
            }
        });


        //LISTENER

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addDialog();
                /*tableau.add("coucou ");
                list.setAdapter(tableau);*/
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mTextStatus.setText("clear");
                // clearDialog();
            }
        });

        list.setLongClickable(true);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                mTextStatus.setText("delete");
                //deleteOne(pos);
                return true;
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //modifyOne(position);
                mTextStatus.setText("modif");
            }
        });

    } //FIN ONCREATE

    //FONCTIONS
    private void addDialog(){

        //Création d'une boite de dialogue
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Ajouter une tache");
        alert.setMessage("Donne moi des détails sur ta tâche :)");

        // Création des deux inputs
        final EditText name = new EditText (this);
        name.setHint("Nom");

        final EditText text = new EditText(this);
        text.setHint("Tache");


        //Affichage des deux inputs
        Context context = getApplicationContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(70, 0, 70, 0);

        layout.addView(name, layoutParams);
        layout.addView(text, layoutParams);


        alert.setView(layout);

        alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                // Random color & add to list
              /* Random rnd = new Random();
               int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));*/


                if(name.length() > 0 || text.length() > 0) {


                    //ECRITURE DANS FIREBASE
                  FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference(name.getText().toString());
                    myRef.setValue(text.getText().toString());

                 /*  ListView list = (ListView) findViewById(R.id.list);
                   ArrayAdapter <String> tableau =
                           new ArrayAdapter<String>(list.getContext(), R.layout.text_field, R.id.textView);
                   tableau.add(text.getText().toString());
                   list.setAdapter(tableau);*/
                   /*mComment = new Comment(color, name.getText().toString(), text.getText().toString(), important);
                   AddItem(mComment);*/
                    //refreshList();
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
}
