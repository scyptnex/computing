#include <iostream>

#include "TestClass.h"

TestClass::TestClass(int val)
{
	length = val;
	std::cout << val << std::endl;
	subs = std::vector<SubClass>(val);
	for(int i=0; i<length; i++)
	{
		subs[i] = SubClass(&val, i);
	}
}

TestClass::~TestClass()
{

}

void TestClass::speak(){
	for(int i=0; i<length; i++)
	{
		subs[i].say();
	}
}

SubClass::SubClass()
{
	val = NULL;
	val2 = NULL;
}

SubClass::SubClass(int* value, int subValue){
	val = value;
	val2 = &subValue;
}

SubClass::~SubClass()
{
	//do nothing
}

void SubClass::say()
{
	std::cout << val << "  -  " << *val2 << std::endl;
}
