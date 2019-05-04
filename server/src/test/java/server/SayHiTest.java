package server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SayHiTest {

    @Test
    public void sayhiEqualResponseString() {
        SayHi hi = new SayHi();
        assertEquals("Hello there!", hi.sayhi());
    }
    @Test
    public void sayhiWrongString(){
        SayHi hicontrol = new SayHi();
        assertNotEquals("This is an incorrect message", hicontrol.sayhi());
    }
}