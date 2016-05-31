#ifndef DIALOG_H
#define DIALOG_H

#include "body.h"
#include <QDialog>
#include <QTimer>
#include <QPainter>

namespace Ui {
class Dialog;
}

class Dialog : public QDialog
{
    Q_OBJECT

public:
    explicit Dialog(QWidget *parent = 0);
    ~Dialog();

protected:
    void paintEvent(QPaintEvent *event);

private:
    Ui::Dialog *ui;
    Body m_sun, m_earth, m_venus;

public slots:
    void nextFrame();
};

#endif // DIALOG_H
