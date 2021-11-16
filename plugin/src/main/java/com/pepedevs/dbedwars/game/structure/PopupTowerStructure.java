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

    public PopupTowerStructure(){
        initNorthBlocks();
        initWestBlocks();
        initEastBlocks();
        initSouthBlocks();
    }

    @Override
    public List<Block> getNorthBlocks() {
        return null;
    }

    @Override
    public List<Block> getWestBlocks() {
        return null;
    }

    @Override
    public List<Block> getEastBlocks() {
        return null;
    }

    @Override
    public List<Block> getSouthBlocks() {
        return null;
    }

    @Override
    public List<Block> getBlocks(BlockFace face) {
        return null;
    }

    @Override
    public void setNorthBlocks(List<Block> blocks) {

    }

    @Override
    public void setWestBlocks(List<Block> blocks) {

    }

    @Override
    public void setEastBlocks(List<Block> blocks) {

    }

    @Override
    public void setSouthBlocks(List<Block> blocks) {

    }

    @Override
    public void setBlocks(BlockFace face, List<Block> blocks) {

    }

    @Override
    public List<Block> getBlocks() {
        return null;
    }

    @Override
    public void setBlocks(List<Block> blocks) {

    }

    @Override
    public int getBlocksToPlacePerSecond() {
        return 0;
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
