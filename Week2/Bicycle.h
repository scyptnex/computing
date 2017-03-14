#ifndef BICYCLE_H
#define BICYCLE_H

#include "Vehicle.h"

namespace week02 {

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

} // end namespace week02

#endif // BICYCLE_H
