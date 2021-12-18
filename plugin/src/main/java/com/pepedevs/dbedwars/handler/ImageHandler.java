package com.pepedevs.dbedwars.handler;

import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.configuration.PluginFiles;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ImageHandler {

    private final DBedwars plugin;
    private final HashMap<String, Color[][]> loadedImages;

    public ImageHandler(DBedwars plugin) {
        this.plugin = plugin;
        this.loadedImages = new HashMap<>();
    }

    public void clearCache() {
        for (File file : PluginFiles.PARTICLE_IMAGES_CACHE.getFile().listFiles()) {
            if (!file.delete())
                System.out.println(
                        "ERROR DELETING FILE "
                                + file.getName()
                                + " in graphics/particle-images-cache");
        }
    }

    public void formatAllImagesToPNG() {
        for (File file : PluginFiles.IMAGES.getFile().listFiles()) {
            if (!file.exists()) continue;
            plugin.getConfigHandler()
                    .getParticleImages()
                    .getImageSettings()
                    .forEach(
                            (key, value) -> {
                                if (file.getName()
                                        .equals(key.concat(".").concat(value.getFormat()))) {
                                    ImageUtils.downscale(
                                            file,
                                            dimensionParser(
                                                    plugin.getConfigHandler()
                                                            .getParticleImages()
                                                            .getGlobalSettings()
                                                            .getDownscaling()),
                                            new File(
                                                    PluginFiles.PARTICLE_IMAGES_CACHE.getFile(),
                                                    "fuck.png"));
                                }
                            });
        }
    }

    public void loadAllFormattedImages() {
        for (File file : PluginFiles.PARTICLE_IMAGES_CACHE.getFile().listFiles()) {
            loadedImages.put(file.getName(), ImageUtils.colorParser(file));
        }
    }

    public HashMap<String, Color[][]> getLoadedImages() {
        return loadedImages;
    }

    private Dimension dimensionParser(String s) {
        return new Dimension(
                (int) Double.parseDouble(s.split("x")[0]),
                (int) Double.parseDouble(s.split("x")[1]));
    }

    public static class ImageUtils {

        public static void downscale(File image, Dimension dimension, File saveLocation) {
            if (!image.exists()) return;

            BufferedImage initialImage = null;
            BufferedImage resizedImage = null;
            Graphics graphics = null;

            try {
                boolean cache = ImageIO.getUseCache();
                ImageIO.setUseCache(false);
                initialImage = ImageIO.read(image);
                resizedImage =
                        new BufferedImage(
                                dimension.width, dimension.height, BufferedImage.TYPE_INT_ARGB);
                graphics = resizedImage.createGraphics();
                graphics.drawImage(initialImage, 0, 0, dimension.width, dimension.height, null);
                ImageIO.write(resizedImage, "png", saveLocation);
                ImageIO.setUseCache(cache);
            } catch (IOException e) {
                System.out.println("ERROR IN CONVERTING " + image.getName() + " to .png format");
                e.printStackTrace();
            } finally {
                if (graphics != null) graphics.dispose();
                if (initialImage != null) initialImage.flush();
                if (resizedImage != null) resizedImage.flush();
            }
        }

        public static void formatToPNG(File image, File saveLocation) {
            BufferedImage image1 = null;
            try {
                image1 = ImageIO.read(image);
                downscale(
                        image, new Dimension(image1.getWidth(), image1.getHeight()), saveLocation);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (image1 != null) image1.flush();
            }
        }

        public static Color[][] colorParser(File pngImage) {
            BufferedImage image = null;
            Color[][] colors = new Color[0][];
            try {
                image = ImageIO.read(pngImage);
                int width = image.getWidth();
                int height = image.getHeight();
                colors = new Color[width][height];
                for (int i = 0; i < width; i++) {
                    for (int i1 = 0; i1 < height; i1++) {
                        if (!isTransparent(image.getRGB(i, i1)))
                            colors[i][i1] = new Color(image.getRGB(i, i1));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (image != null) image.flush();
            }
            return colors;
        }

        private static boolean isTransparent(int rgb) {
            return ((rgb >> 24) == 0x00);
        }
    }
}
