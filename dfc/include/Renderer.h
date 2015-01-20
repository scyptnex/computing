/*
 * Renderer.h
 *
 *  Created on: 18/01/2015
 *      Author: scyptnex
 */

#ifndef RENDERER_H_
#define RENDERER_H_

#include <string>

class RendererEvent{
public:
	RendererEvent(std::string ty, std::string da) : type(ty), data(da) {}
	const std::string type;
	const std::string data;
};

class Renderer{
public:
	virtual ~Renderer() =0;
	virtual bool initialise() =0;
	virtual RendererEvent pollUserInput();
};


#endif /* RENDERER_H_ */

