qs :: [Integer] -> [Integer] 
qs [] = []
qs (x:xs) =
    let front = [y | y <- xs, y <= x] -- smaller or equal to x
        back = [y | y <- xs, y > x] -- larger than x
    in (qs front) ++ [x] ++ (qs back)
    
quicksort :: (Ord a) => [a] -> [a]  
quicksort [] = []  
quicksort (x:xs) =   
    let smallerSorted = quicksort [a | a <- xs, a <= x]  
        biggerSorted = quicksort [a | a <- xs, a > x]  
    in  smallerSorted ++ [x] ++ biggerSorted  

split :: [Integer] -> ([Integer], [Integer])
split []  = ([],[])
split (a:[]) = ([a],[])
split (a:(b:rest)) =
    let sploit = split rest
    in (a:(fst sploit), b:(snd sploit))

merge :: [Integer] -> [Integer] -> [Integer]
merge [] [] = []
merge [] l = l
merge l [] = l
merge (x:xs) (y:ys)
    | x < y    = x:(merge xs (y:ys))
    | otherwise = y:(merge (x:xs) ys)

mergesort :: [Integer] -> [Integer] 
mergesort [] = []
mergesort (x:[]) = [x]
mergesort lst = 
    let splat = split lst
    in (merge (mergesort (fst splat)) (mergesort (snd splat)))
        
main = do
    print (merge [x | x <-[1..10], x `mod` 2 == 1] [x | x <-[1..10], x `mod` 2 == 0])
    print [1..14]
    print (split [1..14])
    print [7,43,6,43,45,6,34,32,23]
    print (qs [7,43,6,43,45,6,34,32,23])
    print (quicksort [7,43,6,43,45,6,34,32,23])
    print (mergesort [7,43,6,43,45,6,34,32,23])
