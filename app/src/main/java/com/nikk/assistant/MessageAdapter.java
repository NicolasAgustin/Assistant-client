package com.nikk.assistant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class MessageAdapter extends
        RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<Message> messages;

    public MessageAdapter(List<Message> mensajes) {
        messages = mensajes;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.message, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MessageAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        Message msg = messages.get(position);

        // Set item views based on your views and data model
        TextView title = holder.titleText;
        title.setText(msg.getTitulo());

        TextView desc = holder.descriptionText;
        desc.setText(msg.getContenido());

        // Agregar para que el titulo de la noticia se vea mas grande que el contenido
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView titleText;
        public TextView descriptionText;

        public ViewHolder(View itemView) {

            super(itemView);

            titleText = (TextView) itemView.findViewById(R.id.title);
            descriptionText = (TextView) itemView.findViewById(R.id.description);
        }
    }


}