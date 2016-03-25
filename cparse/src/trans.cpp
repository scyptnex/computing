#include <iostream>
#include <string>
#include <vector>

#include <boost/spirit/include/qi.hpp>

namespace qi=boost::spirit::qi;
namespace phx=boost::phoenix;


namespace boost { namespace spirit { namespace traits
{

    template <>
    struct transform_attribute<std::string, int, qi::domain>
    {
        typedef int type;
        static int pre(std::string& d) { return 0; }//not useful in this case but required to avoid compiler errors
        static void post(std::string& val, int const& attr) //`val` is the "returned" string, `attr` is what int_ parses
        {
            std::stringstream ss;
            ss << attr;
            val= (ss.str() + "hi");
        }
        static void fail(std::string&) {}
    };
}}}

std::string semantic_transform(int i)
{
    std::stringstream ss;
    ss<<i;
    return ss.str() + "hi";
}

int main(int argc, char* argv[])
{
    std::string test="123";
    std::string test_vector="456 789";

    qi::rule<std::string::const_iterator,std::string()> my_int_as_str = qi::attr_cast(qi::int_);
    qi::rule<std::string::const_iterator,std::vector<std::string>(),qi::space_type> my_int_as_str_vector= *qi::attr_cast(qi::int_);

    std::string result;
    std::vector<std::string> result_vector;

    parse(test.cbegin(),test.cend(),my_int_as_str,result);
    phrase_parse(test_vector.cbegin(),test_vector.cend(),my_int_as_str_vector,qi::space,result_vector);


    std::cout << result << std::endl;
    for(auto& string: result_vector)
        std::cout << string << std::endl;

std::string string_semantic;
qi::parse(test.cbegin(),test.cend(),qi::int_[&semantic_transform],string_semantic);
std::cout << string_semantic << std::endl;

    return 0;
}
