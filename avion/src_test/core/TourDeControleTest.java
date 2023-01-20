package core;

import enstabretagne.base.time.LogicalDateTime;
import org.junit.Test;

public class AvionTest {
    @Test
    public void testAvion(){
        System.out.println("test");
        LogicalDateTime date = new LogicalDateTime("12/12/2012 12:23:34");
        System.out.println(date.truncateToDays());
    }
}
