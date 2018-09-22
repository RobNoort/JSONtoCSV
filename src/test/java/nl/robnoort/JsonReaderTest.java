package nl.robnoort;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class JsonReaderTest {
    private static final Logger logger = LogManager.getLogger(JsonReaderTest.class);
    @Test
    public void getHeader() throws IOException {
        JsonReader reader = new JsonReader("files/simple.json");
        assertTrue(reader.getHeader().equalsIgnoreCase("students.studentName;subjects.name;subjects.marks;students.Age;"));
    }

    @Test
    public void getCsvDataTest() throws IOException {
        JsonReader reader = new JsonReader("files/simple.json");
        assertEquals(reader.getCsvData().size(),7);
    }

    @Test
    public void getCsvDataSameNumberOfSeperatorsTest() throws IOException {
        JsonReader reader = new JsonReader("files/simple.json");
        boolean same = true;
        char seperator = ';';
        int prvcount = 0;
        ArrayList<String> arr = reader.getCsvData();
        for (int i=0;i<3;i++) {                           // NB empty array in last row ignored
            String str = (String) arr.get(i);
            int count = 0;
            // count seperators in current String
            for (int j = 0; j < str.length(); j++) {
                if (str.charAt(j) == seperator) {
                    count++;
                }
            }
            if(i!=0  && prvcount!=count) {
                same = false;
            //   logger.info(" check row " + i +  " prvcount: " + prvcount +   " And count : " + count);
            } else
            //    logger.info(" check row " + i +  " prvcount: " + prvcount +   " And count : " + count);
            prvcount= count;

        }
        assertTrue("All rows equal number of seperators :" + same,same);
    }


    @Test
    public void getFilenameTest() throws IOException{
        JsonReader reader = new JsonReader("files/simple.json");
        assertSame(reader.getFilename(),"files/simple.json");
    }

    @Test
    public void getJsonStringTest()throws IOException {
        JsonReader reader = new JsonReader("files/simple.json");
        assertTrue(reader.getJsonString().startsWith("{ \"students\" "));
    }
}