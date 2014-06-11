//========================================================================
// Simple GLFW example
// Copyright (c) Camilla Berglund <elmindreda@elmindreda.org>
//
// This software is provided 'as-is', without any express or implied
// warranty. In no event will the authors be held liable for any damages
// arising from the use of this software.
//
// Permission is granted to anyone to use this software for any purpose,
// including commercial applications, and to alter it and redistribute it
// freely, subject to the following restrictions:
//
// 1. The origin of this software must not be misrepresented; you must not
//    claim that you wrote the original software. If you use this software
//    in a product, an acknowledgment in the product documentation would
//    be appreciated but is not required.
//
// 2. Altered source versions must be plainly marked as such, and must not
//    be misrepresented as being the original software.
//
// 3. This notice may not be removed or altered from any source
//    distribution.
//
//========================================================================
//! [code]

#include <GLFW/glfw3.h>

#include <stdlib.h>
#include <stdio.h>
#include <iostream>
#include <cmath>

#include "Tank.h"

static bool upPressed = false;
static bool downPressed = false;
static bool leftPressed = false;
static bool rightPressed = false;

static void error_callback(int error, const char* description) {
	fputs(description, stderr);
}

static void key_callback(GLFWwindow* window, int key, int scancode, int action,
		int mods) {
	if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS)
		glfwSetWindowShouldClose(window, GL_TRUE);
	if (key == GLFW_KEY_UP) {
		upPressed = action != GLFW_RELEASE;
	} else if (key == GLFW_KEY_DOWN) {
		downPressed = action != GLFW_RELEASE;
	} else if (key == GLFW_KEY_LEFT) {
		leftPressed = action != GLFW_RELEASE;
	} else if (key == GLFW_KEY_RIGHT) {
		rightPressed = action != GLFW_RELEASE;
	}
}

#define PI 3.14159

void gameTick(Tank& tan) {
	if (upPressed) {
		tan.accel(1);
	}
	if (downPressed) {
		tan.accel(-1);
	}
	if (leftPressed) {
		tan.rot(1);
	}
	if (rightPressed) {
		tan.rot(-1);
	}
	tan.state.x -= tan.state.v*sin(tan.state.h*(PI/180.0f));
	tan.state.y += tan.state.v*cos(tan.state.h*(PI/180.0f));
}

int main(void) {
	GLFWwindow* window;

	glfwSetErrorCallback(error_callback);

	if (!glfwInit())
		exit(EXIT_FAILURE);

	window = glfwCreateWindow(800, 600, "Simple example", NULL, NULL);
	if (!window) {
		glfwTerminate();
		exit(EXIT_FAILURE);
	}

	glfwMakeContextCurrent(window);

	glfwSetKeyCallback(window, key_callback);

	std::cout << "Drawing a window." << std::endl;

	Tank tan = Tank();
	tan.state.x = 50;
	tan.state.y = 50;

	long curTicks = 0;
	float tps = 25.0f;
	while (!glfwWindowShouldClose(window)) {
		float ratio;
		int width, height;

		for (; curTicks < (float) glfwGetTime() * tps; curTicks++) {
			gameTick(tan);
		}

		glfwGetFramebufferSize(window, &width, &height);
		ratio = width / (float) height;

		glViewport(0, 0, width, height);
		glClear(GL_COLOR_BUFFER_BIT);

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		//glOrtho(-ratio, ratio, -1.f, 1.f, -10, 10);
		glOrtho(0, width/2, 0, height/2, -10, 10);
		glMatrixMode(GL_MODELVIEW);

		glLoadIdentity();
		//glRotatef((float) glfwGetTime() * 10.f, 0.f, 0.f, 1.f);
		//glTranslatef((float) glfwGetTime() * 20.f, 0.f, 0.f);
		//tan.state.h = (float) glfwGetTime() * -10.f;
		//tan.state.turretH = (float) glfwGetTime() * 10.f;

		//drawCube(0, 0, 0);
		//drawTri();
		//glLineWidth(2.f);
		tan.draw();

		glfwSwapBuffers(window);
		glfwPollEvents();
	}

	glfwDestroyWindow(window);

	glfwTerminate();
	std::cout << "Byebye" << std::endl;
	exit(EXIT_SUCCESS);
}

//! [code]
