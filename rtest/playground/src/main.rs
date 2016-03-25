
mod emp;

fn orgChart(e: &emp::Emp, indent:u8){
    println!("{}", e);
}

fn main(){
    let emp = emp::Emp::new("Nic","Hollingum",0,0);
    println!("Emp is {:?}", emp);
    println!("Pretty {}", emp.pprint());
    {
        let emp2 = emp::Emp::new2("Bernhard", "Scholz",!0,!0,&emp);
        println!("Display {}", emp2);
    }
    println!("Display {}", emp);
    orgChart(&emp, 0);
}
