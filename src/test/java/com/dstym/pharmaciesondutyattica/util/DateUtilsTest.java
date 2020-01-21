package com.dstym.pharmaciesondutyattica.util;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilsTest {
    @Test
    void testDateToString_nonValidDate() {
        var testStringDate = "some not valid string";
        assertThrows(ParseException.class, () -> DateUtils.dateToString(
                new SimpleDateFormat("d/M/yyyy").parse(testStringDate))
        );
    }

    @Test
    void testDateToString_validDate() throws ParseException {
        var expectedDate = "8/1/2020";
        var actualDate = DateUtils.dateToString(new SimpleDateFormat("d/M/yyyy").parse(expectedDate));
        assertEquals(expectedDate, actualDate);
    }

    @Test
    void testStringToDate_nonValidDate() {
        var testStringDate = "some not valid string";

        var expected = DateUtils.stringToDate(testStringDate);

        // if an exception is cached, then stringToDate() will return null
        // so we just assure that it is null
        assertNull(expected);
    }

    @Test
    void testStringToDate_validDate() throws ParseException {
        var testStringDate = "8/1/2020";

        var expectedDate = new SimpleDateFormat("d/M/yyyy").parse(testStringDate);
        var actualDate = new SimpleDateFormat("d/M/yyyy").parse("8/1/2020");
        assertEquals(expectedDate, actualDate);
    }

    /*
        I should not forget to implement these two tests.
     */
    @Test
    void testGetDateFromTodayPlusDays_nonValidDate() {
        assertTrue(true);
    }

    @Test
    void testGetDateFromTodayPlusDays_validDate() {
        assertTrue(true);
    }
}
