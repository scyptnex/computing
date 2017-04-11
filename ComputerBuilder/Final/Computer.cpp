#include "Computer.h"
#include "PartAllocator.h"

#include <sstream>

int Computer::numberOfRequiredParts = 7;

Computer::Computer() :
    ComputerPlan(),
    m_computerType(""),
    m_requiredParts(new Part*[numberOfRequiredParts]),
    m_additionalParts(0),
    m_numberOfAdditionalParts(-1),
    m_numberOfAssignedAdditionalParts(0)
{
    for(int i=0; i<numberOfRequiredParts; i++) m_requiredParts[i] = 0;
}

Computer::~Computer() {
    for(int i=0; i < numberOfRequiredParts; ++i){
        if(m_requiredParts[i]) delete m_requiredParts[i];
    }
    delete[] m_requiredParts;
    for(int i=0; i<m_numberOfAssignedAdditionalParts; i++){
        delete m_additionalParts[i];
    }
    delete[] m_additionalParts;
}

void Computer::setComputerType(const std::__cxx11::string &computerType) {
    m_computerType = computerType;
}

const std::string& Computer::getComputerType() const {
    return m_computerType;
}

std::string Computer::getComputerSpecifications() const {
    std::stringstream ss;
    ss << getComputerType();
    for(int i=0; i<numberOfRequiredParts; i++){
        ss << std::endl << m_requiredParts[i]->getPartInformation();
    }
    ss << std::endl << "Additional Parts";
    for(int i=0; i<getNumberOfAdditionalParts(); i++){
        ss << std::endl << m_additionalParts[i]->getPartInformation();
    }
    return ss.str();
}

/*
 * Mandatory Parts
 */

void Computer::setCPU(const CPU &p) {
    m_requiredParts[CPUID] = PartAllocator::getPart(p);
}

const CPU& Computer::getCPU() const {
    return *(CPU*)(m_requiredParts[CPUID]);
}

void Computer::setMotherboard(const Motherboard &p) {
    m_requiredParts[MotherboardID] = PartAllocator::getPart(p);
}

const Motherboard& Computer::getMotherboard() const {
    return dynamic_cast<Motherboard&>(*m_requiredParts[MotherboardID]);
}

void Computer::setRAM(Ram *ram, int numberOfRamSticks) {
    m_requiredParts[RamSetID] = new RamSet(PartAllocator::getRamSet(ram, numberOfRamSticks));
}

const RamSet& Computer::getRamSet() const {
    return *((RamSet*)m_requiredParts[RamSetID]);
}

void Computer::setHardDrives(HardDrive **hardDrive, int numberOfHardDrives) {
    m_requiredParts[HardDriveSetID] = new HardDriveSet(PartAllocator::getHardDriveSet(hardDrive, numberOfHardDrives));
}

const HardDriveSet& Computer::getHardDriveSet() const {
    return *((HardDriveSet*)m_requiredParts[HardDriveSetID]);
}

void Computer::setGraphicsCards(GraphicsCard *graphicsCard, int numberOfGraphicsCard) {
    m_requiredParts[GraphicsCardSetID] = new GraphicsCardSet(PartAllocator::getGraphicsCardSet(graphicsCard, numberOfGraphicsCard));
}

const GraphicsCardSet& Computer::getGraphicsCardSet() const {
    return *((GraphicsCardSet*)m_requiredParts[GraphicsCardSetID]);
}

void Computer::setCase(const Case &p) {
    m_requiredParts[CaseID] = PartAllocator::getPart(p);
}

const Case& Computer::getCase() const {
    return *((Case*)m_requiredParts[CaseID]);
}

void Computer::setPowerSupply(const PowerSupply &p) {
    m_requiredParts[PowerSupplyID] = PartAllocator::getPart(p);
}

const PowerSupply& Computer::getPowerSupply() const {
    return *((PowerSupply*)m_requiredParts[PowerSupplyID]);
}

/*
 * Voluntary Parts
 */

void Computer::setNumberOfAdditionalParts(int numberOfAdditionalParts) {
    // For validity reasons we cant discount that the user might restart
    // this process half way through, then we would need to delete the
    // old additional parts and start the list again
    for(int i=0; i<m_numberOfAssignedAdditionalParts; i++){
        delete m_additionalParts[i];
    }
    if(m_numberOfAdditionalParts >= 0) delete[] m_additionalParts;
    m_additionalParts = new Part*[numberOfAdditionalParts];
    m_numberOfAdditionalParts = numberOfAdditionalParts;
    m_numberOfAssignedAdditionalParts = 0;
}

int Computer::getNumberOfAdditionalParts() const {
    // this way if the user said theres room for 5 but has
    // only added 3, we dont run into junk memory
    return m_numberOfAssignedAdditionalParts;
}

void Computer::addAdditionalPart(const Part &part) {
    m_additionalParts[m_numberOfAssignedAdditionalParts++] = PartAllocator::getPart(part);
}

Part** Computer::getAdditionalPartsList() const {
    return m_additionalParts;
}
