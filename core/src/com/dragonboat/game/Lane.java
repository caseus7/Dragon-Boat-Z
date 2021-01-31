package com.dragonboat.game;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import org.w3c.dom.Text;

/**
 * Represents a lane on the course.
 */
public class Lane {
    private final int LEFTBOUNDARY, RIGHTBOUNDARY;
    protected ArrayList<Obstacle> obstacles;
    private int obstacleLimit;
    private String gooseSpritePath = "gooseSouthsprite.png";
    private String logSpritePath = "logBig sprite.png";
    private String[] boostSpritePathPrefixes = {
        "acceleration",
        "health",
        "immune",
        "maneuverability",
        "speed" };
    private String boostSpritePathPostfix = "Boost.png";


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
        if (this.obstacles.size() <= this.obstacleLimit) {
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
        } else
            System.out.println("Obstacle limit reached.");
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
}
