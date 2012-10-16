
; == Task 1 ==

(defun booleanp (expr)
  (member (first expr) '(s-nand s-xor s-not)))

(defun boolean1p (expr)
  (member (first expr) '(s-not)))

(defun boolean2p (expr)
  (member (first expr) '(s-nand s-xor)))

(defun comparatorp (expr)
  (member (first expr) '(s-eq s-le s-ge s-leq s-geq s-diff)))

(defun extract-cols (items)
  (if (null items) 
    nil
    (union
      (if (symbolp (first items)) (list (first items)) nil)
      (extract-cols (rest items)))))

(defun find-columns (expr)
  (cond
    ((booleanp expr) (union (find-columns (nth 1 expr)) (find-columns (nth 2 expr))))
    ((comparatorp expr) (extract-cols (rest expr)))))

(print (find-columns '(s-nand (s-geq fuel 5) (s-le bar baz))))


; == Task 2 ==

(defun nand (a b)
  (not (and a b)))

(defun xor (a b)
  (and (or a b) (not (and a b))))

(defun comparator-to-op (comp)
  (second (assoc comp '((s-eq =) (s-diff /=) (s-le <) (s-leq <=) (s-gt >) (s-geq >=)))))

(defun boolean-to-op (bool)
  (second (assoc bool '((s-not not) (s-nand nand) (s-xor xor)))))

(defun comparator-value (sym data)
  (let ((val (assoc sym data)))
    (if (null val) sym (second val))))

(defun do-comparator (op a b)
  (lambda (data) 
    (funcall (comparator-to-op op) (comparator-value a data) (comparator-value b data))))

(defun do-boolean1 (op a)
  (lambda (data) 
    (funcall (boolean-to-op op) (funcall (transformer a) data))))

(defun do-boolean2 (op a b)
  (lambda (data) 
    (funcall (boolean-to-op op) (funcall (transformer a) data) (funcall (transformer b) data))))

(defun transformer (expr)
  (cond
    ((boolean1p expr)   (do-boolean1   (nth 0 expr) (nth 1 expr)))
    ((boolean2p expr)   (do-boolean2   (nth 0 expr) (nth 1 expr) (nth 2 expr)))
    ((comparatorp expr) (do-comparator (nth 0 expr) (nth 1 expr) (nth 2 expr)))))


(setq tq (transformer '(s-not (s-nand (s-geq fuel 5) (s-le fuel 100)))))
(print (funcall tq '((speed 300) (fuel 50))))
(print (funcall tq '((speed 70)  (fuel 100))))

