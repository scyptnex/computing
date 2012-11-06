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

Lemma div_up : forall a b c, a = div c b -> a + b = S (div c b).
intros.
induction a.
induction b.
induction c.
apply H.
rewrite div_frac.




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









