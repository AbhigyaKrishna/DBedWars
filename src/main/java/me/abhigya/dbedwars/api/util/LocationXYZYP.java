package me.abhigya.dbedwars.api.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public class LocationXYZYP {

    private double x = 0.0D;
    private double y = 0.0D;
    private double z = 0.0D;
    private float yaw = 0.0F;
    private float pitch = 0.0F;

    public LocationXYZYP( ) {
    }

    public LocationXYZYP( double x, double y, double z ) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public LocationXYZYP( double x, double y, double z, float yaw, float pitch ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public static LocationXYZYP valueOf( Location loc ) {
        return new LocationXYZYP( loc.getX( ), loc.getY( ), loc.getZ( ), loc.getYaw( ), loc.getPitch( ) );
    }

    public static LocationXYZYP valueOf( Vector vector ) {
        return new LocationXYZYP( vector.getX( ), vector.getY( ), vector.getZ( ) );
    }

    // Format: x, y, z, y, p
    public static LocationXYZYP valueOf( String s ) {
        String[] arr = s.split( "," );
        if ( arr.length < 5 )
            return null;

        for ( String i : arr ) {
            try {
                Double.parseDouble( i.trim( ) );
            } catch ( NumberFormatException e ) {
                return null;
            }
        }

        return new LocationXYZYP( Double.parseDouble( arr[0] ), Double.parseDouble( arr[1] ), Double.parseDouble( arr[2] ),
                Float.parseFloat( arr[3] ), Float.parseFloat( arr[4] ) );
    }

    public double getX( ) {
        return x;
    }

    public void setX( double x ) {
        this.x = x;
    }

    public double getY( ) {
        return y;
    }

    public void setY( double y ) {
        this.y = y;
    }

    public double getZ( ) {
        return z;
    }

    public void setZ( double z ) {
        this.z = z;
    }

    public float getYaw( ) {
        return yaw;
    }

    public void setYaw( float yaw ) {
        this.yaw = yaw;
    }

    public float getPitch( ) {
        return pitch;
    }

    public void setPitch( float pitch ) {
        this.pitch = pitch;
    }

    public LocationXYZYP getXYZYP( ) {
        return this;
    }

    public void setXYZ( double x, double y, double z ) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setXYZYP( double x, double y, double z, float yaw, float pitch ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public Block getBlock( World world ) {
        return world.getBlockAt( (int) this.x, (int) this.y, (int) this.z );
    }

    public Location toBukkit( World world ) {
        return new Location( world, this.x, this.y, this.z, this.yaw, this.pitch );
    }

    public Vector toVector( ) {
        return new Vector( this.x, this.y, this.z );
    }

    public LocationXYZ toLocationXYZ( ) {
        return new LocationXYZ( this.x, this.y, this.z );
    }

    @Override
    public String toString( ) {
        return String.valueOf( this.x + ", " + this.y + ", " + this.z + ", " + this.yaw + ", " + this.pitch );
    }

    @Override
    public LocationXYZYP clone( ) {
        try {
            LocationXYZYP clone = (LocationXYZYP) super.clone( );
            return clone;
        } catch ( CloneNotSupportedException e ) {
            throw new AssertionError( );
        }
    }

}
