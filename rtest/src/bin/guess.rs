extern crate rand;

use std::io;
use rand::Rng;

fn main() {
    let ans = rand::thread_rng().gen_range(1,101);
    println!("Guess the number!");
    loop{
        println!("");
        println!("Please input your guess.");
        let mut guess = String::new();
        io::stdin().read_line(&mut guess)
            .ok()
            .expect("Failed to read line");
        match guess.trim().parse::<i32>(){
            Ok(x) if x < ans => println!("Too low"),
            Ok(x) if x == ans => break,
            Ok(_) => println!("Too high"),
            Err(e) => println!("Error: {}", e)
        }
    }
    println!("Correct");
}

