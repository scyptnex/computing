/*
 * Tank.cpp
 *
 *  Created on: 11/06/2014
 *      Author: scyptnex
 */

#include <GLFW/glfw3.h>

#include "Tank.h"

Tank::Tank() {
	cols[0] = 1.0f;
	cols[1] = 0.0f;
	cols[2] = 1.0f;
	state.x = 0;
	state.y = 0;
	state.v = 0;
	state.h = 0;
	state.turretH = 0;
}

Tank::~Tank() {

}

void Tank::accel(float f){
	state.v += f;
}

void Tank::rot(float f){
	state.h += f;
}

void Tank::draw() {

	glPushMatrix();
	glLoadIdentity();

	glTranslatef(state.x, state.y, 0);
	glRotatef(state.h, 0, 0, 1);

	glColor3f(cols[0], cols[1], cols[2]);

	glBegin(GL_LINE_LOOP);
	glVertex2f(15, 20);
	glVertex2f(15, -20);
	glVertex2f(-15, -20);
	glVertex2f(-15, 20);
	glEnd();

	glBegin(GL_LINE_LOOP);
	glVertex2f(15, 25);
	glVertex2f(15, -25);
	glVertex2f(25, -25);
	glVertex2f(25, 25);
	glEnd();

	glBegin(GL_LINE_LOOP);
	glVertex2f(-15, 25);
	glVertex2f(-15, -25);
	glVertex2f(-25, -25);
	glVertex2f(-25, 25);
	glEnd();

	glRotatef(state.turretH, 0, 0, 1);

	glBegin(GL_LINE_LOOP);
	glVertex2f(8, 8);
	glVertex2f(8, -8);
	glVertex2f(-8, -8);
	glVertex2f(-8, 8);
	glEnd();

	glBegin(GL_LINE_LOOP);
	glVertex2f(2, 35);
	glVertex2f(2, 0);
	glVertex2f(-2, 0);
	glVertex2f(-2, 35);
	glEnd();

	glPopMatrix();

}
