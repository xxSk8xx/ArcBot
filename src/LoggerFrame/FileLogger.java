package LoggerFrame;

import Utility.Server;
import Utility.Servers;
import Utility.SystemTime;
import com.mysql.cj.xdevapi.JsonParser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

class FileLogger implements Loggable {

    private File file;

    public FileLogger() {
        try {
            this.file = new File("logger.txt");
            file.createNewFile();
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            writer.println("Initialized File Logger.");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void log(boolean success, long serverID, String message) {
        try {
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            if (success) {
                writer.println(SystemTime.getTime() + "[[SERVER ID: " + serverID + "]] {+} " + message);
            } else {
                writer.println(SystemTime.getTime() + "[[SERVER ID: " + serverID + "]] {-} " + message);
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
