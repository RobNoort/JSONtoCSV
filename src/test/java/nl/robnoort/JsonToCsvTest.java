// https://stackoverflow.com/questions/309396/java-how-to-test-methods-that-call-system-exit

package nl.robnoort;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;

import java.io.FileNotFoundException;

import static org.junit.Assert.*;

public class JsonToCsvTest {
    @Rule   // Junit 4.9 and higher
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Test
    public void checkNbrParameters2Test() {
        assertFalse(JsonToCsv.checkNbrParameters2(new String[]{}));
    }
    @Test
    public void checkParametersExactly2() {
        assertTrue(JsonToCsv.checkNbrParameters2(new String[]{"filename","filename"}));
    }

    @Test
    public void checkFileTestExists() {
        assertTrue(JsonToCsv.checkFileRead("files/simple.json"));
    }

    @Test
    public void checkFileTestNotExists() {
        exit.expectSystemExit();
        JsonToCsv.checkFileRead("files/simple.xxx");
    }

}