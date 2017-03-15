#ifndef DIALOG_H
#define DIALOG_H

#include <QDialog>
#include <QPainter>
#include <QPixmap>

class Dialog : public QDialog
{
    Q_OBJECT
    QPixmap img;
public:
    Dialog(QWidget *parent = nullptr);
    ~Dialog();

protected:
    void paintEvent(QPaintEvent *event);

};

#endif // DIALOG_H
