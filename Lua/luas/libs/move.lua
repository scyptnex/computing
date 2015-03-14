--
-- Movement for turtles
--

--goes forward, digging if necessary
function goF()
	while not turtle.forward() do
		turtle.dig()
	end
end

--goes up, digging if necessary
function goU()
	while not turtle.up() do
		turtle.digUp()
	end
end

--goes down, digging if necessary
function goD()
	while not turtle.down() do
		turtle.digDown()
	end
end

--goes forward
function softF()
	while not turtle.forward() do
		--do nothing
	end
end

--goes up
function softU()
	while not turtle.up() do
		--do nothing
	end
end

--goes down
function softD()
	while not turtle.down() do
		--do nothing
	end
end

