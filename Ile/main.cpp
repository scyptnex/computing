#include <QFile>
#include <QTextStream>
#include <iostream>

int main(int argc, char *argv[])
{
    QFile file("in.txt");
    if (!file.open(QIODevice::ReadWrite | QIODevice::Text)){
        std::cout << "Error" << std::endl;
        return 1;
    }
    int lines = 0;
    while (!file.atEnd()) {
        QByteArray line = file.readLine();
        std::cout << lines << ": " << line.trimmed().data() << std::endl;
        lines++;
    }
    QTextStream out(&file);
    out << "line " << lines << "\n";
    file.close();
    return 0;
}
