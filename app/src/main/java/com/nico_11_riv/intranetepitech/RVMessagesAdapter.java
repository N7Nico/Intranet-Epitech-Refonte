package com.nico_11_riv.intranetepitech;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nico_11_riv.intranetepitech.database.setters.infos.CircleTransform;
import com.nico_11_riv.intranetepitech.ui.contents.Messages_content;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by nicol on 13/03/2016.
 */

public class RVMessagesAdapter extends RecyclerView.Adapter<RVMessagesAdapter.ViewHolder> {

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
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_message, viewGroup, false);
        ViewHolder pvh = new ViewHolder(v, context);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ViewHolder ViewHolder, int i) {
        if (persons.get(i).getPicture() == null || persons.get(i).getPicture().equals("null"))
            Picasso.with(context).load(R.drawable.login_x).transform(new CircleTransform()).into(ViewHolder.personPhoto);
        else
            Picasso.with(context).load(persons.get(i).getPicture()).transform(new CircleTransform()).into(ViewHolder.personPhoto);
        ViewHolder.date.setText(persons.get(i).getDate());
        ViewHolder.message.setText(persons.get(i).getTitleMessage());
        ViewHolder.person_name.setText(persons.get(i).getLoginMessage());
        ViewHolder.description.setText(persons.get(i).getMessageContent());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        ImageView personPhoto;
        TextView date;
        TextView message;
        TextView person_name;
        TextView description;
        Context context;

        ViewHolder(View itemView, Context context) {
            super(itemView);
            itemView.setOnClickListener(this);

            this.context = context;

            cv = (CardView) itemView.findViewById(R.id.cv);
            personPhoto = (ImageView) itemView.findViewById(R.id.person_img);
            date = (TextView) itemView.findViewById(R.id.date);
            message = (TextView) itemView.findViewById(R.id.message);
            person_name = (TextView) itemView.findViewById(R.id.person_name);
            description = (TextView) itemView.findViewById(R.id.description);
        }

        @Override
        public void onClick(View view) {

            Drawable myDrawable = personPhoto.getDrawable();

            new MaterialDialog.Builder(context)
                    .title(message.getText())
                    .content("Date : " + date.getText() + " par " + person_name.getText() + "\n\n" + description.getText())
                    .negativeText("Retour")
                    .icon(myDrawable).limitIconToDefaultSize()
                    .show();
        }
    }
}
