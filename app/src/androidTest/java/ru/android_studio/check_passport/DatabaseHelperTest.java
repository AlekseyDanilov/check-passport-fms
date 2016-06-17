package ru.android_studio.check_passport;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.AbstractList;

import ru.android_studio.check_passport.model.TypicalResponse;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/*
* Тестирования взаимодействия с БД
* */
@RunWith(AndroidJUnit4.class)
public class DatabaseHelperTest {

    @Rule
    public ActivityTestRule<PassportActivity> activityActivityTestRule = new ActivityTestRule<>(PassportActivity.class);

    private DatabaseHelper database;

    @Before
    public void setUp() throws Exception {
        activityActivityTestRule.getActivity().deleteDatabase(DatabaseHelper.DB_NAME);
        database = new DatabaseHelper(activityActivityTestRule.getActivity());
    }

    @After
    public void tearDown() throws Exception {
        database.close();
    }

    /**
    * Добавить одну строку в БД
    * */
    @Test
    public void testInsert() throws Exception {
        database.insert("6306", "864750", TypicalResponse.UNKNOWN.getResult());

        AbstractList<Passport> expenseTypes = database.getAll();
        assertThat(expenseTypes.size(), is(1));
    }

    /**
     * Добавить несколько строк в БД
     * */
    @Test
    public void testInsertFewRow() throws Exception {
        database.insert("6306", "864750", TypicalResponse.UNKNOWN.getResult());
        database.insert("6306", "864750", TypicalResponse.UNKNOWN.getResult());
        database.insert("6306", "864750", TypicalResponse.UNKNOWN.getResult());
        database.insert("6306", "864750", TypicalResponse.UNKNOWN.getResult());
        database.insert("6306", "864750", TypicalResponse.UNKNOWN.getResult());

        AbstractList<Passport> expenseTypes = database.getAll();
        assertThat(expenseTypes.size(), is(5));
    }
}
