/*
 * GameComponent.h
 *
 * Stores a single game component data.  All the game data is stored in an
 * array/map of these components, a smart interface may be available to query
 * or modify that index
 */

#ifndef __GAME_COMPONENT_H__
#define __GAME_COMPONENT_H__

#include <string>

namespace dfc{

class GameComponent{
private:
    const std::string componentName;
    const size_t componentID;
    const size_t dataSize;
    const unsigned char* data;
public:
    GameComponent(size_t id, const std::string& name, size_t size);
    virtual ~GameComponent();

    //getters
    std::string getName() const;
};

} // namespace dfc

#endif /*__GAME_COMPONENT_H__*/
