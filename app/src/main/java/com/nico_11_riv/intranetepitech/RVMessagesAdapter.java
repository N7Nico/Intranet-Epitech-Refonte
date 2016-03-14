package com.nico_11_riv.intranetepitech;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nico_11_riv.intranetepitech.database.setters.infos.CircleTransform;
import com.nico_11_riv.intranetepitech.ui.contents.Messages_content;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by nicol on 13/03/2016.
 */

public class RVMessagesAdapter extends RecyclerView.Adapter<RVMessagesAdapter.PersonViewHolder> {

    private List<Messages_content> persons;
    private Context context;

    RVMessagesAdapter(List<Messages_content> persons, Context context) {
        this.persons = persons;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_message, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        if (persons.get(i).getPicture() == null || persons.get(i).getPicture().equals("null"))
            Picasso.with(context).load(R.drawable.login_x).transform(new CircleTransform()).into(personViewHolder.personPhoto);
        else
            Picasso.with(context).load(persons.get(i).getPicture()).transform(new CircleTransform()).into(personViewHolder.personPhoto);
        personViewHolder.date.setText(persons.get(i).getDate());
        personViewHolder.message.setText(persons.get(i).getTitleMessage());
        personViewHolder.person_name.setText(persons.get(i).getLoginMessage());
        personViewHolder.description.setText(persons.get(i).getMessageContent());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        ImageView personPhoto;
        TextView date;
        TextView message;
        TextView person_name;
        TextView description;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            personPhoto = (ImageView) itemView.findViewById(R.id.person_img);
            date = (TextView) itemView.findViewById(R.id.date);
            message = (TextView) itemView.findViewById(R.id.message);
            person_name = (TextView) itemView.findViewById(R.id.person_name);
            description = (TextView) itemView.findViewById(R.id.description);
        }
    }
}
