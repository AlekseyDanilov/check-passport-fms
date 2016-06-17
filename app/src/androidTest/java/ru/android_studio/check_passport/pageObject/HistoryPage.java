package ru.android_studio.check_passport.pageObject;

import android.app.Activity;

import com.squareup.spoon.Spoon;

import ru.android_studio.check_passport.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.actionWithAssertions;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Page Object Страницы Истории
 */
public class HistoryPage extends AbstractPage {
    public HistoryPage(Activity activity) {
        super(activity);
    }

    public void swipeToRequestPage() {
        Spoon.screenshot(getActivity(), "before_swipe_right");
        onView(withId(getId())).perform(actionWithAssertions(swipeRight()));
        Spoon.screenshot(getActivity(), "after_swipe_right");
    }

    @Override
    protected int getId() {
        return R.id.fragment_history;
    }
}
