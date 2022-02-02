package org.wangxyper.ReturnSpawn;

import org.bukkit.Bukkit;

import java.io.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
public class Util {
    public static ThreadPoolExecutor executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),Integer.MAX_VALUE,Long.MAX_VALUE, TimeUnit.MILLISECONDS,new LinkedBlockingDeque<>());
    public static ScheduledThreadPoolExecutor scheduledExecutor = new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors());
    public static InputStream getLocalResource(String fileName){
        return Util.class.getClassLoader().getResourceAsStream(fileName);
    }
    public static void saveResource(String resourcePath, boolean replace) throws IOException {
        if (resourcePath == null || resourcePath.equals("")) {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }
        resourcePath = resourcePath.replace('\\', '/');
        InputStream in = Util.class.getClassLoader().getResourceAsStream("dataRPG.yml");
        if (in == null) {
            throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in /");
        }
        File outFile = new File( resourcePath);
        try {
            if (!outFile.exists() || replace) {
                OutputStream out = new FileOutputStream(outFile);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            } else {
                Bukkit.getLogger().log(Level.WARNING, "Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
            }
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save " + outFile.getName() + " to " + outFile, ex);
        }
    }
}
