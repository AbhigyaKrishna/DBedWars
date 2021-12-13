package com.pepedevs.dbedwars.game.structure;

import com.pepedevs.dbedwars.api.game.struture.DirectionalStructure;
import com.pepedevs.corelib.utils.xseries.XMaterial;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.LinkedHashMap;
import java.util.Map;

public class PopupTowerStructure implements DirectionalStructure {

    private Map<Block, XMaterial> northBlocks;
    private Map<Block, XMaterial> westBlocks;
    private Map<Block, XMaterial> eastBlocks;
    private Map<Block, XMaterial> southBlocks;

    private int blocksPerSecond;
    private Block centre;
    private XMaterial baseMat;

    public PopupTowerStructure(Block centre, XMaterial baseMat) {

        this.centre = centre;
        this.baseMat = baseMat;

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
    public void setBlocks(Map<Block, XMaterial> blocks) {
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
        this.northBlocks = PopupTowerBlocks.getNorthBlocks(this.centre, this.baseMat);
    }

    private void initSouthBlocks() {
        this.southBlocks = PopupTowerBlocks.getSouthBlocks(this.centre, this.baseMat);
    }

    private void initEastBlocks() {
        this.eastBlocks = PopupTowerBlocks.getEastBlocks(this.centre, this.baseMat);
    }

    private void initWestBlocks() {
        this.westBlocks = PopupTowerBlocks.getWestBlocks(this.centre, this.baseMat);
    }

    public XMaterial getBaseMat() {
        return baseMat;
    }

    public void setBaseMat(XMaterial baseMat) {
        this.baseMat = baseMat;
    }

    private static class PopupTowerBlocks {

        private static Map<Block, XMaterial> getEastBlocks(Block centre, XMaterial material) {
            Map<Block, XMaterial> blocks = new LinkedHashMap<>();

            Block layerCentre = centre;

            blocks.put(layerCentre.getRelative(2, 0, -1), material);
            blocks.put(layerCentre.getRelative(2, 0, 0), material);
            blocks.put(layerCentre.getRelative(2, 0, 1), material);
            blocks.put(layerCentre.getRelative(1, 0, 0), XMaterial.LADDER);
            blocks.put(layerCentre.getRelative(1, 0, -2), material);
            blocks.put(layerCentre.getRelative(1, 0, 2), material);
            blocks.put(layerCentre.getRelative(0, 0, -2), material);
            blocks.put(layerCentre.getRelative(0, 0, 2), material);
            blocks.put(layerCentre.getRelative(-1, 0, -1), material);
            blocks.put(layerCentre.getRelative(-1, 0, 1), material);

            layerCentre = layerCentre.getRelative(0, 1, 0);

            blocks.put(layerCentre.getRelative(2, 0, -1), material);
            blocks.put(layerCentre.getRelative(2, 0, 0), material);
            blocks.put(layerCentre.getRelative(2, 0, 1), material);
            blocks.put(layerCentre.getRelative(1, 0, 0), XMaterial.LADDER);
            blocks.put(layerCentre.getRelative(1, 0, -2), material);
            blocks.put(layerCentre.getRelative(1, 0, 2), material);
            blocks.put(layerCentre.getRelative(0, 0, -2), material);
            blocks.put(layerCentre.getRelative(0, 0, 2), material);
            blocks.put(layerCentre.getRelative(-1, 0, -1), material);
            blocks.put(layerCentre.getRelative(-1, 0, 1), material);

            layerCentre = layerCentre.getRelative(0, 1, 0);

            blocks.put(layerCentre.getRelative(2, 0, -1), material);
            blocks.put(layerCentre.getRelative(2, 0, 0), material);
            blocks.put(layerCentre.getRelative(2, 0, 1), material);
            blocks.put(layerCentre.getRelative(1, 0, 0), XMaterial.LADDER);
            blocks.put(layerCentre.getRelative(1, 0, -2), material);
            blocks.put(layerCentre.getRelative(1, 0, 2), material);
            blocks.put(layerCentre.getRelative(0, 0, -2), material);
            blocks.put(layerCentre.getRelative(0, 0, 2), material);
            blocks.put(layerCentre.getRelative(-1, 0, -1), material);
            blocks.put(layerCentre.getRelative(-1, 0, 1), material);

            layerCentre = layerCentre.getRelative(0, 1, 0);

            blocks.put(layerCentre.getRelative(2, 0, -1), material);
            blocks.put(layerCentre.getRelative(2, 0, 0), material);
            blocks.put(layerCentre.getRelative(2, 0, 1), material);
            blocks.put(layerCentre.getRelative(1, 0, 0), XMaterial.LADDER);
            blocks.put(layerCentre.getRelative(1, 0, -2), material);
            blocks.put(layerCentre.getRelative(1, 0, 2), material);
            blocks.put(layerCentre.getRelative(0, 0, -2), material);
            blocks.put(layerCentre.getRelative(0, 0, 2), material);
            blocks.put(layerCentre.getRelative(-1, 0, -1), material);
            blocks.put(layerCentre.getRelative(-1, 0, 0), material);
            blocks.put(layerCentre.getRelative(-1, 0, 1), material);

            layerCentre = layerCentre.getRelative(0, 1, 0);

            blocks.put(layerCentre.getRelative(2, 0, -2), material);
            blocks.put(layerCentre.getRelative(2, 0, -1), material);
            blocks.put(layerCentre.getRelative(2, 0, 0), material);
            blocks.put(layerCentre.getRelative(2, 0, 1), material);
            blocks.put(layerCentre.getRelative(2, 0, 2), material);
            blocks.put(layerCentre.getRelative(1, 0, 0), XMaterial.LADDER);
            blocks.put(layerCentre.getRelative(1, 0, -2), material);
            blocks.put(layerCentre.getRelative(1, 0, -1), material);
            blocks.put(layerCentre.getRelative(1, 0, 1), material);
            blocks.put(layerCentre.getRelative(1, 0, 2), material);
            blocks.put(layerCentre.getRelative(0, 0, -2), material);
            blocks.put(layerCentre.getRelative(0, 0, -1), material);
            blocks.put(layerCentre.getRelative(0, 0, -0), material);
            blocks.put(layerCentre.getRelative(0, 0, 1), material);
            blocks.put(layerCentre.getRelative(0, 0, 2), material);
            blocks.put(layerCentre.getRelative(-1, 0, -2), material);
            blocks.put(layerCentre.getRelative(-1, 0, -1), material);
            blocks.put(layerCentre.getRelative(-1, 0, 0), material);
            blocks.put(layerCentre.getRelative(-1, 0, 1), material);
            blocks.put(layerCentre.getRelative(-1, 0, 2), material);
            blocks.put(layerCentre.getRelative(3, 0, -2), material);
            blocks.put(layerCentre.getRelative(2, 0, -3), material);
            blocks.put(layerCentre.getRelative(3, 0, 2), material);
            blocks.put(layerCentre.getRelative(2, 0, 3), material);
            blocks.put(layerCentre.getRelative(-1, 0, -3), material);
            blocks.put(layerCentre.getRelative(-2, 0, -2), material);
            blocks.put(layerCentre.getRelative(-1, 0, 3), material);
            blocks.put(layerCentre.getRelative(-2, 0, 2), material);

            layerCentre = layerCentre.getRelative(0, 1, 0);

            blocks.put(layerCentre.getRelative(3, 0, -1), material);
            blocks.put(layerCentre.getRelative(3, 0, 0), material);
            blocks.put(layerCentre.getRelative(3, 0, 1), material);
            blocks.put(layerCentre.getRelative(1, 0, -3), material);
            blocks.put(layerCentre.getRelative(1, 0, 3), material);
            blocks.put(layerCentre.getRelative(0, 0, -3), material);
            blocks.put(layerCentre.getRelative(0, 0, 3), material);
            blocks.put(layerCentre.getRelative(-2, 0, -1), material);
            blocks.put(layerCentre.getRelative(-2, 0, 0), material);
            blocks.put(layerCentre.getRelative(-2, 0, 1), material);
            blocks.put(layerCentre.getRelative(3, 0, -2), material);
            blocks.put(layerCentre.getRelative(2, 0, -3), material);
            blocks.put(layerCentre.getRelative(3, 0, 2), material);
            blocks.put(layerCentre.getRelative(2, 0, 3), material);
            blocks.put(layerCentre.getRelative(-1, 0, -3), material);
            blocks.put(layerCentre.getRelative(-2, 0, -2), material);
            blocks.put(layerCentre.getRelative(-1, 0, 3), material);
            blocks.put(layerCentre.getRelative(-2, 0, 2), material);

            layerCentre = layerCentre.getRelative(0, 1, 0);

            blocks.put(layerCentre.getRelative(3, 0, 0), material);
            blocks.put(layerCentre.getRelative(-2, 0, 0), material);
            blocks.put(layerCentre.getRelative(3, 0, -2), material);
            blocks.put(layerCentre.getRelative(2, 0, -3), material);
            blocks.put(layerCentre.getRelative(3, 0, 2), material);
            blocks.put(layerCentre.getRelative(2, 0, 3), material);
            blocks.put(layerCentre.getRelative(-1, 0, -3), material);
            blocks.put(layerCentre.getRelative(-2, 0, -2), material);
            blocks.put(layerCentre.getRelative(-1, 0, 3), material);
            blocks.put(layerCentre.getRelative(-2, 0, 2), material);

            return blocks;
        }

        private static Map<Block, XMaterial> getWestBlocks(Block centre, XMaterial material) {

            Map<Block, XMaterial> blocks = new LinkedHashMap<>();

            Block layerCentre = centre;

            blocks.put(layerCentre.getRelative(-2, 0, 1), material);
            blocks.put(layerCentre.getRelative(-2, 0, 0), material);
            blocks.put(layerCentre.getRelative(-2, 0, -1), material);
            blocks.put(layerCentre.getRelative(-1, 0, 0), XMaterial.LADDER);
            blocks.put(layerCentre.getRelative(-1, 0, 2), material);
            blocks.put(layerCentre.getRelative(-1, 0, -2), material);
            blocks.put(layerCentre.getRelative(0, 0, 2), material);
            blocks.put(layerCentre.getRelative(0, 0, -2), material);
            blocks.put(layerCentre.getRelative(1, 0, 1), material);
            blocks.put(layerCentre.getRelative(1, 0, -1), material);

            layerCentre = layerCentre.getRelative(0, 1, 0);

            blocks.put(layerCentre.getRelative(-2, 0, 1), material);
            blocks.put(layerCentre.getRelative(-2, 0, 0), material);
            blocks.put(layerCentre.getRelative(-2, 0, -1), material);
            blocks.put(layerCentre.getRelative(-1, 0, 0), XMaterial.LADDER);
            blocks.put(layerCentre.getRelative(-1, 0, 2), material);
            blocks.put(layerCentre.getRelative(-1, 0, -2), material);
            blocks.put(layerCentre.getRelative(0, 0, 2), material);
            blocks.put(layerCentre.getRelative(0, 0, -2), material);
            blocks.put(layerCentre.getRelative(1, 0, 1), material);
            blocks.put(layerCentre.getRelative(1, 0, -1), material);

            layerCentre = layerCentre.getRelative(0, 1, 0);

            blocks.put(layerCentre.getRelative(-2, 0, 1), material);
            blocks.put(layerCentre.getRelative(-2, 0, 0), material);
            blocks.put(layerCentre.getRelative(-2, 0, -1), material);
            blocks.put(layerCentre.getRelative(-1, 0, 0), XMaterial.LADDER);
            blocks.put(layerCentre.getRelative(-1, 0, 2), material);
            blocks.put(layerCentre.getRelative(-1, 0, -2), material);
            blocks.put(layerCentre.getRelative(0, 0, 2), material);
            blocks.put(layerCentre.getRelative(0, 0, -2), material);
            blocks.put(layerCentre.getRelative(1, 0, 1), material);
            blocks.put(layerCentre.getRelative(1, 0, -1), material);

            layerCentre = layerCentre.getRelative(0, 1, 0);

            blocks.put(layerCentre.getRelative(-2, 0, 1), material);
            blocks.put(layerCentre.getRelative(-2, 0, 0), material);
            blocks.put(layerCentre.getRelative(-2, 0, -1), material);
            blocks.put(layerCentre.getRelative(-1, 0, 0), XMaterial.LADDER);
            blocks.put(layerCentre.getRelative(-1, 0, 2), material);
            blocks.put(layerCentre.getRelative(-1, 0, -2), material);
            blocks.put(layerCentre.getRelative(0, 0, 2), material);
            blocks.put(layerCentre.getRelative(0, 0, -2), material);
            blocks.put(layerCentre.getRelative(1, 0, 1), material);
            blocks.put(layerCentre.getRelative(1, 0, 0), material);
            blocks.put(layerCentre.getRelative(1, 0, -1), material);

            layerCentre = layerCentre.getRelative(0, 1, 0);

            blocks.put(layerCentre.getRelative(-2, 0, 2), material);
            blocks.put(layerCentre.getRelative(-2, 0, 1), material);
            blocks.put(layerCentre.getRelative(-2, 0, 0), material);
            blocks.put(layerCentre.getRelative(-2, 0, -1), material);
            blocks.put(layerCentre.getRelative(-2, 0, -2), material);
            blocks.put(layerCentre.getRelative(-1, 0, 0), XMaterial.LADDER);
            blocks.put(layerCentre.getRelative(-1, 0, 2), material);
            blocks.put(layerCentre.getRelative(-1, 0, 1), material);
            blocks.put(layerCentre.getRelative(-1, 0, -1), material);
            blocks.put(layerCentre.getRelative(-1, 0, -2), material);
            blocks.put(layerCentre.getRelative(0, 0, 2), material);
            blocks.put(layerCentre.getRelative(0, 0, 1), material);
            blocks.put(layerCentre.getRelative(0, 0, 0), material);
            blocks.put(layerCentre.getRelative(0, 0, -1), material);
            blocks.put(layerCentre.getRelative(0, 0, -2), material);
            blocks.put(layerCentre.getRelative(1, 0, 2), material);
            blocks.put(layerCentre.getRelative(1, 0, 1), material);
            blocks.put(layerCentre.getRelative(1, 0, 0), material);
            blocks.put(layerCentre.getRelative(1, 0, -1), material);
            blocks.put(layerCentre.getRelative(1, 0, -2), material);
            blocks.put(layerCentre.getRelative(-3, 0, 2), material);
            blocks.put(layerCentre.getRelative(-2, 0, 3), material);
            blocks.put(layerCentre.getRelative(-3, 0, -2), material);
            blocks.put(layerCentre.getRelative(-2, 0, -3), material);
            blocks.put(layerCentre.getRelative(1, 0, 3), material);
            blocks.put(layerCentre.getRelative(2, 0, 2), material);
            blocks.put(layerCentre.getRelative(1, 0, -3), material);
            blocks.put(layerCentre.getRelative(2, 0, -2), material);

            layerCentre = layerCentre.getRelative(0, 1, 0);

            blocks.put(layerCentre.getRelative(-3, 0, 1), material);
            blocks.put(layerCentre.getRelative(-3, 0, 0), material);
            blocks.put(layerCentre.getRelative(-3, 0, -1), material);
            blocks.put(layerCentre.getRelative(-1, 0, 3), material);
            blocks.put(layerCentre.getRelative(-1, 0, -3), material);
            blocks.put(layerCentre.getRelative(0, 0, 3), material);
            blocks.put(layerCentre.getRelative(0, 0, -3), material);
            blocks.put(layerCentre.getRelative(2, 0, 1), material);
            blocks.put(layerCentre.getRelative(2, 0, 0), material);
            blocks.put(layerCentre.getRelative(2, 0, -1), material);
            blocks.put(layerCentre.getRelative(-3, 0, 2), material);
            blocks.put(layerCentre.getRelative(-2, 0, 3), material);
            blocks.put(layerCentre.getRelative(-3, 0, -2), material);
            blocks.put(layerCentre.getRelative(-2, 0, -3), material);
            blocks.put(layerCentre.getRelative(1, 0, 3), material);
            blocks.put(layerCentre.getRelative(2, 0, 2), material);
            blocks.put(layerCentre.getRelative(1, 0, -3), material);
            blocks.put(layerCentre.getRelative(2, 0, -2), material);

            layerCentre = layerCentre.getRelative(0, 1, 0);

            blocks.put(layerCentre.getRelative(-3, 0, 0), material);
            blocks.put(layerCentre.getRelative(2, 0, 0), material);
            blocks.put(layerCentre.getRelative(-3, 0, 2), material);
            blocks.put(layerCentre.getRelative(-2, 0, 3), material);
            blocks.put(layerCentre.getRelative(-3, 0, -2), material);
            blocks.put(layerCentre.getRelative(-2, 0, -3), material);
            blocks.put(layerCentre.getRelative(1, 0, 3), material);
            blocks.put(layerCentre.getRelative(2, 0, 2), material);
            blocks.put(layerCentre.getRelative(1, 0, -3), material);
            blocks.put(layerCentre.getRelative(2, 0, -2), material);
            return blocks;
        }

        private static Map<Block, XMaterial> getSouthBlocks(Block centre, XMaterial material) {

            Map<Block, XMaterial> blocks = new LinkedHashMap<>();

            Block layerCentre = centre;

            blocks.put(layerCentre.getRelative(1, 0, 2), material);
            blocks.put(layerCentre.getRelative(0, 0, 2), material);
            blocks.put(layerCentre.getRelative(-1, 0, 2), material);
            blocks.put(layerCentre.getRelative(0, 0, 1), XMaterial.LADDER);
            blocks.put(layerCentre.getRelative(2, 0, 1), material);
            blocks.put(layerCentre.getRelative(-2, 0, 1), material);
            blocks.put(layerCentre.getRelative(2, 0, 0), material);
            blocks.put(layerCentre.getRelative(-2, 0, 0), material);
            blocks.put(layerCentre.getRelative(1, 0, -1), material);
            blocks.put(layerCentre.getRelative(-1, 0, -1), material);

            layerCentre = layerCentre.getRelative(0, 1, 0);

            blocks.put(layerCentre.getRelative(1, 0, 2), material);
            blocks.put(layerCentre.getRelative(0, 0, 2), material);
            blocks.put(layerCentre.getRelative(-1, 0, 2), material);
            blocks.put(layerCentre.getRelative(0, 0, 1), XMaterial.LADDER);
            blocks.put(layerCentre.getRelative(2, 0, 1), material);
            blocks.put(layerCentre.getRelative(-2, 0, 1), material);
            blocks.put(layerCentre.getRelative(2, 0, 0), material);
            blocks.put(layerCentre.getRelative(-2, 0, 0), material);
            blocks.put(layerCentre.getRelative(1, 0, -1), material);
            blocks.put(layerCentre.getRelative(-1, 0, -1), material);

            layerCentre = layerCentre.getRelative(0, 1, 0);

            blocks.put(layerCentre.getRelative(1, 0, 2), material);
            blocks.put(layerCentre.getRelative(0, 0, 2), material);
            blocks.put(layerCentre.getRelative(-1, 0, 2), material);
            blocks.put(layerCentre.getRelative(0, 0, 1), XMaterial.LADDER);
            blocks.put(layerCentre.getRelative(2, 0, 1), material);
            blocks.put(layerCentre.getRelative(-2, 0, 1), material);
            blocks.put(layerCentre.getRelative(2, 0, 0), material);
            blocks.put(layerCentre.getRelative(-2, 0, 0), material);
            blocks.put(layerCentre.getRelative(1, 0, -1), material);
            blocks.put(layerCentre.getRelative(-1, 0, -1), material);

            layerCentre = layerCentre.getRelative(0, 1, 0);

            blocks.put(layerCentre.getRelative(1, 0, 2), material);
            blocks.put(layerCentre.getRelative(0, 0, 2), material);
            blocks.put(layerCentre.getRelative(-1, 0, 2), material);
            blocks.put(layerCentre.getRelative(0, 0, 1), XMaterial.LADDER);
            blocks.put(layerCentre.getRelative(2, 0, 1), material);
            blocks.put(layerCentre.getRelative(-2, 0, 1), material);
            blocks.put(layerCentre.getRelative(2, 0, 0), material);
            blocks.put(layerCentre.getRelative(-2, 0, 0), material);
            blocks.put(layerCentre.getRelative(1, 0, -1), material);
            blocks.put(layerCentre.getRelative(0, 0, -1), material);
            blocks.put(layerCentre.getRelative(-1, 0, -1), material);

            layerCentre = layerCentre.getRelative(0, 1, 0);

            blocks.put(layerCentre.getRelative(2, 0, 2), material);
            blocks.put(layerCentre.getRelative(1, 0, 2), material);
            blocks.put(layerCentre.getRelative(0, 0, 2), material);
            blocks.put(layerCentre.getRelative(-1, 0, 2), material);
            blocks.put(layerCentre.getRelative(-2, 0, 2), material);
            blocks.put(layerCentre.getRelative(0, 0, 1), XMaterial.LADDER);
            blocks.put(layerCentre.getRelative(2, 0, 1), material);
            blocks.put(layerCentre.getRelative(1, 0, 1), material);
            blocks.put(layerCentre.getRelative(-1, 0, 1), material);
            blocks.put(layerCentre.getRelative(-2, 0, 1), material);
            blocks.put(layerCentre.getRelative(2, 0, 0), material);
            blocks.put(layerCentre.getRelative(1, 0, 0), material);
            blocks.put(layerCentre.getRelative(0, 0, 0), material);
            blocks.put(layerCentre.getRelative(-1, 0, 0), material);
            blocks.put(layerCentre.getRelative(-2, 0, 0), material);
            blocks.put(layerCentre.getRelative(2, 0, -1), material);
            blocks.put(layerCentre.getRelative(1, 0, -1), material);
            blocks.put(layerCentre.getRelative(0, 0, -1), material);
            blocks.put(layerCentre.getRelative(-1, 0, -1), material);
            blocks.put(layerCentre.getRelative(-2, 0, -1), material);
            blocks.put(layerCentre.getRelative(2, 0, 3), material);
            blocks.put(layerCentre.getRelative(3, 0, 2), material);
            blocks.put(layerCentre.getRelative(-2, 0, 3), material);
            blocks.put(layerCentre.getRelative(-3, 0, 2), material);
            blocks.put(layerCentre.getRelative(3, 0, -1), material);
            blocks.put(layerCentre.getRelative(2, 0, -2), material);
            blocks.put(layerCentre.getRelative(-3, 0, -1), material);
            blocks.put(layerCentre.getRelative(-2, 0, -2), material);

            layerCentre = layerCentre.getRelative(0, 1, 0);

            blocks.put(layerCentre.getRelative(1, 0, 3), material);
            blocks.put(layerCentre.getRelative(0, 0, 3), material);
            blocks.put(layerCentre.getRelative(-1, 0, 3), material);
            blocks.put(layerCentre.getRelative(3, 0, 1), material);
            blocks.put(layerCentre.getRelative(-3, 0, 1), material);
            blocks.put(layerCentre.getRelative(3, 0, 0), material);
            blocks.put(layerCentre.getRelative(-3, 0, 0), material);
            blocks.put(layerCentre.getRelative(1, 0, -2), material);
            blocks.put(layerCentre.getRelative(0, 0, -2), material);
            blocks.put(layerCentre.getRelative(-1, 0, -2), material);
            blocks.put(layerCentre.getRelative(2, 0, 3), material);
            blocks.put(layerCentre.getRelative(3, 0, 2), material);
            blocks.put(layerCentre.getRelative(-2, 0, 3), material);
            blocks.put(layerCentre.getRelative(-3, 0, 2), material);
            blocks.put(layerCentre.getRelative(3, 0, -1), material);
            blocks.put(layerCentre.getRelative(2, 0, -2), material);
            blocks.put(layerCentre.getRelative(-3, 0, -1), material);
            blocks.put(layerCentre.getRelative(-2, 0, -2), material);

            layerCentre = layerCentre.getRelative(0, 1, 0);

            blocks.put(layerCentre.getRelative(0, 0, 3), material);
            blocks.put(layerCentre.getRelative(0, 0, -2), material);
            blocks.put(layerCentre.getRelative(2, 0, 3), material);
            blocks.put(layerCentre.getRelative(3, 0, 2), material);
            blocks.put(layerCentre.getRelative(-2, 0, 3), material);
            blocks.put(layerCentre.getRelative(-3, 0, 2), material);
            blocks.put(layerCentre.getRelative(3, 0, -1), material);
            blocks.put(layerCentre.getRelative(2, 0, -2), material);
            blocks.put(layerCentre.getRelative(-3, 0, -1), material);
            blocks.put(layerCentre.getRelative(-2, 0, -2), material);
            return blocks;
        }

        private static Map<Block, XMaterial> getNorthBlocks(Block centre, XMaterial material) {
            Map<Block, XMaterial> blocks = new LinkedHashMap<>();

            Block layerCentre = centre;

            blocks.put(layerCentre.getRelative(1, 0, -2), material);
            blocks.put(layerCentre.getRelative(0, 0, -2), material);
            blocks.put(layerCentre.getRelative(-1, 0, -2), material);
            blocks.put(layerCentre.getRelative(0, 0, -1), XMaterial.LADDER);
            blocks.put(layerCentre.getRelative(2, 0, -1), material);
            blocks.put(layerCentre.getRelative(-2, 0, -1), material);
            blocks.put(layerCentre.getRelative(2, 0, 0), material);
            blocks.put(layerCentre.getRelative(-2, 0, 0), material);
            blocks.put(layerCentre.getRelative(1, 0, 1), material);
            blocks.put(layerCentre.getRelative(-1, 0, 1), material);

            layerCentre = layerCentre.getRelative(0, 1, 0);

            blocks.put(layerCentre.getRelative(1, 0, -2), material);
            blocks.put(layerCentre.getRelative(0, 0, -2), material);
            blocks.put(layerCentre.getRelative(-1, 0, -2), material);
            blocks.put(layerCentre.getRelative(0, 0, -1), XMaterial.LADDER);
            blocks.put(layerCentre.getRelative(2, 0, -1), material);
            blocks.put(layerCentre.getRelative(-2, 0, -1), material);
            blocks.put(layerCentre.getRelative(2, 0, 0), material);
            blocks.put(layerCentre.getRelative(-2, 0, 0), material);
            blocks.put(layerCentre.getRelative(1, 0, 1), material);
            blocks.put(layerCentre.getRelative(-1, 0, 1), material);

            layerCentre = layerCentre.getRelative(0, 1, 0);

            blocks.put(layerCentre.getRelative(1, 0, -2), material);
            blocks.put(layerCentre.getRelative(0, 0, -2), material);
            blocks.put(layerCentre.getRelative(-1, 0, -2), material);
            blocks.put(layerCentre.getRelative(0, 0, -1), XMaterial.LADDER);
            blocks.put(layerCentre.getRelative(2, 0, -1), material);
            blocks.put(layerCentre.getRelative(-2, 0, -1), material);
            blocks.put(layerCentre.getRelative(2, 0, 0), material);
            blocks.put(layerCentre.getRelative(-2, 0, 0), material);
            blocks.put(layerCentre.getRelative(1, 0, 1), material);
            blocks.put(layerCentre.getRelative(-1, 0, 1), material);

            layerCentre = layerCentre.getRelative(0, 1, 0);

            blocks.put(layerCentre.getRelative(1, 0, -2), material);
            blocks.put(layerCentre.getRelative(0, 0, -2), material);
            blocks.put(layerCentre.getRelative(-1, 0, -2), material);
            blocks.put(layerCentre.getRelative(0, 0, -1), XMaterial.LADDER);
            blocks.put(layerCentre.getRelative(2, 0, -1), material);
            blocks.put(layerCentre.getRelative(-2, 0, -1), material);
            blocks.put(layerCentre.getRelative(2, 0, 0), material);
            blocks.put(layerCentre.getRelative(-2, 0, 0), material);
            blocks.put(layerCentre.getRelative(1, 0, 1), material);
            blocks.put(layerCentre.getRelative(0, 0, 1), material);
            blocks.put(layerCentre.getRelative(-1, 0, 1), material);

            layerCentre = layerCentre.getRelative(0, 1, 0);

            blocks.put(layerCentre.getRelative(2, 0, -2), material);
            blocks.put(layerCentre.getRelative(1, 0, -2), material);
            blocks.put(layerCentre.getRelative(0, 0, -2), material);
            blocks.put(layerCentre.getRelative(-1, 0, -2), material);
            blocks.put(layerCentre.getRelative(-2, 0, -2), material);
            blocks.put(layerCentre.getRelative(0, 0, -1), XMaterial.LADDER);
            blocks.put(layerCentre.getRelative(2, 0, -1), material);
            blocks.put(layerCentre.getRelative(1, 0, -1), material);
            blocks.put(layerCentre.getRelative(-1, 0, -1), material);
            blocks.put(layerCentre.getRelative(-2, 0, -1), material);
            blocks.put(layerCentre.getRelative(2, 0, 0), material);
            blocks.put(layerCentre.getRelative(1, 0, 0), material);
            blocks.put(layerCentre.getRelative(0, 0, 0), material);
            blocks.put(layerCentre.getRelative(-1, 0, 0), material);
            blocks.put(layerCentre.getRelative(-2, 0, 0), material);
            blocks.put(layerCentre.getRelative(2, 0, 1), material);
            blocks.put(layerCentre.getRelative(1, 0, 1), material);
            blocks.put(layerCentre.getRelative(0, 0, 1), material);
            blocks.put(layerCentre.getRelative(-1, 0, 1), material);
            blocks.put(layerCentre.getRelative(-2, 0, 1), material);
            blocks.put(layerCentre.getRelative(2, 0, -3), material);
            blocks.put(layerCentre.getRelative(3, 0, -2), material);
            blocks.put(layerCentre.getRelative(-2, 0, -3), material);
            blocks.put(layerCentre.getRelative(-3, 0, -2), material);
            blocks.put(layerCentre.getRelative(3, 0, 1), material);
            blocks.put(layerCentre.getRelative(2, 0, 2), material);
            blocks.put(layerCentre.getRelative(-3, 0, 1), material);
            blocks.put(layerCentre.getRelative(-2, 0, 2), material);

            layerCentre = layerCentre.getRelative(0, 1, 0);

            blocks.put(layerCentre.getRelative(1, 0, -3), material);
            blocks.put(layerCentre.getRelative(0, 0, -3), material);
            blocks.put(layerCentre.getRelative(-1, 0, -3), material);
            blocks.put(layerCentre.getRelative(3, 0, -1), material);
            blocks.put(layerCentre.getRelative(-3, 0, -1), material);
            blocks.put(layerCentre.getRelative(3, 0, 0), material);
            blocks.put(layerCentre.getRelative(-3, 0, 0), material);
            blocks.put(layerCentre.getRelative(1, 0, 2), material);
            blocks.put(layerCentre.getRelative(0, 0, 2), material);
            blocks.put(layerCentre.getRelative(-1, 0, 2), material);
            blocks.put(layerCentre.getRelative(2, 0, -3), material);
            blocks.put(layerCentre.getRelative(3, 0, -2), material);
            blocks.put(layerCentre.getRelative(-2, 0, -3), material);
            blocks.put(layerCentre.getRelative(-3, 0, -2), material);
            blocks.put(layerCentre.getRelative(3, 0, 1), material);
            blocks.put(layerCentre.getRelative(2, 0, 2), material);
            blocks.put(layerCentre.getRelative(-3, 0, 1), material);
            blocks.put(layerCentre.getRelative(-2, 0, 2), material);

            layerCentre = layerCentre.getRelative(0, 1, 0);

            blocks.put(layerCentre.getRelative(0, 0, -3), material);
            blocks.put(layerCentre.getRelative(0, 0, 2), material);
            blocks.put(layerCentre.getRelative(2, 0, -3), material);
            blocks.put(layerCentre.getRelative(3, 0, -2), material);
            blocks.put(layerCentre.getRelative(-2, 0, -3), material);
            blocks.put(layerCentre.getRelative(-3, 0, -2), material);
            blocks.put(layerCentre.getRelative(3, 0, 1), material);
            blocks.put(layerCentre.getRelative(2, 0, 2), material);
            blocks.put(layerCentre.getRelative(-3, 0, 1), material);
            blocks.put(layerCentre.getRelative(-2, 0, 2), material);
            return blocks;
        }
    }
}
