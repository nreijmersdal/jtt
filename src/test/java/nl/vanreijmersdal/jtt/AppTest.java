package nl.vanreijmersdal.jtt;

import junit.framework.TestCase;

/**
 * Unit tests for JTT.
 */
public class AppTest  extends TestCase {

    public void testTimeSpend() {
        TimeTracker t = new TimeTracker();
        String actual = t.timeSpend(61000);
        assertEquals("1.01", actual);
    
        actual = t.timeSpend(121000);
        assertEquals("2.01", actual);

        actual = t.timeSpend(131000);
        assertEquals("2.11", actual);

        actual = t.timeSpend(59000);
        assertEquals("0.59", actual);
    }
}
