package com.pepedevs.dbedwars.game.structure;

import com.pepedevs.dbedwars.api.game.struture.DirectionalStructure;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.List;

public class PopupTowerStructure implements DirectionalStructure {

    private List<Block> northBlocks;
    private List<Block> westBlocks;
    private List<Block> eastBlocks;
    private List<Block> southBlocks;

    private int blocksPerSecond;

    public PopupTowerStructure(){
        initNorthBlocks();
        initWestBlocks();
        initEastBlocks();
        initSouthBlocks();
    }

    @Override
    public List<Block> getNorthBlocks() {
        return new ArrayList<>(northBlocks);
    }

    @Override
    public List<Block> getWestBlocks() {
        return new ArrayList<>(westBlocks);
    }

    @Override
    public List<Block> getEastBlocks() {
        return new ArrayList<>(eastBlocks);
    }

    @Override
    public List<Block> getSouthBlocks() {
        return new ArrayList<>(southBlocks);
    }

    @Override
    public List<Block> getBlocks(BlockFace face) {
        return null;
    }

    @Override
    public void setNorthBlocks(List<Block> blocks) {
        this.northBlocks = blocks;
    }

    @Override
    public void setWestBlocks(List<Block> blocks) {
        this.westBlocks = blocks;
    }

    @Override
    public void setEastBlocks(List<Block> blocks) {
        this.eastBlocks = blocks;
    }

    @Override
    public void setSouthBlocks(List<Block> blocks) {
        this.southBlocks = blocks;
    }

    @Override
    public void setBlocks(BlockFace face, List<Block> blocks) {
        switch (face){
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
    public List<Block> getBlocks() {
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

    private void initNorthBlocks() {
        this.northBlocks = new ArrayList<>();
    }

    private void initSouthBlocks() {
        this.southBlocks = new ArrayList<>();
    }

    private void initEastBlocks() {
        this.eastBlocks = new ArrayList<>();
    }

    private void initWestBlocks() {
        this.westBlocks = new ArrayList<>();
    }


}
