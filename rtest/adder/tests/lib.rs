extern crate adder;

#[test]
fn it_works() {
    assert_eq!(5, adder::add_two(3));
    assert_eq!(2, adder::add_two(0));
}

