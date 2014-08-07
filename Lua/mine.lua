function goF()
	while not turtle.forward() do
		turtle.dig()
	end
end

function checkUD()
	if not turtle.compareUp() then
		turtle.digUp()
	end
	if not turtle.compareDown() then
		turtle.digDown()
	end
end

function digOut(forwards, right)
	rcount = 0
	away = true
	while rcount < right do
		fcount = 0
		while fcount < forwards do
			goF()
			checkUD()
			fcount = fcount + 1
		end
		rcount = rcount+1
		if rcount ~= right then
			if away then
				turtle.turnRight()
				goF()
				checkUD()
				turtle.turnRight()
			else
				turtle.turnLeft()
				goF()
				checkUD()
				turtle.turnLeft()
			end
			away = not away
		end
	end
	if away then
		turtle.turnRight()
		turtle.turnRight()
		for f = 1, forwards do
			goF()
		end
	end
	turtle.turnRight()
	for r = 2, right do
		goF()
	end
end

fwd = 0
rgh = 0

print("at the left-backmost corner or the area")
print("How many blocks forward")
fwd = tonumber(io.read())
print("How many blocks right")
rgh = tonumber(io.read())

turtle.select(1)
if turtle.getFuelLevel() < fwd*rgh*2 or turtle.getItemCount(1) < 1 then
	print("Out of fuel or smooth stone missing")
	exit()
else
	print("going " .. fwd .. " forwards " .. rgh .. " right")
end

digOut(fwd, rgh)