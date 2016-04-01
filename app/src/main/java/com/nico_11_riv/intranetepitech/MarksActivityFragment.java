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
import com.nico_11_riv.intranetepitech.database.Mark;
import com.nico_11_riv.intranetepitech.database.Userinfos;
import com.nico_11_riv.intranetepitech.database.setters.marks.PMarks;
import com.nico_11_riv.intranetepitech.database.setters.user.GUser;
import com.nico_11_riv.intranetepitech.database.setters.user.GUserInfos;
import com.nico_11_riv.intranetepitech.database.setters.user.PUserInfos;
import com.nico_11_riv.intranetepitech.toolbox.CircleTransform;
import com.nico_11_riv.intranetepitech.toolbox.IsConnected;
import com.nico_11_riv.intranetepitech.ui.adapters.RVMarksAdapter;
import com.nico_11_riv.intranetepitech.ui.contents.MarkContent;
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
 *
 * Created by nicol on 15/03/2016.
 *
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

    @ViewById
    TextView noinfos;

    private GUser user = new GUser();
    private GUserInfos user_info = new GUserInfos();
    private RVMarksAdapter adapter;
    private static int def_nb = 8;
    private static int def_semester = 11;
    private IsConnected ic;

    @UiThread
    public void mToast(String msg, int time) {
        Toast.makeText(getContext(), msg, time).show();
    }

    @AfterInject
    void afterInject() {
        api.setRestErrorHandler(ErrorHandler);
    }

    @UiThread
    void setadpt(ArrayList<MarkContent> items) {
        adapter = new RVMarksAdapter(items, getContext());
        rv.setAdapter(adapter);
    }

    @UiThread
    void filluserinfosui() {
        TextView tv = (TextView) getActivity().findViewById(R.id.menu_login);
        tv.setText(user_info.getLogin());
        tv = (TextView)getActivity().findViewById(R.id.menu_email);
        tv.setText(user_info.getEmail());

        ImageView iv = (ImageView) getActivity().findViewById(R.id.menu_img);
        Picasso.with(getContext()).load(user_info.getPicture()).transform(new CircleTransform()).into(iv);
    }

    void fillmarksui() {
        ArrayList<MarkContent> items = new ArrayList<>();
        List<Mark> marks = Select.from(Mark.class).where(Condition.prop("login").eq(user.getLogin())).list();

        for (int i = marks.size() - 1; i > 0; i--) {
            Mark info = marks.get(i);
            items.add(new MarkContent(info.getFinalnote(), info.getCorrecteur(), info.getTitle(), info.getTitlemodule()));
        }
        setadpt(items);
    }

    @UiThread
    void fillnewmarksui() {
        if (def_nb != 8) {
            if (def_semester != 11)
                adapter.filter((def_nb + 1) * 5, "B" + Integer.toString(def_semester) + "%");
            else
                adapter.filter((def_nb + 1) * 5, "All");
        } else {
            if (def_semester != 11)
                adapter.filter(0, "B" + Integer.toString(def_semester) + "%");
            else
                adapter.filter(0, "All");
        }
    }

    void setUserInfos() {
        List <Userinfos> uInfos = Userinfos.findWithQuery(Userinfos.class, "SELECT * FROM Userinfos WHERE login = ?", user.getLogin());
        if (uInfos.size() > 0)
            filluserinfosui();
        if (ic.connected()) {
            Userinfos.deleteAll(Userinfos.class, "login = ?", user.getLogin());
            api.setCookie("PHPSESSID", user.getToken());
            try {
                PUserInfos infos = new PUserInfos();
                infos.init(api.getuserinfo(user.getLogin()));
            } catch (HttpClientErrorException e) {
                Log.d("Response", e.getResponseBodyAsString());
                mToast(e.getMessage(), Toast.LENGTH_SHORT);
            } catch (NullPointerException e) {
                mToast(e.getMessage(), Toast.LENGTH_SHORT);
                e.printStackTrace();
            }
            user_info = new GUserInfos();
            filluserinfosui();
        }
    }

    void setMarks() {
        fillmarksui();
        if (ic.connected()) {
            String m = null;
            api.setCookie("PHPSESSID", user.getToken());
            try {
                m = api.getmarksandmodules(user.getLogin());
            } catch (HttpClientErrorException e) {
                Log.d("Response", e.getResponseBodyAsString());
                mToast(e.getMessage(), Toast.LENGTH_SHORT);
            } catch (NullPointerException e) {
                mToast(e.getMessage(), Toast.LENGTH_SHORT);
                e.printStackTrace();
            }
            PMarks marks = new PMarks();
            marks.init(m);
            fillnewmarksui();
        }
    }

    @UiThread
    void setProgressBar() {
        marks_progress.setVisibility(View.GONE);
        List<Mark> m = Mark.findWithQuery(Mark.class, "SELECT * FROM Mark WHERE login = ?", user.getLogin());
        if (m.size() < 1)
            noinfos.setVisibility(View.VISIBLE);
    }

    @Background
    void profile_marks() {
        setUserInfos();
        setMarks();
        setProgressBar();
    }

    @AfterViews
    void init() {
        ic = new IsConnected(getActivity().getApplicationContext());
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        profile_marks();
    }

    public void filter(int size, String semester, int def_nb_act, int def_semester_act) {
        if (adapter != null) {
            def_nb = def_nb_act;
            def_semester = def_semester_act;
            adapter.filter(size, semester);
        }
        else
            mToast("Attendez le chargement de la page", Toast.LENGTH_SHORT);
    }

    public void search(String text) {
        if (adapter != null)
            adapter.search(text);
        else
            mToast("Attendez le chargement de la page", Toast.LENGTH_SHORT);
    }
}
