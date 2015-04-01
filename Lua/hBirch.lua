os.loadAPI("libs/tree")

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

--goes down, digging if necessary
function goD()
	while not turtle.down() do
		turtle.digDown()
	end
end

--digs out the leaf blocks
function unLeaf()
	turtle.dig()
	goF()
	turtle.digUp()
	turtle.digDown()
end

-- clears a tree by digging to the top, spiraling the leaves, then digging down the trunk, finally replacing the sappling
function unTree()
	if turtle.forward() then return end
	tree.birch(false, true)
	goF()
end

local tArgs = {...}
local trees = 8
if tArgs[1] then trees = tonumber(tArgs[1]) end
if turtle.getFuelLevel() < trees*60 then
	print("Out of fuel")
	exit();
end

function selectSlotWith(name)
	for slot=1,16 do
		local inv = turtle.getItemDetail(slot)
		if inv and (inv.name == name) then
			turtle.select(slot)
			return true
		end
	end
	return false
end

for i=1,2 do
	for t=1,trees do
		unTree()
		if not turtle.detectDown() and selectSlotWith("minecraft:sapling") then
			turtle.placeDown()
		end
		if t ~= trees then
			for d=1,4 do
				goF()
			end
		end
	end
	goF()
	turtle.turnRight()
	turtle.turnRight()
end
