#include "Bus.h"

namespace vehicle {

    Bus::Bus(int numberOfPassengers,
             int topSpeed,
             double kilometresPerLitre,
             bool sb,
             bool stand,
             int numberOfWheels) :
        MotorVehicle(numberOfPassengers, topSpeed, numberOfWheels, kilometresPerLitre),
        m_seatBeltsInstalled(sb),
        m_standingPassengersAllowed(stand) {}

    Bus::Bus(int numberOfPassengers,
             int topSpeed,
             double kilometresPerLitre,
             std::string color,
             bool sb,
             bool stand,
             int numberOfWheels) :
        MotorVehicle(numberOfPassengers, topSpeed, numberOfWheels, color, kilometresPerLitre),
        m_seatBeltsInstalled(sb),
        m_standingPassengersAllowed(stand) {}

    int Bus::getSafetyRating()
    {
        int SafetyRating = 0;

        if (m_seatBeltsInstalled)
        {
            SafetyRating += 3;
        }
        if (!m_standingPassengersAllowed)
        {
            SafetyRating += 2;
        }
        return SafetyRating;
    }

} // end namespace vehicle
