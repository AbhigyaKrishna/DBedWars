package me.abhigya.dbedwars.utils;

import me.Abhigya.core.util.file.FileUtils;
import me.Abhigya.core.util.server.Version;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.configuration.PluginFiles;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class PluginFileUtils {

    public static FileConfiguration set(File file, String key, Object value) {
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        configuration.set(key, value);
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return configuration;
    }

    public static boolean saveWorldRegions(String worldName, String fileName) {
        File file = new File(PluginFiles.ARENA_DATA_ARENACACHE.getFile(), fileName + ".zip");
        if (file.exists()) file.delete();

        File region = new File(worldName, "region/");
        File poi = new File(worldName, "poi/");

        try {
            ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(file));

            File[] files = region.listFiles();
            if (region.isDirectory()) {

                for (File current : files) {
                    if (current.getName().endsWith(".mca")) {
                        try {
                            zip.putNextEntry(new ZipEntry(current.getName()));
                            FileUtils.copyFile(current, zip);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            if (DBedwars.getInstance().getServerVersion().isNewerEquals(Version.v1_14_R1)
                    && poi.isDirectory()) {

                files = poi.listFiles();
                for (File current : files) {
                    if (current.getName().endsWith(".mca")) {
                        try {
                            zip.putNextEntry(new ZipEntry("poi/" + current.getName()));
                            FileUtils.copyFile(current, zip);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            File worldFolder = new File(worldName);
            for (File current : worldFolder.listFiles()) {
                if (!current.isFile() && current.getName().startsWith("DIM")) {
                    PluginFileUtils.saveZip(current, worldFolder, zip);
                }
            }

            zip.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void copyWorldRegion(String worldName, String fileName) {
        File region = new File(worldName, "region/");
        File region_dbw = new File(worldName, "region_dbw/");
        File poi = new File(worldName, "poi/");

        try {
            FileUtils.deleteDirectory(region);
            FileUtils.deleteDirectory(region_dbw);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (DBedwars.getInstance().getServerVersion().isNewerEquals(Version.v1_14_R1)
                && poi.exists()) {
            for (File current : poi.listFiles()) {
                if (current != null
                        && current.getName() != null
                        && current.getName().endsWith(".mca")) {
                    current.delete();
                }
            }
        }

        File worldFolder = new File(worldName);
        for (File current : worldFolder.listFiles()) {
            if (!current.isFile() && current.getName().startsWith("DIM")) {
                try {
                    FileUtils.deleteDirectory(current);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            File f = new File(PluginFiles.ARENA_DATA_ARENACACHE.getFile(), fileName + ".zip");
            ZipInputStream zip = new ZipInputStream(new FileInputStream(f));

            ZipEntry entry;
            int count;
            while ((entry = zip.getNextEntry()) != null) {

                String sub =
                        (!entry.getName().contains("/") && !entry.getName().contains("\\")
                                        ? region_dbw.getName() + "/"
                                        : "")
                                + entry.getName();
                File subFile = new File(worldFolder, sub);
                if (subFile.exists()) {
                    subFile.delete();
                }
                subFile.getParentFile().mkdirs();
                byte[] data = new byte[1024];
                FileOutputStream outputStream = new FileOutputStream(subFile);
                BufferedOutputStream buffer = new BufferedOutputStream(outputStream, 1024);
                while ((count = zip.read(data, 0, 1024)) != -1) {
                    buffer.write(data, 0, count);
                }
                buffer.flush();
                buffer.close();
                outputStream.close();
            }

            FileUtils.deleteDirectory(region);
            if (region_dbw.exists()) {
                region_dbw.renameTo(region);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveZip(File folder, File worldFolder, ZipOutputStream zip) {
        for (File current : folder.listFiles()) {
            if (current.isDirectory()) saveZip(current, worldFolder, zip);
            else {
                try {
                    zip.putNextEntry(
                            new ZipEntry(
                                    current.getAbsolutePath()
                                            .replace(worldFolder.getAbsolutePath(), "")));
                    FileUtils.copyFile(current, zip);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
