data L a = Nil | Cons a (L a)

instance (Show a) => Show (L a) where
    show Nil = ","
    show (Cons a x) =  "," ++ (show a) ++ (show x)

lst :: [a] -> L a
lst [] = Nil
lst (x:xs) = Cons x (lst xs)

main = do
    print "Hello World!"
    print (lst [1,2,3])
