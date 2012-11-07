Fixpoint pascall n k :=
match k with
0 => 1
| S pk => match n with
	0 => 0
	| S pn => pascall pn pk + pascall pn k
end
end.

Eval compute in pascall 2 4.
Eval compute in pascall 0 9.
Eval compute in pascall 4 0.
Eval compute in pascall 4 1.
Eval compute in pascall 4 2.
Eval compute in pascall 4 3.
Eval compute in pascall 4 4.
Eval compute in pascall 1 0.
Eval compute in pascall 1 1.

Fixpoint div_aux n k cur :=
match k with
0 => 0
| S pk => match n with
0 => 0
| S pn => match cur with 
0 => S (div_aux pn k pk)
| S pc => div_aux pn k pc
end
end
end.

Definition div n k :=
match k with
0 => 0
| S pk => (div_aux n k pk)
end.

Eval compute in div_aux 15 1 0.
Eval compute in div 5 3.
Eval compute in div 6 3.
Eval compute in div 7 0.
Eval compute in div 7 1.
Eval compute in div 7 2.
Eval compute in div 7 3.
Eval compute in div 7 4.
Eval compute in div 7 5.
Eval compute in div 7 6.
Eval compute in div 7 7.
Eval compute in div 7 8.

Require Import Arith.


Lemma div_frac_aux : forall k c , (div_aux 0 k c) = 0.
Proof.
intros.
induction k.
simpl.
reflexivity.
simpl.
reflexivity.
Qed.

Lemma div_frac : forall k, div 0 k = 0.
Proof.
intros.
induction k.
simpl.
reflexivity.
simpl.
reflexivity.
Qed.

Fixpoint pascall' n k :=
match k with
0 => 1
| S pk => match n with
0 => 0
| S pn => div ((pascall' pn pk) * n) k
end
end.

Eval compute in pascall' 4 0.
Eval compute in pascall' 4 1.
Eval compute in pascall' 4 2.
Eval compute in pascall' 4 3.
Eval compute in pascall' 4 4.

Lemma xeror : forall x , x = 0 -> S x = 1.
intros.
induction x.
reflexivity.
rewrite H.
reflexivity.
Qed.

Lemma da_trans : forall a b c, b = S c -> div a b = div_aux a b c.
intros.
induction b.
discriminate H.
simpl.
rewrite H.
assert (b = c).
SearchRewrite ( S _ ).
Check eq_S.
auto.
auto.
Qed.



Lemma div_unit : forall a, div a 1 = a.
intros.
induction a.
simpl.
ring.
simpl.
auto.
Qed.


Lemma div_up : forall a b c, b > 0 -> a = div c b -> (S a) = div (c + b) b.
intros.
destruct H.
rewrite H0.
simpl.
induction c.
simpl.
reflexivity.
SearchRewrite (S _) .
simpl.
rewrite eq_S with (y := (div_aux (c + 1) 1 0)).
ring.
rewrite <- da_trans.
rewrite <- da_trans.
assert (c+1 = S c).
ring.
rewrite H.
rewrite div_unit.
rewrite div_unit.
reflexivity.
reflexivity.
reflexivity.
induction m.
auto.
simpl.





Lemma psc : forall n k, pascall n k = pascall' n k.
intros.
induction n.
simpl.
induction k.
reflexivity.
reflexivity.
induction k.
simpl.
reflexivity.
simpl pascall.
simpl.









