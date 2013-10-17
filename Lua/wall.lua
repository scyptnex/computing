function down()
	while not turtle.down() do
		if turtle.getFuelLevel() < 1 then
			if not turtle.refuel(1) then
				print("Out of fuel")
				exit()
			end
		else
			turtle.digDown()
		end
	end
end

function forward()
	while not turtle.forward() do
		if turtle.getFuelLevel() < 1 then
			if not turtle.refuel(1) then
				print("Out of fuel")
				exit()
			end
		else
			turtle.dig()
		end
	end
end

function compareInventory(count)
	for slot = 2,(1+count) do
		turtle.select(slot)
		if turtle.compare() then
			turtle.select(1)
			return true
		end
	end
	turtle.select(1)
	return false
end

function unwall(right, under, goRight, avoid)
	for height = 1,under do
		for side = 1, right do
			if not compareInventory(avoid) then
				turtle.dig()
			end
			if side < right then
				if goRight then
					turtle.turnRight()
				else
					turtle.turnLeft()
				end
				forward()
				if goRight then
					turtle.turnLeft()
				else
					turtle.turnRight()
				end
			end
		end
		down()
		goRight = not goRight
	end
end

-----------------------------------------------------------------------------------------------

forwards = 0
local right = 0
local below = 0
local checks = 0

print("at the lower-left-backmost corner or the area")
print("How many blocks right")
right = tonumber(io.read())
print("How many blocks DOWN")
below = tonumber(io.read())
print("How many blocks to AVOID")
checks = tonumber(io.read())

unwall(right, below, true, checks)