package com.nico_11_riv.intranetepitech.database.setters.marks;

import com.nico_11_riv.intranetepitech.database.Marks;
import com.nico_11_riv.intranetepitech.database.setters.user.GUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PMarks {
    public PMarks(String api) {
        GUser user = new GUser();
        try {
            JSONObject json = new JSONObject(api);
            if (json.has("notes")) {
                JSONArray marks = json.getJSONArray("notes");
                List<Marks> m = Marks.findWithQuery(Marks.class, "SELECT * FROM Marks WHERE token = ?", user.getToken());
                for (int n = 0; n < m.size(); n++) {
                    Marks mark = m.get(n);
                    mark.setOld(true);
                    mark.save();
                }
                for (int i = 0; i < marks.length(); ++i) {
                    JSONObject tmp = marks.getJSONObject(i);
                    Marks note;
                    m = Marks.findWithQuery(Marks.class, "SELECT * FROM Marks WHERE token = ? AND codeacti = ?", user.getToken(), tmp.getString("codeacti"));
                    if (m.size() > 0)
                        note = m.get(0);
                    else
                        note = new Marks(user.getToken());
                    note.setOld(false);
                    note.setScolyear(tmp.getString("scolaryear"));
                    note.setCodemodule(tmp.getString("codemodule"));
                    note.setTitlemodule(tmp.getString("titlemodule"));
                    note.setCodeinstance(tmp.getString("codeinstance"));
                    note.setCodeacti(tmp.getString("codeacti"));
                    note.setTitle(tmp.getString("title"));
                    note.setDate(tmp.getString("date"));
                    note.setCorrecteur(tmp.getString("correcteur"));
                    note.setFinalnote(tmp.getString("final_note"));
                    note.setComment(tmp.getString("comment"));
                    note.save();
                }
                m = Marks.findWithQuery(Marks.class, "SELECT * FROM Marks WHERE token = ?", user.getToken());
                for (int n = 0; n < m.size(); n++) {
                    Marks mark = m.get(n);
                    if (mark.isOld())
                        mark.delete();
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
