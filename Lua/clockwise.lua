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

going = true
while going do
	forward()
	turtle.turnLeft()
	searching = true
	looked = 0
	while searching do
		looked = looked + 1
		if looked >= 4 then
			searching = false
			going = false
		elseif turtle.detect() then
			searching = false
		else
			turtle.turnRight()
		end
	end
end


