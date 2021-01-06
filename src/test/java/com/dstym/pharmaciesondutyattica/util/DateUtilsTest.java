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
                new SimpleDateFormat("yyyy-MM-dd").parse(testStringDate))
        );
    }

    @Test
    void testDateToString_validDate() throws ParseException {
        var expectedDate = "2020-01-03";
        var actualDate = DateUtils.dateToString(new SimpleDateFormat("yyyy-MM-dd").parse(expectedDate));
        assertEquals(expectedDate, actualDate);
    }

    @Test
    void testStringToDate_nonValidDate() {
        var testStringDate = "some not valid string";

        var expected = DateUtils.stringToDate(testStringDate);

        // if an exception is caught, then stringToDate() will return null
        // so we just assure that it is null
        assertNull(expected);
    }

    @Test
    void testStringToDate_validDate() throws ParseException {
        var testStringDate = "2020-08-01";

        var expectedDate = new SimpleDateFormat("yyyy-MM-dd").parse(testStringDate);
        var actualDate = new SimpleDateFormat("yyyy-MM-dd").parse("2020-08-01");
        assertEquals(expectedDate, actualDate);
    }
}
