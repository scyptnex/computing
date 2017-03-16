#include "battlesphere.h"
#include <QTimer>

namespace si {

    BattleSphere::BattleSphere(QWidget *parent) : QDialog(parent) {
        defender.load(":/images/defender.png");
        bullet.load(":/images/fireball.png");
        setStyleSheet("background-color: #000000;");
        this->resize(600, 400);
        update();

        QTimer *timer = new QTimer(this);
        connect(timer, SIGNAL(timeout()), this, SLOT(nextFrame()));
        timer->start(32);
    }

    BattleSphere::~BattleSphere() {}

    void BattleSphere::paintEvent(QPaintEvent *event) {
        QPainter painter(this);
        painter.drawPixmap(dx, dy, defender);
        painter.drawPixmap(bx, by, bullet);
    }

    void BattleSphere::nextFrame() {
        // animate the defender
        int maxX = this->width()-defender.width();
        dx += ds;
        if(dx >= maxX){
            dx = (2*maxX)-dx;
            ds *= -1;
        } else if (dx <= 0) {
            dx *= -1;
            ds *= -1;
        }

        // shoot or animate the bullet
        if(by <= -100){
            bx = dx + (defender.width()/2) - (bullet.width()/2);
            by = dy - bullet.height();
        } else {
            by -= bs;
        }

        update();
    }

} // end namespace si
