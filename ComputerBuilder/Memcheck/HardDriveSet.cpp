#include "HardDriveSet.h"
#include "PartAllocator.h"

#include <sstream>

using namespace std;

HardDriveSet::HardDriveSet() :
    Part(),
    m_hardDrives(0),
    m_numberOfHardDrives(0)
{}

HardDriveSet::HardDriveSet(const HardDriveSet &hardDriveSet) :
    Part(hardDriveSet),
    m_hardDrives(new HardDrive*[hardDriveSet.m_numberOfHardDrives]),
    m_numberOfHardDrives(hardDriveSet.m_numberOfHardDrives)
{
    for(int i=0; i<m_numberOfHardDrives; i++){
        m_hardDrives[i] = PartAllocator::getHardDrive(hardDriveSet.m_hardDrives[i]);
    }
}

HardDriveSet::HardDriveSet(HardDrive **hardDrives, int numberOfHardDrives) :
    Part("Hard Drive Set"),
    m_hardDrives(new HardDrive*[numberOfHardDrives]),
    m_numberOfHardDrives(numberOfHardDrives)
{
    for(int i=0; i<numberOfHardDrives; i++){
        m_hardDrives[i] = PartAllocator::getHardDrive(hardDrives[i]);
    }
}

HardDriveSet::~HardDriveSet() {
    if(m_hardDrives) {
        for(int i=0; i<m_numberOfHardDrives; i++){
            delete m_hardDrives[i];
        }
        delete[] m_hardDrives;
    }
}

std::string HardDriveSet::getPartInformation() const {
    std::stringstream ss;
    ss << "Hard Drives: Number Of Drives: " << m_numberOfHardDrives;
    for(int i=0; i<m_numberOfHardDrives; ++i){
        ss << std::endl << "Drive " << (i+1) << ": " << m_hardDrives[i]->getPartInformation();
    }
    return ss.str();
}
