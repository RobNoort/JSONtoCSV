package nl.robnoort;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import nl.robnoort.JsonToCsv;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONTokener;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;


public class JsonReader {

    //region private variables
    private static final Logger logger = LogManager.getLogger(JsonReader.class);
    private String jsonString;
    private JSONObject jsonObject;
    private String header;
    private ArrayList<String> csvData = new ArrayList<>();
    private String fileName;
    private String arrayName;
    //endregion

    // constructor
    public JsonReader(String fileName) throws IOException {

        //immutable alleen getters geen public setters
        logger.info("Starting constructor");

        this.fileName = fileName;
        this.jsonString = getStringFromFile(fileName);
        logger.info("String retrieved from: " + fileName + "\n " + this.jsonString);

        this.jsonObject = setJsonObject();
        logger.info("JsonObject created");

        // retrieve header (keys) from first row in the JSON object
        this.header = setHeader();
        logger.info("Header set: " + header);

        // retrieve all data (values) for the attributes for all rows
        setCsvData();
        logger.info("Csv Data set to : " + csvData.toString());

    }

    //region public getters
    String getHeader() {
        return header;
    }

    ArrayList getCsvData() {
        return csvData;
    }

    String getFilename() {
        return fileName;
    }

    String getJsonString() {
        return jsonString;
    }

    //endregion

    //region private setters
    private JSONObject setJsonObject() {
        return (JSONObject) new JSONTokener(jsonString).nextValue();
    }

    private String setHeader() {
        // get the name of the main array
        this.arrayName = jsonObject.names().getString(0);

        String returnHeader = "";
        JSONArray jsonArray = null;

        @SuppressWarnings("unchecked")
                Iterator<String> keys = jsonObject.keys();

        String firstKey = keys.next();
        if (jsonObject.get(firstKey) instanceof JSONArray) {
            jsonArray = (JSONArray) jsonObject.get(firstKey);
            returnHeader += getArrayKeys(jsonArray, arrayName); //, returnHeader);
        }
        else
            logger.error("JSON is not an array");

        if (jsonArray == null) logger.info("jsonArray empty");
        else logger.info("jsonArray : " + jsonArray.toString());

        return returnHeader;
    }

    private void setCsvData() {

        this.csvData.add(this.header);

        this.arrayName = jsonObject.names().getString(0).toString();

        JSONArray jsonArray = null;

        @SuppressWarnings("unchecked")  //scheelt oranje waarschuwing
                Iterator<String> keys = jsonObject.keys();
        String firstKey = keys.next();

        if (jsonObject.get(firstKey) instanceof JSONArray) {
            jsonArray = (JSONArray) jsonObject.get(firstKey);
            this.csvData.addAll(getCsvRows(jsonArray));
        } else logger.error("No JSON array");
        if (jsonArray == null) logger.error("jsonArray empty");
        else logger.info("jsonArray : " + jsonArray.toString());


    }
    //endregion

    //region helper methods

    // reading the (json) string from the given file
    private String getStringFromFile(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }

    // helper for setHeader
    private String getArrayKeys(JSONArray jsonArray, String arrayName){ //, String currentHeader) {


        StringBuilder concatHeader = new StringBuilder("");
        for (Object key : jsonArray.getJSONObject(1).keySet()) {
            if (key != null) {
                String keyString = (String) key;
                Object attr = jsonArray.getJSONObject(1).get(keyString);


                if (!(attr instanceof JSONArray))
                    concatHeader.append(arrayName + "." + (String) key + ";");
                else
                    concatHeader.append(getArrayKeys((JSONArray) attr, keyString)); //, concatHeader);
                }
            }
        return concatHeader.toString();
    }

    // helper for setCSVData, gets all objects of main array
    private ArrayList<String> getCsvRows(JSONArray jsonArray) {
        ArrayList<String> rows = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            logger.info("looping throug Array : " + i);

            String csvRow = "";
            int maxLenght = 1;

            // get ith object
            JSONObject obj = jsonArray.getJSONObject(i);

            maxLenght = getMaxlenghtOfAllAttributeArrays(obj);

            rows.addAll(getCsvRowsForJSONObject(obj, maxLenght));

        }
        return rows;

    }

    // helper for getCsvRows, gets all dat for the current row
    private ArrayList<String> getCsvRowsForJSONObject(JSONObject obj, int rows) {
        // loop maximum array length
        ArrayList<String> ret = new ArrayList<>();

        for (int i = 0; i < rows; i++) {
            StringBuilder row = new StringBuilder("");
            for (Object key : obj.keySet()) {

                if (key != null) {

                    String keyString = (String) key;
                    Object attr = obj.get(keyString);
                    if (attr instanceof JSONArray ) {
                        if(((JSONArray) attr).length()>0) {
                            for (Object Subkey : ((JSONArray) attr).getJSONObject(i).keySet()) {
                                if (((JSONArray) attr).length() > i) {
                                    row.append( ((JSONArray) attr).getJSONObject(i).getString((String) Subkey) + ";");
                                } else row.append( ";");
                            }
                        }
                        else row.append(";empty array cant determine fields;");
                        } else if (i == 0) {
                        row.append(obj.getString(keyString) + ";");
                    } else row.append(";");
                }

            }
            logger.info("row : " + row);
            ret.add(row.toString());
        }

// if row == 0 add attr of array to row 1 data
// if row > 0 add ; per attr
// if array found save rowUntilArray per array?
// if arraylength > current row
// add attr row i to row
// else add ; per attribute
// continu main array
        return ret;
    }



    // determining per row what the maximum number of rows in the subarrays is.
    // Per row this is the number of rows added
    private int getMaxlenghtOfAllAttributeArrays(JSONObject obj) {
        int maxLenght = 1;
        for (Object key : obj.keySet()) {
            if (key != null) {
                String keyString = (String) key;
                Object attr = obj.get(keyString);
                if (attr instanceof JSONArray) {
                    maxLenght = ((JSONArray) attr).length() > maxLenght ? ((JSONArray) attr).length() : maxLenght;
                    logger.info(" found subarray " + keyString + ": maxLength : " + maxLenght);
                }
            }
            logger.info("maxLength after looping: " + maxLenght);
        }
        return maxLenght;
    }

    //endregion
}

