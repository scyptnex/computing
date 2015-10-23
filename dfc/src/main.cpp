#include "libtcod.hpp"
#include <iostream>
#include <algorithm>

struct GameModel{
    int p_x;
    int p_y;
    int g_width;
    int g_height;

    GameModel() : p_x(0), p_y(0), g_width(80), g_height(50) {}
};

struct GameController{
    virtual bool modify(GameModel&) = 0;
};

struct GameView{
    virtual void init(const GameModel&) = 0;
    virtual void render(const GameModel&) = 0;
};

struct TCODUI : public GameView, public GameModel{

    bool du = false;
    bool dd = false;
    bool dl = false;
    bool dr = false;

    virtual void init(const GameModel& game){
        TCODConsole::initRoot(game.g_width,game.g_height,"libtcod C++ tutorial",false);
        TCODSystem::setFps(25);
    }

    virtual void render(const GameModel& game){
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
        TCODConsole::root->clear();
        TCODConsole::root->putChar(game.p_x,game.p_y,'@');
        TCODConsole::flush();
    }

    virtual bool modify(GameModel& game){
        if(TCODConsole::isWindowClosed()) return false;
        if(du) game.p_y--;
        if(dd) game.p_y++;
        if(dr) game.p_x++;
        if(dl) game.p_x--;
        game.p_y = std::max(game.p_y, 0);
        game.p_x = std::max(game.p_x, 0);
        game.p_y = std::min(game.p_y, game.g_height-1);
        game.p_x = std::min(game.p_x, game.g_width-1);
        return true;
    }
};

using namespace std;

int main() {
    GameModel game;
    game.p_x = 40;
    game.p_y = 25;
    TCODUI ui;
    ui.init(game);
    while (true) {
        if(!ui.modify(game)) break;
        ui.render(game);
    }
    return 0;
}
