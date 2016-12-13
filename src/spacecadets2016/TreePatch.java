package spacecadets2016;

import java.awt.Color;

import robocode.AdvancedRobot;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.ScannedRobotEvent;

public class TreePatch extends AdvancedRobot {

	double speed = 15;
	// a few variables, some of which I never used
	double previousH, nowH, radarBearing, gunBearing;
	boolean lockOn;

	double bearing;
	double power = 0;

	@Override
	public void run() {

		setBodyColor(Color.green);
		setGunColor(Color.green);
		setRadarColor(Color.green);

		previousH = 0;
		nowH = 0;

		lockOn = false;
		setAdjustRadarForGunTurn(true);
		setAdjustRadarForRobotTurn(true);
		setAdjustGunForRobotTurn(true);

		while (true) {

			setTurnRadarLeft(360);
			
				setTurnRight(speed);
				setAhead(speed);
			

			execute();
		}
	}

	@Override
	public void onScannedRobot(ScannedRobotEvent e) {

		// get the bearing
		bearing = getHeading() + e.getBearing();
		power = 0;

		// calculate the bearing for the gun and radar because they're both
		// seperate
		gunBearing = bearing - getGunHeading();
		radarBearing = bearing - getRadarHeading();

		setTurnGunRight(gunBearing);
		setTurnRadarRight(radarBearing);

		setTurnRight(speed);
		setAhead(speed);

		if (getGunHeat() == 0 && this.getGunTurnRemaining() < 5) {

			if (e.getDistance() > 300) {

				power = 1;
			} else if (e.getDistance() <= 300 && e.getDistance() > 200) {
				power = 1.5;
			} else if (e.getDistance() <= 200 && e.getDistance() > 100) {
				power = 2;
			} else if (e.getDistance() <= 100) {
				power = 3;
			}
			if (this.getEnergy() < 40) {
				//System.out.println("energy");
				power = 1;
			}
			if (this.getEnergy() > 20 || e.getDistance() < 30) {
				setFire(power);
			}

		}
		execute();
	}

	@Override
	public void onHitWall(HitWallEvent event) {

	}

	@Override
	public void onHitByBullet(HitByBulletEvent event) {

		bearing = getHeading() + event.getBearing();
		power = 0;

		gunBearing = bearing - getGunHeading();
		radarBearing = bearing - getRadarHeading();

		setTurnGunRight(gunBearing);

		if (getGunHeat() == 0 && this.getGunTurnRemaining() < 5) {

			power = 2;

			if (this.getEnergy() < 20) {
				power = 1;
			}
			setFire(power);
		}
		execute();
	}

	@Override
	public void onHitRobot(HitRobotEvent event) {
	
		stop();
		bearing = getHeading() + event.getBearing();
		power = 0;

		gunBearing = bearing - getGunHeading();
		radarBearing = bearing - getRadarHeading();

		setTurnGunRight(gunBearing);

		setTurnRadarRight(radarBearing);

		if (getGunHeat() == 0 && this.getGunTurnRemaining() < 5) {

			power = 3;

			if (this.getEnergy() < 20) {
				power = 2.5;
			}
			if (this.getEnergy() < 10)
				power = 1;
			setFire(power);
		}
		execute();
		
	}
}
