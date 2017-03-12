#ifndef VEHICLE_H
#define VEHICLE_H

#include <string>

namespace vehicle
{
    class Vehicle
    {
    public:
        Vehicle(int numberOfPassengers, int topSpeed, int numberOfWheels, std::string colour = "red") :
              m_numberOfPassengers(numberOfPassengers),
              m_topSpeed(topSpeed),
              m_numberOfWheels(numberOfWheels),
              m_colour(colour) {}

        virtual ~Vehicle() = default;

        virtual std::string getColor() { return m_colour; }

        virtual int getTopSpeed() { return m_topSpeed; }

        virtual int getNumberOfWheels() { return m_numberOfWheels; }

        virtual int getNumberOfPassengers() { return m_numberOfPassengers; }

        virtual int getSafetyRating() = 0;

    protected:
        int m_numberOfPassengers;
        int m_topSpeed;
        int m_numberOfWheels;
        std::string m_colour;
    };
}

#endif // VEHICLE_H
