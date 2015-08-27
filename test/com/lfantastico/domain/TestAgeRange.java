package com.lfantastico.domain;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestAgeRange {

    public TestAgeRange() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testContains() {
        assertTrue(new AgeRange(20,30).contains(20));
        assertTrue(new AgeRange(20,30).contains(25));
        assertTrue(new AgeRange(20,30).contains(30));
        assertFalse(new AgeRange(20,30).contains(19));
        assertFalse(new AgeRange(20,30).contains(31));
    }

    @Test
    public void testIntersects() {
        assertTrue(new AgeRange(20,30).intersects(new AgeRange(30,40)));
        assertTrue(new AgeRange(30,40).intersects(new AgeRange(20,30)));
        assertFalse(new AgeRange(20,29).intersects(new AgeRange(30,39)));
        assertFalse(new AgeRange(30,39).intersects(new AgeRange(20,29)));
    }
}
