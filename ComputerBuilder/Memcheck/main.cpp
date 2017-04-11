#include "Computer.h"

#include <iostream>

bool buildFullComputerTest()
{
    bool validation = true;
    for (long i = 0; i < 1000000; ++i)
    {
        Computer* computer = new Computer();
        computer->setComputerType("Gaming Computer");
        computer->setCase(Case("NZXT Phantom 240", Tower));
        computer->setPowerSupply(PowerSupply("Cougar CMX1000 V3", 1000));
        computer->setMotherboard(Motherboard("ASUS Maximus V EXTREME", "LGA 1155", 4, 2));
        computer->setCPU(CPU("Intel Core i7-3770K Ivy Bridge Quad-Core", 3.5, "LGA 1155"));

        Ram* ram = new Ram[4];
        ram[0] = Ram("HyperX FURY", 1866, 8);
        ram[1] = Ram("HyperX FURY", 1866, 8);
        ram[2] = Ram("HyperX FURY", 1866, 8);
        ram[3] = Ram("HyperX FURY", 1866, 8);
        computer->setRAM(ram, 4);
        delete[] ram;

        HardDrive** hardDrives = new HardDrive*[3];
        hardDrives[0] = new SolidStateDrive("SAMSUNG 850 EVO-Series MZ-75E1T0B/AM", 1000);
        hardDrives[1] = new HardDiscDrive("WD BLACK SERIES WD2003FZEX", 2000, 7200);
        hardDrives[2] = new HardDiscDrive("WD BLACK SERIES WD2003FZEX", 2000, 7200);
        computer->setHardDrives(hardDrives, 3);
        for (int i = 0; i < 3; ++i)
        {
            delete hardDrives[i];
        }
        delete[] hardDrives;

        GraphicsCard* graphicsCards = new GraphicsCard[2];
        graphicsCards[0] = GraphicsCard("GIGABYTE GV-N75TOC-2GI G-SYNC Support GeForce GTX 750 Ti", 1033, 2);
        graphicsCards[1] = GraphicsCard("GIGABYTE GV-N75TOC-2GI G-SYNC Support GeForce GTX 750 Ti", 1033, 2);
        computer->setGraphicsCards(graphicsCards, 2);
        delete[] graphicsCards;

        // We are only going to assign 2. To check the destructor behaves correctly.
        computer->setNumberOfAdditionalParts(3);
        computer->addAdditionalPart(CoolingFan("Cooler Master SickleFlow 120 - Sleeve Bearing 120mm", 1600));
        computer->addAdditionalPart(BluRayDrive("LG Black 12X BD-ROM", 12));

        std::string expected ="Gaming Computer\n";
        expected += "CPU: Intel Core i7-3770K Ivy Bridge Quad-Core, Clock Speed: 3.5 Ghz, Socket: LGA 1155\n";
        expected += "Motherboard: ASUS Maximus V EXTREME, Socket: LGA 1155\nNo. of Ram Slots: 4, No. of Graphics Cards Permitted: 2\n";
        expected += "Ram: Number Of Sticks: 4\n";
        expected += "Slot 1: HyperX FURY, Frequency: 1866 Mhz, Size: 8 GB\n";
        expected += "Slot 2: HyperX FURY, Frequency: 1866 Mhz, Size: 8 GB\n";
        expected += "Slot 3: HyperX FURY, Frequency: 1866 Mhz, Size: 8 GB\n";
        expected += "Slot 4: HyperX FURY, Frequency: 1866 Mhz, Size: 8 GB\n";
        expected += "Hard Drives: Number Of Drives: 3\n";
        expected += "Drive 1: SAMSUNG 850 EVO-Series MZ-75E1T0B/AM, Size: 1000 GB\n";
        expected += "Drive 2: WD BLACK SERIES WD2003FZEX, Size: 2000 GB, 7200 RPM\n";
        expected += "Drive 3: WD BLACK SERIES WD2003FZEX, Size: 2000 GB, 7200 RPM\n";
        expected += "Graphics Cards: Number Of Cards: 2\n";
        expected += "Slot 1: GIGABYTE GV-N75TOC-2GI G-SYNC Support GeForce GTX 750 Ti, Frequency: 1033 Mhz, Size: 2 GB\n";
        expected += "Slot 2: GIGABYTE GV-N75TOC-2GI G-SYNC Support GeForce GTX 750 Ti, Frequency: 1033 Mhz, Size: 2 GB\n";
        expected += "Case: NZXT Phantom 240, Case Type: Tower\n";
        expected += "Power Supply: Cougar CMX1000 V3, Maximum Power: 1000 Watts\n";
        expected += "Additional Parts\n";
        expected += "Cooling Fan: Cooler Master SickleFlow 120 - Sleeve Bearing 120mm, Speed: 1600 RPM\n";
        expected += "Blu-ray Drive: LG Black 12X BD-ROM, Read Speed: 12X";
        std::string actual = std::string(computer->getComputerSpecifications());

        delete computer;

        validation = validation && expected == actual;
    }
    return validation;
}

int main()
{
    int numberOfTestsFailed = 0;

    // computer tests

    if (!buildFullComputerTest())
    {
        numberOfTestsFailed++;
    }

    if (0 == numberOfTestsFailed)
    {
        std::cout << "All Tests Passed!" << std::endl;
    }
    else
    {
        std::cout << numberOfTestsFailed << " Tests Failed" << std::endl;
    }
}

