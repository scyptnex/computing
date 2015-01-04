--goes forward, digging if necessary
function goF()
	while not turtle.forward() do
		turtle.dig()
	end
end

local tArgs = {...}
local forward = tonumber(tArgs[1])
if turtle.getFuelLevel() < forward then
	print("Out of fuel")
	exit();
end
print("Digging forward " .. forward)
for f=1,forward do
	while turtle.detectUp() do turtle.digUp() end
	while turtle.detectDown() do turtle.digDown() end
	if f ~= forward then goF() end
end