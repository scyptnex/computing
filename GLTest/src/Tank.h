/*
 * Tank.h
 *
 *  Created on: 11/06/2014
 *      Author: scyptnex
 */

#ifndef TANK_H_
#define TANK_H_

struct TankState {
	float x;
	float y;
	float h;
	float v;
	float turretH;
};

class Tank {
private:
	float cols[3];
public:
	TankState state;
	Tank();
	virtual ~Tank();
	void draw();
	void accel(float acc);
	void rot(float r);
};

#endif /* TANK_H_ */
