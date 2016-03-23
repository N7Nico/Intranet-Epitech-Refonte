package com.nico_11_riv.intranetepitech.ui.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nico_11_riv.intranetepitech.R;
import com.nico_11_riv.intranetepitech.database.Marks;
import com.nico_11_riv.intranetepitech.database.setters.user.GUser;
import com.nico_11_riv.intranetepitech.ui.contents.MarkContent;

import java.util.List;
import java.util.Objects;

/**
 * Created by nicol on 13/03/2016.
 */

public class RVMarksAdapter extends RecyclerView.Adapter<RVMarksAdapter.ViewHolder> {

    private GUser gUser = new GUser();
    private List<MarkContent> marks;
    private Context context;

    public RVMarksAdapter(List<MarkContent> marks, Context context) {
        this.marks = marks;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return marks.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_marks, viewGroup, false);
        ViewHolder pvh = new ViewHolder(v, context);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ViewHolder ViewHolder, int i) {
        ViewHolder.mark.setText(marks.get(i).getMark());
        ViewHolder.corrector.setText(marks.get(i).getCorrector());
        ViewHolder.project.setText(marks.get(i).getEvent());
        ViewHolder.module.setText(marks.get(i).getModule());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void filter(int position, String semester) {
        List<Marks> new_marks;
        if (!Objects.equals(semester, "All"))
            new_marks = Marks.findWithQuery(Marks.class, "SELECT * FROM Marks WHERE login = ? AND titlemodule LIKE ?", gUser.getLogin(), semester);
        else
            new_marks = Marks.findWithQuery(Marks.class, "SELECT * FROM Marks WHERE login = ?", gUser.getLogin());
        marks.clear();
        for (int i = new_marks.size() - 1; i > 0; i--) {
            if (i == new_marks.size() - position - 1 && position != 0)
                break;
            Marks info = new_marks.get(i);
            marks.add(new MarkContent(info.getFinalnote(), info.getCorrecteur(), info.getTitle(), info.getTitlemodule(), info.getComment()));
        }
        notifyDataSetChanged();
    }

    public void search(String text) {
        List<Marks> new_marks;
        new_marks = Marks.findWithQuery(Marks.class, "SELECT * FROM Marks WHERE login = ? AND finalnote LIKE ?", gUser.getLogin(), "%" + text + "%");
        if (new_marks.size() == 0) {
            new_marks = Marks.findWithQuery(Marks.class, "SELECT * FROM Marks WHERE login = ? AND title LIKE ?", gUser.getLogin(), "%" + text + "%");
            if (new_marks.size() == 0) {
                new_marks = Marks.findWithQuery(Marks.class, "SELECT * FROM Marks WHERE login = ? AND correcteur LIKE ?", gUser.getLogin(), "%" + text + "%");
                if (new_marks.size() == 0) {
                    new_marks = Marks.findWithQuery(Marks.class, "SELECT * FROM Marks WHERE login = ? AND titlemodule LIKE ?", gUser.getLogin(), "%" + text + "%");
                }
            }
        }
        marks.clear();
        for (int i = new_marks.size() - 1; i > 0; i--) {
            Marks info = new_marks.get(i);
            marks.add(new MarkContent(info.getFinalnote(), info.getCorrecteur(), info.getTitle(), info.getTitlemodule(), info.getComment()));
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        TextView mark;
        TextView corrector;
        TextView project;
        TextView module;
        Context context;

        ViewHolder(View itemView, Context context) {
            super(itemView);
            itemView.setOnClickListener(this);

            this.context = context;

            cv = (CardView) itemView.findViewById(R.id.cv);
            mark = (TextView) itemView.findViewById(R.id.mark);
            corrector = (TextView) itemView.findViewById(R.id.corrector);
            project = (TextView) itemView.findViewById(R.id.project);
            module = (TextView) itemView.findViewById(R.id.module);
        }

        public String escape(String s) {
            StringBuilder builder = new StringBuilder();
            boolean previousWasASpace = false;
            for (char c : s.toCharArray()) {
                if (c == ' ') {
                    if (previousWasASpace) {
                        builder.append("&nbsp;");
                        previousWasASpace = false;
                        continue;
                    }
                    previousWasASpace = true;
                } else {
                    previousWasASpace = false;
                }
                switch (c) {
                    case '<':
                        builder.append("&lt;");
                        break;
                    case '>':
                        builder.append("&gt;");
                        break;
                    case '&':
                        builder.append("&amp;");
                        break;
                    case '"':
                        builder.append("&quot;");
                        break;
                    case '\n':
                        builder.append("<br>");
                        break;
                    // We need Tab support here, because we print StackTraces as HTML
                    case '\t':
                        builder.append("&nbsp; &nbsp; &nbsp;");
                        break;
                    default:
                        if (c < 128) {
                            builder.append(c);
                        } else {
                            builder.append("&#").append((int) c).append(";");
                        }
                }
            }
            return builder.toString();
        }

        @Override
        public void onClick(View view) {

            List<Marks> marks = Marks.findWithQuery(Marks.class, "Select * FROM Marks WHERE finalnote = ? AND correcteur = ? AND title = ? AND titlemodule = ?", mark.getText().toString(), corrector.getText().toString(), project.getText().toString(), module.getText().toString());
            Marks mark = marks.get(0);
            new MaterialDialog.Builder(context)
                    .title(mark.getTitle())
                    .content(Html.fromHtml("<b>Note :</b> " + mark.getFinalnote() + "<br /><b>Correcteur :</b> " + mark.getCorrecteur() + "<br /><b>Module :</b> " + mark.getTitlemodule() + "<br /><br />" + escape(mark.getComment())))
                    .negativeText("Retour")
                    .icon(context.getDrawable(R.drawable.logo)).limitIconToDefaultSize()
                    .show();
        }
    }
}
