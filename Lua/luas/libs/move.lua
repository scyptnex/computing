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
	print("[using " .. amt .. "/" .. turtle.getFuelLevel() .. " fuel]")
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

-- Turn to the heading
function turn(newH)
	while heading ~= newH do
		local lt = (heading - newH + 4)%4
		local rt = (newH - heading + 4)%4
		if lt < rt then left() else right() end
	end
end

-- Move softly to the x (-back +forward) location
function softX(newX)
	if newX == x then return end
	if newX < x then turn(2) else turn(0) end
	while x ~= newX do softF() end
end

-- Move softly to the z (-left +right) location
function softZ(newZ)
	if newZ == z then return end
	if newZ < z then turn(3) else turn(1) end
	while z ~= newZ do softF() end
end

