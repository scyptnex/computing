#include <functional>
#include <iostream>

void doer(std::function<void()>& func){
    func();
}

struct stat {
    static int count;
    static void callback(){
        ++count;
    }
};
int stat::count = 0;

struct inst {
    int count = 0;
    void callback(){
        count++;
    }
};


int main(){
    std::cerr << stat::count << std::endl;
    std::function<void()> stater = stat::callback;
    doer(stater);
    std::cerr << stat::count << std::endl;
    std::cerr << std::endl;
    inst in;
    std::cerr << in.count << std::endl;
    std::function<void()> inster = in.callback;
    doer(inster);
    std::cerr << in.count << std::endl;
    return 0;
}

