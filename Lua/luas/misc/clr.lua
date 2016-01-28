function up()
	while not turtle.up() do
		if turtle.getFuelLevel() < 1 then
			if not turtle.refuel(1) then
				print("Out of fuel")
				exit()
			end
		else
			turtle.digUp()
		end
	end
end

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

function chisel(abv, goUp)
	local cur = 1
	while cur < abv do
		if goUp then
			up()
		else
			down()
		end
		cur = cur + 1
	end
end

function slice(fwd, abv, goingUp)
	local cur = 0
	while cur < fwd do
		chisel(abv, goingUp)
		goingUp = not goingUp
		if cur < fwd-1 then
			forward()
		end
		cur = cur + 1
	end
	return goingUp
end

function carve(fwd, rgt, abv)
	print("Carving " .. fwd .. "x" .. rgt .. "x" .. abv .. " area")
	local goingRight = true
	local upwards = true
	local cur = 0
	while cur < rgt do
		upwards = slice(fwd, abv, upwards)
		if cur < rgt - 1 then
			if goingRight then
				turtle.turnRight()
			else
				turtle.turnLeft()
			end
			forward()
			if goingRight then
				turtle.turnRight()
			else
				turtle.turnLeft()
			end
			goingRight = not goingRight
		end
		cur = cur + 1
	end
end

--------------------------------------------------------------------------------------

fuel_req = 0
forwards = 0
right = 0
above = 0

print("at the lower-left-backmost corner or the area")
print("How many blocks forward")
forwards = tonumber(io.read())
print("How many blocks right")
right = tonumber(io.read())
print("How many blocks up")
above = tonumber(io.read())

carve(forwards, right, above)