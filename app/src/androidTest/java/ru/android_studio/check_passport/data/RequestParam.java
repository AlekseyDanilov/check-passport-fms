package ru.android_studio.check_passport.data;

/*
* Параметры для "Страницы запроса"
* */
public enum RequestParam {
    EXPIRED("6305", "864750"), // истёк срок действия
    EXISTED("6304", "012345"),

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
