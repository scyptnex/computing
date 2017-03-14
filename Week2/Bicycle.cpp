#include "Bicycle.h"

namespace week02 {

    Bicycle::Bicycle(int numberOfPassengers,
            int topSpeed,
            bool helmet,
            int numberOfWheels) :
        Vehicle(numberOfPassengers, topSpeed, numberOfWheels),
        m_helmetUsed(helmet) {}

    Bicycle::Bicycle(int numberOfPassengers,
            int topSpeed,
            std::string colour,
            bool helmet,
            int numberOfWheels) :
        Vehicle(numberOfPassengers, topSpeed, numberOfWheels, colour),
        m_helmetUsed(helmet) {}

    int Bicycle::getSafetyRating()
    {
        if (m_helmetUsed)
        {
            return 5;
        }
        else
        {
            return 0;
        }
    }

} // end namespace week02
