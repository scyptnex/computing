// ================================================================
// $Id: ct.cc,v 1.1 2012/04/10 01:37:54 jlinoff Exp jlinoff $
//
// Description: Cipher manipulation program.
// Copyright:   Copyright (c) 2012 by Joe Linoff
// Version:     1.1
// Author:      Joe Linoff
//
// LICENSE
//   The cipher package is free software; you can redistribute it and/or
//   modify it under the terms of the GNU General Public License as
//   published by the Free Software Foundation; either version 2 of the
//   License, or (at your option) any later version.
//       
//   The cipher package is distributed in the hope that it will be useful,
//   but WITHOUT ANY WARRANTY; without even the implied warranty of
//   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//   General Public License for more details. You should have received
//   a copy of the GNU General Public License along with the change
//   tool; if not, write to the Free Software Foundation, Inc., 59
//   Temple Place, Suite 330, Boston, MA 02111-1307 USA.
// ================================================================
#include "cipher.h"
#include <stdexcept>
#include <string>
#include <sstream>
#include <fstream>
#include <iostream>
#include <iomanip>
#include <cstdlib> // exit, atoi
using namespace std;

typedef unsigned int uint;

// ================================================================
// Print the help.
// ================================================================
void help()
{
  cout <<
    "NAME\n"
    "\tct - Cipher tool that used to encrypt or decrypt files.\n"
    "\n"
    "SYNOPSIS\n"
    "\tct [OPTIONS]\n"
    "\n"
    "DESCRIPTION\n"
    "\tEncrypt or decrypt a file.\n"
    "\n"
    "OPTIONS\n"
    "\t-c <num>\tCount of number of init rounds.\n"
    "\n"
    "\t-C <cipher>\tThe name of the cipher to use (ex. aes-256-cbc).\n"
    "\n"
    "\t--d\t\tDecrypt.\n"
    "\n"
    "\t-D <digest>\tThe name of the digest to use (ex. sha1).\n"
    "\n"
    "\t--debug\t\tTurn on internal debugging.\n"
    "\n"
    "\t--e\t\tEncrypt.\n"
    "\n"
    "\t-h\t\tThis help message.\n"
    "\n"
    "\t-i <file>\tInput file.\n"
    "\n"
    "\t-n <file>\tDo not embed the salt prefix. The result will not be\n"
    "\t\t\tcompatible with openssl.\n"
    "\n"
    "\t-o <file>\tOutput file.\n"
    "\n"
    "\t-p <pass>\tPassphrase.\n"
    "\n"
    "\t-s <salt>\tSalt as a string.\n"
    "\n"
    "\t-x <salt>\tSalt as hex digits (16)..\n"
    "\n"
    "\t-v\t\tIncrease the level of verbosity.\n"
    "\n"
    "\t-V\t\tPrint the version number and exit.\n"
    "\n"
    "EXAMPLES\n"
    "\t% # Help\n"
    "\t% ./ct.exe -h\n"
    "\n"
    "\t% # Encrypt stdin to stdout\n"
    "\t% echo 'Lorem ipsum dolor sit amet' | ./ct.exe -e\n"
    "\tU2FsdGVkX19nxmlzUf9K7K0Z40ZlovVWSfndp4VHLKSl9j5FXuvHi7dz08nVDsrV\n"
    "\n"
    "\t% # Decrypt stdin to stdout\n"
    "\t% echo U2FsdGVkX19nxmlzUf9K7K0Z40ZlovVWSfndp4VHLKSl9j5FXuvHi7dz08nVDsrV | ./ct.exe -d\n"
    "\tLorem ipsum dolor sit amet\n"
    "\n"
    "\t% # Encrypt a file\n"
    "\t% # It is okay to reference the same file.\n"
    "\t\% ./ct.exe -e -p 'Tally Ho!' -i foo.txt -o foo.txt\n"
    "\n"
    "\t% # Decrypt a file\n"
    "\t% # It is okay to reference the same file.\n"
    "\t\% ./ct.exe -d -p 'Tally Ho!' -i foo.txt -o foo.txt\n"
    "\n"
    "\t% # Encrypt/decrypt using pipes. Use -n to strip out the salt prefix.\n"
    "\t% echo 'Lorem ipsum dolor sit amet' |\\\n"
    "\t\t./ct.exe -e -n -s 12345678 -p foobar |\\\n"
    "\t\t./ct.exe -d -n -s 12345678 -p foobar\n"
    "\tLorem ipsum dolor sit amet\n"
    "\n"
    "AUTHOR\n"
    "\tJoe Linoff\n"
    "\n"
    << endl;
  exit(0);
}

// ================================================================
// Verbose output
// ================================================================
#define PKV(n) vdump(#n,n)
template<typename T> void vdump(const string& k,const T& v)
{
  cout << setw(16) << left << k << " : " << v << endl;
}
template<> void vdump<string>(const string& k,const string& v)
{
  cout << setw(16) << left << k << " : '" << v << "'";
  if (v.size()<4) {
    cout << "\t";
  }
  cout << "\t\t";
  cout << "(" << v.size() << ")";
  cout << endl;
}

// ================================================================
// CHK_ARG
// ================================================================
#define CHK_ARG \
  if (++i==argc) {				    \
    cout << "ERROR: missing argument for "+opt;	    \
    exit(1);					    \
  }


// ================================================================
// atoh
// ================================================================
uint atoh(char x)
{
  if (x>='0' && x<='9') {
    return x-'0';
  }
  if (x>='a' && x<='f') {
    return 10+(x-'a');
  }
  if (x>='A' && x<='F') {
    return 10+(x-'A');
  }
  throw runtime_error("atoi(): invalid hex digit encountered");
  return 0;
}

// ================================================================
// MAIN
// ================================================================
int main(int argc,char** argv)
{
  string ifn;
  string ofn;
  string pass;
  string salt;
  string cipher=CIPHER_DEFAULT_CIPHER;
  string digest=CIPHER_DEFAULT_DIGEST;
  uint   count=CIPHER_DEFAULT_COUNT;
  uint   v=0;
  bool   debug = false;
  bool   encrypt = true;
  bool   embed = true;

  for(int i=1;i<argc;++i) {
    string opt = argv[i];
    if      (opt=="-h"     ) { help(); }
    else if (opt=="--debug") { debug = true; }
    else if (opt=="-c"     ) { CHK_ARG count = atoi(argv[i]); }
    else if (opt=="-C"     ) { CHK_ARG cipher = argv[i]; }
    else if (opt=="-d"     ) { encrypt = false; }
    else if (opt=="-D"     ) { CHK_ARG digest = argv[i]; }
    else if (opt=="-e"     ) { encrypt = true; }
    else if (opt=="-i"     ) { CHK_ARG ifn = argv[i]; }
    else if (opt=="-n"     ) { embed = false; }
    else if (opt=="-o"     ) { CHK_ARG ofn = argv[i]; }
    else if (opt=="-p"     ) { CHK_ARG pass = argv[i]; }
    else if (opt=="-s"     ) { CHK_ARG salt = argv[i]; }
    else if (opt=="-v"     ) { ++v; }
    else if (opt=="-V"     ) { cout << "Version: 1.1" << endl; exit(0); }
    else if (opt=="-x"     ) {
      // Special handling for hex specification
      // of the salt.
      CHK_ARG 
      string x = argv[i];
      if (x.size()!=16) {
	cout << "ERROR: value must be 16 hex digits for "+opt<<endl;
	exit(1);
      }
      salt = "";
      if (v) {
	cout << setw(16) << left << "xsalt" << " : ";
      }
      for(const char* p=x.c_str();*p;p+=2) {
	uint h =(atoh(*p)*16) + atoh(*(p+1));
	if (v) {
	  cout << hex << setw(2) << h << dec;
	}
	salt += char(h);
      }
      if (v) {
	cout << endl;
      }
    }
    else {
      cout << "ERROR: unrecognized option "+opt<<endl;
      exit(1);
    }
  }

  // Print out some useful information.
  if (v) {
    PKV(ifn);
    PKV(ofn);
    PKV(pass);
    PKV(salt);
    PKV(cipher);
    PKV(digest);
    PKV(count);
    PKV(debug);
  }

  try {
    // Collect the input data.
    string in;
    if (ifn.empty()) {
      string x((istreambuf_iterator<char>(cin)),
	       istreambuf_iterator<char>());
      in = x;
    }
    else {
      ifstream ifs(ifn.c_str());
      if (!ifs) {
	string msg = "cannot read file: "+ifn;
	throw runtime_error(msg);
      }
      string x((istreambuf_iterator<char>(ifs)),
	       istreambuf_iterator<char>());
      ifs.close();
      in = x;
    }
    if (v) {
      cout << "input: (" << in.size() << ")" << endl;
      cout << "<begin>" << endl;
      cout << in << endl;
      cout << "<endl>" << endl;
    }
    if (in.empty()) {
      throw runtime_error("zero length input is not supported");
    }

    Cipher mgr(cipher,digest,count,embed);
    mgr.debug(debug);
    string out;
    if (encrypt) {
      out = mgr.encrypt(in,pass,salt);
    }
    else {
      out = mgr.decrypt(in,pass,salt);
    }

    // Write the output data.
    if (ofn.empty()) {
      cout << out;
      if (encrypt) {
	cout << endl;
      }
    }
    else {
      ofstream ofs(ofn.c_str());
      if (!ofs) {
	string msg = "cannot write file: "+ofn;
	throw runtime_error(msg);
      }
      ofs << out;
      if (encrypt) {
	ofs << endl;
      }
      ofs.close();
    }
  }
  catch (exception& e) {
    cerr << "ERROR: " << e.what() << endl;
    return 1;
  }
  return 0;
}
