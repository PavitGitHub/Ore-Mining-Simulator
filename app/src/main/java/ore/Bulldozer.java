package ore;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;

import java.awt.*;

/**
 * Team 06
 * Pavit Vathna and Daniel Duong
 */
public class Bulldozer extends Machine {
    public Bulldozer(OreSim oreSim) {
        super( oreSim, "sprites/bulldozer.png");  // Rotatable
    }

    @Override
    public boolean canMove(Location location) {
        OreSim oreSim = super.getOreSim();
        // Test if try to move into ore, clay or other machine
        Color c = oreSim.getBg().getColor(location);
        Ore ore = (Ore)oreSim.getOneActorAt(location, Ore.class);
        Clay clay = (Clay)oreSim.getOneActorAt(location, Clay.class);
        Bulldozer bulldozer = (Bulldozer)oreSim.getOneActorAt(location, Bulldozer.class);
        Pusher pusher = (Pusher) oreSim.getOneActorAt(location, Pusher.class);
        Rock rock = (Rock) oreSim.getOneActorAt(location, Rock.class);

        if (clay != null) {
            setObstaclesDestroyed(getObstaclesDestroyed() + 1);
            clay.removeSelf();
            return true;
        }

        return !c.equals(oreSim.getBorderColor()) && ore == null && rock == null && bulldozer == null && pusher == null;
    }

    @Override
    public void autoMoveNext() {
        OreSim oreSim = super.getOreSim();
        if (oreSim.isFinished()) {
            return;
        }

        Location next = setMoveDirection();
        if (next != null && this.canMove(next)) {
            setNumberMoves(getNumberMoves() + 1);
            setLocation(next);
        }
        oreSim.refresh();


    }
}
