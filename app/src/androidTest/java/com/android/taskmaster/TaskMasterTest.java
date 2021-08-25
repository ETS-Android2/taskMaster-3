package com.android.taskmaster;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class TaskMasterTest  {
    // --Commented out by Inspection (8/25/21, 3:54 PM):private static final String TAG = "JUDY";
    // --Commented out by Inspection (8/25/21, 3:54 PM):private TaskDao taskDao;
    // --Commented out by Inspection (8/25/21, 3:54 PM):String taskState;
    @Rule
    public final ActivityScenarioRule rule = new ActivityScenarioRule<>(MainActivity.class);
    @Test
    public void usernameTest(){
        onView(withId(R.id.imageButton)).perform(click());
        onView(withId(R.id.username_edit_txt)).perform(typeText("mahmood"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.save_username_btn)).perform(click());
        onView(withId(R.id.goHome_btn)).perform(click());
        onView(withId(R.id.Username_main)).check(matches(withText("mahmood")));
    }

    @Test
    public void addTaskTest(){
        onView(withId(R.id.addBtn)).perform(click());
        onView(withId(R.id.task_title_input)).perform(typeText("Task A"),ViewActions.closeSoftKeyboard());
        onView(withId(R.id.task_desc)).perform(typeText("Task A"),ViewActions.closeSoftKeyboard());
        onView(withId(R.id.addTaskBtn)).perform(click());
//        List str = (TextView)withId(R.id.textView7);
//        System.out.println("mmmmmmmmmmmmm"+str.getText().toString());
//        onView(withId(R.id.list)).check(matches(isDisplayed()));
//        onView(withText(endsWith("Task A"))).check(matches(isDisplayed()));
//        Matcher<View> strings = withId(R.id.list);
//        System.out.println("mmmmmmmmmmmmmmmmmmmmmmmmmmmmm"+strings.toString());
//        onView(withId(R.id.list))
//                .perform(MainActivity.actionOnItemAtPosition(3, click()));
//        AppDB database = Room.databaseBuilder(, AppDB.class, AddTask.TASK)
//                .allowMainThreadQueries().build();
//        taskDao = database.taskDao();

//        onView(withId(R.id.textView7)).check(matches(withText("Task A")));


//onView(withId(R.id.spinner)).perform(click());
//onData(allOf(is(instanceOf(String.class)), is("Task A"))).perform(click());
//onView(withId(R.id.spinner)).check(matches(withSpinnerText(containsString("Task A"))));



    }


}
