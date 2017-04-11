#include "GamingComputerBuilder.h"

GamingComputerBuilder::GamingComputerBuilder() : ComputerBuilder()
{

}

GamingComputerBuilder::~GamingComputerBuilder()
{

}

void GamingComputerBuilder::createComputer()
{
    m_computer = new Computer();
    m_computer->setComputerType("Gaming");
    this->ComputerBuilder::createComputer();
}

void GamingComputerBuilder::addCPU()
{
    CPU cpu ("Intel Core i7-4820K Ivy Bridge-E Quad-Core", 3.7, "LGA 1155");
    m_computer->setCPU(cpu);
}

void GamingComputerBuilder::addMotherboard()
{
    Motherboard motherboard("Intel S2600CP2 SSI EEB Server Motherboard", "LGA 2011", 16, 1);
    m_computer->setMotherboard(motherboard);
}

void GamingComputerBuilder::addRAM()
{
    Ram* ram = new Ram[16];
    for (int i = 0; i < 16; ++i)
    {
        ram[i] = Ram("Kingston 240-Pin DDR3 Server Memory", 1600, 16);
    }
    m_computer->setRAM(ram, 16);
    delete[] ram;
}

void GamingComputerBuilder::addHardDrive()
{
    HardDrive** hardDrive = new HardDrive*[14];

    for (int i = 0; i < 14; ++i)
    {
         hardDrive[i] = new SolidStateDrive("SAMSUNG PM853T Data Center Series", 960);
    }
    m_computer->setHardDrives(hardDrive, 14);
    for (int i = 0; i < 14; ++i)
    {
         delete hardDrive[i];
    }
    delete[] hardDrive;
}

void GamingComputerBuilder::addGraphicsCard()
{
    GraphicsCard* graphicsCard = new GraphicsCard[2];
    graphicsCard[0] = GraphicsCard("Nvidia something something i dont know this stuff...", 650, 1);
    m_computer->setGraphicsCards(graphicsCard, 1);
    delete[] graphicsCard;
}

void GamingComputerBuilder::addPowerSupply()
{
    PowerSupply powerSupply ("Athena Power AP-U2ATX70FEP8 20+4Pin", 700);
    m_computer->setPowerSupply(powerSupply);
}

void GamingComputerBuilder::addCase()
{
    Case computerCase ("ARK IPC-4U600 Black 1.2mm SECC Zinc-Coated Steel 4U Rackmount Server Chassis", Cabinet);
    m_computer->setCase(computerCase);
}

void GamingComputerBuilder::addAdditionalParts()
{
    BluRayDrive brd("Sony and the adventures of an obsolete technology", 9001);
    m_computer->setNumberOfAdditionalParts(1);
    m_computer->addAdditionalPart(brd);
}
