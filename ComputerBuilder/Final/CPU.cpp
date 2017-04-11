#include "CPU.h"

#include <sstream>
#include <iomanip>

using namespace std;

CPU::CPU() :
    Part(""),
    m_clockSpeed(0),
    m_socketType("")
{}

CPU::CPU(const CPU &cpu) :
    Part(cpu.m_name),
    m_clockSpeed(cpu.m_clockSpeed),
    m_socketType(cpu.m_socketType)
{}

CPU::CPU(const string& name, double clockSpeed, const string& socketType) :
    Part(name),
    m_clockSpeed(clockSpeed),
    m_socketType(socketType)
{}


CPU::~CPU() {}


string CPU::getPartInformation() const {
    stringstream ss;
    ss << "CPU: " << getPartName() << ", Clock Speed: " << std::fixed << std::setprecision(1) << m_clockSpeed << " Ghz, Socket: " << m_socketType;
    return ss.str();
}
