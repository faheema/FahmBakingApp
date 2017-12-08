package com.ap.fahm.bakingapp;

import android.os.Build;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.ap.fahm.bakingapp.ui.recipe.RecipeListActivity;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Note : Run this test only once, just after app installed (permission have not been granted)
 * Do not run @{@link SecondActivityPhoneLayoutScreenTest} before running this test.
 */

@RunWith(AndroidJUnit4.class)
public class FirstStoragePermissionScreenTest {

    @Rule
    public ActivityTestRule<RecipeListActivity> mActivityTestRule =
            new ActivityTestRule<>(RecipeListActivity.class);

    @BeforeClass
    public static void grantPhonePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getInstrumentation().getUiAutomation().executeShellCommand(
                    "pm grant " + getTargetContext().getPackageName()
                            + " android.permission.WRITE_EXTERNAL_STORAGE");
        }
    }

    @Test
    public void permissionTest() {
        onView(withId(R.id.tv_storage_permission_message)).check(matches(isDisplayed()));
        onView(withText("OK"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());
    }


}
