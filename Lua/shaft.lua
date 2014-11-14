-- shaft.lua [distance]
--start at the bottom, place torches on the way back

--goes forward, digging if necessary
function goF()
	while not turtle.forward() do
		turtle.dig()
	end
end

--goes up, digging if necessary
function goU()
	while not turtle.up() do
		turtle.digUp()
	end
end

tArgs={...}
fwd = tonumber(tArgs[1])
if (turtle.getFuelLevel() < (fwd*2 + 2) or turtle.getItemCount(1) < 1 or turtle.getItemCount(2) < 32) then
	print("Out of fuel or no torches or <32 stone")
	exit()
end

for f=1,fwd do
	goF()
end
turtle.turnRight()
turtle.turnRight()
goU()

for f=1,fwd do
	if (f % 10 == 1) then
		turtle.select(1)
		turtle.placeDown()
	end
	if (not turtle.detectUp()) then
		turtle.select(2)
		turtle.placeUp()
	end
	goF()
end