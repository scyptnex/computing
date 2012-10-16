(defun contains_r (elem lst)
	(if (null lst)
		nil
		(if (eql (first lst) elem)
			t
			(contains_r elem (rest lst))
		)
	)
)

(defun duplicates_r (rlst ilst)
	(cond
		((null ilst) rlst)
		((contains_r (first ilst) rlst) (duplicates_r rlst (rest ilst)))
		(t (duplicates_r (append rlst (list (first ilst))) (rest ilst)))
	)
)

(defun all-columns (query)
	(cond
		((eql (first query) 's-nand)
			(append (all-columns (nth 1 query)) (all-columns (nth 2 query)))
		)
		((eql (first query) 's-xor)
			(append (all-columns (nth 1 query)) (all-columns (nth 2 query)))
		)
		((eql (first query) 's-not)
			(all-columns (nth 1 query))
		)
		( t
			(list (nth 1 query))
		)
	)
)

(defun find-columns (query)
	(duplicates_r (list) (all-columns query))
)

(find-columns '(s-nand (s-geq fuel 5) (s-le fuel 100)))
