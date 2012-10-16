(defun transformer (query)
	(cond
		((eql (first query) 's-nand)
			(lambda (row)
				(not (and (funcall (transformer (nth 1 query)) row) (funcall (transformer (nth 2 query)) row)))
			)
		)
		((eql (first query) 's-xor)
			(lambda (row)
				(lisp-will-never-succeed-as-a-language-without-xor
					(funcall (transformer (nth 1 query)) row)
					(funcall (transformer (nth 2 query)) row)
				)
			)
		)
		((eql (first query) 's-not)
			(lambda (row)
				(not (funcall (transformer (nth 1 query)) row))
			)
		)
		( t
			(lambda (row)
				(roweval_p (nth 0 query) (nth 1 query) (nth 2 query) row)
			)
		)
	)
)

(defun roweval_p (pred col val row)
	(setq sock (assoc col row))
	(if (null sock)
		nil
		(predicate_p pred (nth 1 sock) val)
	)
)

(defun predicate_p (pred v1 v2)
	(cond
		((eql pred 's-eq) (= v1 v2))
		((eql pred 's-ge) (> v1 v2))
		((eql pred 's-le) (< v1 v2))
		((eql pred 's-geq) (>= v1 v2))
		((eql pred 's-leq) (<= v1 v2))
		(t (/= v1 v2))
	)
)

(defun lisp-will-never-succeed-as-a-language-without-xor (a b)
	(or (and a (not b)) (and (not a) b))
)

(setq tq (transformer '(s-nand (s-geq fuel 5) (s-le fuel 100))))
(print (funcall tq '((speed 300) (fuel 36))))
(print (funcall tq '((speed 56) (fuel 100))))



(setq row1 '((fuel 1)) )
(setq row2 '((speed 1)) )
(setq row3 '((fuel 1) (b 3)) )
(setq row4 '((fuel 1) (b 4)) )
(setq table (list row1 row2 row3 row4))

"(print (numberp (nth 1 (assoc 'a row1))))"

"(if (null (assoc 'a row1)) (print 5) (print 4))"

"(setq query '(s-xor (s-eq b 3) (s-eq c 0) ) )
(print query)
(setq query_f (transformer query) )
(print (funcall query_f row1))
(print (mapcar query_f table))"

"(print (ceq 1 row))"
"(print (roweval_rp 's-diff 'a 1 row1))"
"(rowevalp '= 'cheese '5 )"
"(setq tq (eq_f 'cheese 1))
(print (funcall tq '((dog 3) (cheese 2) (happy 4))))"
"(setq tq 'geq5)"
"(print (funcall tq 5))"