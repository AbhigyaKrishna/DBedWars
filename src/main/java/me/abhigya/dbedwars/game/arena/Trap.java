package me.abhigya.dbedwars.game.arena;

import me.abhigya.dbedwars.api.game.ArenaPlayer;
import me.abhigya.dbedwars.api.game.Team;
import me.abhigya.dbedwars.api.util.TrapEnum;
import me.abhigya.dbedwars.configuration.configurable.ConfigurableTrap;

import java.util.*;
import java.util.function.Consumer;

public class Trap implements me.abhigya.dbedwars.api.game.Trap {

    private String id;
    private ConfigurableTrap cfgTrap;
    private TrapEnum.TriggerType trigger;

    private Map< TrapEnum.TargetType, Set< Consumer< ArenaPlayer > > > actions;

    public Trap( String id, TrapEnum.TriggerType trigger ) {
        this.id = id;
        this.trigger = trigger;
        this.actions = new HashMap<>( );
    }

    public Trap( ConfigurableTrap trap ) {
        this( trap.getId( ), trap.getTrigger( ) );
        this.cfgTrap = trap;
        this.parseTrapAction( );
    }

    @Override
    public void trigger( ArenaPlayer target, Team team ) {
        this.actions.forEach( ( t, a ) -> {
            if ( t == TrapEnum.TargetType.TEAM ) {
                team.getPlayers( ).forEach( p -> a.forEach( c -> c.accept( p ) ) );
            } else if ( t == TrapEnum.TargetType.ENEMY_TEAM ) {
                target.getTeam( ).getPlayers( ).forEach( p -> a.forEach( c -> c.accept( p ) ) );
            } else if ( t == TrapEnum.TargetType.ENEMY_AT_BASE ) {
                a.forEach( c -> c.accept( target ) );
            } else if ( t == TrapEnum.TargetType.TEAM_AT_BASE ) {
                team.getPlayers( ).forEach( p -> {
                    if ( team.getIslandArea( ).contains( p.getPlayer( ).getLocation( ).toVector( ) ) )
                        a.forEach( c -> c.accept( p ) );
                } );
            } else if ( t == TrapEnum.TargetType.RANDOM_TEAM_PLAYER ) {
                ArenaPlayer p = new ArrayList<>( team.getPlayers( ) ).get( new Random( ).nextInt( team.getPlayers( ).size( ) ) );
                a.forEach( c -> c.accept( p ) );
            } else if ( t == TrapEnum.TargetType.RANDOM_ENEMY_PLAYER ) {
                ArenaPlayer p = new ArrayList<>( target.getTeam( ).getPlayers( ) ).get( new Random( ).nextInt( target.getTeam( ).getPlayers( ).size( ) ) );
                a.forEach( c -> c.accept( p ) );
            } else if ( t == TrapEnum.TargetType.ALL_ENEMY ) {
                target.getArena( ).getTeams( ).forEach( tm -> {
                    if ( !tm.equals( team ) )
                        tm.getPlayers( ).forEach( p -> a.forEach( c -> c.accept( p ) ) );
                } );
            } else if ( t == TrapEnum.TargetType.ALL_PLAYER ) {
                target.getArena( ).getPlayers( ).forEach( p -> a.forEach( c -> c.accept( p ) ) );
            }
        } );
    }

    private void parseTrapAction( ) {
        cfgTrap.getTrapActions( ).forEach( a -> {
            TrapEnum.TargetType targetType = TrapEnum.TargetType.match( a.getTarget( ) );
            Set< Consumer< ArenaPlayer > > actions = new HashSet<>( );
            a.getExecutables( ).forEach( e -> {
                for ( TrapEnum.ActionType value : TrapEnum.ActionType.VALUES ) {
                    if ( e.startsWith( "[" + value.name( ) ) )
                        actions.add( value.getAction( e ) );
                }
            } );

            this.actions.put( targetType, actions );
        } );
    }

    @Override
    public String getId( ) {
        return id;
    }

    @Override
    public TrapEnum.TriggerType getTrigger( ) {
        return trigger;
    }

    @Override
    public Map< TrapEnum.TargetType, Set< Consumer< ArenaPlayer > > > getActions( ) {
        return actions;
    }

}
