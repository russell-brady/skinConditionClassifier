package com.example.cathal.skinconditionclassifier;

import android.content.Intent;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.content.Intent.getIntent;
import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class TestSplashPage {
    @Rule
    public final ActivityRule<SplashActivity> main = new ActivityRule<>(SplashActivity.class);

    @Test
    public void splashScreenWorking() {
        onView(withId(R.id.splashscreen)).check(matches(isDisplayed()));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            return;
        }
        onView(withText("Upload Classification")).check(matches(isDisplayed()));
        onView(withText("Camera Classification")).check(matches(isDisplayed()));

    }
}
