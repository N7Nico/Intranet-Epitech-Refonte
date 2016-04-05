package com.nico_11_riv.intranetepitech.toolbox;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

import com.nico_11_riv.intranetepitech.LoginActivity_;
import com.nico_11_riv.intranetepitech.MarksActivity_;
import com.nico_11_riv.intranetepitech.ModulesActivity_;
import com.nico_11_riv.intranetepitech.ProfileActivity_;
import com.nico_11_riv.intranetepitech.ProjectsActivity_;
import com.nico_11_riv.intranetepitech.R;
import com.nico_11_riv.intranetepitech.ScheduleActivity_;
import com.nico_11_riv.intranetepitech.database.Schedule;
import com.nico_11_riv.intranetepitech.database.User;
import com.nico_11_riv.intranetepitech.database.setters.user.GUser;

import java.util.List;

/**
 *
 * Created by nicol on 30/03/2016.
 *
 */

public class MenuSelect {
    private GUser user = new GUser();
    public MenuSelect() {
    }

    public Intent select(Activity activity, DrawerLayout drawer_layout, int id) {
        if (id == R.id.nav_profile) {
            drawer_layout.closeDrawer(GravityCompat.START);
            return new Intent(activity, ProfileActivity_.class);
        } else if (id == R.id.nav_marks) {
            drawer_layout.closeDrawer(GravityCompat.START);
            return new Intent(activity, MarksActivity_.class);
        } else if (id == R.id.nav_modules) {
            drawer_layout.closeDrawer(GravityCompat.START);
            return new Intent(activity, ModulesActivity_.class);
        } else if (id == R.id.nav_projects) {
            drawer_layout.closeDrawer(GravityCompat.START);
            return new Intent(activity, ProjectsActivity_.class);
        } else if (id == R.id.nav_schedule) {
            drawer_layout.closeDrawer(GravityCompat.START);
            return new Intent(activity, ScheduleActivity_.class);
        } else if (id == R.id.nav_logout) {
            drawer_layout.closeDrawer(GravityCompat.START);
            List<User> users = User.find(User.class, "connected = ?", "true");
            User user = users.get(0);
            user.setConnected("false");
            user.save();
            return new Intent(activity, LoginActivity_.class);
        }
        drawer_layout.closeDrawer(GravityCompat.START);
        return (new Intent(activity, ProfileActivity_.class));
    }
}
