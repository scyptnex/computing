use std::thread;
use std::sync::{Mutex, Arc};

struct Table {
    forks: Vec<Mutex<()>>,
}

struct Philosopher {
    name:  String,
    left:  usize,
    right: usize,
}

impl Philosopher {
    fn new(name: &str, l: usize, r: usize) -> Philosopher {
        Philosopher {
            name: name.to_string(),
            left: l,
            right: r
        }
    }

    fn eat(&self, table: &Table) {
        let _left = table.forks[self.left].lock().unwrap();
        let _right = table.forks[self.right].lock().unwrap();
        println!("{} starts eating!", self.name);
        thread::sleep_ms(1000);
        println!("{} done eating!", self.name);
    }
}

fn main() {
    let tabl = Arc::new(Table { forks: vec![
        Mutex::new(()),
        Mutex::new(()),
        Mutex::new(()),
        Mutex::new(()),
        Mutex::new(()),
    ]});
    let phils = vec![
        Philosopher::new("Brad M",0,1),
        Philosopher::new("Andrew B",1,2),
        Philosopher::new("Daniel W",2,3),
        Philosopher::new("Nathan H",3,4),
        Philosopher::new("Tom K",4,0)
    ];
    let handles: Vec<_> = phils.into_iter().map(|p| {
        let tabl = tabl.clone();
        thread::spawn(move || {
            p.eat(&tabl);
        })
    }).collect();
    for h in handles{
        h.join().unwrap();
    }
    let nms = vec![
        "bye",
        "hi",
        "ola"
    ];
    println!("{} has item", nms.contains(&"hi"));
}

