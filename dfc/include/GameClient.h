/*
 * GameClient.h
 *
 *  Created on: 19/01/2015
 *      Author: scyptnex
 */

#ifndef GAMECLIENT_H_
#define GAMECLIENT_H_

#include <string>

class GameClient {
public:
	GameClient();
	virtual ~GameClient();
	void defineGameProperty(unsigned int ident, const std::string& property, unsigned char* propertyData);
	void updateGameState();
};

#endif /* GAMECLIENT_H_ */
