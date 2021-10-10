package me.abhigya.dbedwars.utils;

import me.Abhigya.core.particle.particlelib.ParticleBuilder;
import me.Abhigya.core.particle.particlelib.ParticleEffect;
import me.abhigya.dbedwars.DBedwars;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ImageDisplayer {

    BufferedImage image;

    public ImageDisplayer(BufferedImage unCompressedImage) throws IOException {
        this.image = unCompressedImage;
    }

    public void start(Location topLeft){
        new BukkitRunnable() {
            @Override
            public void run() {
                for (int x = 0; x <50 ; x++) {
                    for (int y = 0; y < 50; y++) {
                        Color color = new Color(image.getRGB(x,y));
                        new ParticleBuilder(ParticleEffect.REDSTONE,topLeft.clone().add(x/10.0,-y/10.0,0)).setColor(color).setSpeed(0).setAmount(1).display();
                    }
                }
            }
        }.runTaskTimerAsynchronously(DBedwars.getInstance(),0,1);
    }

    public static BufferedImage resize(final URL imageurl, final Dimension size) throws IOException {
        BufferedImage image = ImageIO.read(imageurl);
        BufferedImage resized = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = resized.createGraphics();
        g.drawImage(image, 0, 0, size.width, size.height, null);
        g.dispose();
        return resized;
    }

    public static BufferedImage getImage(URL imageurl) throws IOException {
        boolean usecache = ImageIO.getUseCache();
        ImageIO.setUseCache(false);
        BufferedImage image = resize(imageurl, new Dimension(50, 50));
        ImageIO.setUseCache(usecache);
        return image;
    }

}
