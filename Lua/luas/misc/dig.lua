--goes forward, digging if necessary
function goF()
	while not turtle.forward() do
		turtle.dig()
	end
end

local tArgs = {...}
local forward = tonumber(tArgs[1])
local right = tonumber(tArgs[2])
if turtle.getFuelLevel() < forward then
	print("Out of fuel")
	exit();
end
print("Digging forward " .. forward .. " right " .. right)
for r=1,right do
	for f=1,forward do
		while turtle.detectUp() do turtle.digUp() end
		while turtle.detectDown() do turtle.digDown() end
		if f ~= forward then goF() end
	end
	if r ~= right then
		if r%2 == 1 then turtle.turnRight() else turtle.turnLeft() end
		goF()
		if r%2 == 1 then turtle.turnRight() else turtle.turnLeft() end
	end
end