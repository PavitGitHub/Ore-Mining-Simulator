package ore;

import ch.aplu.jgamegrid.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.Properties;

/**
 * Team 06
 * Pavit Vathna and Daniel Duong
 */

public class OreSim extends GameGrid implements GGKeyListener {
  private final Color borderColor = new Color(100, 100, 100);


  private MapGrid grid;
  private int nbHorzCells;
  private int nbVertCells;

  private Ore[] ores;
  private Target[] targets;
  private Pusher pusher = null;
  private Bulldozer bulldozer = null;
  private Excavator excavator = null;
  private boolean finished = false;
  private Properties properties;
  private boolean isAutoMode;
  private double gameDuration;
  private List<String> controls;
  private int movementIndex;

  private final Analyser analyser;

  public OreSim(Properties properties, MapGrid grid) {

    super(grid.getNbHorzCells(), grid.getNbVertCells(), 30, false);
    GGBackground bg = getBg();
    drawBoard(bg);
    this.grid = grid;
    nbHorzCells = grid.getNbHorzCells();
    nbVertCells = grid.getNbVertCells();
    this.properties = properties;
    this.analyser = new Analyser(this);

    ores = new Ore[grid.getNbOres()];
    targets = new Target[grid.getNbOres()];

    isAutoMode = properties.getProperty("movement.mode").equals("auto");
    gameDuration = Integer.parseInt(properties.getProperty("duration"));
    setSimulationPeriod(Integer.parseInt(properties.getProperty("simulationPeriod")));
    controls = Arrays.asList(properties.getProperty("machines.movements").split(","));
  }

  /**
   * Check the number of ores that are collected
   * @return
   */

  public int checkOresDone() {
    int nbTarget = 0;
    for (int i = 0; i < grid.getNbOres(); i++)
    {
      if (ores[i].getIdVisible() == 1)
        nbTarget++;
    }

    return nbTarget;
  }

  /**
   * The main method to run the game
   * @param isDisplayingUI
   * @return
   */
  public String runApp(boolean isDisplayingUI) {
    GGBackground bg = getBg();
    drawBoard(bg);
    drawActors();
    addKeyListener(this);
    if (isDisplayingUI) {
      show();
    }

    if (isAutoMode) {
        doRun();
    }

    int oresDone = checkOresDone();
    double ONE_SECOND = 1000.0;
    while(oresDone < grid.getNbOres() && gameDuration >= 0) {
      try {
        Thread.sleep(simulationPeriod);
        double minusDuration = (simulationPeriod / ONE_SECOND);
        gameDuration -= minusDuration;
        String title = String.format("# Ores at Target: %d. Time left: %.2f seconds", oresDone, gameDuration);
        setTitle(title);
        if (isAutoMode) {

          if (controls != null && movementIndex < controls.size()) {
;
            String[] currentMove = controls.get(movementIndex).split("-");
            String machine = currentMove[0];
            machine = machine.replaceAll("\\*", "");
            setMovementIndex(movementIndex + 1);

            switch (machine) {
              case "P":
                pusher.autoMoveNext();
                break;
              case "B":
                bulldozer.autoMoveNext();
                break;
              case "E":
                excavator.autoMoveNext();
            }

          }
          analyser.updateLogResult();
        }

        oresDone = checkOresDone();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }

    doPause();

    if (oresDone == grid.getNbOres()) {
      setTitle("Mission Complete. Well done!");
    } else if (gameDuration < 0) {
      setTitle("Mission Failed. You ran out of time");
    }

    analyser.updateStatistics();
    finished = true;
    return analyser.getLogResult().toString();
  }


  /**
   * The method is automatically called by the framework when a key is pressed. Based on the pressed key, the pusher
   *  will change the direction and move
   * @param evt
   * @return
   */
  public boolean keyPressed(KeyEvent evt) {
    if (finished)
      return true;

    Location next = null;
    switch (evt.getKeyCode()) {
      case KeyEvent.VK_LEFT:
        next = pusher.getLocation().getNeighbourLocation(Location.WEST);
        pusher.setDirection(Location.WEST);
        break;
      case KeyEvent.VK_UP:
        next = pusher.getLocation().getNeighbourLocation(Location.NORTH);
        pusher.setDirection(Location.NORTH);
        break;
      case KeyEvent.VK_RIGHT:
        next = pusher.getLocation().getNeighbourLocation(Location.EAST);
        pusher.setDirection(Location.EAST);
        break;
      case KeyEvent.VK_DOWN:
        next = pusher.getLocation().getNeighbourLocation(Location.SOUTH);
        pusher.setDirection(Location.SOUTH);
        break;
    }

    Target curTarget = (Target) getOneActorAt(pusher.getLocation(), Target.class);
    if (curTarget != null){
      curTarget.show();
    }


    if (next != null && pusher.canMove(next)) {
      pusher.setLocation(next);
      analyser.updateLogResult();
    }
    refresh();
    return true;
  }

  public boolean keyReleased(KeyEvent evt)
  {
    return true;
  }


  /**
   * Draw all different actors on the board: pusher, ore, target, rock, clay, bulldozer, excavator
   */
  public void drawActors()
  {
    int oreIndex = 0;
    int targetIndex = 0;

    for (int y = 0; y < nbVertCells; y++)
    {
      for (int x = 0; x < nbHorzCells; x++)
      {
        Location location = new Location(x, y);
        ElementType a = grid.getCell(location);
        if (a == ElementType.PUSHER) {
          pusher = new Pusher(this);
          addActor(pusher, location);
          pusher.setUpMachine(controls, "P");
        }
        if (a == ElementType.ORE) {
          ores[oreIndex] = new Ore();
          addActor(ores[oreIndex], location);
          oreIndex++;
        }
        if (a == ElementType.TARGET) {
          targets[targetIndex] = new Target();
          addActor(targets[targetIndex], location);
          targetIndex++;
        }

        if (a == ElementType.ROCK) {
          addActor(new Rock(), location);
        }

        if (a == ElementType.CLAY) {
          addActor(new Clay(), location);
        }

        if (a == ElementType.BULLDOZER) {
          bulldozer = new Bulldozer(this);
          addActor(bulldozer, location);
          bulldozer.setUpMachine(controls, "B");

        }
        if (a == ElementType.EXCAVATOR) {
          excavator = new Excavator(this);
          addActor(excavator, location);
          excavator.setUpMachine(controls, "E");

        }
      }
    }
    System.out.println("ores = " + Arrays.asList(ores));
    setPaintOrder(Target.class);
  }

  /**
   * Draw the basic board with outside color and border color
   * @param bg
   */

  public void drawBoard(GGBackground bg){
    bg.clear(new Color(230, 230, 230));
    bg.setPaintColor(Color.darkGray);
    for (int y = 0; y < nbVertCells; y++)
    {
      for (int x = 0; x < nbHorzCells; x++)
      {
        Location location = new Location(x, y);
        ElementType a = grid.getCell(location);
        if (a != ElementType.OUTSIDE)
        {
          bg.fillCell(location, Color.lightGray);
        }
        if (a == ElementType.BORDER)  // Border
          bg.fillCell(location, borderColor);
      }
    }
  }

  public int getMovementIndex() {
    return movementIndex;
  }

  public void setMovementIndex(int movementIndex) {
    this.movementIndex = movementIndex;
  }

  public MapGrid getGrid() {
    return grid;
  }

  public boolean isFinished() {
    return finished;
  }

  public void setFinished(boolean finished) {
    this.finished = finished;
  }

  public Pusher getPusher() {
    return pusher;
  }

  public Bulldozer getBulldozer() {
    return bulldozer;
  }

  public Excavator getExcavator() {
    return excavator;
  }

  public Color getBorderColor() {
    return borderColor;
  }
}
