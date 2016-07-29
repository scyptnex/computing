/************************************************************************* 
 *                                cl.cpp                                 *
 *                                                                       *
 * Author: Nic H.                                                        *
 * Date: 2016-Jul-29                                                     *
 *************************************************************************/

#include "cl.h"

#include <boost/program_options.hpp>

#include <fstream>
#include <iostream>
#include <iterator>

namespace po = boost::program_options;
using namespace std;

// A helper function to simplify the main part.
template<class T>
ostream& operator<<(ostream& os, const vector<T>& v){
    copy(v.begin(), v.end(), ostream_iterator<T>(os, ", ")); 
    return os;
}


int tst(int ac, char* av[])
{
    try {
        int opt;
        string config_file;
        string hello_name;

        // Declare a group of options that will be 
        // allowed only on command line
        po::options_description generic("Generic options");
        generic.add_options()
            ("version,v", "print version string")
            ("verbose", "print extra information")
            ("help", "produce help message")
            ("config,c", po::value<string>(&config_file)->default_value("multiple_sources.cfg"),
             "name of a file of a configuration.")
            ;

        // Declare a group of options that will be 
        // allowed both on command line and in
        // config file
        po::options_description config("Configuration");
        config.add_options()
            ("optimization", po::value<int>(&opt)->default_value(10), 
             "optimization level")
            ("include-path,I", 
             po::value< vector<string> >()->composing(), 
             "include path")
            ("hi.there,x", "say hello")
            ("hi.name,y", po::value<string>(&hello_name)->default_value("Nic"), "set the hello's name")
            ;

        // Hidden options, will be allowed both on command line and
        // in config file, but will not be shown to the user.
        po::options_description hidden("Hidden options");
        hidden.add_options()
            ("input-file", po::value< vector<string> >(), "input file")
            ;


        po::options_description cmdline_options;
        cmdline_options.add(generic).add(config).add(hidden);

        po::options_description config_file_options;
        config_file_options.add(config).add(hidden);

        po::options_description visible("Allowed options");
        visible.add(generic).add(config);

        po::positional_options_description p;
        p.add("input-file", -1);

        po::variables_map vm;
        store(po::command_line_parser(ac, av).
                options(cmdline_options).positional(p).run(), vm);
        notify(vm);

        if (vm.count("help")) {
            if(vm.count("verbose")){
                cout << cmdline_options << endl;
            } else {
                cout << visible << "\n";
            }
            return 0;
        }

        if (vm.count("version")) {
            cout << "Multiple sources example, version 1.0\n";
            return 0;
        }

        ifstream ifs(config_file.c_str());
        if (!ifs)
        {
            cout << "can not open config file: " << config_file << "\n";
            return 0;
        }
        else
        {
            store(parse_config_file(ifs, config_file_options), vm);
            notify(vm);
        }

        if (vm.count("include-path"))
        {
            cout << "Include paths are: " 
                << vm["include-path"].as< vector<string> >() << "\n";
        }

        if (vm.count("input-file"))
        {
            cout << "Input files are: " 
                << vm["input-file"].as< vector<string> >() << "\n";
        }

        if(vm.count("hi.there")){
            cout << "Hello " << hello_name << endl;
        }

        cout << "Optimization level is " << opt << "\n";                
    }
    catch(exception& e)
    {
        cout << "Exc: " << e.what() << "\n";
        return 1;
    }    
    return 0;
}

void Configuration::go(int argc, char** argv){
    tst(argc, argv);
}

