package com.example.codeswitch;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.codeswitch.model.BaseObject;
import com.example.codeswitch.model.Skill;
import com.example.codeswitch.model.User;
import com.example.codeswitch.network.ApiManager;
import com.example.codeswitch.network.CustomCallback;
import com.example.codeswitch.network.DaoFactory;
import com.example.codeswitch.network.SkillDao;
import com.example.codeswitch.network.UserDao;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditProfileActivity extends ModifiedActivity {

    private String gameState;
    private TextView usernameTextView;
    private List<String> userUpdatedSkills;
//    private List<TextView> skillTextView = new ArrayList<>();
    private ConstraintLayout layout;
    private User currentUser;
    private final String TAG = "EditProfile";
    private UserDao userDao;
    private SkillDao skillDao;

    Button addskill;
    ///categories
    String[] SkillGroups = new String[]{
            "Artificial Intelligence",
            "Database",
            "Language",
            "Misc",
            "Mobile",
            "Web Development"
    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        DaoFactory daoFactory = new DaoFactory();
        userDao = daoFactory.getUserDao();
        skillDao = daoFactory.getSkillDao();

        addskill = (Button)findViewById(R.id.AddSkill);

        getDetails();
//        List<String> userUpdatedSkills = new ArrayList<>();
        List<String> savedSkills = currentUser.getSkills();
        userUpdatedSkills = new ArrayList<String>(savedSkills);





        addskill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<String> userTempSkills = new ArrayList<>();

                final AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(EditProfileActivity.this);


                alertdialogbuilder.setTitle("Select A Field ");


                alertdialogbuilder.setItems(SkillGroups, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, final int which) {
                        final String selectedField = Arrays.asList(SkillGroups).get(which);
                        //pass selectedField to retrieve list of associated skills
                        final List<String> skillsArray = new ArrayList<>();


                        //=============================================================
                        ApiManager.callApi(skillDao.getSkillList(), new CustomCallback<List<Skill>>() {
                            @Override
                            public void onResponse(List<Skill> response) {
                                if (response != null) {

                                    ApiManager.callApi(skillDao.getSkillsInGroup(selectedField), new CustomCallback<List<Skill>>() {
                                        @Override
                                        public void onResponse(List<Skill> response) {
                                            List<String> skillNames = new ArrayList<>();
                                            for (Skill skill: response) {
                                                if(!(userUpdatedSkills.contains(skill.getName())))
                                                skillsArray.add(skill.getName());
                                            }
                                            String[] finalSkillsArray = new String[skillsArray.size()];
                                            finalSkillsArray = skillsArray.toArray(finalSkillsArray);


                                                final AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);

                                                final boolean[] checkedItems = new boolean[finalSkillsArray.length];
//                                                Arrays.fill(checkedItems, Boolean.FALSE);
                                                for(int i=0; i<finalSkillsArray.length; i++){
                                                    if(userTempSkills.contains(finalSkillsArray[i])){
                                                        checkedItems[i] = true;
                                                    }
                                                    else checkedItems[i] = false;

                                                }
                                                builder.setMultiChoiceItems(finalSkillsArray, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                                                    }
                                                });
                                                builder.setCancelable(false);
                                                builder.setTitle("Select Skill To Add");
                                            final String[] finalSkillsArray1 = finalSkillsArray;
                                            builder.setNeutralButton("Back", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        int a = 0;
                                                        while(a < checkedItems.length)
                                                        {
                                                            boolean value = checkedItems[a];

                                                            if(!value){
                                                                userTempSkills.remove(finalSkillsArray1[a]);
                                                            }
                                                            else if(userTempSkills.contains(finalSkillsArray1[a])){
                                                            }
                                                            else if(value){
                                                                userTempSkills.add(finalSkillsArray1[a]);
                                                            }

                                                            a++;
                                                        }
                                                    alertdialogbuilder.show();
                                                    }
                                                });
                                                AlertDialog dialog = builder.create();

                                                dialog.show();

                                            }


                                    });

//

                                }
                                else {
                                    Log.d("Debug", "Response was null");
                                }
                            }
                        });



                    }
                });
                alertdialogbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //store selected values!! wew!!
//                        userUpdatedSkills.addAll(userTempSkills);
                        List<String> tempCopy = new ArrayList<>(userTempSkills);
                        tempCopy.removeAll(userUpdatedSkills);
                        userUpdatedSkills.addAll(tempCopy);
                        ViewGroup myViewGroup = (ViewGroup) findViewById (R.id.userSkillsGridLayout);
                        myViewGroup.removeAllViews();
                        myViewGroup.refreshDrawableState();
                        display();

                    }
                });

                alertdialogbuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });


                AlertDialog dialog = alertdialogbuilder.create();

                dialog.show();
            }
        });

        Button removeskill;
        removeskill = (Button)findViewById(R.id.RemoveSkill);
        removeskill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] userSkillsArray = new String[userUpdatedSkills.size()];
                userSkillsArray = userUpdatedSkills.toArray(userSkillsArray);


                AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(EditProfileActivity.this);


                alertdialogbuilder.setTitle("Select Skill To Remove");

                alertdialogbuilder.setItems(userSkillsArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectedSkill = userUpdatedSkills.get(which);
                        userUpdatedSkills.remove(selectedSkill);
                        ViewGroup myViewGroup = (ViewGroup) findViewById (R.id.userSkillsGridLayout);
                        myViewGroup.removeAllViews();
                        myViewGroup.refreshDrawableState();
                        display();

                        //remove selectedSkill from user's set of skills
                        //confirmation?


                    }
                });

                AlertDialog dialog = alertdialogbuilder.create();

                dialog.show();
            }
        });

        Button reset;
        reset = (Button)findViewById(R.id.Reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userUpdatedSkills.clear();
                List<String> savedSkills = currentUser.getSkills();
                userUpdatedSkills = new ArrayList<String>(savedSkills);
                ViewGroup myViewGroup = (ViewGroup) findViewById (R.id.userSkillsGridLayout);
                myViewGroup.removeAllViews();
                myViewGroup.refreshDrawableState();
                display();
            }
        });
        Button apply;
        reset = (Button)findViewById(R.id.Apply);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ApiManager.callApi(userDao.updateUserSkills(currentUser.getId(), userUpdatedSkills), new CustomCallback<BaseObject>() {
                    @Override
                    public void onResponse(BaseObject response) {
                        if (response != null) {
                            Log.d("Debug", response.toString());
                            Toast.makeText(getApplicationContext(),"Applied",Toast.LENGTH_SHORT).show();
                            currentUser.setSkills(userUpdatedSkills);
                            saveUserToPrefs(currentUser);

                        }
                        else {
                            Log.d("Debug", "Response was null");
                        }
                    }
                });

            }
        });
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        MenuItem menuItem = bottomNavigationView.getMenu().getItem(3);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.ic_job_search:
                        Intent intent_toJS = new Intent(EditProfileActivity.this, JobSearchActivity.class);
                        startActivity(intent_toJS);
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        break;
                    case R.id.ic_course_search:
                        Intent intent_toCS = new Intent(EditProfileActivity.this, CourseSearchActivity.class);
                        startActivity(intent_toCS);
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        break;
                    case R.id.ic_saved_jobs:
                        Intent intent_toSJ = new Intent(EditProfileActivity.this, SavedJobsActivity.class);
                        startActivity(intent_toSJ);
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        break;
                    case R.id.ic_profile:
                        //already here
                        break;
                }

                return false;
            }
        });
        layout = (ConstraintLayout)findViewById(R.id.relLayoutMiddle);
        display();

    }
    public void getDetails(){
        currentUser = getUserFromPrefs();

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void display(){
        usernameTextView = findViewById(R.id.Username);
        usernameTextView.setText(currentUser.getEmail());
        List<String> skills = userUpdatedSkills;

        int i=0;
        androidx.gridlayout.widget.GridLayout sgl = findViewById(R.id.userSkillsGridLayout);
        sgl.setColumnCount(2);

        for (String s: skills) {
            TextView tv = new TextView(this);
            tv.setId(i+1000);
            tv.setText(s);
            tv.setTextSize(18); //set size of text
            tv.setTextColor(0xff333333);//set text color
            tv.setBackgroundResource(R.drawable.skills_border);
            Typeface font = Typeface.createFromAsset(getAssets(), "lato.ttf");
            tv.setTypeface(font);


            sgl.addView(tv);


        }

    }


}
