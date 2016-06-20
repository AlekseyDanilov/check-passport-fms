package ru.android_studio.check_passport;

import com.squareup.spoon.Spoon;

import org.junit.Test;

import ru.android_studio.check_passport.data.CaptchaParam;
import ru.android_studio.check_passport.data.RequestParam;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Тестирование страницы кода подтверждения
 */
public class CaptchaFragmentTestUI extends TestUI<PassportActivity> {

    /*
    *   Тестирование загрузки капчи и ввод неправильного номера
    * */
    @Test
    public void testInvalidCaptcha() {
        Spoon.screenshot(activity, "start");

        database.clear();
        assertThat(database.getAll().size(), is(0));

        requestPage.writeSeries(RequestParam.EXISTED);
        requestPage.writeNumber(RequestParam.EXISTED);
        requestPage.clickRequestBtn();
        captchaPage.isDisplayed();
        captchaPage.writeCaptcha(CaptchaParam.INVALID);
        captchaPage.clickCheckCaptchaBtn();
        captchaPage.isToastInvalidDisplayed();

        assertThat(database.getAll().size(), is(0));
        Spoon.screenshot(activity, "end");
    }
}