package ru.android_studio.check_passport;

import com.squareup.spoon.Spoon;

import org.junit.Test;

import ru.android_studio.check_passport.data.RequestParam;

/**
 * Тестирование страницы запроса
 */
public class RequestFragmentTest extends MainTest {

    /*
    *   Тестирование загрузки капчи
    * */
    @Test
    public void testCaptchaLoading() {
        Spoon.screenshot(activity, "start");

        database.clear();
        requestPage.writeSeries(RequestParam.EXISTED);
        requestPage.writeNumber(RequestParam.EXISTED);
        requestPage.clickRequestBtn();
        captchaPage.isDisplayed();

        Spoon.screenshot(activity, "end");
    }

    /*
    * Загрузка капчи и закрытие экрана по кнопки back
    * */
    @Test
    public void testCaptchaLoadingAndClose() {
        Spoon.screenshot(activity, "start");

        database.clear();
        requestPage.writeSeries(RequestParam.EXISTED);
        requestPage.writeNumber(RequestParam.EXISTED);
        requestPage.clickRequestBtn();
        captchaPage.isDisplayed();
        captchaPage.close();

        Spoon.screenshot(activity, "end");
    }

    /*
    * Слайд на страницу истории
    * */
    @Test
    public void swipeToHistory() {
        Spoon.screenshot(activity, "start");

        requestPage.swipeToHistoryPage();

        Spoon.screenshot(activity, "end");
    }

    /*
    * Слайды влево - вправо
    * */
    @Test
    public void swipeBackToPassport() {
        Spoon.screenshot(activity, "start");

        requestPage.swipeToHistoryPage();
        historyPage.swipeToRequestPage();

        Spoon.screenshot(activity, "end");
    }
}