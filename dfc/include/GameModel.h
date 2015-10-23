/* 
 *                               GameModel.h
 * 
 * Author: Nic H.
 * Date: 2015-Oct-23
 * 
 * The global state of the current game instance.  The game has exactly one
 * GameModel, which is read by GameViews and modified by GameControllers.
 * By definition the views only have read-access to the model, and the
 * controllers run in sequence (in the same thread), therefore the game
 * model does not need global locks to ensure thread safety.
 */

#ifndef __GAME_MODEL_H__
#define __GAME_MODEL_H__

#include <iostream>

struct GameModel {
};

#endif /* __GAME_MODEL_H__ */
