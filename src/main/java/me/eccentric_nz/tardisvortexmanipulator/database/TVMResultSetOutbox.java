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
import me.eccentric_nz.tardisvortexmanipulator.storage.TVMMessage;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TVMResultSetOutbox {

    private final TVMDatabase service = TVMDatabase.getInstance();
    private final Connection connection = service.getConnection();
    private final TARDISVortexManipulatorPlugin plugin;
    private final String where;
    private final int start, limit;
    private final List<TVMMessage> mail = new ArrayList<>();
    private final String prefix;

    public TVMResultSetOutbox(TARDISVortexManipulatorPlugin plugin, String where, int start, int limit) {
        this.plugin = plugin;
        this.where = where;
        this.start = start;
        this.limit = limit;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the messages table. This method builds an SQL query string from the parameters
     * supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = String.format("SELECT * FROM " + prefix + "messages WHERE uuid_from = ? ORDER BY date DESC LIMIT %d, %d", start, start + limit);
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, where);
            resultSet = statement.executeQuery();
            if (resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    TVMMessage tvmMessage = new TVMMessage();
                    tvmMessage.setId(resultSet.getInt("message_id"));
                    tvmMessage.setWho(UUID.fromString(resultSet.getString("uuid_to")));
                    tvmMessage.setMessage(resultSet.getString("message"));
                    tvmMessage.setDate(getFormattedDate(resultSet.getLong("date")));
                    mail.add(tvmMessage);
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("Outbox error for messages table! " + e.getMessage());
            return false;
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing messages table! " + e.getMessage());
            }
        }
        return true;
    }

    private String getFormattedDate(long milliseconds) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(plugin.getConfig().getString("date_format"));
        Date date = new Date(milliseconds);
        return simpleDateFormat.format(date);
    }

    public List<TVMMessage> getMail() {
        return mail;
    }
}
