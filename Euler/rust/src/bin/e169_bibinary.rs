extern crate num;
use num::bigint::BigUint;
use num::traits::{Zero, One};

fn main() {
    let mut val = num::pow(BigUint::parse_bytes(b"10", 10).unwrap(), 25);
    let two = BigUint::one() + BigUint::one();
    let mut o_ctr:u64 = 1;
    let mut ctr:u64 = 1;
    let mut zer:u64 = 0;
    while val > BigUint::zero() {
        if &val % &two == BigUint::zero() {
            zer += 1;
            println!("");
        } else {
            let n_ctr = (zer+1)*ctr + (ctr-o_ctr);
            o_ctr = ctr;
            ctr = n_ctr;
            zer = 0;
        }
        val = &val / &two;
    }
    println!("{}", ctr);
}
