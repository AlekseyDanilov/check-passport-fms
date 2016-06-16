package ru.android_studio.check_passport;

import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.squareup.spoon.Spoon;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.actionWithAssertions;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;



@RunWith(AndroidJUnit4.class)
//@SdkSuppress(minSdkVersion = 15)
@LargeTest
public class PassportActivityTest {

    @Rule
    public ActivityTestRule<PassportActivity> activityActivityTestRule = new ActivityTestRule<>(PassportActivity.class);

    private String TAG = "PassportActivityTest";

    private PassportActivity activity;

    @Before
    public void setUp() {
        activity = activityActivityTestRule.getActivity();
    }


    // @TODO Добавить больше номеров для проверок
    enum RequestParam {
        EXPIRED("6305", "864750"),
        EXISTED("", ""),
        NULL("", ""),
        UNKNOWN("1111", "111111"),
        INVALID_SERIES("1111", "111111"),
        INVALID_NUMBER("1111", "111111");

        private final String series;
        private final String number;

        RequestParam(String series, String number) {
            this.series = series;
            this.number = number;
        }

        public String getSeries() {
            return series;
        }

        public String getNumber() {
            return number;
        }
    }

    @Test
    public void testCaptchaLoading() {
        Spoon.screenshot(activity, "start");
        // @TODO Выключить WI-FI
        RequestParam requestParam = RequestParam.EXPIRED;

        onView(withId(R.id.series)).perform(actionWithAssertions(typeText(requestParam.getSeries())), closeSoftKeyboard());
        Spoon.screenshot(activity, "series");

        onView(withId(R.id.number)).perform(actionWithAssertions(typeText(requestParam.getNumber())), closeSoftKeyboard());
        Spoon.screenshot(activity, "number");

        // @TODO Исправить проблему с зависанием по клику
        // ошибка подключения
        onView(withId(R.id.request_btn)).perform(actionWithAssertions(click()));
        Spoon.screenshot(activity, "loading");

        onView(withId(R.id.fragment_captcha)).check(ViewAssertions.matches(isDisplayed()));
        Spoon.screenshot(activity, "after_login");
    }

    @Test
    public void testCaptchaLoadingAndClose() {
        testCaptchaLoading();
        onView(withId(R.id.fragment_captcha)).perform(actionWithAssertions(pressBack()));
        Spoon.screenshot(activity, "end");
    }

    @Test
    public void swipeToHistory() {
        Spoon.screenshot(activity, "start");
        onView(withId(R.id.fragment_passport)).perform(actionWithAssertions(swipeLeft()));
        Spoon.screenshot(activity, "end");
    }

    @Test
    public void swipeBackToPassport() {
        Spoon.screenshot(activity, "start");

        onView(withId(R.id.fragment_passport)).perform(actionWithAssertions(swipeLeft()));

        onView(withId(R.id.fragment_history)).perform(actionWithAssertions(swipeRight()));

        Spoon.screenshot(activity, "end");
    }
}