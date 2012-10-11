main = do
	putStrLn "Name:"
	name <- getLine
	putStrLn ("Hello " ++ name  ++ ", welcome!")
