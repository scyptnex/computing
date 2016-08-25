import qualified Data.Map as Map

data L a = Var a | Lam a (L a) | App (L a) (L a) deriving ( Eq )

instance Show a => Show (L a) where
    show (Var x) = show x
    show (Lam h b) = "\\" ++ (show h) ++ ".(" ++ (show b) ++ ")"
    show (App l r) = "(" ++ (show l) ++ " " ++ (show r) ++ ")"

alpha_eq :: Ord a => Eq b => L a -> L b -> Map.Map a b -> Bool
alpha_eq (Var v1) (Var v2) m = Map.findWithDefault v2 v1 m == v2
alpha_eq (Lam h1 b1) (Lam h2 b2) m = alpha_eq b1 b2 (Map.insert h1 h2 m)
alpha_eq (App l1 r1) (App l2 r2) m = (alpha_eq l1 l2 m) && (alpha_eq r1 r2 m)
alpha_eq _ _ _ = False

substitute :: Eq a => L a -> a -> L a -> L a
substitute (Var x) v s
    | x == v = s
    | otherwise = Var x
substitute (Lam h b) v s
    | h == v = Lam h b -- scoping causes this, i.e. this lambda redefines the substitution variable
    | otherwise = Lam h (substitute b v s)
substitute (App l r) v s = App (substitute l v s) (substitute r v s)

beta :: Eq a => L a -> L a
beta (Var x) = (Var x)
beta (Lam h b) = (Lam h (beta b))
beta (App (Lam h b) x) = substitute b h x

tokenize_sub :: [Char] -> ([Char],[Char])
tokenize_sub [] = ([],[])
tokenize_sub (' ':rest) = ([],rest)
tokenize_sub ('\t':rest) = ([],rest)
tokenize_sub ('(':rest) = ([],('(':rest))
tokenize_sub (')':rest) = ([],(')':rest))
tokenize_sub ('.':rest) = ([],('.':rest))
tokenize_sub ('\\':rest) = ([],('\\':rest))
tokenize_sub (c:rest) = let (e, r) = tokenize_sub rest
                        in (c:e, r)

tokenize :: [Char] -> [[Char]]
tokenize [] = []
tokenize (' ':rest) = tokenize rest
tokenize ('\t':rest) = tokenize rest
tokenize ('(':rest) = "(":tokenize rest
tokenize (')':rest) = ")":tokenize rest
tokenize ('.':rest) = ".":tokenize rest
tokenize ('\\':rest) = "\\":tokenize rest
tokenize str = let (tok,rest) = tokenize_sub str
               in tok:(tokenize rest)

parse :: [[Char]] -> L [Char]


main = do
    let exp = (App (Lam "x" (Var "x")) (Var "y"))
    print exp
    print (beta exp)
    print (alpha_eq exp (Var "z") Map.empty)
    print (alpha_eq exp (App (Var "y") (Var "y")) Map.empty)
    print (alpha_eq exp (App (Lam 7 (Var 7)) (Var 2)) Map.empty)
    print $ tokenize "\\true false    .true  true"
