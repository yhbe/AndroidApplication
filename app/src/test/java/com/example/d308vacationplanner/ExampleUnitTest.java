package com.example.d308vacationplanner;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    public boolean isValidQuery(String query) {
        String regex = ".*[0-9&@!#%$&^*()_+=<>?/|{}\\[\\]:;\"',.~`].*";
        if (query.matches(regex) || query.length() > 15) {
            return false;
        }
        return true;
    }

    @Test
    public void testValidQuery() {
        assertTrue(isValidQuery("New York"));
    }

    @Test
    public void testInvalidQuerySymbols() {
        assertFalse(isValidQuery("New Yo$"));
    }

    @Test
    public void testInvalidLength() {assertFalse(isValidQuery("New York City Is A Very Very Nice City"));}

}