package com.pepedevs.dbedwars.game.structure;

import com.pepedevs.dbedwars.api.game.struture.DirectionalStructure;
import me.Abhigya.core.util.xseries.XMaterial;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PopupTowerStructure implements DirectionalStructure {

    private Map<Block, XMaterial> northBlocks;
    private Map<Block, XMaterial> westBlocks;
    private Map<Block, XMaterial> eastBlocks;
    private Map<Block, XMaterial> southBlocks;

    private int blocksPerSecond;
    private Block centre;

    public PopupTowerStructure(Block centre) {

        this.centre = centre;

        initNorthBlocks();
        initWestBlocks();
        initEastBlocks();
        initSouthBlocks();
    }

    @Override
    public Map<Block, XMaterial> getNorthBlocks() {
        return new LinkedHashMap<>(northBlocks);
    }

    @Override
    public Map<Block, XMaterial> getWestBlocks() {
        return new LinkedHashMap<>(westBlocks);
    }

    @Override
    public Map<Block, XMaterial> getEastBlocks() {
        return new LinkedHashMap<>(eastBlocks);
    }

    @Override
    public Map<Block, XMaterial> getSouthBlocks() {
        return new LinkedHashMap<>(southBlocks);
    }

    @Override
    public Map<Block, XMaterial> getBlocks(BlockFace face) {
        return null;
    }

    @Override
    public void setNorthBlocks(Map<Block, XMaterial> blocks) {
        this.northBlocks = blocks;
    }

    @Override
    public void setWestBlocks(Map<Block, XMaterial> blocks) {
        this.westBlocks = blocks;
    }

    @Override
    public void setEastBlocks(Map<Block, XMaterial> blocks) {
        this.eastBlocks = blocks;
    }

    @Override
    public void setSouthBlocks(Map<Block, XMaterial> blocks) {
        this.southBlocks = blocks;
    }

    @Override
    public void setBlocks(BlockFace face, Map<Block, XMaterial> blocks) {
        switch (face) {
            case NORTH:
                this.northBlocks = blocks;
                return;
            case EAST:
                this.eastBlocks = blocks;
                return;
            case WEST:
                this.westBlocks = blocks;
                return;
            case SOUTH:
                this.southBlocks = blocks;
        }
    }

    @Override
    public Map<Block, XMaterial> getBlocks() {
        return getNorthBlocks();
    }

    @Override
    public void setBlocks(List<Block> blocks) {
        setNorthBlocks(blocks);
    }

    @Override
    public int getBlocksToPlacePerSecond() {
        return this.blocksPerSecond;
    }

    @Override
    public void setBlocksToPlacePerSecond(int n) {
        this.blocksPerSecond = n;
    }

    @Override
    public Block getCentre() {
        return this.centre;
    }

    @Override
    public void setCentre(Block centre) {
        this.centre = centre;
    }

    private void initNorthBlocks() {
        this.northBlocks = new LinkedHashMap<>();

        Block layerCentre = this.centre;

        northBlocks.add(layerCentre.getRelative(2, 0, -1));
        northBlocks.add(layerCentre.getRelative(2, 0, 0));
        northBlocks.add(layerCentre.getRelative(2, 0, 1));
        northBlocks.add(layerCentre.getRelative(1, 0, 0));
        northBlocks.add(layerCentre.getRelative(1, 0, -2));
        northBlocks.add(layerCentre.getRelative(1, 0, 2));
        northBlocks.add(layerCentre.getRelative(0, 0, -2));
        northBlocks.add(layerCentre.getRelative(0, 0, 2));
        northBlocks.add(layerCentre.getRelative(-1, 0, -1));
        northBlocks.add(layerCentre.getRelative(-1, 0, 1));

        layerCentre = layerCentre.getRelative(0, 1, 0);

        northBlocks.add(layerCentre.getRelative(2, 0, -1));
        northBlocks.add(layerCentre.getRelative(2, 0, 0));
        northBlocks.add(layerCentre.getRelative(2, 0, 1));
        northBlocks.add(layerCentre.getRelative(1, 0, 0));
        northBlocks.add(layerCentre.getRelative(1, 0, -2));
        northBlocks.add(layerCentre.getRelative(1, 0, 2));
        northBlocks.add(layerCentre.getRelative(0, 0, -2));
        northBlocks.add(layerCentre.getRelative(0, 0, 2));
        northBlocks.add(layerCentre.getRelative(-1, 0, -1));
        northBlocks.add(layerCentre.getRelative(-1, 0, 1));

        layerCentre = layerCentre.getRelative(0, 1, 0);

        northBlocks.add(layerCentre.getRelative(2, 0, -1));
        northBlocks.add(layerCentre.getRelative(2, 0, 0));
        northBlocks.add(layerCentre.getRelative(2, 0, 1));
        northBlocks.add(layerCentre.getRelative(1, 0, 0));
        northBlocks.add(layerCentre.getRelative(1, 0, -2));
        northBlocks.add(layerCentre.getRelative(1, 0, 2));
        northBlocks.add(layerCentre.getRelative(0, 0, -2));
        northBlocks.add(layerCentre.getRelative(0, 0, 2));
        northBlocks.add(layerCentre.getRelative(-1, 0, -1));
        northBlocks.add(layerCentre.getRelative(-1, 0, 1));

        layerCentre = layerCentre.getRelative(0, 1, 0);

        northBlocks.add(layerCentre.getRelative(2, 0, -1));
        northBlocks.add(layerCentre.getRelative(2, 0, 0));
        northBlocks.add(layerCentre.getRelative(2, 0, 1));
        northBlocks.add(layerCentre.getRelative(1, 0, 0));
        northBlocks.add(layerCentre.getRelative(1, 0, -2));
        northBlocks.add(layerCentre.getRelative(1, 0, 2));
        northBlocks.add(layerCentre.getRelative(0, 0, -2));
        northBlocks.add(layerCentre.getRelative(0, 0, 2));
        northBlocks.add(layerCentre.getRelative(-1, 0, -1));
        northBlocks.add(layerCentre.getRelative(-1, 0, 0));
        northBlocks.add(layerCentre.getRelative(-1, 0, 1));

        layerCentre = layerCentre.getRelative(0, 1, 0);

        northBlocks.add(layerCentre.getRelative(2, 0, -2));
        northBlocks.add(layerCentre.getRelative(2, 0, -1));
        northBlocks.add(layerCentre.getRelative(2, 0, 0));
        northBlocks.add(layerCentre.getRelative(2, 0, 1));
        northBlocks.add(layerCentre.getRelative(2, 0, 2));
        northBlocks.add(layerCentre.getRelative(1, 0, 0));
        northBlocks.add(layerCentre.getRelative(1, 0, -2));
        northBlocks.add(layerCentre.getRelative(1, 0, -1));
        northBlocks.add(layerCentre.getRelative(1, 0, 1));
        northBlocks.add(layerCentre.getRelative(1, 0, 2));
        northBlocks.add(layerCentre.getRelative(0, 0, -2));
        northBlocks.add(layerCentre.getRelative(0, 0, -1));
        northBlocks.add(layerCentre.getRelative(0, 0, -0));
        northBlocks.add(layerCentre.getRelative(0, 0, 1));
        northBlocks.add(layerCentre.getRelative(0, 0, 2));
        northBlocks.add(layerCentre.getRelative(-1, 0, -2));
        northBlocks.add(layerCentre.getRelative(-1, 0, -1));
        northBlocks.add(layerCentre.getRelative(-1, 0, 0));
        northBlocks.add(layerCentre.getRelative(-1, 0, 1));
        northBlocks.add(layerCentre.getRelative(-1, 0, 2));
        northBlocks.add(layerCentre.getRelative(3, 0, -2));
        northBlocks.add(layerCentre.getRelative(2, 0, -3));
        northBlocks.add(layerCentre.getRelative(3, 0, 2));
        northBlocks.add(layerCentre.getRelative(2, 0, 3));
        northBlocks.add(layerCentre.getRelative(-1, 0, -3));
        northBlocks.add(layerCentre.getRelative(-2, 0, -2));
        northBlocks.add(layerCentre.getRelative(-1, 0, 3));
        northBlocks.add(layerCentre.getRelative(-2, 0, 2));

        layerCentre = layerCentre.getRelative(0, 1, 0);

        northBlocks.add(layerCentre.getRelative(3, 0, -1));
        northBlocks.add(layerCentre.getRelative(3, 0, 0));
        northBlocks.add(layerCentre.getRelative(3, 0, 1));
        northBlocks.add(layerCentre.getRelative(1, 0, -3));
        northBlocks.add(layerCentre.getRelative(1, 0, 3));
        northBlocks.add(layerCentre.getRelative(0, 0, -3));
        northBlocks.add(layerCentre.getRelative(0, 0, 3));
        northBlocks.add(layerCentre.getRelative(-2, 0, -1));
        northBlocks.add(layerCentre.getRelative(-2, 0, 0));
        northBlocks.add(layerCentre.getRelative(-2, 0, 1));
        northBlocks.add(layerCentre.getRelative(3, 0, -2));
        northBlocks.add(layerCentre.getRelative(2, 0, -3));
        northBlocks.add(layerCentre.getRelative(3, 0, 2));
        northBlocks.add(layerCentre.getRelative(2, 0, 3));
        northBlocks.add(layerCentre.getRelative(-1, 0, -3));
        northBlocks.add(layerCentre.getRelative(-2, 0, -2));
        northBlocks.add(layerCentre.getRelative(-1, 0, 3));
        northBlocks.add(layerCentre.getRelative(-2, 0, 2));

        layerCentre = layerCentre.getRelative(0, 1, 0);

        northBlocks.add(layerCentre.getRelative(3, 0, 0));
        northBlocks.add(layerCentre.getRelative(-2, 0, 0));
        northBlocks.add(layerCentre.getRelative(3, 0, -2));
        northBlocks.add(layerCentre.getRelative(2, 0, -3));
        northBlocks.add(layerCentre.getRelative(3, 0, 2));
        northBlocks.add(layerCentre.getRelative(2, 0, 3));
        northBlocks.add(layerCentre.getRelative(-1, 0, -3));
        northBlocks.add(layerCentre.getRelative(-2, 0, -2));
        northBlocks.add(layerCentre.getRelative(-1, 0, 3));
        northBlocks.add(layerCentre.getRelative(-2, 0, 2));
    }

    private void initSouthBlocks() {
        this.southBlocks = new LinkedHashMap<>();

        Block layerCentre = this.centre;

        westBlocks.add(layerCentre.getRelative(-2, 0, 1));
        westBlocks.add(layerCentre.getRelative(-2, 0, 0));
        westBlocks.add(layerCentre.getRelative(-2, 0, -1));
        westBlocks.add(layerCentre.getRelative(-1, 0, 0));
        westBlocks.add(layerCentre.getRelative(-1, 0, 2));
        westBlocks.add(layerCentre.getRelative(-1, 0, -2));
        westBlocks.add(layerCentre.getRelative(0, 0, 2));
        westBlocks.add(layerCentre.getRelative(0, 0, -2));
        westBlocks.add(layerCentre.getRelative(1, 0, 1));
        westBlocks.add(layerCentre.getRelative(1, 0, -1));

        layerCentre = layerCentre.getRelative(0, 1, 0);

        westBlocks.add(layerCentre.getRelative(-2, 0, 1));
        westBlocks.add(layerCentre.getRelative(-2, 0, 0));
        westBlocks.add(layerCentre.getRelative(-2, 0, -1));
        westBlocks.add(layerCentre.getRelative(-1, 0, 0));
        westBlocks.add(layerCentre.getRelative(-1, 0, 2));
        westBlocks.add(layerCentre.getRelative(-1, 0, -2));
        westBlocks.add(layerCentre.getRelative(0, 0, 2));
        westBlocks.add(layerCentre.getRelative(0, 0, -2));
        westBlocks.add(layerCentre.getRelative(1, 0, 1));
        westBlocks.add(layerCentre.getRelative(1, 0, -1));

        layerCentre = layerCentre.getRelative(0, 1, 0);

        westBlocks.add(layerCentre.getRelative(-2, 0, 1));
        westBlocks.add(layerCentre.getRelative(-2, 0, 0));
        westBlocks.add(layerCentre.getRelative(-2, 0, -1));
        westBlocks.add(layerCentre.getRelative(-1, 0, 0));
        westBlocks.add(layerCentre.getRelative(-1, 0, 2));
        westBlocks.add(layerCentre.getRelative(-1, 0, -2));
        westBlocks.add(layerCentre.getRelative(0, 0, 2));
        westBlocks.add(layerCentre.getRelative(0, 0, -2));
        westBlocks.add(layerCentre.getRelative(1, 0, 1));
        westBlocks.add(layerCentre.getRelative(1, 0, -1));

        layerCentre = layerCentre.getRelative(0, 1, 0);

        westBlocks.add(layerCentre.getRelative(-2, 0, 1));
        westBlocks.add(layerCentre.getRelative(-2, 0, 0));
        westBlocks.add(layerCentre.getRelative(-2, 0, -1));
        westBlocks.add(layerCentre.getRelative(-1, 0, 0));
        westBlocks.add(layerCentre.getRelative(-1, 0, 2));
        westBlocks.add(layerCentre.getRelative(-1, 0, -2));
        westBlocks.add(layerCentre.getRelative(0, 0, 2));
        westBlocks.add(layerCentre.getRelative(0, 0, -2));
        westBlocks.add(layerCentre.getRelative(1, 0, 1));
        westBlocks.add(layerCentre.getRelative(1, 0, 0));
        westBlocks.add(layerCentre.getRelative(1, 0, -1));

        layerCentre = layerCentre.getRelative(0, 1, 0);

        westBlocks.add(layerCentre.getRelative(-2, 0, 2));
        westBlocks.add(layerCentre.getRelative(-2, 0, 1));
        westBlocks.add(layerCentre.getRelative(-2, 0, 0));
        westBlocks.add(layerCentre.getRelative(-2, 0, -1));
        westBlocks.add(layerCentre.getRelative(-2, 0, -2));
        westBlocks.add(layerCentre.getRelative(-1, 0, 0));
        westBlocks.add(layerCentre.getRelative(-1, 0, 2));
        westBlocks.add(layerCentre.getRelative(-1, 0, 1));
        westBlocks.add(layerCentre.getRelative(-1, 0, -1));
        westBlocks.add(layerCentre.getRelative(-1, 0, -2));
        westBlocks.add(layerCentre.getRelative(0, 0, 2));
        westBlocks.add(layerCentre.getRelative(0, 0, 1));
        westBlocks.add(layerCentre.getRelative(0, 0, 0));
        westBlocks.add(layerCentre.getRelative(0, 0, -1));
        westBlocks.add(layerCentre.getRelative(0, 0, -2));
        westBlocks.add(layerCentre.getRelative(1, 0, 2));
        westBlocks.add(layerCentre.getRelative(1, 0, 1));
        westBlocks.add(layerCentre.getRelative(1, 0, 0));
        westBlocks.add(layerCentre.getRelative(1, 0, -1));
        westBlocks.add(layerCentre.getRelative(1, 0, -2));
        westBlocks.add(layerCentre.getRelative(-3, 0, 2));
        westBlocks.add(layerCentre.getRelative(-2, 0, 3));
        westBlocks.add(layerCentre.getRelative(-3, 0, -2));
        westBlocks.add(layerCentre.getRelative(-2, 0, -3));
        westBlocks.add(layerCentre.getRelative(1, 0, 3));
        westBlocks.add(layerCentre.getRelative(2, 0, 2));
        westBlocks.add(layerCentre.getRelative(1, 0, -3));
        westBlocks.add(layerCentre.getRelative(2, 0, -2));

        layerCentre = layerCentre.getRelative(0, 1, 0);

        westBlocks.add(layerCentre.getRelative(-3, 0, 1));
        westBlocks.add(layerCentre.getRelative(-3, 0, 0));
        westBlocks.add(layerCentre.getRelative(-3, 0, -1));
        westBlocks.add(layerCentre.getRelative(-1, 0, 3));
        westBlocks.add(layerCentre.getRelative(-1, 0, -3));
        westBlocks.add(layerCentre.getRelative(0, 0, 3));
        westBlocks.add(layerCentre.getRelative(0, 0, -3));
        westBlocks.add(layerCentre.getRelative(2, 0, 1));
        westBlocks.add(layerCentre.getRelative(2, 0, 0));
        westBlocks.add(layerCentre.getRelative(2, 0, -1));
        westBlocks.add(layerCentre.getRelative(-3, 0, 2));
        westBlocks.add(layerCentre.getRelative(-2, 0, 3));
        westBlocks.add(layerCentre.getRelative(-3, 0, -2));
        westBlocks.add(layerCentre.getRelative(-2, 0, -3));
        westBlocks.add(layerCentre.getRelative(1, 0, 3));
        westBlocks.add(layerCentre.getRelative(2, 0, 2));
        westBlocks.add(layerCentre.getRelative(1, 0, -3));
        westBlocks.add(layerCentre.getRelative(2, 0, -2));

        layerCentre = layerCentre.getRelative(0, 1, 0);

        westBlocks.add(layerCentre.getRelative(-3, 0, 0));
        westBlocks.add(layerCentre.getRelative(2, 0, 0));
        westBlocks.add(layerCentre.getRelative(-3, 0, 2));
        westBlocks.add(layerCentre.getRelative(-2, 0, 3));
        westBlocks.add(layerCentre.getRelative(-3, 0, -2));
        westBlocks.add(layerCentre.getRelative(-2, 0, -3));
        westBlocks.add(layerCentre.getRelative(1, 0, 3));
        westBlocks.add(layerCentre.getRelative(2, 0, 2));
        westBlocks.add(layerCentre.getRelative(1, 0, -3));
        westBlocks.add(layerCentre.getRelative(2, 0, -2));
    }

    private void initEastBlocks() {
        this.eastBlocks = new LinkedHashMap<>();
    }

    private void initWestBlocks() {
        this.westBlocks = new LinkedHashMap<>();
    }
}
