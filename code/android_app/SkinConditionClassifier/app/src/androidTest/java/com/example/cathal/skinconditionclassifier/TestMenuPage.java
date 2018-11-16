package com.example.cathal.skinconditionclassifier;

import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class TestMenuPage {
    @Rule
    public final ActivityRule<MainActivity> main = new ActivityRule<>(MainActivity.class);

    @Test
    public void checkUploadTextAppears() {

        onView(withText("Upload Classification")).check(ViewAssertions.matches(isDisplayed()));
        onView(withText("Camera Classification")).check(ViewAssertions.matches(isDisplayed()));

    }

    @Test
    public void checkUploadClassificationRoute() {
        onView(withText("Upload Classification")).perform(click());
        onView(withText("Select Image")).check(ViewAssertions.matches(isDisplayed()));

    }

    @Test
    public void checkCameraClassificationRoute() {
        onView(withText("Camera Classification")).perform(click());
        onView(withId(R.id.cameraView)).check(ViewAssertions.matches(isDisplayed()));

    }
}
