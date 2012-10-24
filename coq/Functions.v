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

Lemma ex2: forall A B C D: Prop,(A->B)/\(C->D)/\A/\C -> B/\D.
Proof.
intros.
destruct H as [HAB].
destruct H as [HCD].
destruct H.
split.
apply HAB.
assumption.
apply HCD.
assumption.
Qed.

Lemma ex4: forall A B C: Prop, A\/(B\/C)->(A\/B)\/C.
Proof.
intros.
destruct H as [H1 | [H2 | H3]].
left;left;exact H1.
left;right;exact H2.
right; exact H3.
Qed.

Lemma ex5: forall A B: Prop, (A\/B)/\~A -> B.
Proof.
intros.
destruct H.
destruct H.
destruct H0.
exact H.
exact H.
Qed.

Lemma euq:
   forall A:Type,
   forall P Q: A->Prop,
   (forall x, P x) \/ (forall y, Q y)
      -> forall x, P x \/ Q x.
intros.
destruct H as [HP | HQ].
left.
apply HP.
right.
apply HQ.
Qed.

Fixpoint sum_n n :=
match n with
0 => 0
| S p => p + sum_n p
end.

Lemma successor_squared : forall n, S n * S n = n*n+2*n+1.
Proof.
intros.
ring.
Qed.


Lemma sum_n_p : forall n, 2*sum_n n + n = n * n.
Proof.
induction n.
reflexivity.
rewrite successor_squared.
rewrite <- IHn.
simpl.
ring.
Qed.

Lemma evenb_p : forall n, evenb n = true -> exists x, n=2*x.
Proof.
assert (Main: forall n, (evenb n = true -> exists x, n=2*x) /\ (evenb (S n) = true -> exists x, S n = 2*x)).
induction n.
split.
exists 0.
ring.
simpl.
intros H.
discriminate H.
split.
destruct IHn as [_ IHn'].
exact IHn'.
simpl.
intros H.
destruct IHn as [IHn' _].
assert (H' : exists x, n=2*x).
apply IHn'.
exact H.
destruct H' as [x q].
exists (x+1).
rewrite q.
ring.
intros n ev.
destruct (Main n) as [H _].
apply H.
exact ev.
Qed.

Fixpoint add n m := match n with 0 => m | S p => add p (S m) end.
Eval compute in add 5 5.
Eval compute in add 5 4.
Eval compute in add 4 5.
Eval compute in add 1 10.

Require Import Arith.

Lemma unit_commute : forall n m, add n (S m) = S (add n m).
Proof.
induction n; intros; simpl.
reflexivity.
rewrite IHn; reflexivity.
Qed.

Lemma unit_commute2 : forall n m, add (S n) m = S (add n m).
Proof.
induction n.
intros.
simpl.
ring.
intros.
apply IHn.
Qed.

Lemma same_func : forall n m, add n m = n + m.
Proof.
induction n.
simpl.
reflexivity.
intros.
simpl.
rewrite unit_commute.
Search (S _ = S _).
apply eq_S.
apply IHn.
Qed.

Fixpoint sum_odd_n (n:nat) : nat :=
 match n with 0 => 0
| S p => 1 + 2*p + sum_odd_n p end.

Eval compute in sum_odd_n 0.
Eval compute in sum_odd_n 1.
Eval compute in sum_odd_n 2.
Eval compute in sum_odd_n 3.

Lemma odd_square : forall n:nat, sum_odd_n n = n*n.
Proof.
induction n.
simpl.
reflexivity.
rewrite successor_squared.
simpl.
rewrite IHn.
ring.
Qed.

Fixpoint count n l :=
match l with
nil => 0
| a::tl =>
let r := count n tl in if beq_nat n a then 1+r else r
end.

Lemma insert_incr : forall n l, count n (insert n l) = 1+count n l.
Proof.
induction l.
simpl.
rewrite <- beq_nat_refl.
reflexivity.
simpl.
case (leb n a).
simpl.
rewrite <- beq_nat_refl.
reflexivity.
simpl.
case (beq_nat n a).
rewrite IHl.
ring.
rewrite IHl.
ring.
Qed.

Inductive bin : Type :=
L : bin
| N : bin -> bin -> bin.

Check N L (N L L).

Inductive e6 : Type :=
 e6_c
| e6_w3 (n : nat) (t1 t2 : e6)
| e6_4 (b : bool) (t1 t2 t3 : e6).

Definition example7 (t : bin):bool :=
match t with N L L => false | _ => true end.

Fixpoint flatten_aux (t1 t2:bin) : bin :=
match t1 with
L => N L t2
| N t'1 t'2 => flatten_aux t'1 (flatten_aux t'2 t2)
end.

Fixpoint flatten (t:bin) :bin :=
match t with
 L => L | N t1 t2 => flatten_aux t1 (flatten t2)
end.

Eval compute in flatten (N L (N L (N L (N L L)))).
Eval compute in flatten (N (N (N (N (N (N L L) L) L) L) L) L).

Fixpoint size (t:bin) : nat :=
match t with
L => 1 | N t1 t2 => 1 + size t1 + size t2
end.

Eval compute in flatten (N (N (N L L) (N L L)) (N (N L L) L)).

Lemma example7_size : forall t, example7 t = false -> size t = 3.
intros t.
destruct t.
simpl.
intros H.
discriminate H.
destruct t1.
destruct t2.
simpl.
intro.
reflexivity.
intros H.
discriminate H.
intros H.
discriminate H.
Qed.

Lemma flatten_aux_size : forall t1 t2, size (flatten_aux t1 t2) = size t1 + size t2 + 1.
induction t1.
intros t2.
simpl.
ring.
intros t2.
simpl.
rewrite IHt1_1.
rewrite IHt1_2.
ring.
Qed.

Lemma flatten_size : forall t, size (flatten t) = size t.
induction t.
simpl.
reflexivity.
simpl.
rewrite flatten_aux_size.
rewrite IHt2.
ring.
Qed.

Lemma not_subterm_self_1 : forall x y, ~ x = N x y.
induction x.
intros y.
discriminate.
intros y abs.
injection abs.
intros h2 h1.
assert (IHx1' : x1 <> N x1 x2).
apply IHx1.
case IHx1'.
exact h1.
Qed.







