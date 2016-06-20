package ru.android_studio.check_passport;

import org.junit.Test;

import java.util.AbstractList;

import ru.android_studio.check_passport.model.TypicalResponse;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/*
* Тестирования взаимодействия с БД
* */
public class DatabaseHelperTest extends MainTest {

    /**
     * Добавить одну строку в БД
     */
    @Test
    public void testInsert() throws Exception {
        database.clear();

        database.insert("6306", "864750", TypicalResponse.UNKNOWN.getResult());

        AbstractList<Passport> expenseTypes = database.getAll();
        assertThat(expenseTypes.size(), is(1));
    }

    /**
     * Добавить несколько строк в БД
     */
    @Test
    public void testInsertFewRow() throws Exception {
        database.clear();

        database.insert("6306", "864750", TypicalResponse.UNKNOWN.getResult());
        database.insert("6306", "864750", TypicalResponse.UNKNOWN.getResult());
        database.insert("6306", "864750", TypicalResponse.UNKNOWN.getResult());
        database.insert("6306", "864750", TypicalResponse.UNKNOWN.getResult());
        database.insert("6306", "864750", TypicalResponse.UNKNOWN.getResult());

        AbstractList<Passport> expenseTypes = database.getAll();
        assertThat(expenseTypes.size(), is(5));
    }
}
