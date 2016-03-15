package com.nico_11_riv.intranetepitech.ui.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nico_11_riv.intranetepitech.R;
import com.nico_11_riv.intranetepitech.database.setters.infos.CircleTransform;
import com.nico_11_riv.intranetepitech.ui.contents.Message_content;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by nicol on 13/03/2016.
 */

public class RVMessagesAdapter extends RecyclerView.Adapter<RVMessagesAdapter.ViewHolder> {

    private List<Message_content> messages;
    private Context context;

    public RVMessagesAdapter(List<Message_content> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_message, viewGroup, false);
        ViewHolder pvh = new ViewHolder(v, context);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ViewHolder ViewHolder, int i) {
        if (messages.get(i).getPicture() == null || messages.get(i).getPicture().equals("null"))
            Picasso.with(context).load(R.drawable.login_x).transform(new CircleTransform()).into(ViewHolder.person_img);
        else
            Picasso.with(context).load(messages.get(i).getPicture()).transform(new CircleTransform()).into(ViewHolder.person_img);
        ViewHolder.date.setText(messages.get(i).getDate());
        ViewHolder.message.setText(messages.get(i).getTitleMessage());
        ViewHolder.person_name.setText(messages.get(i).getLoginMessage());
        ViewHolder.description.setText(messages.get(i).getMessageContent());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        ImageView person_img;
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
            person_img = (ImageView) itemView.findViewById(R.id.person_img);
            date = (TextView) itemView.findViewById(R.id.date);
            message = (TextView) itemView.findViewById(R.id.message);
            person_name = (TextView) itemView.findViewById(R.id.person_name);
            description = (TextView) itemView.findViewById(R.id.description);
        }

        @Override
        public void onClick(View view) {

            Drawable myDrawable = person_img.getDrawable();

            new MaterialDialog.Builder(context)
                    .title(message.getText())
                    .content(Html.fromHtml("<b>Date :</b> " + date.getText() + "<br /><b>Par :</b> " + person_name.getText() + "<br /><br />" + description.getText()))
                    .negativeText("Retour")
                    .icon(myDrawable).limitIconToDefaultSize()
                    .show();
        }
    }
}
