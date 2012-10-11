import Data.Char

doubleMe x = x + x

doubleUs x y = doubleMe x + doubleMe y

doubleSmallNumber x = if x > 100
	then x
	else doubleMe x

cons car cdr = car:cdr

scyElem a b = b !! a

stringToLower a = if (null a)
			then []
			else (toLower (head a)):(stringToLower (tail a))

scyCompare a b = if a > b
		then GT
		else if a == b
		then EQ
		else LT

caseInsensitiveCompare a b = scyCompare (stringToLower a) (stringToLower b)

quicksort :: (Ord a) => [a] -> [a]
quicksort [] = []
quicksort (x:xs) =
	(quicksort [a | a <- xs, a <= x]) ++ (x:(quicksort [a | a <- xs, a > x]))

merge a [] = a
merge [] b = b
merge (x:xs) (y:ys) =
		if x > y
		then y:(merge (x:xs) ys)
		else x:(merge xs (y:ys))

split :: [a] -> ([a],[a])
split [] = ([],[])
split a = 	let hlf = quot (length a) 2
		in ((take hlf a),(drop hlf a))

mergesort :: (Ord a) => [a] -> [a]
mergesort [] = []
mergesort [x] = [x]
mergesort lst = let (front,back) = split lst
		in merge (mergesort front) (mergesort back)
