package ore;

import ch.aplu.jgamegrid.Actor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Team 06
 * Pavit Vathna and Daniel Duong
 */

public class Analyser {

    private StringBuilder logResult = new StringBuilder();
    private final OreSim oreSim;

    public Analyser(OreSim oreSim) {
        this.oreSim = oreSim;
    }

    /**
     * The method will generate a log result for all the movements of all actors
     * The log result will be tested against our expected output.
     * Your code will need to pass all the 3 test suites with 9 test cases.
     */
    public void updateLogResult() {
        List<Actor> pushers = oreSim.getActors(Pusher.class);
        List<Actor> ores = oreSim.getActors(Ore.class);
        List<Actor> targets = oreSim.getActors(Target.class);
        List<Actor> rocks = oreSim.getActors(Rock.class);
        List<Actor> clays = oreSim.getActors(Clay.class);
        List<Actor> bulldozers = oreSim.getActors(Bulldozer.class);
        List<Actor> excavators = oreSim.getActors(Excavator.class);

        logResult.append(oreSim.getMovementIndex() + "#");
        logResult.append(ElementType.PUSHER.getShortType()).append(actorLocations(pushers)).append("#");
        logResult.append(ElementType.ORE.getShortType()).append(actorLocations(ores)).append("#");
        logResult.append(ElementType.TARGET.getShortType()).append(actorLocations(targets)).append("#");
        logResult.append(ElementType.ROCK.getShortType()).append(actorLocations(rocks)).append("#");
        logResult.append(ElementType.CLAY.getShortType()).append(actorLocations(clays)).append("#");
        logResult.append(ElementType.BULLDOZER.getShortType()).append(actorLocations(bulldozers)).append("#");
        logResult.append(ElementType.EXCAVATOR.getShortType()).append(actorLocations(excavators));

        logResult.append("\n");
    }

    /**
     * Students need to modify this method so it can write an actual statistics into the statistics file. It currently
     *  only writes the sample data.
     */
    public void updateStatistics() {
        File statisticsFile = new File("statistics.txt");
        FileWriter fileWriter = null;
        Pusher pusher = oreSim.getPusher();
        Bulldozer bulldozer = oreSim.getBulldozer();
        Excavator excavator = oreSim.getExcavator();
        int pusherMoves = 0;
        int bulldozerMoves = 0;
        int bulldozerObstaclesDestroyed = 0;

        int excavatorMoves = 0;
        int excavatorObstaclesDestroyed = 0;
        try {
            fileWriter = new FileWriter(statisticsFile);
            if (pusher != null) {
                pusherMoves = pusher.getNumberMoves();

            }
            if (excavator != null) {
                excavatorMoves = excavator.getNumberMoves();
                excavatorObstaclesDestroyed = excavator.getObstaclesDestroyed();

            }
            if (bulldozer != null) {
                bulldozerMoves = bulldozer.getNumberMoves();
                bulldozerObstaclesDestroyed = bulldozer.getObstaclesDestroyed();

            }
            fileWriter.write(String.format("Pusher-1 Moves: %d\n", pusherMoves));
            fileWriter.write(String.format("Excavator-1 Moves: %d\n", excavatorMoves));
            fileWriter.write(String.format("Excavator-1 Rock removed: %d\n", excavatorObstaclesDestroyed));
            fileWriter.write(String.format("Bulldozer-1 Moves: %d\n", bulldozerMoves));
            fileWriter.write(String.format("Bulldozer-1 Clay removed: %d\n", bulldozerObstaclesDestroyed));
        } catch (IOException e) {
            System.out.println("Cannot write to file - e: " + e.getLocalizedMessage());
        } finally {
            try {
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Cannot close file - e: " + e.getLocalizedMessage());
            }
        }
    }

    /**
     * Transform the list of actors to a string of location for a specific kind of actor.
     * @param actors
     * @return
     */
    public String actorLocations(List<Actor> actors) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean hasAddedColon = false;
        boolean hasAddedLastComma = false;
        for (int i = 0; i < actors.size(); i++) {
            Actor actor = actors.get(i);
            if (actor.isVisible()) {
                if (!hasAddedColon) {
                    stringBuilder.append(":");
                    hasAddedColon = true;
                }
                stringBuilder.append(actor.getX() + "-" + actor.getY());
                stringBuilder.append(",");
                hasAddedLastComma = true;
            }
        }

        if (hasAddedLastComma) {
            stringBuilder.replace(stringBuilder.length() - 1, stringBuilder.length(), "");
        }

        return stringBuilder.toString();
    }

    public StringBuilder getLogResult() {
        return logResult;
    }
}
