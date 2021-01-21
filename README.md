# Contents
- [About Us](#about-us)
- [Game Description](#game-description)
- [Assessment Content](#assessment-content)
  - [Deliverables](#deliverables)
  - [Executables](#executables)
  - [Javadocs](#javadocs)
  - [Weekly Snapshots](#weekly-snapshots)

# About Us

<img src="core/assets/dragonboatz Logo.png">

Ever wanted to race Dragon boats down the River Ouse? 
Of course, the answer is yes and we have just the game for you.
DragonBoat Z!

Our Team consists of:
* Dan Atkinson
* Divyansh Pandey
* Eliot Sheehan
* Hannah Pope
* Matthew Turner
* Mayan Lamont

# Game Description

<img src="core/assets/example screen for website.png">

Dragon Boat Z is a single-player Boat Racing game based on the annual Dragon Boat Race held in York along the
River Ouse.

In Dragon Boat Z, the player competes against 6 AI opponents, racing their dragon boats across 3 legs to achieve the fastest time to cross the finish line.

Upon starting the game, the player selects 1 of 7 boats as their boat that they would like to race with.
Every boat has 4 statistics with each boat having a different distribution of these statistics.

## Boat Statistics
- <strong>Robustness</strong>
  - Determines how much damage a boat can take. 
  - A boat with higher robustness will lose a smaller percentage damage to the durability upon collision with an obstacle.
- <strong>Manoeuvrability</strong>
  - Determines how fast the boat can avoid obstacles. 
  - A boat with higher manoeuvrability will be able to move side to side better without losing speed.
- <strong>Max Speed</strong>
  - Determines how fast a boat can go. 
  - A boat with higher max speed will be able to go faster than other boats.
- <strong>Acceleration</strong>
  - Determines how quickly a boat can achieve its max speed. 
  - A boat with higher acceleration will achieve its max speed before other boats.

## Obstacles and Penalties
During the races, there are a series of obstacles that will be floating down the Ouse. The player must avoid these obstacles in order to not damage their boat.
If the boat's durability is reduced to 0 at any point within the game, the game ends and the player loses. So, watch out for those geese!!
Whilst navigating the obstacles, the player must make sure to stay in their lane to avoid incurring a time penalty.

## Winning the Game
If the player manages to achieve one of the 3 fastest times across the 3 legs, they will qualify for the final race where they will compete against the other 2 fastest boats.
Upon completing the final race, the player will be awarded a medal, bronze, silver, or gold respective to their finishing position.
