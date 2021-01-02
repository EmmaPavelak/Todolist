package com.ynov.todolist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;

public class EditTask extends AppCompatActivity {
    EditText name,message,date;
   // DatePicker date;

    Button btnUpdate;
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_edit_task);

        name = findViewById(R.id.name);
        message = findViewById(R.id.message);
        date = findViewById(R.id.date);

        btnUpdate = findViewById(R.id.btnUpdate);

        //Recupérer les valeurs de la taches modif
        name.setText(getIntent().getStringExtra("name"));
        message.setText(getIntent().getStringExtra("message"));
        date.setText(getIntent().getStringExtra("date"));
       // date.setValue(getIntent().getStringExtra("date"));
        String numtache = getIntent().getStringExtra("idtache");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Taches").child("tache"+ numtache);


        //edit tache
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("name").setValue(name.getText().toString());
                        dataSnapshot.getRef().child("message").setValue(message.getText().toString());
                        dataSnapshot.getRef().child("date").setValue(date.getText().toString());
                        //dataSnapshot.getRef().child("date").setValue(date.getDayOfMonth()+"/"+ (date.getMonth() + 1)+"/"+date.getYear());
                        dataSnapshot.getRef().child("idtache").setValue(numtache);
                        Intent a= new Intent(EditTask.this,MainActivity.class);
                        startActivity(a);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });


    }

}
