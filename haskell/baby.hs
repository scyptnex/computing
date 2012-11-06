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

multThree x y z = x * y * z

applyTwice f x = f (f x)

zipWith' :: (a -> b-> c) -> [a] -> [b] -> [c]
zipWith' _ [] _ = []
zipWith' _ _ [] = []
zipWith' f (x:xs) (y:ys) = f x y : zipWith' f xs ys

chain :: (Integral a) => a -> [a]
chain 1 = [1]
chain n 
	| even n = n:chain (n `div` 2)
	| odd n = n:chain (n*3 + 1)

choose n 0 = 1
choose 0 k = 0
choose n k = choose (n-1) (k-1) * n `div` k

