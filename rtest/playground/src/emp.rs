
use std;

#[derive(Debug)]
pub struct Emp<'a> {
    fname: String,
    lname: String,
    lvl: u8,
    wage: u64,
    manager: Option<&'a Emp<'a>>,
}

impl<'a> Emp<'a>{
    pub fn new(f: &str, l: &str, lvl: u8, wa: u64) -> Emp<'a> {
        Emp{
            fname: f.to_string(),
            lname: l.to_string(),
            lvl: lvl,
            wage: wa,
            manager: None,
        }
    }
    pub fn new2(f: &str, l: &str, lvl: u8, wa: u64, ma: &'a Emp) -> Emp<'a> {
        Emp{
            fname: f.to_string(),
            lname: l.to_string(),
            lvl: lvl,
            wage: wa,
            manager: Some(ma),
        }
    }
    pub fn pprint(&self) -> String {
        return format!("[\"{} {}\", level {}, salary ${}{}]", self.fname, self.lname, self.lvl, self.wage, match self.manager {
                Some(m) => format!(", manager: {} {}", m.fname, m.lname),
                None => "".to_string(),
            });
    }
}

impl<'a> std::fmt::Display for Emp<'a>{
    fn fmt(&self, f: &mut std::fmt::Formatter) -> std::fmt::Result {
        return write!(f, "{}", self.pprint());
    }

}
