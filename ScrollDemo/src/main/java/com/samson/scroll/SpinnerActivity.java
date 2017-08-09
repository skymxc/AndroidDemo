package com.samson.scroll;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.jaredrummler.materialspinner.MaterialSpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by samson on 16-12-1.0
 */
public class SpinnerActivity extends Activity {


    protected MaterialSpinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner);
        spinner = (MaterialSpinner) findViewById(R.id.menu);
        List<User> users = new ArrayList<>();
        users.add(new User("15910721339", "111111"));
        users.add(new User("15910721354", "111121"));
        users.add(new User("15910721234", "111331"));
        users.add(new User("15910721444", "111221"));
        users.add(new User("15910721654", "111661"));
        spinner.setItems(users);
    }

    class User {
        String userName;
        String password;

        public User(String userName, String password) {
            this.userName = userName;
            this.password = password;
        }

        @Override
        public String toString() {
            return userName;
        }
    }
}
