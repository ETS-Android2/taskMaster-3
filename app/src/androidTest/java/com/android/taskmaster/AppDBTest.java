package com.android.taskmaster;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import androidx.recyclerview.widget.RecyclerView;
import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.StringEndsWith.endsWith;
@RunWith(AndroidJUnit4.class)
public class AppDBTest {
    private TaskDao taskDao1;
    private AppDB db;
    private List<TaskItem> taskList;
    @Rule
    public ActivityScenarioRule rule = new ActivityScenarioRule<>(MainActivity.class);
    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDB.class).build();
        taskDao1 = db.taskDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void addTaskTest1() {
        ActivityScenario scenario = rule.getScenario();
        onView(withId(R.id.addBtn)).perform(click());
        onView(withId(R.id.task_title_input)).perform(typeText("Task A"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.task_desc)).perform(typeText("Task A"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.addTaskBtn)).perform(click());
        onView(withId(R.id.goHome)).perform(click());
        Espresso.onView(withId(R.id.list)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
//        taskList = taskDao1.findAll();
//        System.out.println(";;;;;;;;;;;"+taskList.toString());
//        String byTitle = taskDao1.findByName("Task A");
//        System.out.println("aaaaaaaaaaaaaaaa"+Arrays.toString(byTitle.toArray()));
//        assertThat(byTitle.get(0), equalTo("Task A"));

    }
}