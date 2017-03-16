#include "battlesphere.h"
#include <QApplication>

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    si::BattleSphere w;
    w.show();
    return a.exec();
}
