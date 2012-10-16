(defun sadd (e1 e2)
	(cond
		(
			(and (symbolp e1) (numberp e2))
			(if (= e2 0) e1 (list '+ e1 e2))
		)
		(
			(and (symbolp e2) (numberp e1))
			(if (= e1 0) e2 (list '+ e1 e2))
		)
		(
			(and (symbolp e1) (symbolp e2))
			(if (eq e1 e2)
		)
		(t (+ e1 e2))
	)
)

(sadd 'x 'x)