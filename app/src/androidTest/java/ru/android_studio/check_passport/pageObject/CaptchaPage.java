package ru.android_studio.check_passport.pageObject;

import android.app.Activity;
import android.support.test.espresso.NoMatchingRootException;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;

import com.squareup.spoon.Spoon;

import ru.android_studio.check_passport.R;
import ru.android_studio.check_passport.data.CaptchaParam;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.actionWithAssertions;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Страница с кодом подтверждения от ФМС
 */
public class CaptchaPage extends AbstractPage {
    public CaptchaPage(Activity activity) {
        super(activity);
    }

    public void close() {
        Spoon.screenshot(getActivity(), "before_close");
        onView(withId(getId())).perform(actionWithAssertions(pressBack()));
        Spoon.screenshot(getActivity(), "after_close");
    }

    public void isDisplayed() {
        onView(withId(getId())).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    public void isToastInvalidDisplayed() {
        boolean exceptionCaptured = true;
        try {
            onView(withText(R.string.toast_error_captcha_not_valid)).inRoot(withDecorView(not(is(activity.getWindow().getDecorView()))))
                    .check(doesNotExist());

        } catch (NoMatchingRootException e) {
            exceptionCaptured = false;
        } finally {
            assertTrue(exceptionCaptured);
        }
    }

    @Override
    protected int getId() {
        return R.id.fragment_captcha;
    }

    public void writeCaptcha(CaptchaParam captchaParam) {
        Spoon.screenshot(activity, "before_write_number");
        onView(withId(R.id.captcha_et)).perform(actionWithAssertions(typeText(captchaParam.getNumber())), closeSoftKeyboard());
        Spoon.screenshot(activity, "after_write_number");
    }

    public void clickCheckCaptchaBtn() {
        Spoon.screenshot(activity, "before_click_request_btn");
        onView(withId(R.id.check_btn)).perform(actionWithAssertions(click()));
        Spoon.screenshot(activity, "after_click_request_btn");
    }
}