/************************************************************************* 
 *                            regression.cpp                             *
 *                                                                       *
 * Author: Nic H.                                                        *
 * Date: 2016-Jul-01                                                     *
 *                                                                       *
 * Execute regression tests for the different matrix multipliers.        *
 *************************************************************************/

#include <chrono>
#include <iostream>
#include <memory>
#include <string>
#include <random>

#include "simple_matrix.h"
#include "dense_bit_matrix.h"

using namespace std;
using namespace sparse_matrix;

const unsigned runs = 1000000;

struct base_case {
    virtual void go() = 0;
    virtual void zero() = 0;
    virtual void set(unsigned, bool, unsigned, unsigned) = 0;
    virtual bool get(unsigned, unsigned, unsigned) const = 0;
    virtual void show(unsigned, int) const = 0;
};

template<typename Mat> struct regression_case : public base_case {

    Mat ls[runs];
    Mat rs[runs];
    Mat os[runs];

    void go() {
        for(unsigned r=0; r<runs; r++){
            Mat::multiply(ls[r], rs[r], os[r]);
        }
    }

    void zero() {
    }

    void set(unsigned run, bool left, unsigned row, unsigned col) {
        (left ? ls : rs)[run].set(row, col);
    }

    bool get(unsigned run, unsigned row, unsigned col) const {
        return os[run].get(row, col);
    }

    void show(unsigned run, int sign) const {
        (sign == 0 ? os : (sign == 1 ? ls : rs))[run].draw(std::cerr);
        std::cerr << std::endl;
    }

};

int main(int argc, char** argv){
    string names[] = {"simple", "dense"};
    cout << (argc-1) << " tests with " << runs << " runs" << endl;
    std::vector<unique_ptr<base_case>> cases(argc-1);
    for(unsigned i=1; i<argc; ++i){
        if(string(argv[i]) == names[0]){
            cases[i-1] = unique_ptr<base_case>(new regression_case<simple_matrix<8>>());
        } else if(string(argv[i]) == names[1]){
            cases[i-1] = unique_ptr<base_case>(new regression_case<dense_bit_matrix<unsigned long>>());
        } else {
            cerr << "Unknown case \"" << argv[i] << "\", cases are:";
            for(auto n : names){
                cerr << " \"" << n << "\"";
            }
            cerr << endl;
            return 1;
        }
        cases[i-1]->zero();
    }

    std::random_device rand_dev;
    std::default_random_engine gen(rand_dev());
    std::uniform_int_distribution<> rdm_set_bits(2,72); // too few bits are disinteresting, need more than 64 incase of collision
    std::uniform_int_distribution<> rdm_row_col(0,7);
    for(int run=0; run<runs; ++run){
        for(bool left : {true, false}){
            unsigned set_bits = rdm_set_bits(gen);
            for(unsigned s=0; s<set_bits; ++s){
                unsigned row = rdm_row_col(gen);
                unsigned col = rdm_row_col(gen);
                for(const auto& cs : cases){
                    cs->set(run, left, row, col);
                }
            }
        }
    }
    
    for(auto& cs : cases){
        auto start = chrono::steady_clock::now();
        cs->go();
        auto end = chrono::steady_clock::now();
        std::cout << "time: " << std::chrono::duration<double>(end - start).count() << std::endl;
    }

    for(unsigned run=0; run<runs; ++run){
        for(unsigned chk=1; chk<cases.size(); ++chk){
            for(int rc=0; rc<64; rc++){
                unsigned r = rc/8;
                unsigned c = rc%8;
                if((cases[chk]->get(run, r, c) && !cases[0]->get(run, r, c))||
                        (!cases[chk]->get(run, r, c) && cases[0]->get(run, r, c))) {
                    std::cerr << "difference in case " << run << std::endl;
                    cases[0]->show(run, 1);
                    std::cerr << std::endl;
                    cases[0]->show(run, 2);
                    std::cerr << std::endl;
                    cases[0]->show(run, 0);
                    std::cerr << "-- vs --" << std::endl;
                    cases[chk]->show(run, 0);
                    return 1;
                }
            }
        }
    }

    return 0;
}
