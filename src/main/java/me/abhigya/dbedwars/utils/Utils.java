package me.abhigya.dbedwars.utils;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.Abhigya.core.util.math.LocationUtils;
import me.Abhigya.core.util.reflection.general.ClassReflection;
import me.Abhigya.core.util.reflection.general.MethodReflection;
import me.Abhigya.core.util.server.Version;
import me.Abhigya.core.util.xseries.XMaterial;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.api.game.Team;
import me.abhigya.dbedwars.api.util.BwItemStack;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Utils {

    public static final List< Material > BEDS = Arrays.asList( XMaterial.RED_BED.parseMaterial( ), XMaterial.BLACK_BED.parseMaterial( ),
            XMaterial.BLUE_BED.parseMaterial( ), XMaterial.BROWN_BED.parseMaterial( ), XMaterial.CYAN_BED.parseMaterial( ),
            XMaterial.GRAY_BED.parseMaterial( ), XMaterial.GREEN_BED.parseMaterial( ), XMaterial.LIGHT_BLUE_BED.parseMaterial( ),
            XMaterial.LIGHT_GRAY_BED.parseMaterial( ), XMaterial.LIME_BED.parseMaterial( ), XMaterial.MAGENTA_BED.parseMaterial( ),
            XMaterial.ORANGE_BED.parseMaterial( ), XMaterial.PINK_BED.parseMaterial( ), XMaterial.PURPLE_BED.parseMaterial( ),
            XMaterial.WHITE_BED.parseMaterial( ), XMaterial.YELLOW_BED.parseMaterial( ) );

    public static ItemStack addPluginData( ItemStack item ) {
        NBTItem nbti = new NBTItem( item );
        nbti.setString( "plugin", "dbedwars" );
        return nbti.getItem( );
    }

    public static boolean hasPluginData( ItemStack item ) {
        NBTItem nbti = new NBTItem( item );
        return nbti.hasNBTData( ) && nbti.hasKey( "plugin" ) && nbti.getString( "plugin" ).equals( "dbedwars" );
    }

    public static ItemStack addNbtData( ItemStack item, String key, Object value ) {
        NBTItem nbti = new NBTItem( item );
        if ( value instanceof String ) {
            nbti.setString( key, (String) value );
        } else if ( value instanceof Short ) {
            nbti.setShort( key, (short) value );
        } else if ( value instanceof Integer ) {
            nbti.setInteger( key, (int) value );
        } else if ( value instanceof Long ) {
            nbti.setLong( key, (long) value );
        } else if ( value instanceof Byte ) {
            nbti.setByte( key, (byte) value );
        } else if ( value instanceof Boolean ) {
            nbti.setBoolean( key, (boolean) value );
        } else if ( value instanceof Double ) {
            nbti.setDouble( key, (double) value );
        } else if ( value instanceof Float ) {
            nbti.setFloat( key, (Float) value );
        } else if ( value instanceof byte[] ) {
            nbti.setByteArray( key, (byte[]) value );
        } else if ( value instanceof int[] ) {
            nbti.setIntArray( key, (int[]) value );
        } else if ( value instanceof ItemStack ) {
            nbti.setItemStack( key, (ItemStack) value );
        } else if ( value instanceof UUID ) {
            nbti.setUUID( key, (UUID) value );
        } else {
            nbti.setObject( key, value );
        }

        return nbti.getItem( );
    }

    public static boolean hasNBTData( ItemStack item, String key ) {
        NBTItem nbti = new NBTItem( item );
        return nbti.hasNBTData( ) && nbti.hasKey( key );
    }

    public static < T > T getValue( ItemStack item, String key, Class< T > clazz ) {
        NBTItem nbti = new NBTItem( item );
        return nbti.getObject( key, clazz );
    }

    public static ItemStack removeNBTData( ItemStack item, String key ) {
        if ( !hasNBTData( item, key ) )
            return item;

        NBTItem nbti = new NBTItem( item );
        nbti.removeKey( key );
        return nbti.getItem( );
    }

    public static boolean isUnMergeable( ItemStack item ) {
        return hasNBTData( item, "unmerge" ) && new NBTItem( item ).getBoolean( "unmerge" );
    }

    public static ItemStack setUnStackable( ItemStack stack ) {
        try {
            Class< ? > cbItemStack = ClassReflection.getCraftClass( "CraftItemStack", "inventory" );
            Object nmsItemStack = MethodReflection.get( cbItemStack, "asNMSCopy", ItemStack.class ).invoke( null, stack );
            Object item = MethodReflection.invoke( nmsItemStack, "getItem" );
            MethodReflection.invoke( item, "c", 1 );
            return (ItemStack) MethodReflection.get( cbItemStack, "asBukkitCopy", nmsItemStack.getClass( ) ).invoke( null, nmsItemStack );

        } catch ( NoSuchMethodException | InvocationTargetException | IllegalAccessException e ) {
            e.printStackTrace( );
        }
        return stack;
    }

    public static void setSpawnInventory( Player player, Team team, @Nullable ItemStack[] previousContent, @Nullable ItemStack[] previousArmor ) {
        BwItemStack helmet = new BwItemStack( XMaterial.LEATHER_HELMET.parseMaterial( ) );
        BwItemStack chestPlate = new BwItemStack( XMaterial.LEATHER_CHESTPLATE.parseMaterial( ) );
        BwItemStack leggings = new BwItemStack( XMaterial.LEATHER_LEGGINGS.parseMaterial( ) );
        BwItemStack boots = new BwItemStack( XMaterial.LEATHER_BOOTS.parseMaterial( ) );

        // Helmet
        LeatherArmorMeta helmetMeta = (LeatherArmorMeta) helmet.getItemMeta( );
        helmetMeta.setColor( team.getColor( ).getColor( ) );
        helmet.setItemMeta( helmetMeta );

        // ChestPlate
        LeatherArmorMeta chestPlateMeta = (LeatherArmorMeta) chestPlate.getItemMeta( );
        chestPlateMeta.setColor( team.getColor( ).getColor( ) );
        chestPlate.setItemMeta( chestPlateMeta );

        // Leggings
        LeatherArmorMeta leggingsMeta = (LeatherArmorMeta) leggings.getItemMeta( );
        leggingsMeta.setColor( team.getColor( ).getColor( ) );
        leggings.setItemMeta( leggingsMeta );

        // Boots
        LeatherArmorMeta bootsMeta = (LeatherArmorMeta) boots.getItemMeta( );
        bootsMeta.setColor( team.getColor( ).getColor( ) );
        boots.setItemMeta( bootsMeta );

        player.getInventory( ).setHelmet( helmet.toItemStack( ) );
        player.getInventory( ).setChestplate( chestPlate.toItemStack( ) );
        player.getInventory( ).setLeggings( leggings.toItemStack( ) );
        player.getInventory( ).setBoots( boots.toItemStack( ) );

        player.getInventory( ).setItem( 0, new BwItemStack( XMaterial.WOODEN_SWORD.parseItem( ) ).toItemStack( ) );
    }

    public static void clearInventory( Player player ) {
        ItemStack[] items = player.getInventory( ).getContents( );
    }

    public static Block findBed( Location location, byte x, byte y, byte z ) {
        Location corner = location.clone( ).add( x, y, z );
        Location corner2 = location.clone( ).subtract( x, y, z );
        Set< Block > blocks = LocationUtils.getBlocksBetween( corner, corner2 );

        if ( DBedwars.getInstance( ).getServerVersion( ).isOlderEquals( Version.v1_8_R3 ) )
            return blocks.stream( ).filter( block -> block.getType( ).equals( Material.valueOf( "BED_BLOCK" ) ) ).findFirst( ).orElse( null );

        return blocks.stream( ).filter( block -> BEDS.contains( block.getType( ) ) ).findFirst( ).orElse( null );
    }

    public static boolean isBed( Block block ) {
        return BEDS.contains( block.getType( ) ) || block.getType( ).name( ).equals( "BED_BLOCK" );
    }

    @SafeVarargs
    public static < T > T[] mergeArray( T[]... arrays ) {
        List< T > list = new ArrayList<>( );
        for ( T[] array : arrays ) {
            list.addAll( Arrays.asList( array ) );
        }

        return list.toArray( arrays[0] );
    }

}
