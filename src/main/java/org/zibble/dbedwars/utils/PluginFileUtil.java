package org.zibble.dbedwars.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.version.Version;
import org.zibble.dbedwars.configuration.PluginFiles;
import org.zibble.dbedwars.utils.reflection.annotation.MethodRef;
import org.zibble.dbedwars.utils.reflection.annotation.ReflectionAnnotations;
import org.zibble.dbedwars.utils.reflection.resolver.wrapper.MethodWrapper;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class PluginFileUtil {

    @MethodRef(clazz = URLClassLoader.class, value = "addURL(java.net.URL)")
    public static final MethodWrapper<Void> ADD_URL_METHOD = null;

    public static final String LEVEL_DATA_FILE_NAME = "level.dat";
    public static final String REGION_FOLDER_NAME = "region";

    static {
        ReflectionAnnotations.INSTANCE.load(PluginFileUtil.class);
    }

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
        File file = new File(PluginFiles.Folder.ARENA_DATA_ARENACACHE, fileName + ".zip");
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
                            Files.copy(current.toPath(), zip);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            if (DBedwars.getInstance().getServerVersion().isNewerEquals(Version.v1_14_R1) && poi.isDirectory()) {

                files = poi.listFiles();
                for (File current : files) {
                    if (current.getName().endsWith(".mca")) {
                        try {
                            zip.putNextEntry(new ZipEntry("poi/" + current.getName()));
                            Files.copy(current.toPath(), zip);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            File worldFolder = new File(worldName);
            for (File current : worldFolder.listFiles()) {
                if (!current.isFile() && current.getName().startsWith("DIM")) {
                    PluginFileUtil.saveZip(current, worldFolder, zip);
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

        deleteDirectory(region);
        deleteDirectory(region_dbw);

        if (DBedwars.getInstance().getServerVersion().isNewerEquals(Version.v1_14_R1)
                && poi.exists()) {
            for (File current : poi.listFiles()) {
                if (current != null && current.getName().endsWith(".mca")) {
                    current.delete();
                }
            }
        }

        File worldFolder = new File(worldName);
        for (File current : worldFolder.listFiles()) {
            if (!current.isFile() && current.getName().startsWith("DIM")) {
                deleteDirectory(current);
            }
        }

        try {
            File f = new File(PluginFiles.Folder.ARENA_DATA_ARENACACHE, fileName + ".zip");
            ZipInputStream zip = new ZipInputStream(new FileInputStream(f));

            ZipEntry entry;
            int count;
            while ((entry = zip.getNextEntry()) != null) {

                String sub = (!entry.getName().contains("/") && !entry.getName().contains("\\") ? region_dbw.getName() + "/" : "")
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

            deleteDirectory(region);
            if (region_dbw.exists()) {
                region_dbw.renameTo(region);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveZip(File folder, File worldFolder, ZipOutputStream zip) {
        for (File current : folder.listFiles()) {
            if (current.isDirectory())
                saveZip(current, worldFolder, zip);
            else {
                try {
                    zip.putNextEntry(new ZipEntry(current.getAbsolutePath().replace(worldFolder.getAbsolutePath(), "")));
                    Files.copy(current.toPath(), zip);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean downloadFile(String url, File file) {
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void loadJar(File file) {
        try {
            ADD_URL_METHOD.invoke(DBedwars.class.getClassLoader(), file.toURI().toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkWorldFolder(File world_folder) {
        if (world_folder.isDirectory() && world_folder.exists()) {
            File dat = new File(world_folder, LEVEL_DATA_FILE_NAME);
            File region = new File(world_folder, REGION_FOLDER_NAME);

            return dat.exists() && region.exists();
        } else {
            return false;
        }
    }

    public static void deleteDirectory(File file) {
        for (File subfile : file.listFiles()) {
            if (subfile.isDirectory()) {
                deleteDirectory(subfile);
            }
            subfile.delete();
        }
    }

}