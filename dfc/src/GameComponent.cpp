
#include "GameComponent.h"

using namespace std;

namespace dfc{

GameComponent::GameComponent(size_t id, const string& name, size_t size) : componentName(name), componentID(id), dataSize(size), data(new unsigned char[size]) {
}

GameComponent::~GameComponent(){
    delete[] data;
}

string GameComponent::getName() const{
    return componentName;
}

} // namespace dfc

