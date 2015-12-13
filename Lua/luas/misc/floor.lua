--goes forward
function softF()
	while not turtle.forward() do
		--do nothing
	end
end

function placeItem(name)
	local worked, dat = turtle.inspectDown()
	if worked and dat.name == name then return true end
	for slot=1,16 do
		local inv = turtle.getItemDetail(slot)
		if inv and (inv.name == name) then
			turtle.select(slot)
			if worked then turtle.digDown() end
			turtle.placeDown()
			return true
		end
	end
	return false
end

local tArgs = {...}
local forward = tonumber(tArgs[1])
local right = tonumber(tArgs[2])
local inv = turtle.getItemDetail(slot)
if not inv then
	print("No item in slot")
	exit()
end
local itm = inv.name
print("forward " .. forward .. ", right " .. right)
if turtle.getFuelLevel() > forward*right then
	for r=1,right do
		for f=1,forward do
			if not placeItem(itm) then
				print("Could not place item")
				exit()
			end
			if f ~= forward then softF() end
		end
		if r ~= right then
			if r%2 == 1 then turtle.turnRight() else turtle.turnLeft() end
			softF()
			if r%2 == 1 then turtle.turnRight() else turtle.turnLeft() end
		end
	end
else
	print("Not enough fuel")
end
