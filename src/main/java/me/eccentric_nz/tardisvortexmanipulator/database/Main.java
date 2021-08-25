/*
 * Copyright (C) 2021 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardisvortexmanipulator.database;

import java.io.*;
import java.sql.*;

/**
 * @author eccentric_nz
 */
public class Main {

    public static void main(String[] args) {
        UserInterface.main(args);
    }

    /**
     * Reads an SQLite database and dumps the records as SQL statements to a file.
     *
     * @param console the output window of the tool
     * @param sqlite  the SQLite file to migrate
     * @param mySql   the SQL file to write to
     * @param prefix  the desired table prefix
     */
    public static void process(PrintWriter console, File sqlite, File mySql, String prefix) throws IOException {
        if (!sqlite.canRead()) {
            console.println("Specified original file " + sqlite + " does not exist or cannot be read!");
            return;
        }
        if (mySql.exists()) {
            console.println("Specified output file " + mySql + " exists, please remove it before running this program!");
            return;
        }
        if (!mySql.createNewFile()) {
            console.println("Could not create specified output file " + mySql + " please ensure that it is in a valid directory which can be written to.");
            return;
        }
        if (!prefix.isEmpty()) {
            console.println("***** Using prefix: " + prefix);
        }
        console.println("***** Starting conversion process, please wait.");
        Connection connection = null;
        try {
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:" + sqlite.getCanonicalPath());
            } catch (ClassNotFoundException e) {
                console.println("***** ERROR: SQLite JDBC driver not found!");
                return;
            }
            if (connection == null) {
                console.println("***** ERROR: Could not connect to SQLite database!");
                return;
            }
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(mySql, false))) {
                bufferedWriter.write("-- TARDISVortexManipulator SQL Dump");
                bufferedWriter.newLine();
                bufferedWriter.newLine();
                bufferedWriter.write("SET SQL_MODE = \"NO_AUTO_VALUE_ON_ZERO\";");
                bufferedWriter.newLine();
                bufferedWriter.newLine();
                Statement statement = connection.createStatement();
                int i = 0;
                for (SQL.TABLE table : SQL.TABLE.values()) {
                    console.println("Reading and writing " + table.toString() + " table");
                    bufferedWriter.write(SQL.SEPARATOR);
                    bufferedWriter.newLine();
                    bufferedWriter.newLine();
                    bufferedWriter.write(SQL.COMMENT);
                    bufferedWriter.newLine();
                    bufferedWriter.write(SQL.STRUCTURE + table);
                    bufferedWriter.newLine();
                    bufferedWriter.write(SQL.COMMENT);
                    bufferedWriter.newLine();
                    bufferedWriter.newLine();
                    bufferedWriter.write(String.format(SQL.CREATES.get(i), prefix));
                    bufferedWriter.newLine();
                    bufferedWriter.newLine();
                    String countMessage = "SELECT COUNT(*) AS count FROM " + table;
                    ResultSet resultSetCount = statement.executeQuery(countMessage);
                    if (resultSetCount.isBeforeFirst()) {
                        resultSetCount.next();
                        int count = resultSetCount.getInt("count");
                        console.println("Found " + count + " " + table + " records");
                        String query = "SELECT * FROM " + table;
                        ResultSet resultSet = statement.executeQuery(query);
                        if (resultSet.isBeforeFirst()) {
                            int b = 1; // TODO Rename this.
                            bufferedWriter.write(SQL.COMMENT);
                            bufferedWriter.newLine();
                            bufferedWriter.write(SQL.DUMP + table);
                            bufferedWriter.newLine();
                            bufferedWriter.write(SQL.COMMENT);
                            bufferedWriter.newLine();
                            bufferedWriter.newLine();
                            bufferedWriter.write(String.format(SQL.INSERTS.get(i), prefix));
                            bufferedWriter.newLine();
                            while (resultSet.next()) {
                                String end = (b == count) ? ";" : ",";
                                b++;
                                String s;
                                switch (table) {
                                    case beacons -> {
                                        s = String.format(SQL.VALUES.get(i), resultSet.getInt("beacon_id"), resultSet.getString("uuid"), resultSet.getString("location"), resultSet.getString("block_type"), resultSet.getInt("data")) + end;
                                        bufferedWriter.write(s);
                                    }
                                    case manipulator -> {
                                        s = String.format(SQL.VALUES.get(i), resultSet.getString("uuid"), resultSet.getInt("tachyon_level")) + end;
                                        bufferedWriter.write(s);
                                    }
                                    case messages -> {
                                        s = String.format(SQL.VALUES.get(i), resultSet.getInt("message_id"), resultSet.getString("uuid_to"), resultSet.getString("uuid_from"), resultSet.getString("message"), resultSet.getString("date"), resultSet.getInt("read")) + end;
                                        bufferedWriter.write(s);
                                    }
                                    case saves -> {
                                        s = String.format(SQL.VALUES.get(i), resultSet.getInt("save_id"), resultSet.getString("uuid"), resultSet.getString("save_name"), resultSet.getString("world"), resultSet.getFloat("x"), resultSet.getFloat("y"), resultSet.getFloat("z"), resultSet.getFloat("yaw"), resultSet.getFloat("pitch")) + end;
                                        bufferedWriter.write(s);
                                    }
                                    default -> {
                                    }
                                }
                                bufferedWriter.newLine();
                            }
                        }
                    }
                    i++;
                }
                bufferedWriter.write(SQL.SEPARATOR);
            }
        } catch (SQLException e) {
            console.println("***** SQL ERROR: " + e.getMessage());
            return;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    console.println("***** SQL ERROR: " + e.getMessage());
                }
            }
        }
        console.println("***** Your SQLite database has been converted!");
    }
}
