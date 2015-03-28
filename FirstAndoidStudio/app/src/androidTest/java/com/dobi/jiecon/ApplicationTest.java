package com.dobi.jiecon;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.dobi.jiecon.utils.AppsNameFilter;

import junit.framework.Assert;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }
    public void testAppNameRefine(){
        String name = AppsNameFilter.appNameRefine("Launcher app");
        Assert.assertEquals(name,"Launch..");
    }
}