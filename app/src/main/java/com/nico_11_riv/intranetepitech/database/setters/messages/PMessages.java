package com.nico_11_riv.intranetepitech.database.setters.messages;

import com.nico_11_riv.intranetepitech.database.Messages;
import com.nico_11_riv.intranetepitech.database.setters.user.GUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PMessages {
    public PMessages() {
    }

    public void init(String api) {
        GUser user = new GUser();
        try {
            JSONArray messages = new JSONArray(api);
            List<Messages> m = Messages.findWithQuery(Messages.class, "SELECT * FROM Messages WHERE login = ?", user.getLogin());
            for (int n = 0; n < m.size(); n++) {
                Messages message = m.get(n);
                message.setOld(true);
                message.save();
            }
            for (int i = 0; i < messages.length(); ++i) {
                JSONObject tmp = messages.getJSONObject(i);
                Messages msg;
                m = Messages.findWithQuery(Messages.class, "SELECT * FROM Messages WHERE login = ? AND idmessage = ?", user.getLogin(), tmp.getString("id"));
                if (m.size() > 0)
                    msg = m.get(0);
                else
                    msg = new Messages(user.getLogin());
                msg.setOld(false);
                msg.setIdmessage(tmp.getString("id"));
                msg.setTitle(tmp.getString("title"));
                msg.setContent(tmp.getString("content"));
                msg.setDate(tmp.getString("date"));
                msg.setLogincorrector(tmp.getJSONObject("user").getString("title"));
                msg.setPicture(tmp.getJSONObject("user").getString("picture"));
                msg.save();
            }
            m = Messages.findWithQuery(Messages.class, "SELECT * FROM Messages WHERE login = ?", user.getLogin());
            for (int n = 0; n < m.size(); n++) {
                Messages message = m.get(n);
                if (message.isOld())
                    message.delete();
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
