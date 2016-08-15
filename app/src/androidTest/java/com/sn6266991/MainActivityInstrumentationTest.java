package com.sn6266991;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Checks;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.sn6266991.activity.BookmarksActivity;
import com.sn6266991.activity.MainActivity;
import com.sn6266991.activity.SettingsActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * 6266991
 *
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityInstrumentationTest {

    public static final Matcher<Uri> URI_URL_MATCHER = new Matcher<Uri>() {
        @Override
        public boolean matches(Object item) {
            String url = item.toString();
            return url.startsWith("http://") || url.startsWith("https://");
        }

        @Override
        public void describeMismatch(Object item, Description mismatchDescription) {

        }

        @Override
        public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {

        }

        @Override
        public void describeTo(Description description) {

        }
    };

    @Rule
    public IntentsTestRule mActivityRule = new IntentsTestRule<>(MainActivity.class);

    Activity currentActivity;

    /*@Test
    public void sayHello(){
        onView(withText("Say hello!")).perform(click());
        onView(withId(R.id.recycler_articles)).check(matches(withText("Hello, World!")));
    }*/

    @Test
    public void clickNewsItemIntent(){
        onView(withId(R.id.recycler_articles)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        intended(allOf(hasAction(Intent.ACTION_VIEW), hasData(URI_URL_MATCHER)));
    }

    /*@Test
    public void clickNewsItemGrayOut(){
        onView(withId(R.id.recycler_articles)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withRecyclerView(R.id.recycler_articles).atPosition(1)).check(matches(withTextColor(Color.GRAY)));
    }*/

    @Test
    public void goToSettings(){
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(R.string.menu_settings)).perform(click());
        intended(IntentMatchers.hasComponent(SettingsActivity.class.getName()));

        Activity nextActivity = getActivityInstance();

        onView(withText(R.string.preference_title_theme)).perform(click());
        onView(withText("Blue")).perform(click());

        Assert.assertEquals(
                getPrimaryColor(),
                ContextCompat.getColor(nextActivity, R.color.md_indigo_500)
        );

        onView(withText(R.string.preference_title_theme)).perform(click());
        onView(withText("White")).perform(click());

        Assert.assertEquals(
                getPrimaryColor(),
                ContextCompat.getColor(nextActivity, R.color.md_white_1000)
        );
    }

    @Test
    public void goToBookmarks(){
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(R.string.menu_bookmark)).perform(click());
        intended(IntentMatchers.hasComponent(BookmarksActivity.class.getName()));
    }


    public Activity getActivityInstance(){
        getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                Collection<Activity> resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
                if (resumedActivities.iterator().hasNext()){
                    currentActivity = resumedActivities.iterator().next();
                }
            }
        });

        return currentActivity;
    }

    public int getPrimaryColor(){
        TypedValue typedValue = new TypedValue();
        getActivityInstance().getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    // Convenience helper
    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    public static Matcher<View> withTextColor(final int color) {
        Checks.checkNotNull(color);
        return new BoundedMatcher<View, TextView>(TextView.class) {
            @Override
            public boolean matchesSafely(TextView warning) {
                return color == warning.getCurrentTextColor();
            }
            @Override
            public void describeTo(Description description) {
                description.appendText("with text color: ");
            }
        };
    }

}
