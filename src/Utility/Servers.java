package Utility;

import Utility.Model.Server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class Servers {
    public static HashMap<Long, Server> activeServers = new HashMap<>();

    public static void save() throws SQLException {
        for (Map.Entry<Long, Server> e : activeServers.entrySet()) {
            Server s = e.getValue();
            if (s.drop && inSQL(s.getID())) {
                delete(s.getID());
                continue;
            } else if (s.drop && !(inSQL(s.getID()))) {
                continue;
            }
            saveServer(s);
        }
    }

    public static void delete(long id) throws SQLException {
        PreparedStatement stmt = Settings.SQL_CONNECTION.prepareStatement("DELETE FROM servers WHERE id=?");
            stmt.setLong(1, id);
        stmt.execute();
        stmt.close();
    }

    private static boolean inSQL(long id) throws SQLException {
        PreparedStatement stmt = Settings.SQL_CONNECTION.prepareStatement("SELECT * FROM servers WHERE id=?");
            stmt.setLong(1, id);
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }

    public static void saveServer(Server server) throws SQLException {
        PreparedStatement stmt = Settings.SQL_CONNECTION.prepareStatement(
                "SELECT * FROM servers WHERE id=?");
            stmt.setLong(1, server.getID());
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            PreparedStatement statement = Settings.SQL_CONNECTION.prepareStatement(
                    "UPDATE servers set server=? WHERE id=?");
                statement.setObject(1, server);
                statement.setLong(2, server.getID());
            statement.execute();
            statement.close();
        } else {
            PreparedStatement statement = Settings.SQL_CONNECTION.prepareStatement(
                    "INSERT INTO servers (id, server) VALUES (?,?);");
                statement.setLong(1, server.getID());
                statement.setObject(2, server);
            statement.execute();
            statement.close();
        }
        stmt.close();
    }

    public static void load() throws SQLException, IOException, ClassNotFoundException {
        activeServers.clear();
        Statement stmt = Settings.SQL_CONNECTION.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM servers");
        while (rs.next()) {
            byte[] byteArray = rs.getBytes("server");
            ObjectInputStream objectIn = new ObjectInputStream(new ByteArrayInputStream(byteArray));
            Server thisServer = (Server)objectIn.readObject();
            activeServers.put(thisServer.getID(), thisServer);
        }
        System.out.println("Servers initialized.");
    }
}
