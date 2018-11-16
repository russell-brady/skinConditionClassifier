package com.example.cathal.skinconditionclassifier;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.content.Context;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class TestUploadPage {

    private Bitmap bitmap;
    private ImageView imgView;

    @Rule
    public final ActivityRule<UploadActivity> main = new ActivityRule<>(UploadActivity.class);

    @Test
    public void checkCorrectActivity() {
        onView(withId(R.id.btnSelect)).check(matches(isDisplayed()));
        onView(withId(R.id.btnUpload)).check(matches(isDisplayed()));
        onView(withId(R.id.btnUrl)).check(matches(isDisplayed()));
    }

    @Test
    public void clickSubmitButtonWithNoImage() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            return;
        }
        onView(withId(R.id.btnUpload)).perform(click());
        onView(withText("Unavailable while the image is not selected!")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            return;
        }
    }

    @Test
    public void clickVisualisationButtonWithNoClassification() {
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

//    @Test
//    public void uploadImage() {
//        bitmap = Bitmap.createBitmap(224, 224, Bitmap.Config.ARGB_8888);
//        bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, false); //// new
//        imgView.setImageBitmap(bitmap);
//
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            return;
//        }
//
//    }

}