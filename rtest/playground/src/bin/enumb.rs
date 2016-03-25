
trait EnumStringer {
    fn enum_name() -> String;
    fn lst_all() -> String;
}

macro_rules! omni_enum{
    ( $e:ident { $( $x:ident ),+ $(,)* } ) => {
        #[derive(Debug)]
        pub enum $e{
            $( $x ),+
        }

        impl EnumStringer for $e{
            fn enum_name() -> String {
                stringify!($e).to_string()
            }
            fn lst_all() -> String {
                let mems = vec![$( stringify!($x) ),+];
                mems.join(",")
            }
        }

        impl ::std::str::FromStr for $e{
            type Err = &'static str;
            fn from_str(s: &str) -> Result<Self, Self::Err> {
                match s {
                    $(
                        stringify!($x) => Ok($e::$x),
                    )+
                    _ => Err("Unknown enum member"),
                }
            }
        }
    };
}

omni_enum!(Foo {
    Bar,
    Baz,
});

omni_enum!(Col {
    Hue,
    Lum,
    Sat,
});

omni_enum!( Batman {
    Zap,
    Bam,
    Kapow,
});

fn main(){
    let f = Foo::Bar;
    println!("HW {:?}", f);
    println!("Choose a {}: {}", Batman::enum_name(), Batman::lst_all());
    //let j = <Batman as ::std::str::FromStr>::from_str("Zap");
    let mut ln = String::new();
    std::io::stdin().read_line(&mut ln).ok().expect("Couldnt read line");
    let ln_enum = ln.trim().parse::<Batman>();
    println!("bm {:?}", ln_enum);
}
