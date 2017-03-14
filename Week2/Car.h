#ifndef CAR_H
#define CAR_H

#include "MotorVehicle.h"

namespace week02 {

    class Car : public MotorVehicle
    {
        public:
            Car(int numberOfPassengers,
                    int topSpeed,
                    double kilometresPerLitre,
                    int numberOfAirBags = 2,
                    bool abs = true,
                    int numberOfWheels = 4);

            Car(int numberOfPassengers,
                    int topSpeed,
                    double kilometresPerLitre,
                    std::string color,
                    int numberOfAirBags = 2,
                    bool abs = true,
                    int numberOfWheels = 4);

            virtual ~Car() = default;

            int getSafetyRating();

        protected:
            int m_numberOfAirBags;
            bool m_abs;
    };

} // end namespace week02

#endif // CAR_H
