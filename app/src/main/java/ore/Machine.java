package ore;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;

import javax.naming.ldap.Control;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Team 06
 * Pavit Vathna and Daniel Duong
 */

public abstract class Machine extends Actor {

    private final OreSim oreSim;
    private List<String> controls = null;
    private int autoMovementIndex = 0;
    private int numberMoves = 0;
    private int obstaclesDestroyed = 0;

    public Machine(OreSim oreSim, String spritePath) {
        super(true, spritePath);
        this.oreSim = oreSim;
    }

    public abstract boolean canMove(Location location);

    public void setUpMachine(List<String> controls, String machine) {
        this.controls = new ArrayList<>();

        for (String control: controls) {
            String[] currentMove = control.split("-");
            if (!control.isEmpty()) {

                String machine2 = currentMove[0];
                machine2 = machine2.replaceAll("\\*", "");
                String move = currentMove[1];
                move = move.replaceAll("\\*", "");
                if (machine2.equals(machine)) {
                    this.controls.add(move);
                }
            }
        }
    }

    /**
     * Sets the rotation to the direction the machine is moving
     * @return The location that the machine is to move
     */
    public Location setMoveDirection() {

        if (controls != null && autoMovementIndex < controls.size()) {
            String move = controls.get(autoMovementIndex);

            autoMovementIndex += 1;
            Location next = null;
            switch (move) {
                case "L":
                    next = getLocation().getNeighbourLocation(Location.WEST);
                    setDirection(Location.WEST);
                    break;
                case "U":
                    next = getLocation().getNeighbourLocation(Location.NORTH);
                    setDirection(Location.NORTH);
                    break;
                case "R":
                    next = getLocation().getNeighbourLocation(Location.EAST);
                    setDirection(Location.EAST);
                    break;
                case "D":
                    next = getLocation().getNeighbourLocation(Location.SOUTH);
                    setDirection(Location.SOUTH);
                    break;
            }

            return next;


        }

        return null;

    }

    public OreSim getOreSim() {
        return oreSim;
    }


    public abstract void autoMoveNext();

    public List<String> getControls() {
        return controls;
    }

    public void setControls(List<String> controls) {
        this.controls = controls;
    }

    public int getAutoMovementIndex() {
        return autoMovementIndex;
    }

    public void setAutoMovementIndex(int autoMovementIndex) {
        this.autoMovementIndex = autoMovementIndex;
    }

    public int getNumberMoves() {
        return numberMoves;
    }

    public void setNumberMoves(int numberMoves) {
        this.numberMoves = numberMoves;
    }

    public int getObstaclesDestroyed() {
        return obstaclesDestroyed;
    }

    public void setObstaclesDestroyed(int obstaclesDestroyed) {
        this.obstaclesDestroyed = obstaclesDestroyed;
    }
}
