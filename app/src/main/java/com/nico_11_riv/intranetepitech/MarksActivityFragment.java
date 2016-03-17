package com.nico_11_riv.intranetepitech;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nico_11_riv.intranetepitech.api.APIErrorHandler;
import com.nico_11_riv.intranetepitech.api.IntrAPI;
import com.nico_11_riv.intranetepitech.database.Marks;
import com.nico_11_riv.intranetepitech.database.Userinfos;
import com.nico_11_riv.intranetepitech.database.setters.infos.CircleTransform;
import com.nico_11_riv.intranetepitech.database.setters.infos.Guserinfos;
import com.nico_11_riv.intranetepitech.database.setters.infos.Puserinfos;
import com.nico_11_riv.intranetepitech.database.setters.marks.PMarks;
import com.nico_11_riv.intranetepitech.database.setters.user.GUser;
import com.nico_11_riv.intranetepitech.ui.adapters.RVMarksAdapter;
import com.nico_11_riv.intranetepitech.ui.contents.Mark_content;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicol on 15/03/2016.
 */

@EFragment(R.layout.fragment_marks)
public class MarksActivityFragment extends Fragment {

    @RestService
    IntrAPI api;

    @Bean
    APIErrorHandler ErrorHandler;
    @ViewById
    RecyclerView rv;
    @ViewById
    ProgressBar marks_progress;
    private ArrayList<Mark_content> items;
    private List<Marks> marks;
    private GUser gUser = new GUser();
    private Guserinfos user_info = new Guserinfos();
    private RVMarksAdapter adapter;
    private TextView tv;
    private ImageView iv;

    @AfterInject
    void afterInject() {
        api.setRestErrorHandler(ErrorHandler);
    }

    @UiThread
    void setadpt(ArrayList<Mark_content> items) {
        adapter = new RVMarksAdapter(items, getContext());

        marks_progress.setVisibility(View.GONE);

        rv.setAdapter(adapter);
    }

    void fillmarksui() {
        marks = Select.from(Marks.class).where(Condition.prop("token").eq(gUser.getToken())).list();
        items = new ArrayList<Mark_content>();
        for (int i = marks.size() - 1; i > 0; i--) {
            Marks info = marks.get(i);
            items.add(new Mark_content(info.getFinalnote(), info.getCorrecteur(), info.getTitle(), info.getTitlemodule(), info.getComment()));
        }
    }

    @UiThread
    void filluserinfosui() {
        tv = (TextView) getActivity().findViewById(R.id.menu_login);
        tv.setText(user_info.getLogin());
        tv = (TextView)getActivity().findViewById(R.id.menu_email);
        tv.setText(user_info.getEmail());
        iv = (ImageView) getActivity().findViewById(R.id.menu_img);
        Picasso.with(getContext()).load(user_info.getPicture()).transform(new CircleTransform()).into(iv);
    }

    void setUserInfos() {
        List <Userinfos> uinfos = Userinfos.findWithQuery(Userinfos.class, "SELECT * FROM Userinfos WHERE token = ?", gUser.getToken());
        if (uinfos.size() > 0)
            filluserinfosui();
        Userinfos.deleteAll(Userinfos.class, "token = ?", gUser.getToken());
        api.setCookie("PHPSESSID", gUser.getToken());
        try {
            Puserinfos infos = new Puserinfos(api.getuserinfo(gUser.getLogin()));
        } catch (HttpClientErrorException e) {
            Log.d("Response", e.getResponseBodyAsString());
            Toast.makeText(getContext(), "Erreur de l'API", Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            Toast.makeText(getContext(), "Erreur de l'API", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        user_info = new Guserinfos();
        filluserinfosui();
    }

    void setMarks() {
        Marks.deleteAll(Marks.class, "token = ?", gUser.getToken());
        String m = null;
        api.setCookie("PHPSESSID", gUser.getToken());
        try {
            m = api.getmarks(gUser.getLogin());
        } catch (HttpClientErrorException e) {
            Log.d("Response", e.getResponseBodyAsString());
            Toast.makeText(getContext(), "Erreur de l'API", Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            Toast.makeText(getContext(), "Erreur de l'API", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        PMarks marks = new PMarks(m);
        api.setCookie("PHPSESSID", gUser.getToken());
        try {
            api.getuserinfo(gUser.getLogin());
        } catch (HttpClientErrorException e) {
            Log.d("Response", e.getResponseBodyAsString());
            Toast.makeText(getContext(), "Erreur de l'API", Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            Toast.makeText(getContext(), "Erreur de l'API", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        fillmarksui();
    }

    @Background
    void profile_marks() {
        setUserInfos();
        setMarks();

        setadpt(items);
    }

    @AfterViews
    void init() {
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        profile_marks();
    }

    public void filter(int size, String semester) {
        if (adapter != null)
            adapter.filter(size, semester);
        else
            Toast.makeText(getContext(), "Attendez le chargement de la page", Toast.LENGTH_SHORT).show();
    }

    public void search(String text) {
        if (adapter != null)
            adapter.search(text);
        else
            Toast.makeText(getContext(), "Attendez le chargement de la page", Toast.LENGTH_SHORT).show();
    }
}
