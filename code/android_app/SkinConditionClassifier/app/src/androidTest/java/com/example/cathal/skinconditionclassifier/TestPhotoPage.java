package com.example.cathal.skinconditionclassifier;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class TestPhotoPage {
    @Rule
    public final ActivityRule<SkinActivity> main = new ActivityRule<>(SkinActivity.class);

    @Test
    public void checkCorrectActivity() {
        onView(withId(R.id.classifySkinButton)).check(matches(isDisplayed()));
        onView(withId(R.id.cameraView)).check(matches(isDisplayed()));
        onView(withId(R.id.btnUrl)).check(matches(isDisplayed()));
    }

    @Test
    public void tryVisualisationWithoutClassification() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            return;
        }
        onView(withId(R.id.btnUrl)).perform(click());
        onView(withText("Unavailable while the image is not uploaded!")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            return;
        }
    }

    @Test
    public void classifyPhoto() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            return;
        }
        onView(withId(R.id.classifySkinButton)).perform(click());
        try {
            Thread.sleep(12000);
        } catch (InterruptedException e) {
            return;
        }
        onView(withId(R.id.responseStatus)).check(matches(isDisplayed()));
        onView(withId(R.id.btnUrl)).perform(scrollTo()).perform(click());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            return;
        }

    }




}
