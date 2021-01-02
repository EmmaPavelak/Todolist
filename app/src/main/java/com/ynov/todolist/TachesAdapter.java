package com.ynov.todolist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TachesAdapter extends RecyclerView.Adapter<TachesAdapter.MyViewHolder> {

    Context context;
    ArrayList<Taches> Taches;

    public TachesAdapter(Context c, ArrayList<Taches> a){
        context = c ;
        Taches = a;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.text_field, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(Taches.get(position).getName());
        holder.message.setText(Taches.get(position).getMessage());
        holder.date.setText(Taches.get(position).getDate());


       holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aa = new Intent(context,EditTask.class);
                aa.putExtra("name", Taches.get(position).getName());
                aa.putExtra("message", Taches.get(position).getMessage());
                aa.putExtra("date", Taches.get(position).getDate());
                aa.putExtra("idtache", Taches.get(position).getIdtache());
                context.startActivity(aa);
            }
        });

       /*longtouch*/
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                String numtache = Taches.get(position).getIdtache();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference().child("Taches").child("tache"+ numtache);
                new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete task")
                        .setMessage("Are you sure you want to delete this task?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which) {
                                //delete tache
                                myRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if(task.isSuccessful()){
                                            Intent intent = new Intent(context, MainActivity.class);
                                            context.startActivity(intent);

                                        }else {
                                            Toast.makeText(context,"deletedontwork", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }

        });


        /*fin longtouch*/
    }
    @Override
    public int getItemCount() {
        return Taches.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name, message, date, idtache;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            name =(TextView) itemView.findViewById(R.id.name);
            message =(TextView) itemView.findViewById(R.id.message);
            date =(TextView) itemView.findViewById(R.id.date);
        }

    }
}

