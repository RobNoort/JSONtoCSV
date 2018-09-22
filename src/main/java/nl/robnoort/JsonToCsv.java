package nl.robnoort;

import java.io.*;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import org.json.simple.JSONObject;

import static java.lang.System.exit;


public class JsonToCsv {
    private static final Logger logger = LogManager.getLogger(JsonToCsv.class);
    private static String fileName;
    private static JSONObject jsonObject;


    public static void main(String[] args) throws IOException {

        // exit if error found
        doChecksOnArgs(args);

        //get json from first file parameter
        JsonReader jsonReader = new JsonReader(args[0]);

        //generate csv data from json
        ArrayList<String> csv = jsonReader.getCsvData();

        //write csv to second file parameter
        writeCsvFile(args[1], csv);
    }

    //region checks
    protected static void doChecksOnArgs(String[] args) throws IOException {
        logger.info("start");
        logger.info("check args for 2 parameters");
        boolean check = checkNbrParameters2(args);
        if (!check) {
            endApplicationWithError("Proper Usage is: java JSONtoCSV filename");
        }
        logger.info("parameters checked");

        logger.info("check file for 1st parameter");
        checkFileRead(args[0]);                              // there are 2 parameters
        logger.info("file checked ");

        logger.info("check file for 2nd parameter it should not exist");
        checkFileWrite(args[1]);                             // there are 2 parameters
        logger.info("file checked ");


    }

    // protected for testing
    static boolean checkNbrParameters2(String[] args) {
        return (args.length != 2);
    }

    static boolean checkFileRead(String filename) {
        logger.info("checkFile");
        File file = new File(filename);
        if (!file.isFile()) endApplicationWithError("File not found, check parameter " + filename);
        if (!file.canRead()) endApplicationWithError("Not able to read file, check parameter " + filename);
        return true;

    }

    static void checkFileWrite(String filename) throws IOException {
        logger.info("checkFile");
        File file = new File(filename);
        if (!file.createNewFile()) endApplicationWithError("Not be able to create file, check parameter " + filename);
    }

    static void endApplicationWithError(String message) {
        System.out.println("ERROR: " + message);
        exit(1);
    }

    //endregion

    //region write file
    private static void writeCsvFile(String filename, ArrayList<String> arr) throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true));
        for (String str : arr) {
            writer.append(str);
            writer.newLine();
        }
        writer.close();
    }   //endregion

}

