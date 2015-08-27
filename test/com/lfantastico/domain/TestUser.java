/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lfantastico.domain;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author maurice
 */
public class TestUser {
    private User user;

    public TestUser() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        user = new User();
        user.setDateOfBirth(date(1964,Calendar.FEBRUARY,29));
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testAge() {
        assertEquals(45, user.getAgeOn(date(2010,Calendar.FEBRUARY,27)));
        assertEquals(45, user.getAgeOn(date(2010,Calendar.FEBRUARY,28)));
        assertEquals(46, user.getAgeOn(date(2010,Calendar.MARCH,1)));
        assertEquals(46, user.getAgeOn(date(2010,Calendar.MARCH,2)));
    }

    private static Date date(int year, int month, int day) {
        Calendar cal = Calendar.getInstance(
                TimeZone.getTimeZone("Europe/Zurich"));
        cal.clear();
        cal.set(year, month, day);
        return cal.getTime();
    }
}
