--
-- Movement for turtles
--

-- records turtle location
local x = 0 -- +x = forward, -x = backwards
local y = 0 -- minecraft y i.e. height
local z = 0 -- +z = right, -z = left
local heading = 0 -- turtle facing, +x, +z, -x, -z

-- reset the current locaion
function reset()
	x = 0
	y = 0
	z = 0
	heading = 0
end

function headingName()
	if heading == 0 then return "forwards"
	elseif heading == 1 then return "rightwards"
	elseif heading == 2 then return "backwards"
	elseif heading == 3 then return "leftwards"
	end
end

function location()
	return x, y, z, heading
end

-- Returns true if there is enough fuel for this many moves
function fuelCheck(amt)
	return (turtle.getFuelLevel() > amt)
end

--turns right
function right()
	turtle.turnRight()
	heading = (heading+1)%4
end

--turns left
function left()
	turtle.turnLeft()
	heading = (heading+3)%4
end

--goes forward, digging if necessary
function goF()
	if turtle.getFuelLevel() < 1 then
		print("out of fuel")
		return
	end
	while not turtle.forward() do
		turtle.dig()
	end
	if heading == 0 then x = x+1
	elseif heading == 1 then z = z+1
	elseif heading == 2 then x = x-1
	elseif heading == 3 then z = z-1
	end
end

--goes up, digging if necessary
function goU()
	if turtle.getFuelLevel() < 1 then
		print("out of fuel")
		return
	end
	while not turtle.up() do
		turtle.digUp()
	end
	y = y + 1
end

--goes down, digging if necessary
function goD()
	if turtle.getFuelLevel() < 1 then
		print("out of fuel")
		return
	end
	while not turtle.down() do
		turtle.digDown()
	end
	y = y - 1
end

--goes forward
function softF()
	if turtle.getFuelLevel() < 1 then
		print("out of fuel")
		return
	end
	while not turtle.forward() do
		--do nothing
	end
	if heading == 0 then x = x+1
	elseif heading == 1 then z = z+1
	elseif heading == 2 then x = x-1
	elseif heading == 3 then z = z-1
	end
end

--goes up
function softU()
	if turtle.getFuelLevel() < 1 then
		print("out of fuel")
		return
	end
	while not turtle.up() do
		--do nothing
	end
	y = y + 1
end

--goes down
function softD()
	if turtle.getFuelLevel() < 1 then
		print("out of fuel")
		return
	end
	while not turtle.down() do
		--do nothing
	end
	y = y - 1
end

