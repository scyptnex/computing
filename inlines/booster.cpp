#include <iostream>
#include <boost/mpl/copy.hpp>
#include <boost/mpl/for_each.hpp>
#include <boost/mpl/range_c.hpp>
#include <boost/mpl/vector_c.hpp>
#include <boost/mpl/vector.hpp>
#include <boost/mpl/reverse.hpp>
#include <boost/mpl/transform.hpp>
#include <boost/mpl/transform_view.hpp>
#include <boost/mpl/unpack_args.hpp>
#include <boost/mpl/joint_view.hpp>
#include <boost/mpl/zip_view.hpp>

struct value_printer
{
    template< typename U > void operator()(U x)
    {
        std::cout << x << '\n';
    }
};

struct pair_printer{
    template<typename P> void operator()(P p){
        std::cout << std::get<0>(p) << " - " << std::get<1>(p) << "\n";
    };
};

using namespace boost::mpl;

typedef range_c<int,0,4>::type one;

typedef transform_view<one, int_<5>> ROR;

int main()
{
    //for_each<
    //    transform_view<
    //        zip_view<
    //            vector<
    //                range_c<int,1,10>,
    //                range_c<int,10,19>
    //            >
    //        >,
    //        unpack_args<
    //            std::pair<_1, _2>
    //        >
    //    >
    //>( pair_printer() );
    //for_each< one >( value_printer() );
    static_assert(size<ROR>::value == 4, "bar");
    for_each< joint_view<one, one> >( value_printer() );
}
