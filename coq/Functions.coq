Check True.
Check False.
Check 3.
Check (3+4).
Check (3,4).
Check (3=5).
Check ((3=5)/\True).
Check nat -> Prop.
Check (3 <= 6).
Check (3,3=5):nat*Prop.
Check (fun x:nat => x=3).
Check (forall x:nat, x < 3 \/ (exists y:nat, x=y+3)).
Check (let f := fun x => (x*3,x) in f 3).
Locate "_ <= _".
Check and.
Check (and True False).
Eval compute in let f := fun x => (3*x,x) in f 3.
Eval compute in let f := fun u w x y z => u + w + x + y + z in f 1 2 3 4 5.
Check fun a b c d e => a+b+c+d+e.
Eval compute in (fun a b c d e => a + b + c + d + e) 1 2 3 4 5.

Definition example1 := fun x : nat => x*x+2*x+1.
Check example1.
Definition example (x:nat) := x*x+2*x+1.
Check example.
Eval compute in example 1.
Eval compute in example 2.
Eval compute in example 3.
Eval compute in example 4.

Require Import Bool.
Eval compute in if true then 3 else 5.
Search bool.

Require Import Arith.
Definition is_zero (n:nat) :=
 match n with
  0 => true
 | S p => false
 end.

Print pred.
Eval compute in (is_zero 1).
Fixpoint sum_n2 n s :=
match n with
0 => s
| S p => sum_n2 p (p+s)
end.
Eval compute in (sum_n2 0 0).
Eval compute in (sum_n2 0 1).
Eval compute in (sum_n2 0 2).
Eval compute in (sum_n2 1 0).
Eval compute in (sum_n2 1 1).
Eval compute in (sum_n2 1 2).
Eval compute in (sum_n2 2 0).
Eval compute in (sum_n2 2 1).
Eval compute in (sum_n2 2 2).
