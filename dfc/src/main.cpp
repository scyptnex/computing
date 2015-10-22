#include "libtcod.hpp"

#include <iostream>

int main() {
    int playerx=40,playery=25;
    TCODConsole::initRoot(80,50,"libtcod C++ tutorial",false);
    bool du = false;
    bool dd = false;
    bool dl = false;
    bool dr = false;
    while ( !TCODConsole::isWindowClosed() ) {
        TCOD_key_t key;
        TCODSystem::checkForEvent(TCOD_EVENT_KEY,&key,NULL);
        while(key.vk){
            switch(key.vk) {
                case TCODK_UP : du = key.pressed; break;
                case TCODK_DOWN : dd = key.pressed; break;
                case TCODK_LEFT : dl = key.pressed; break;
                case TCODK_RIGHT : dr = key.pressed; break;
                default:break;
            }
            TCODSystem::checkForEvent(TCOD_EVENT_KEY,&key,NULL);
        }
        if(du) playery--;
        if(dd) playery++;
        if(dr) playerx++;
        if(dl) playerx--;
        TCODSystem::setFps(25);
        TCODConsole::root->clear();
        TCODConsole::root->putChar(playerx,playery,'@');
        TCODConsole::flush();
    }
    return 0;
}
