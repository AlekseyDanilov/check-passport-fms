package ru.android_studio.check_passport.data;

/**
 * Параметры запроса капчи (прикрутить распознование капчи)
 */
public enum CaptchaParam {
    INVALID("6305");

    private final String number;

    CaptchaParam(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }
}
