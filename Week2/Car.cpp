#include "Car.h"

namespace vehicle {

    Car::Car(int numberOfPassengers,
             int topSpeed,
             double kilometresPerLitre,
             int numberOfAirBags,
             bool abs,
             int numberOfWheels) :
        MotorVehicle(numberOfPassengers, topSpeed, numberOfWheels, kilometresPerLitre),
        m_numberOfAirBags(numberOfAirBags),
        m_abs(abs) {}

    Car::Car(int numberOfPassengers,
             int topSpeed,
             double kilometresPerLitre,
             std::string color,
             int numberOfAirBags,
             bool abs,
             int numberOfWheels) :
        MotorVehicle(numberOfPassengers, topSpeed, numberOfWheels, color, kilometresPerLitre),
        m_numberOfAirBags(numberOfAirBags),
        m_abs(abs) {}

    int Car::getSafetyRating() {
        int SafetyRating = 0;
        if (m_numberOfAirBags >= 4)
        {
            SafetyRating += 3;
        }
        else if (m_numberOfAirBags >= 2)
        {
            SafetyRating += 2;
        }
        else if (m_numberOfAirBags > 0)
        {
            SafetyRating += 1;
        }

        if (m_abs)
        {
            SafetyRating += 2;
        }
        return SafetyRating;
    }

} // end namespace vehicle
