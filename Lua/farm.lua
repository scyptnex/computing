-- Dig a 9x9 farming plot
-- place water in the center
-- place the turtle above one of the corners, so that th farm is in front and to the right
-- (optional) place a chest above the turtle
-- place seeds in slot 1

--goes forward, digging if necessary
function goF()
	while not turtle.forward() do
		turtle.dig()
	end
end

--Refuel using everything you can
function allRefuel()
	for i=1,16 do
		turtle.select(i)
		turtle.refuel()
	end
	turtle.select(1)
end

function farmBlock()
	local success, data = turtle.inspectDown()
	if success and data.metadata == 7 then
		turtle.digDown()
		turtle.placeDown()
	end
end

function doFarm()
	for r=1,9 do
		for f=1,9 do
			farmBlock()
			if f~=9 then goF() end
		end
		if(r ~= 9) then
			if r%2==1 then turtle.turnRight() else turtle.turnLeft() end
			goF()
			if r%2==1 then turtle.turnRight() else turtle.turnLeft() end
		end
	end
	turtle.turnLeft()
	for i=1,8 do goF() end
	turtle.turnLeft()
	for i=1,8 do goF() end
	for i=2,16 do
		turtle.select(i)
		turtle.dropUp()
	end
	turtle.select(1)
	turtle.turnLeft()
	turtle.turnLeft()
end

allRefuel()
if turtle.getFuelLevel() < 9*9*2 then
	print("Out of fuel")
	exit()
elseif turtle.getItemCount(1) < 1 then
	print("Put at least 1 seed in slot 1")
	exit()
else
	print("Farming")
	doFarm()
end