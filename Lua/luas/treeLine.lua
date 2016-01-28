os.loadAPI("libs/tree")
os.loadAPI("libs/inventory")
os.loadAPI("libs/args")

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

-- clears a tree by digging to the top, spiraling the leaves, then digging down the trunk, finally replacing the sappling
function unTree()
	if turtle.forward() then return end
	tree.birch(false, true)
	goF()
end

local trees = args.iArgDef(1, 8, ...)
print("Cutting down ", trees, " trees")
if turtle.getFuelLevel() < trees*60 then
	print("Out of fuel")
	exit();
end

for i=1,2 do
	for t=1,trees do
		unTree()
		if not turtle.detectDown() and inventory.selectSlotWith("minecraft:sapling") then
			turtle.placeDown()
			turtle.select(1)
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
