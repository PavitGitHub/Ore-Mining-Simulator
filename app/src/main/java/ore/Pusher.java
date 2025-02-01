package ore;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;

import java.awt.*;
import java.util.List;

/**
 * Team 06
 * Pavit Vathna and Daniel Duong
 */

public class Pusher extends Machine {


    public Pusher(OreSim oreSim) {
        super(oreSim, "sprites/pusher.png");  // Rotatable
    }

    /**
     * Method to move pusher automatically based on the instructions input from properties file
     */
    @Override
    public void autoMoveNext() {

        OreSim oreSim = super.getOreSim();
        if (oreSim.isFinished()) {
            return;
        }

        Location next = setMoveDirection();
        Target curTarget = (Target) oreSim.getOneActorAt(getLocation(), Target.class);
        if (curTarget != null) {
            curTarget.show();
        }
        if (next != null && this.canMove(next)) {
            setNumberMoves(getNumberMoves() + 1);
            setLocation(next);
        }
        oreSim.refresh();


    }

    /**
     * Check if we can move the pusher into the location
     * @param location
     * @return
     */
    @Override
    public boolean canMove(Location location) {
        OreSim oreSim = super.getOreSim();
        // Test if try to move into border, rock or clay
        Color c = oreSim.getBg().getColor(location);
        Rock rock = (Rock)oreSim.getOneActorAt(location, Rock.class);
        Clay clay = (Clay)oreSim.getOneActorAt(location, Clay.class);
        Bulldozer bulldozer = (Bulldozer)oreSim.getOneActorAt(location, Bulldozer.class);
        Excavator excavator = (Excavator)oreSim.getOneActorAt(location, Excavator.class);
        if (c.equals(oreSim.getBorderColor()) || rock != null || clay != null || bulldozer != null || excavator != null)
            return false;
        else {
            // Test if there is an ore

            Ore ore = (Ore)oreSim.getOneActorAt(location, Ore.class);
            if (ore != null) {

                // Try to move the ore
                ore.setDirection(this.getDirection());
                if (moveOre(ore))
                    return true;
                else
                    return false;

            }
        }

        return true;
    }

    /**
     * When the pusher pushes the ore in 1 direction, this method will be called to check if the ore can move in that direction
     *  and if it can move, then it changes the location
     * @param ore
     * @return boolean
     */

    public boolean moveOre(Ore ore) {
        OreSim oreSim = super.getOreSim();

        Location next = ore.getNextMoveLocation();
        // Test if try to move into border
        Color c = oreSim.getBg().getColor(next);;
        Rock rock = (Rock)oreSim.getOneActorAt(next, Rock.class);
        Clay clay = (Clay)oreSim.getOneActorAt(next, Clay.class);
        if (c.equals(oreSim.getBorderColor()) || rock != null || clay != null)
            return false;

        // Test if there is another ore
        Ore neighbourOre =
                (Ore)oreSim.getOneActorAt(next, Ore.class);
        if (neighbourOre != null)
            return false;

        // Reset the target if the ore is moved out of target
        Location currentLocation = ore.getLocation();
        List<Actor> actors = oreSim.getActorsAt(currentLocation);
        if (actors != null) {
            for (Actor actor : actors) {
                if (actor instanceof Target) {
                    Target currentTarget = (Target) actor;
                    currentTarget.show();
                    ore.show(0);
                }
            }
        }

        // Move the ore
        ore.setLocation(next);

        // Check if we are at a target
        Target nextTarget = (Target) oreSim.getOneActorAt(next, Target.class);
        if (nextTarget != null) {
            ore.show(1);
            nextTarget.hide();
        } else {
            ore.show(0);
        }

        return true;
    }
}
