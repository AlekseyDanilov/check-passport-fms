package ru.android_studio.check_passport.pageObject;

import android.app.Activity;

import com.squareup.spoon.Spoon;

import ru.android_studio.check_passport.R;
import ru.android_studio.check_passport.data.RequestParam;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.actionWithAssertions;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Страница запроса
 */
public class RequestPage extends AbstractPage {
    public RequestPage(Activity activity) {
        super(activity);
    }

    /**
     * Переход на страницу истории
     * */
    public void swipeToHistoryPage() {
        Spoon.screenshot(activity, "before_swipe_left");
        onView(withId(getId()))
                .perform(actionWithAssertions(swipeLeft()));
        Spoon.screenshot(activity, "after_swipe_left");
    }

    /**
     * Написать 'Серия'
     * */
    public void writeSeries(RequestParam requestParam) {
        Spoon.screenshot(activity, "before_series");
        onView(withId(R.id.series))
                .perform(actionWithAssertions(typeText(requestParam.getSeries())), closeSoftKeyboard());
        Spoon.screenshot(activity, "after_series");
    }

    /**
     * Написать 'Номер'
     * */
    public void writeNumber(RequestParam requestParam) {
        Spoon.screenshot(activity, "before_write_number");
        onView(withId(R.id.number))
                .perform(actionWithAssertions(typeText(requestParam.getNumber())), closeSoftKeyboard());
        Spoon.screenshot(activity, "after_write_number");
    }

    /**
     * Нажать на кнопку "Запроса'
     * */
    public void clickRequestBtn() {
        Spoon.screenshot(activity, "before_click_request_btn");
        onView(withId(R.id.request_btn))
                .perform(actionWithAssertions(click()));
        Spoon.screenshot(activity, "after_click_request_btn");
    }

    @Override
    protected int getId() {
        return R.id.fragment_request;
    }
}
