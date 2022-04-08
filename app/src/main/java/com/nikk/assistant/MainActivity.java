package com.nikk.assistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.nikk.assistant.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public Button send;
    public TextView input;

    private ListView listview;
    private ArrayList<Message> mensajes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        send = findViewById(R.id.cmd_send);
        input = findViewById(R.id.cmd_input);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference cmdRef = database.getReference("operations/cmd");
        DatabaseReference resultRef = database.getReference("operations/result");
        DatabaseReference notifyRef = database.getReference("operations/notify");

        RecyclerView resultados = (RecyclerView) findViewById(R.id.resultados);

        mensajes = new ArrayList<Message>();

        send.setOnClickListener(v -> {
            String comando = input.getText().toString();
            input.setText("");
            notifyRef.removeValue();
            cmdRef.setValue(comando);
            mensajes.clear();
        });

        notifyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                @NonNull
                String CHANNEL_ID = "1";

                Iterable<DataSnapshot> nodos = snapshot.getChildren();
                int id = 0;
                for (DataSnapshot nodo: nodos){

                    String full = nodo.getValue(String.class);

                    String description = full.split("\\|")[0];
                    String time = full.split("\\|")[1];

                    NotificationCompat.Builder builder =
                            new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_stat_name)
                            .setContentTitle("Aviso de recordatorio vencido")
                            .setContentText(description + " a las " + time)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        CharSequence name = getString(R.string.channel_name);
                        String nDescription = getString(R.string.channel_description);
                        int importance = NotificationManager.IMPORTANCE_DEFAULT;
                        NotificationChannel channel =
                                new NotificationChannel(CHANNEL_ID, name, importance);
                        channel.setDescription(description);
                        // Register the channel with the system; you can't change the importance
                        // or other notification behaviors after this
                        NotificationManager notificationManager =
                                getSystemService(NotificationManager.class);
                        notificationManager.createNotificationChannel(channel);
                    }

                    NotificationManagerCompat notificationManager =
                            NotificationManagerCompat.from(MainActivity.this);

                    // notificationId is a unique int for each notification that you must define
                    notificationManager.notify(id, builder.build());
                    id++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        resultRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                String value = snapshot.getValue(String.class);
                Log.d("Main", "Resultado actualizado " + value);
                if (value == null)
                    return;

                String[] mensajesResult = value.split("\n");

                ArrayList<Message> auxiliar = new ArrayList<Message>();

                for (String mensaje: mensajesResult) {
                    Log.i("PROCESANDO", mensaje);
                    mensajes.add(Message.createMessage(mensaje));
                }

                Log.d("Mensajes", mensajes.size() + "");
//                mensajes.clear();
//                mensajes.addAll(auxiliar);

                MessageAdapter adapter = new MessageAdapter(mensajes);
                resultados.setAdapter(adapter);
                resultados.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                resultRef.setValue("");

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Main", "Failed to read value.", error.toException());
            }
        });

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library

    }

}