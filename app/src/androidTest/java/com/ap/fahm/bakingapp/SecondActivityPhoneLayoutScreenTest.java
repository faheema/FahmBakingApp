package com.ap.fahm.bakingapp;

import android.os.Build;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.ap.fahm.bakingapp.ui.recipe.RecipeDetailActivity;
import com.ap.fahm.bakingapp.ui.recipe.RecipeListActivity;
import com.ap.fahm.bakingapp.ui.step.StepDetailActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Note : Do not run this test before run @{@link FirstStoragePermissionScreenTest}.
 * I've run this test several time and all of them were passed.
 * If it failed, please run it again.
 *
 * Just run this on phone. Don't run on tablet.
 */

@RunWith(AndroidJUnit4.class)
public class SecondActivityPhoneLayoutScreenTest {

    @Rule
    public IntentsTestRule<RecipeListActivity> mActivityTestRule =
            new IntentsTestRule<>(RecipeListActivity.class);

    private IdlingResource mIdlingResource;

    @BeforeClass
    public static void grantPhonePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getInstrumentation().getUiAutomation().executeShellCommand(
                    "pm grant " + getTargetContext().getPackageName()
                            + " android.permission.WRITE_EXTERNAL_STORAGE");
        }
    }

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void allInteractionTest() {
        onView(withId(R.id.fl_recipe_container)).check(matches(isDisplayed()));
        onView(withId(R.id.rv_recipes)).check(matches(isDisplayed()));
        onView(withId(R.id.rv_recipes))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        intended(hasComponent(RecipeDetailActivity.class.getName()));
        onView(withId(R.id.vp_container)).check(matches(isDisplayed()));
        onView(withId(R.id.rv_ingredient)).check(matches(isDisplayed()));
        onView(withId(R.id.vp_container)).perform(swipeLeft());
        onView(withId(R.id.rv_step)).check(matches(isDisplayed()));
        onView(withId(R.id.rv_step))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.rv_step)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, RecyclerViewViewAction
                        .clickChildViewWithId(R.id.tv_step_short_description)));
        intended(hasComponent(StepDetailActivity.class.getName()));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }

}
