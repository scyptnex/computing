#ifndef BICYCLE_H
#define BICYCLE_H

#include "Vehicle.h"

namespace vehicle {

    class Bicycle : public Vehicle
    {
    public:
        Bicycle(int numberOfPassengers,
            int topSpeed,
            bool helmet = false,
            int numberOfWheels = 2);

        Bicycle(int numberOfPassengers,
            int topSpeed,
            std::string color,
            bool helmet = false,
            int numberOfWheels = 2);

        virtual ~Bicycle() = default;

        int getSafetyRating();

    protected:
        bool m_helmetUsed;
    };

} // end namespace vehicle

#endif // BICYCLE_H
