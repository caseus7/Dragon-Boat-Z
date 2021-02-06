package com.dragonboat.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import org.w3c.dom.Text;

/**
 * Represents a lane on the course.
 */
public class Lane {
    private final int LEFTBOUNDARY, RIGHTBOUNDARY;
    protected ArrayList<Obstacle> obstacles;
    private int obstacleLimit;
    private static String gooseSpritePath = "gooseSouthsprite.png";
    private static String logSpritePath = "logBig sprite.png";
    private static String[] boostSpritePathPrefixes = {
        "acceleration",
        "health",
        "immune",
        "maneuverability",
        "speed" };
    private static String boostSpritePathPostfix = "Boost.png";


    /**
     * Creates a lane instance.
     *
     * @param leftBoundary  X-position for the left boundary of the lane.
     * @param rightBoundary X-position for the right boundary of the lane.
     */
    public Lane(int leftBoundary, int rightBoundary) {
        this.LEFTBOUNDARY = leftBoundary;
        this.RIGHTBOUNDARY = rightBoundary;
        this.obstacleLimit = 10;

        obstacles = new ArrayList<>();
    }

    /**
     * Creates a lane instance.
     *
     * @param leftBoundary  X-position for the left boundary of the lane.
     * @param rightBoundary X-position for the right boundary of the lane.
     * @param obstacleLimit Limit for the number of obstacles in the lane.
     */
    public Lane(int leftBoundary, int rightBoundary, int obstacleLimit) {
        this.LEFTBOUNDARY = leftBoundary;
        this.RIGHTBOUNDARY = rightBoundary;
        this.obstacleLimit = obstacleLimit;

        obstacles = new ArrayList<>();
    }

    /**
     * <p>
     * Spawns obstacle in the lane.
     * </p>
     * <p>
     * Spawns specified obstacle in the lane. Checks that the obstacle limit hasn't
     * been reached, if not checks the obstacle type for Goose or Log and
     * instantiates it as the corresponding obstacle, with the correct texture. Then
     * adds it to the Lane's obstacle list.
     * </p>
     *
     * @param x            X-position for the obstacle spawn location.
     * @param y            Y-position for the obstacle spawn location.
     * @param obstacleType Obstacle type.
     */
    public void SpawnObstacle(int x, int y, String obstacleType) {
        if (this.obstacles.size() < this.obstacleLimit) {
            if (obstacleType.equals("Goose")) {
                Goose goose = new Goose(x, y, new Texture(Gdx.files.internal(gooseSpritePath)), this);
                this.obstacles.add(goose);

            } else if (obstacleType.equals("Log")) {
                Log log = new Log(x, y, new Texture(Gdx.files.internal(logSpritePath)), this);
                this.obstacles.add(log);

            } else if (obstacleType.equals("Boost")){
                String boostType = boostSpritePathPrefixes[ThreadLocalRandom.current().nextInt(3, 5)];
                Boost boost = new Boost(
                    x, y,
                    new Texture(Gdx.files.internal(boostType + boostSpritePathPostfix)),
                    this,
                    boostType );
                this.obstacles.add(boost);
            }
        }
    }

    /**
     * <p>
     * Removes obstacle from obstacle list.
     * </p>
     * <p>
     * Obstacle should be removed upon collision with boat or leaving the course.
     * area.
     * </p>
     *
     * @param obstacle Obstacle to be removed.
     */
    public void RemoveObstacle(Obstacle obstacle) {
        this.obstacles.remove(obstacle);
    }

    /**
     * --ASSESSMENT 2--
     * Removes all obstacles in the lane.
     */
    public void removeAllObstacles() {
        this.obstacles = new ArrayList<Obstacle>();
    }

    // getters and setters

    /**
     *
     * @return Int representing the x-position of the lane's left boundary.
     */
    public int getLeftBoundary() {
        return this.LEFTBOUNDARY;
    }

    /**
     *
     * @return Int representing the x-position of the lane's right boundary.
     */
    public int getRightBoundary() {
        return this.RIGHTBOUNDARY;
    }

    /**
    * @return All the obstacles in the lane
    */
    public ArrayList<Obstacle> getObstacles() {
        return this.obstacles;
    }

    /**
    * @return The number of obstacles in the lane
    */
    public int getObstacleCount() {
        return this.obstacles.size();
    }

    /**
    * @return The maximum number of obstacles that can be in the lane
    */
    public int getObstacleLimit() {
        return this.obstacleLimit;
    }

    /**
     * --ASSESSMENT 2--
     * Sets a new obstacle limit.
     *
     * @param newLimit Int representing the new max number of obstacles to be spawned.
     */
    public void setObstacleLimit(int newLimit) {
        this.obstacleLimit = newLimit;
    }

    /**
     * --ASSESSMENT 2--
     * Converts data about the instance into JSON so it can be recreated later.
     *
     * @return JSON string storing the instance's info.
     */
    public String toJSON() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("className", "Lane");
        data.put("LEFTBOUNDARY", this.LEFTBOUNDARY);
        data.put("RIGHTBOUNDARY", this.RIGHTBOUNDARY);
        data.put("obstacleLimit", this.obstacleLimit);

        // Add obstacle data
        ArrayList<String> obstacleData = new ArrayList<>();
        for (Obstacle obst : this.obstacles) {
            if (obst instanceof Goose) {
                Goose goose = (Goose) obst;
                obstacleData.add(goose.toJSON());
            }
            else if (obst instanceof Log) {
                Log log = (Log) obst;
                obstacleData.add(log.toJSON());
            }
            else if (obst instanceof Boost) {
                Boost boost = (Boost) obst;
                obstacleData.add(boost.toJSON());
            }
        }
        data.put("obstacles", obstacleData);

        return IO.toJSON(data);
    }

    /**
     * --ASSESSMENT 2--
     * Creates a Lane object from the data passed.
     *
     * @param data HashMap storing data about a Lane object, likely gained by converting an instance to JSON first.
     * @return Lane representing the information about the lane.
     */
    public static Lane makeLane(HashMap<String, Object> data) {
        int _leftBoundary = (int) data.get("LEFTBOUNDARY");
        int _rightBoundary = (int) data.get("RIGHTBOUNDARY");
        int _obstacleLimit = (int) data.get("obstacleLimit");
        Lane lane = new Lane(_leftBoundary, _rightBoundary, _obstacleLimit);

        // Create and add the obstacles to the lane
        Array<String> obstacleStrings = (Array) data.get("obstacles");
        for (int i = 0; i < obstacleStrings.size; i++) {
            HashMap<String, Object> obstacleData =
                IO.hashMapFromJSON(obstacleStrings.get(i));
            String _class = (String) obstacleData.get("className");
            if (_class.equals("Goose")) {
                Texture tex = new Texture(Gdx.files.internal(gooseSpritePath));
                Goose goose = Goose.makeGoose(obstacleData, tex, lane);
                lane.obstacles.add(goose);
            }
            else if (_class.equals("Log")) {
                Texture tex = new Texture(Gdx.files.internal(logSpritePath));
                Log log = Log.makeLog(obstacleData, tex, lane);
                lane.obstacles.add(log);
            }
            else if (_class.equals("Boost")) {
                String boostType = (String) obstacleData.get("type");
                String texPath =
                    obstacleData.get("type") + boostSpritePathPostfix;
                Texture tex = new Texture(Gdx.files.internal(texPath));
                Boost boost = Boost.makeBoost(obstacleData, tex, lane);
                lane.obstacles.add(boost);
            }
        }
        return lane;
    }
}
