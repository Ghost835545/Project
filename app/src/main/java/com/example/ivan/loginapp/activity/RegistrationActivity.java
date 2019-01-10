package com.example.ivan.loginapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.example.ivan.loginapp.Group;
import com.example.ivan.loginapp.R;
import com.example.ivan.loginapp.User;
import com.example.ivan.loginapp.rest.Connection;

import java.util.Date;


public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //ActionBar actionBar = getActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        CheckBox checkBox = findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    Spinner spinner = findViewById(R.id.groups);
                    spinner.setSelection(0);
                }
            }
        });
        Spinner spinner = findViewById(R.id.groups);
        HttpRequestTask task = new HttpRequestTask(this);
        task.execute();
        initReg();
    }
    public void startActivity()
    {
        final Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

    private void initReg() {

        final EditText fioE = findViewById(R.id.fio);
        final Spinner spinnerGr = findViewById(R.id.groups);
        final EditText loginE = findViewById(R.id.login_r);
        final EditText passwordE = findViewById(R.id.password_r);
        final EditText phoneE = findViewById(R.id.phone);
        final EditText emailE = findViewById(R.id.email);
        final Button button = findViewById(R.id.buttonReg);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inputRegistr(fioE.getText().toString(), (Group) spinnerGr.getSelectedItem(), loginE.getText().toString(), Security.encryptPass(passwordE.getText().toString()), phoneE.getText().toString(), emailE.getText().toString());

            }
        });

    }

    private void inputRegistr(String fio, Group group, String login, String password, String phone, String email) {

        User user = new User();
        user.setFio(fio);
        user.setGroup(group);
        user.setLogin(login);
        user.setPassword(password);
        user.setPhone(phone);
        user.setEmail(email);
        user.setDateReg(new Date());
        user.setDateAuth(new Date());
        user.setStatus((byte)1);
        RegistUserTask task = new RegistUserTask(user);
        task.execute();

    }

    private class HttpRequestTask extends AsyncTask<Void, Void, Group[]> {
        @Override
        protected Group[] doInBackground(Void... params) {
            try {

                Group[] groups = new Connection().getGroups();
                return groups;


            } catch (Exception e) {

            }
            return new Group[0];
        }

        @Override
        protected void onPostExecute(Group[] groups) {
            Spinner spinner = findViewById(R.id.groups);
            GroupAdapter grad = new GroupAdapter(RegistrationActivity.this, groups);
            spinner.setAdapter(grad);
        }

        HttpRequestTask(RegistrationActivity activity) {
            this.activity = activity;
        }

        RegistrationActivity activity;


    }

    private class RegistUserTask extends AsyncTask<Void, Void, User> {
        User user = null;

        RegistUserTask(User newuser) {
            this.user = newuser;
        }

        @Override
        protected User doInBackground(Void... params) {
            try {
                User user = new Connection().registUser(this.user);
                return user;

            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(User newuser) {

            // for(Group g:groups)
            //Log.i("t1", newuser.getFio());

            Toast.makeText(getApplicationContext(),  "Вы зарегистрировались!", Toast.LENGTH_SHORT).show();
            startActivity();


        }


    }

    private class GroupAdapter extends ArrayAdapter<Group> {
        GroupAdapter(Context ob, Group[] gr) {
            super(ob, R.layout.list_item, gr);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Group group = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, null);
            }
            ((TextView) convertView.findViewById(R.id.text1)).setText(group.getNameGroup());
            return convertView;
        }


    }
}

