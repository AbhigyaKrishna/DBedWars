package me.abhigya.dbedwars.utils;

import me.Abhigya.core.particle.particlelib.ParticleBuilder;
import me.Abhigya.core.particle.particlelib.ParticleEffect;
import me.Abhigya.core.util.tasks.Workload;
import me.abhigya.dbedwars.DBedwars;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BetterImageDisplayer {

    private final BufferedImage resizedImage;
    private int amount;
    private float particleSpeed;
    private Dimension dimensions;

    public BetterImageDisplayer(File pngImage, Dimension resizedDimensions) throws IOException {
        boolean usecache = ImageIO.getUseCache();
        BufferedImage initialImage = ImageIO.read(pngImage);
        ImageIO.setUseCache(false);
        resizedImage = new BufferedImage(resizedDimensions.width,resizedDimensions.height,BufferedImage.TYPE_INT_ARGB);
        Graphics g = resizedImage.createGraphics();
        g.drawImage(initialImage,0,0,resizedDimensions.width,resizedDimensions.height,null);
        g.dispose();
        ImageIO.setUseCache(usecache);
        amount = 1;
        particleSpeed = 0;
        this.dimensions = resizedDimensions;
    }

    public BetterImageDisplayer setParticleAmount(int amount){
        this.amount = amount;
        return this;
    }

    public BetterImageDisplayer setParticleSpeed(float particleSpeed){
        this.particleSpeed = particleSpeed;
        return this;
    }

    public void start(DBedwars plugin, int delay, int period, Location topLeft, double distanceMultiplier) {
        plugin.getThreadHandler().addAsyncWork(new Workload(){
            @Override
            public void compute() {
                for (int x = 0; x < dimensions.width; x++) {
                    for (int y = 0; y < dimensions.height; y++) {
                        new ParticleBuilder(ParticleEffect.REDSTONE, topLeft.clone().add(x * distanceMultiplier, -y * distanceMultiplier, 0))
                                .setAmount(amount)
                                .setSpeed(particleSpeed)
                                .setColor(new Color(resizedImage.getRGB(x, y)))
                                .display();
                    }
                }
            }

            @Override
            public boolean reSchedule() {
                return true;
            }

            @Override
            public boolean shouldExecute() {
                return true;
            }
        });
    }
}
