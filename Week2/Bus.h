#ifndef BUS_H
#define BUS_H

#include "MotorVehicle.h"

namespace week02 {

    class Bus : public MotorVehicle
    {
        public:
            Bus(int numberOfPassengers,
                    int topSpeed,
                    double kilometresPerLitre,
                    bool seatBeltsInstalled = false,
                    bool standingPassengersAllowed = true,
                    int numberOfWheels = 6);

            Bus(int numberOfPassengers,
                    int topSpeed,
                    double kilometresPerLitre,
                    std::string color,
                    bool seatBeltsInstalled = false,
                    bool standingPassengersAllowed = true,
                    int numberOfWheels = 6);

            virtual ~Bus() = default;

            int getSafetyRating();

        protected:
            bool m_seatBeltsInstalled;
            bool m_standingPassengersAllowed;
    };

} // end namespace week02

#endif // BUS_H
