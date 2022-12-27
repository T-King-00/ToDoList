package com.project.listapp;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void homeitems() {
        TodayItems n=new TodayItems();
        assertEquals(1,n.adpt.getItemCount());
    }

}