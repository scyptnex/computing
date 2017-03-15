#include "dialog.h"
#include <QApplication>
#include <QDir>
#include <iostream>

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    Dialog w;
    w.show();

    std::cout << QDir::currentPath().toStdString() << std::endl;
    
    return a.exec();
}
