#-------------------------------------------------
#
# Project created by QtCreator 2017-03-13T09:19:05
#
#-------------------------------------------------

QT       += core

QT       -= gui

TARGET = Week2
CONFIG   += console
CONFIG   += c++11
CONFIG   -= app_bundle

TEMPLATE = app


SOURCES += main.cpp \
    Car.cpp \
    Bus.cpp \
    Bicycle.cpp

HEADERS += \
    Vehicle.h \
    MotorVehicle.h \
    Car.h \
    Bus.h \
    Bicycle.h
