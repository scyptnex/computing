#include "PowerSupply.h"

#include "Utils.h"

PowerSupply::PowerSupply() : Part("Generic Supply"), m_watts(550)
{

}

PowerSupply::PowerSupply(const std::string &name, int watts)
    : Part(name), m_watts(watts)
{

}

PowerSupply::~PowerSupply()
{

}

PowerSupply::PowerSupply(const PowerSupply &powerSupply)
    : Part(powerSupply.m_name), m_watts(powerSupply.m_watts)
{

}

std::string PowerSupply::getPartInformation() const
{
    char maximumPower[5];
    xitoa(m_watts, maximumPower, 10);

    std::string partInformation ("Power Supply: " + m_name + ", Maximum Power: " + maximumPower + " Watts");
    return partInformation;
}
