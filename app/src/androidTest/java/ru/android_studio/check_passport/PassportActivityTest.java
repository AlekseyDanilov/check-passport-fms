package ru.android_studio.check_passport;

import android.support.test.espresso.NoMatchingRootException;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.squareup.spoon.Spoon;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.InvocationTargetException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

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
    public void validRequestParams() {
        Spoon.screenshot(activity, "initial_state");

        // @TODO Выключить WI-FI
        RequestParam requestParam = RequestParam.EXPIRED;

        onView(withId(R.id.series)).perform(typeText(requestParam.getSeries()), closeSoftKeyboard());
        onView(withId(R.id.number)).perform(typeText(requestParam.getNumber()), closeSoftKeyboard());

        // @TODO Исправить проблему с зависанием по клику
        // ошибка подключения
        // onView(withId(R.id.request_btn)).perform(click());

        boolean exceptionCaptured = false;
        try {
            onView(withText(R.string.toast_error_internet_unavailable))
                    .inRoot(withDecorView(not(is(activity.getWindow().getDecorView()))))
                    .check(doesNotExist());

        } catch (NoMatchingRootException e) {
            exceptionCaptured = true;
        } finally {
            assertTrue(exceptionCaptured);
        }

        Spoon.screenshot(activity, "after_login");
    }
}