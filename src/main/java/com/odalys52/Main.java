package com.odalys52;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class Main {

    public static void main(String[] args) throws IOException, CsvValidationException {
        // Literally just calls our parser right now (....and is called for tests)
        CsvParser csvP = new CsvParser("src/Data/SEOExample.csv");
        csvP.printCsv();

        // Load the json
        /*
        1. Create instance of GSON
        2. Create a JsonReader object using FileReader
        3. Array of class instances of AuthorParser, assign data from our JsonReader
        4. foreach loop to check data
         */
        Gson gson = new Gson();
        JsonReader jread = new JsonReader(new FileReader("src/Data/authors.json"));
        AuthorParser[] authors = gson.fromJson(jread, AuthorParser[].class);

        //for (var element : authors) {
          //  System.out.println(element.getName());
        //}

        Connection conn = null;
        JsonParser jsonParser = new JsonParser();

        try {
            JsonObject jsonObject = (JsonObject) jsonParser.parse(new FileReader("src/Data/authors.json"));
            // jsonArray
            JsonArray jsonArray = (JsonArray) jsonObject.get("authors");
            BookStoreDB.createNewDatabase("BookStore.db");

            PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO AUTHOR VALUES (?,?,?)");

            for(Object author : jsonArray){
                JsonObject record = (JsonObject) author;
                String name = record.get("author_name").toString();
                String email = record.get("author_email").toString();
                String site = record.get("author_url").toString();
                preparedStatement.setString(1,name);
                preparedStatement.setString(2,email);
                preparedStatement.setString(3,site);
                preparedStatement.executeUpdate();
            }
            System.out.println("The database has been updated");
        }
        catch (FileNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }
}