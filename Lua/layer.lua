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

function checkLeft()
		turtle.turnLeft()
		local rtn = turtle.detect()
		turtle.turnRight()
		return rtn
end

function checkRight()
		turtle.turnRight()
		local rtn = turtle.detect()
		turtle.turnLeft()
		return rtn
end

going = true
while going do
	-- move forward up to the next block
	while not turtle.detect() do
		forward()
	end
	-- cut out blocks in a line
	has_left = false
	has_right = false
	while turtle.detect() do
		if not has_left and not has_right then
			has_left = checkLeft()
		end
		if not has_left and not has_right then
			has_right = checkRight()
		end
		forward()
	end
	-- continue until we run out of blocks beside us
	if has_left then
		while checkLeft() do
			forward()
		end
	elseif has_right then
		while checkRight() do
			forward()
		end
	end
	-- do a u-turn
	if has_left then
		turtle.turnLeft()
		forward()
		turtle.turnLeft()
	elseif has_right then
		turtle.turnRight()
		forward()
		turtle.turnRight()
	else
		going = false
	end
end