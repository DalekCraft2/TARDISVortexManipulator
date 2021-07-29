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

import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulatorPlugin;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.sql.*;
import java.util.MissingFormatArgumentException;

/**
 * @author eccentric_nz
 */
public class Converter implements Runnable {

    private final TARDISVortexManipulatorPlugin plugin;
    private final TVMDatabase service = TVMDatabase.getInstance();
    private final Connection connection = service.getConnection();
    private final Connection sqliteConnection;
    private final String prefix;
    private final CommandSender sender;

    /**
     * Reads an SQLite database and transfers the records to a MySQL database.
     *
     * @param plugin the output window of the tool
     * @param sender the person who ran the command
     */
    public Converter(TARDISVortexManipulatorPlugin plugin, CommandSender sender) throws Exception {
        this.plugin = plugin;
        this.sender = sender;
        prefix = this.plugin.getPrefix();
        sqliteConnection = getSqliteConnection();
    }

    @Override
    public void run() {
        if (plugin.getConfig().getString("storage.database").equals("sqlite")) {
            sender.sendMessage(plugin.getMessagePrefix() + "You need to set the database provider to 'mysql' in the config!");
            return;
        }
        if (!prefix.isEmpty()) {
            sender.sendMessage(plugin.getMessagePrefix() + "***** Using prefix: " + prefix);
        }
        sender.sendMessage(plugin.getMessagePrefix() + "Starting conversion process, please wait. This may cause the server to become unresponsive!");
        try {
            Statement readStatement = sqliteConnection.createStatement();
            Statement writeStatement = connection.createStatement();
            connection.setAutoCommit(false);
            int i = 0;
            for (SQL.TABLE table : SQL.TABLE.values()) {
                sender.sendMessage(plugin.getMessagePrefix() + "Reading and writing " + table.toString() + " table");
                String count = "SELECT COUNT(*) AS count FROM " + table;
                ResultSet resultSetCount = readStatement.executeQuery(count);
                if (resultSetCount.isBeforeFirst()) {
                    resultSetCount.next();
                    int c = resultSetCount.getInt("count"); // TODO Rename this variable, and the previous "count".
                    sender.sendMessage(plugin.getMessagePrefix() + "Found " + c + " " + table + " records");
                    String query = "SELECT * FROM " + table;
                    ResultSet resultSet = readStatement.executeQuery(query);
                    if (resultSet.isBeforeFirst()) {
                        int b = 1;
                        StringBuilder stringBuilder = new StringBuilder();
                        try {
                            stringBuilder.append(String.format(SQL.INSERTS.get(i), prefix));
                        } catch (MissingFormatArgumentException missingFormatArgumentException) {
                            sender.sendMessage(plugin.getMessagePrefix() + "INSERT " + table);
                        }
                        while (resultSet.next()) {
                            String end = (b == c) ? ";" : ",";
                            b++;
                            String string;
                            try {
                                switch (table) {
                                    case beacons -> {
                                        string = String.format(SQL.VALUES.get(i), resultSet.getInt("beacon_id"), resultSet.getString("uuid"), resultSet.getString("location"), resultSet.getString("block_type"), resultSet.getInt("data")) + end;
                                        stringBuilder.append(string);
                                    }
                                    case manipulator -> {
                                        string = String.format(SQL.VALUES.get(i), resultSet.getString("uuid"), resultSet.getInt("tachyon_level")) + end;
                                        stringBuilder.append(string);
                                    }
                                    case messages -> {
                                        string = String.format(SQL.VALUES.get(i), resultSet.getInt("message_id"), resultSet.getString("uuid_to"), resultSet.getString("uuid_from"), resultSet.getString("message"), resultSet.getString("date"), resultSet.getInt("read")) + end;
                                        stringBuilder.append(string);
                                    }
                                    case saves -> {
                                        string = String.format(SQL.VALUES.get(i), resultSet.getInt("save_id"), resultSet.getString("uuid"), resultSet.getString("save_name"), resultSet.getString("world"), resultSet.getFloat("x"), resultSet.getFloat("y"), resultSet.getFloat("z"), resultSet.getFloat("yaw"), resultSet.getFloat("pitch")) + end;
                                        stringBuilder.append(string);
                                    }
                                    default -> {
                                    }
                                }
                            } catch (MissingFormatArgumentException e) {
                                sender.sendMessage(plugin.getMessagePrefix() + "VALUES " + table);
                            }
                        }
                        String insert = stringBuilder.toString();
                        writeStatement.addBatch(insert);
                    }
                }
                i++;
            }
            writeStatement.executeBatch();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            sender.sendMessage(plugin.getMessagePrefix() + "***** SQL ERROR: " + e.getMessage());
            e.printStackTrace();
            return;
        } finally {
            if (sqliteConnection != null) {
                try {
                    sqliteConnection.close();
                } catch (SQLException e) {
                    sender.sendMessage(plugin.getMessagePrefix() + "***** DATABASE CLOSE ERROR: " + e.getMessage());
                }
            }
        }
        sender.sendMessage(plugin.getMessagePrefix() + "***** Your SQLite database has been converted to MySQL!");
    }

    public Connection getSqliteConnection() throws Exception {
        try {
            Class.forName("org.sqlite.JDBC");
            String path = plugin.getDataFolder() + File.separator + "TVM.db";
            return DriverManager.getConnection("jdbc:sqlite:" + path);
        } catch (ClassNotFoundException ignore) {
            return null;
        }
    }
}
