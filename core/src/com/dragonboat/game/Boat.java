package com.dragonboat.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Represents a Boat, controlled by either a Player or Opponent.
 *
 * @see Player
 * @see Opponent
 */
public class Boat {
    /*
     * Direct representation based off the UML diagram
     * https://drive.google.com/file/d/15O95umnJIoApnsj8I9ejEtMxrDGYJWAC/view?usp=sharing
     */

    private int ROBUSTNESS, MAXSPEED, MAX_DURABILITY;
    private float ACCELERATION, MANEUVERABILITY;

    public int boostTimer;
    public Texture texture;
    protected float yPosition, xPosition, penalties;
    protected int width = 56, height = 182;
    protected Lane lane;
    protected int leftBound;
    protected int rightBound;
    private DragonBoatGame game;
    private int boatNumber;
    private float currentSpeed, fastestLegTime, tiredness;
    private int durability;
    private Texture[] textureFrames;
    private int frameCounter;
    private String name;
    private boolean finished;
    private int threshold = 5;
    private boolean immune;
    private String boosted;
    private int nextTextureFrameCounterInit;
    // This is used to control how fast the animation progresses. When it gets
    // to 0 the next animation frame will be played
    private int nextTextureFrameCounter;
    // This caps the max speed the animation can progress. Each render frame
    // `nextTextureFrameCounter` can be reduced by at most this amount
    private int nextTextureFrameCounterReductionCap;
    private int nextTextureFrameCounterReductionCapBoosted;

    /**
     * Creates a Boat instance in a specified Lane.
     *
     * @param yPosition Y-position of the boat.
     * @param lane      Lane object.
     * @param name      String identifier.
     */
    public Boat(DragonBoatGame game, float yPosition, Lane lane, String name) {
        this.game = game;
        this.xPosition = lane.getRightBoundary() - (lane.getRightBoundary() - lane.getLeftBoundary()) / 2 - this.width / 2;
        this.yPosition = yPosition;
        this.currentSpeed = 0f;
        this.penalties = 0;
        this.tiredness = 0f;
        this.lane = lane;
        this.fastestLegTime = 0;
        this.textureFrames = new Texture[4];
        frameCounter = 0;
        this.name = name;
        this.immune = false;
        this.boosted = "";
        boostTimer = 0;
        this.nextTextureFrameCounterInit = 10;
        this.nextTextureFrameCounter = this.nextTextureFrameCounterInit;
        this.nextTextureFrameCounterReductionCap = 3;
        this.nextTextureFrameCounterReductionCapBoosted = 5;
        this.leftBound = 0;
        this.rightBound = Gdx.graphics.getWidth() - this.width;
    }

    /**
     * Decreases the x-position of the boat respective to the boat's maneuverability
     * and speed, and decreases the speed by 3%.
     */
    public void SteerLeft() {
        float newXPosition = Math.max(
            this.xPosition - this.MANEUVERABILITY * this.currentSpeed,
            this.xPosition - (this.xPosition - this.leftBound) );
        if (this.xPosition != newXPosition) {
            this.xPosition = newXPosition;
            this.currentSpeed *= 0.985;
        }
    }

    /**
     * Increases the x-position of the boat respective to the boat's maneuverability
     * and speed, and decreases the speed by 3%.
     */
    public void SteerRight() {
        float newXPosition = Math.min(
            this.xPosition + this.MANEUVERABILITY * this.currentSpeed,
            this.xPosition + (this.rightBound - this.xPosition) );
        if (this.xPosition != newXPosition) {
            this.xPosition = newXPosition;
            this.currentSpeed *= 0.985;
        }
    }

    /**
     * Increases the y-position of the boat respective to the boat's speed, and
     * decreases the speed by 0.08%.
     */
    public void MoveForward() {
        this.yPosition += this.currentSpeed;
        this.currentSpeed *= 0.9992;
    }

    /**
     * If the boat has enough stamina, increase the speed of the boat by the boat's
     * acceleration, if not, do nothing.
     */
    public void IncreaseSpeed() {
        if (this.tiredness <= 75) {
            this.currentSpeed = (this.currentSpeed + this.ACCELERATION) >= this.MAXSPEED ? this.MAXSPEED
                    : this.currentSpeed + this.ACCELERATION;
        }
    }

    /**
     * Decreases the speed of the boat by 0.015 if the resulting speed is greater
     * than 0.
     */
    public void DecreaseSpeed() {
        /*
         * Very basic deceleration function, acting as water friction. could be updated
         * using functions from
         * https://denysalmaral.com/2019/05/boat-sim-notes-1-water-friction.html to be
         * more realistic.
         */
        this.currentSpeed = (this.currentSpeed - 0.015) <= 0 ? 0 : this.currentSpeed - 0.015f;
    }

    /**
     * Checks each obstacle for a collision.
     *
     * --ASSESSMENT 2--
     * Also checks for collisions with power-ups.
     *
     * @param backgroundOffset How far up the course the player is.
     * @return Boolean representing if a collision occurs.
     */
    public boolean CheckCollisions(int backgroundOffset) {
        // Iterate through obstacles.
        ArrayList<Obstacle> obstacles = game.getObstacles();
        ArrayList<Integer> obstaclesToRemove = new ArrayList<>();
        for (Obstacle o : obstacles) {
            if (o.getX() > this.xPosition + threshold && o.getX() < this.xPosition + this.width - threshold) {
                if (o.getY() + backgroundOffset > this.yPosition + threshold
                        && o.getY() + backgroundOffset < this.yPosition + this.height - threshold) {
                    if (o instanceof Boost){
                        Boost(((Boost) o).getType());
                    } else {
                        if(immune == false) {
                            this.ApplyDamage(o.getDamage());
                        }
                    }
                    obstaclesToRemove.add(obstacles.indexOf(o));
                }
            }
        }
        for (int i : obstaclesToRemove) {
            // this.lane.RemoveObstacle(obstacles.get(i));
            obstacles.get(i).Remove();
            return true;
        }
        return false;
    }

    /**
     * --ASSESSMENT 2--
     * Defines the various different power-ups and what
     * effect they have on the boat.
     *
     * @param type The type of power-up.
     */
    public void Boost(String type){
        if (type == "health"){
            this.durability += 20;
        } else if (type == "acceleration"){
            this.ACCELERATION += 0.2f;
            boosted = "acceleration";
        } else if (type == "immune"){
            this.immune = true;
            boosted = "immune";
        } else if (type == "maneuverability"){
            this.MANEUVERABILITY += 0.2f;
            boosted = "maneuverability";
        } else if (type == "speed"){
            this.currentSpeed += 1;
            this.MAXSPEED += 1;
            boosted = "speed";
        }
//        System.out.println("MAN" + this.MANEUVERABILITY);
//        System.out.println("HEL" + this.durability);
//        System.out.println("ACC" + this.ACCELERATION);
//        System.out.println("SPD" + this.currentSpeed);
//        System.out.println("IMM" + this.immune);
    }

    /**
     * --ASSESSMENT 2--
     * Reverses the effects of a power-up that has been applied to
     * the boat; setting the stats back to the original values.
     */
    public void removeBoost(){
        System.out.println("Boost Removed");
        if (this.boosted == "acceleration") {
            this.ACCELERATION -= 0.2f;
        } else if (this.boosted == "immune"){
            this.immune = false;
        } else if (this.boosted == "speed"){
            System.out.println(this.currentSpeed);
            this.currentSpeed -= 1;
            this.MAXSPEED -= 1;
            System.out.println(this.currentSpeed);
        } else if (this.boosted == "maneuverability"){
            this.MANEUVERABILITY -= 0.2f;
        }
        this.boosted = "";
    }

    /**
     * Decreases the durability of the boat by the obstacle damage divided by the
     * boat's robustness. If the boat's robustness is 0 then just decrease by
     * the obstacle damage.
     *
     * @param obstacleDamage Amount of damage an Obstacle inflicts on the boat.
     * @return Boolean representing whether the durability of the boat is below 0.
     */
    public boolean ApplyDamage(int obstacleDamage) {
        int durabilityReduction;
        if (this.ROBUSTNESS == 0) {
            durabilityReduction = obstacleDamage;
        }
        else {
            durabilityReduction = obstacleDamage / this.ROBUSTNESS;
        }

        this.durability -= durabilityReduction;
        this.currentSpeed *= 0.9;
        return this.durability <= 0;
    }

    /**
     * Checks if the boat is between the left boundary and the right boundary of the Lane.
     *
     * @return Boolean representing whether the Boat is in the Lane.
     */
    public boolean CheckIfInLane() {
        return this.xPosition + threshold > this.lane.getLeftBoundary()
                && this.xPosition + this.width - threshold < this.lane.getRightBoundary();
    }

    /**
     * Updates the boat's fastest time.
     *
     * @param elapsedTime Time it took the boat to finish the current race.
     */
    public void UpdateFastestTime(float elapsedTime) {
        if (this.fastestLegTime > elapsedTime + this.penalties || this.fastestLegTime == 0) {
            this.fastestLegTime = elapsedTime + this.penalties;
        }
    }

    /**
     * Increases the tiredness of the boat by 0.75 if the tiredness is less than
     * 100.
     */
    public void IncreaseTiredness() {
        this.tiredness += this.tiredness >= 100 ? 0 : 0.75f;
    }

    /**
     * Decreases the tiredness of the boat by 1 if the tiredness is greater than 0.
     */
    public void DecreaseTiredness() {
        this.tiredness -= this.tiredness <= 0 ? 0 : 1f;
    }

    /**
     * Progresses the animation depending on the boat's speed
     */
    public void ProgressAnimation() {
        if (this.currentSpeed > 0) {
            int counterReductionCap = this.nextTextureFrameCounterReductionCap;
            // Increase the max speed of the animation while boosted
            if (this.boosted == "speed") {
                counterReductionCap = this.nextTextureFrameCounterReductionCapBoosted;
            }

            float speedRatio = this.currentSpeed / this.MAXSPEED;
            this.nextTextureFrameCounter = (int) (this.nextTextureFrameCounter
                - (speedRatio * counterReductionCap) );
            if (this.nextTextureFrameCounter <= 0) {
                AdvanceTextureFrame();
                this.nextTextureFrameCounter = this.nextTextureFrameCounterInit;
            }
        }
    }

    /**
     * Generates all frames for animating the boat.
     *
     * @param boatName Boat name, used to get correct asset.
     */
    public void GenerateTextureFrames(char boatName) {
        Texture[] frames = new Texture[4];
        for (int i = 1; i <= frames.length; i++) {
            frames[i - 1] = new Texture(Gdx.files.internal("boat" + boatName + " sprite" + i + ".png"));
        }
        this.setTextureFrames(frames);
    }

    /**
     * Resets necessary stats for the next race.
     */
    public void Reset() {
        this.xPosition = lane.getRightBoundary() - (lane.getRightBoundary() - lane.getLeftBoundary()) / 2 - width / 2;
        this.yPosition = 0;
        this.currentSpeed = 0f;
        this.penalties = 0;
        this.durability = MAX_DURABILITY;
        this.tiredness = 0f;
        this.finished = false;

    }

    /**
     * Resets the boat's fastest leg time.
     */
    public void ResetFastestLegTime() {
        this.fastestLegTime = 0;
    }

    // getters and setters

    /**
     *
     * @param t Texture to use.
     */
    public void setTexture(Texture t) {
        this.texture = t;
    }

    /**
     *
     * @param frames Texture frames for animation.
     */
    public void setTextureFrames(Texture[] frames) {
        this.textureFrames = frames;
    }

    /**
     *
     * @return Float representing fastest race/leg time.
     */
    public float getFastestTime() {
        return this.fastestLegTime;
    }

    /**
     *
     * @return Int representing x-position of boat.
     */
    public int getX() {
        return Math.round(this.xPosition);
    }

    /**
     *
     * @return Int representing y-position of boat.
     */
    public int getY() {
        return Math.round(this.yPosition);
    }

    /**
     * @return x-position of the boat without it being rounded to the closest integer.
     *
     * Used for testing.
     */
    public float getXPosition() {
        return this.xPosition;
    }

    /**
     * @return y-position of the boat without it being rounded to the closest integer.
     *
     * Used for testing.
     */
    public float getYPosition() {
        return this.yPosition;
    }

    /**
     *
     * @return Int representing the y coordinate range of the boat (length).
     */
    public int getHeight() {
        return this.height;
    }

    /**
     *
     * @return width of the boat
     */
    public int getWidth() {
        return this.width;
    }

    /**
     *
     * @return String representing name of the boat.
     */
    public String getName() {
        return this.name;
    }

    /**
     *
     * @return Boolean representing if the boat has finished the current race.
     */
    public boolean finished() {
        return this.finished;
    }

    /**
     *
     * @param f Boolean representing if the boat has finished the race.
     */
    public void setFinished(boolean f) {
        this.finished = f;
    }

    /**
     *--ASSESSMENT 2--
     *
     * @return Boolean representing whether a power-up has been applied.
     */
    public boolean isBoosted(){
        if (this.boosted == ""){
            return false;
        } else{
            return true;
        }
    }

    /**
     *
     * @return Float representing the current speed of the boat.
     */
    public float getCurrentSpeed() {
        return this.currentSpeed;
    }

    /**
     *
     * @param finishY Y-position of the finish line.
     * @return Float representing the progress of the boat from 0 to 1.
     */
    public float getProgress(int finishY) {
        return Math.min((this.yPosition) / finishY, 1);
    }

    /**
     * <p>
     * Assigns the selected boat template to the boat.
     * </p>
     * <p>
     * This includes stats and texture.
     * </p>
     *
     * @param boatNo Number of the boat template selected.
     */
    public void ChooseBoat(int boatNo) {
        this.boatNumber = boatNo;
        char boatLabel = (char) (65 + boatNo);
        this.setTexture(new Texture(Gdx.files.internal("boat" + boatLabel + " sprite1.png")));
        this.GenerateTextureFrames(boatLabel);
        this.setStats(boatLabel);
        this.Reset();
    }

    /**
     * Implicitly sets the stats of the boat, given each attribute.
     *
     * @param maxspeed        Top speed the boat can reach.
     * @param robustness      How resilient to obstacle damage the boat is.
     * @param acceleration    How much the speed increases each frame.
     * @param maneuverability How easily the boat can move left or right.
     */
    public void setStats(
            int maxspeed,
            int maxDurability,
            int robustness,
            float acceleration,
            float maneuverability) {
        this.MAXSPEED = maxspeed;
        this.MAX_DURABILITY = maxDurability;
        this.ROBUSTNESS = robustness;
        this.ACCELERATION = acceleration;
        this.MANEUVERABILITY = maneuverability;
    }

    /**
     * Interpolates predetermined stats from a boat label, and sets the stats based
     * on those.
     *
     * @param boatLabel A character between A-G representing a specific boat.
     */
    public void setStats(char boatLabel) {
        int[] maxspeeds = { 5, 4, 5, 5, 4, 7, 5 };
        int[] maxDurabilities = { 50, 50, 50, 50, 50, 50, 50 };
        int[] robustnesses = { 2, 4, 1, 4, 8, 3, 5 };
        float[] accelerations = { 0.094f, 0.031f, 0.125f, 0.063f, 0.047f, 0.022f, 0.031f };
        float[] maneuverabilities = { 0.375f, 1f, 0.375f, 0.5f, 0.25f, 0.125f, 0.625f };

        int boatNo = boatLabel - 65;

        this.setStats(maxspeeds[boatNo], maxDurabilities[boatNo], robustnesses[boatNo], accelerations[boatNo], maneuverabilities[boatNo]);
    }

    public int getBoatNumber() {
        return this.boatNumber;
    }

    /**
     *
     * @return Float representing the maneuverability of the boat.
     */
    public float getManeuverability() {
        return this.MANEUVERABILITY;
    }

    /**
     *
     * @return Float representing the acceleration of the boat.
     */
    public float getAcceleration() {
        return this.ACCELERATION;
    }

    /**
     *
     * @return Int representing the robustness of the boat.
     */
    public int getRobustness() {
        return this.ROBUSTNESS;
    }

    /**
     *
     * @return Int representing the maximum durability of the boat.
     */
    public int getMaxDurability() {
        return this.MAX_DURABILITY;
    }

    /**
     *
     * @return Int representing the durability of the boat.
     */
    public int getDurability() {
        return this.durability;
    }

    /**
     *
     * @return Int representing the maximum speed of the boat.
     */
    public int getMaxSpeed() {
        return this.MAXSPEED;
    }

    /**
     *
     * @return Float representing the tiredness of the boat crew.
     */
    public float getTiredness() {
        return this.tiredness;
    }

    /**
     *--ASSESSMENT 2--
     *
     * @return Boolean representing whether a boat has the immune power-up.
     */
    public boolean getImmune() { return this.immune;}

    /**
     * --ASSESSMENT 2--
     *
     * @return String representing the type of boost applied.
     */
    public String getBoosted() {
        return this.boosted;
    }

    /**
     *
     * @return Float representing the time penalty incurred for the current race.
     */
    public float getPenalty() {
        return this.penalties;
    }

    /**
     *
     * @param penalty Float to add to the boat's penalty total for the current race.
     */
    public void applyPenalty(float penalty) {
        this.penalties += penalty;
    }

    /**
     *
     * @param lane Lane object for the boat.
     */
    public void setLane(Lane lane) {
        this.lane = lane;
        this.xPosition = lane.getRightBoundary() - (lane.getRightBoundary() - lane.getLeftBoundary()) / 2 - width / 2;
    }

    /**
     * Sets the minimum x position of the boat.
     */
    public void setLeftBound(int leftBound) {
        this.leftBound = leftBound;
    }

    /**
     * Sets the maximum x position of the boat.
     */
    public void setRightBound(int rightBound) {
        this.rightBound = rightBound;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setXPosition(float x) {
        this.xPosition = x;
    }

    public void setYPosition(float y) {
        this.yPosition = y;
    }

    public void setCurrentSpeed(float speed) {
        this.currentSpeed = speed;
    }

    public void setDurability(int dur) {
        this.durability = dur;
    }

    public void setTiredness(float tire) {
        this.tiredness = tire;
    }

    public void setImmune(boolean immu) {
        this.immune = immu;
    }

    public void setBoosted(String boostType) {
        this.boosted = boostType;
    }

    public void setBoostTimer(int time) {
        this.boostTimer = time;
    }

    public void setPenalties(float pen) {
        this.penalties = pen;
    }

    public void setFastestLegTime(float time) {
        this.fastestLegTime = time;
    }

    /**
     * Sets the xPosition of the boat
     */
    public void setXPosition(int xPosition) { this.xPosition = xPosition; }

    /**
     * Advances to the next frame of the animation
     */
    private void AdvanceTextureFrame() {
        this.frameCounter = this.frameCounter == this.textureFrames.length - 1 ? 0 : this.frameCounter + 1;
        this.setTexture(this.textureFrames[this.frameCounter]);
    }
}
