Require Import Bool.
Require Import Arith.
Require Import List.

Inductive c_stream : Type :=
A (n:nat)
| PL : c_stream -> c_stream -> c_stream
| FB : c_stream -> c_stream
| SJ : (list c_stream) -> c_stream
.
Check (SJ ((A 3)::nil)).

Fixpoint num_nodes (s:c_stream) :=
match s with
A n => 1
| PL s1 s2 => 1 + (num_nodes s1) + (num_nodes s2)
| FB ss => 1 + (num_nodes ss)
| SJ lst => match lst with
            nil => 1
           |ss::rst => (num_nodes ss) + (num_nodes (SJ rst))
            end
end.


