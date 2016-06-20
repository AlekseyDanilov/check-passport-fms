package ru.android_studio.check_passport;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/*
* Последовательность тестов для UI
* */
@RunWith(Suite.class)
@Suite.SuiteClasses({CaptchaFragmentTestUI.class, RequestFragmentTestUI.class})
public class SuiteUI {
}
