#ifndef TESTCLASS_H
#define TESTCLASS_H

#include <vector>

class SubClass{
	public:
		SubClass();
		SubClass(int* value, int subValue);
		~SubClass();
		void say();
	private:
		int* val;
		int* val2;
};

class TestClass
{
	public:
		TestClass(int val);
		virtual ~TestClass();
		void speak();
	protected:
	private:
		int length;
		std::vector<SubClass> subs;
};

#endif // TESTCLASS_H
