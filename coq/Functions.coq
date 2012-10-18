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

Require Import List.
Check 1::2::3::nil.
Eval compute in map (fun x => x+3) (1::2::3::nil).

Search list.

Fixpoint rec_range n l :=
match n with
0 => 0::l
| S p => (rec_range p (cons n l))
end.

Definition get_range (x:nat) := (rec_range x (nil: list nat)).
Eval compute in get_range 5.

Fixpoint evenb n :=
match n with
0 => true
| 1 => false
| S (S p) => evenb p
end.
Definition head_evb l :=
match l with nil => false | a::tl => evenb a end.
Fixpoint sum_list l :=
match l with nil => 0 | n::tl => n+ sum_list tl end.

Fixpoint insert n l :=
match l with
nil => n::nil
| a::tl => if leb n a then n::l else a::insert n tl end.
Fixpoint sort l :=
match l with
nil => nil
| a::tl => insert a (sort tl)
end.
Eval compute in sort (1::4::3::22::5::16::7::nil).

Definition lfts l :=
match l with
nil => true
| a::nil => true
| a::b::tl => if leb a b then true else false
end.
Eval compute in lfts nil.
Eval compute in lfts (4::nil).
Eval compute in lfts (4::5::nil).
Eval compute in lfts (5::4::nil).
Fixpoint sortedp l :=
match l with
nil => lfts l
| a::nil => lfts l
| a::tl => if lfts l then sortedp tl else false
end.

Fixpoint cnn n l :=
match l with
nil => 0
| a::tl => (if beq_nat n a then 1 else 0) + (cnn n tl)
end.
Eval compute in cnn 3 (1::3::4::6::3::nil).

Definition head_sorted_sol (l : list nat) : bool :=
match l with
a::b::_ => leb a b
| _ => true
end.
Fixpoint sorted_sol (l : list nat) : bool :=
match l with
a::tl => if head_sorted_sol (a::tl) then sorted_sol tl else false
| nil => true
end.

Search True.
Search le.
SearchRewrite (_ + (_-_)).

Lemma example2 : forall a b:Prop, a /\ b -> b /\ a.
Proof.
intros a b H.
split.
destruct H as [H1 H2].
assumption.
destruct H as [H1 H2].
assumption.
Qed.

Lemma example3 : forall A B, A \/ B -> B \/ A.
Proof.
intros A B H.
destruct H as [H1 | H2].
right; assumption.
left; assumption.
Qed.

Check le_n.
Check le_S.

Lemma example4 : 3 <= 5.
Proof.
apply le_S.
apply le_S.
apply le_n.
Qed.

Check le_trans.
Lemma example5 : forall x y, x <= 10 -> 10 <= y -> x <= y.
Proof.
intros x y x10 y10.
apply le_trans with (m := 10).
assumption.
assumption.
Qed.

Lemma example6 : forall x y, (x+y)*(x+y) = x*x+2*x*y+y*y.
Proof.
intros x y.
SearchRewrite (_*(_+_)).
rewrite mult_plus_distr_l.
rewrite mult_plus_distr_r.
rewrite mult_plus_distr_r.
rewrite plus_assoc.
rewrite <- plus_assoc with (n := x*x).
rewrite mult_comm with (n:=y) (m:=x).
pattern (x*y) at 1; rewrite <- mult_1_l.
rewrite <- mult_succ_l.
rewrite mult_assoc.
reflexivity.
Qed.

Lemma ex1 : forall A B C:Prop, A/\(B/\C)->(A/\B)/\C.
Proof.
intros.
destruct H as [HA HBC].
destruct HBC as [HB HC].
split.
split.
assumption.
assumption.
assumption.
Qed.

Lemma ex3 : forall A: Prop, ~(A/\~A).
Proof.
intros.
Search (_/\~_).
intros H.
destruct H.
destruct H0.
exact H.
Qed.

