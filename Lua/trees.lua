
TYPE_BIR = 1
TYPE_JUN = 2
TYPE_OAK = 3
TYPE_SPR = 4
TREE_NAMES = {"Birch", "Jungle", "Oak", "Spruce"}

TREES_FAR = 5
TREES_RIGHT = 5
TREE_SPACE = 5
TREE_MAX_HEIGHT = 10
NUM_TREES = TREES_FAR * TREES_RIGHT
FUEL_REQ = (NUM_TREES * TREE_MAX_HEIGHT) + (TREES_FAR*TREES_RIGHT*TREE_SPACE*2) + (TREES_RIGHT*TREE_SPACE*2) + 100

function goF()
	while not turtle.forward() do
		turtle.dig()
	end
end

function goD()
	while not turtle.down() do
		turtle.digDown()
	end
end

function goU()
	while not turtle.up() do
		turtle.digUp()
	end
end

-- Refuels up to the given level
-- Returns false if it cant refuel
-- Assumes there is a chest with fuel above the turtle
function refuelUntil(reqAmt)
	while turtle.getFuelLevel() < reqAmt do
		if not turtle.suckUp() then
			print("Couldn't get fuel from the chest")
			return false
		end
		if not turtle.refuel(1) then
			print("Got a non-fuel item from fuel chest")
			return false
		end
		if not turtle.dropUp() then
			print("Couldn't return fuel to chest")
			return false
		end
	end
	return true
end

--returns true if all inventory slots are empty
function hasEmptyInventory()
	for idx = 1, 16 do
		if turtle.getItemCount(idx) > 0 then
			return false
		end
	end
	turtle.select(1)
	return true
end

--returns the selected tree
function getTreeType()
	return 1
end

function notGetTreeType()
	print("Select a tree")
	for idx = 1, table.getn(TREE_NAMES) do
		print(idx .. " " .. TREE_NAMES[idx])
	end
	local sel = 0
	while sel < 1 or sel > table.getn(TREE_NAMES) do
		sel = tonumber(io.read())
	end
	return sel
end

-- returns true if a tree was cut down
-- tree must be directly in front of turtle, turtle will finish above the sappling, even if there is no tree
function doTree(replantSappling)
	goF()
	if turtle.detectUp() then
		turtle.digDown()
		local rise = 0
		while turtle.detectUp() do
			goU()
			rise = rise + 1
		end
		for lwr = 1, rise do
			goD()
		end
	end
	if replantSappling and not turtle.detectDown() then
		turtle.select(1)
		turtle.placeDown()
	end
end

function prepare(sapplingType)
	turtle.turnLeft()
	for idx = 1, sapplingType-1 do
		goF()
	end
	turtle.select(1)
	turtle.suckDown()
	if turtle.getItemCount(1) > (TREES_FAR*TREES_RIGHT) then
		turtle.dropDown(turtle.getItemCount(1) - (TREES_FAR*TREES_RIGHT))
	end
	if turtle.getItemCount(1) < (TREES_FAR*TREES_RIGHT) then
		print("I might run out of sapplings")
	end
	turtle.turnRight()
	goF()
	goF()
	turtle.turnRight()
	for idx = 1, sapplingType-1 do
		goF()
	end
	goF()
	goF()
	goF()
end

function deforest()
	local turnRight = true
	local sapplingCount = turtle.getItemCount(1)
	print("Will deforest and re-plant " .. sapplingCount .. " sapplings")
	for col=1, TREES_RIGHT do
		for row=1, TREES_FAR do
			if doTree(sapplingCount > 0) then
				sapplingCount = sapplingCount - 1
			end
			if row < TREES_FAR then
				goF()
				goF()
				goF()
				goF()
			end
		end
		if col < TREES_RIGHT then
			goF()
			if turnRight then
				turtle.turnRight()
			else
				turtle.turnLeft()
			end
			goF()
			goF()
			goF()
			goF()
			goF()
			if turnRight then
				turtle.turnRight()
			else
				turtle.turnLeft()
			end
			turnRight = not turnRight
		end
	end
	if turnRight then
		turtle.turnRight()
		goF()
		turtle.turnRight()
		for backTrack=1, TREE_SPACE*(TREES_FAR-1) + 1 do
			goF()
		end
		turtle.turnRight()
		goF()
	else
		goF()
		turtle.turnRight()
	end
	for leftTrack=1, TREE_SPACE*(TREES_RIGHT-1) do
		goF()
	end
	turtle.turnRight()
end

function rebase()
	turtle.turnLeft()
	turtle.turnLeft()
	goF()
	goF()
	goF()
	goF()
	goF()
	turtle.turnLeft()
	goF()
	goF()
	for idx = 1, 16 do
		dat = turtle.getItemDetail(idx)
		if dat and dat.name == "minecraft:log" then
			turtle.select(idx)
			turtle.dropUp()
		end
	end
	turtle.turnLeft()
	goF()
	goF()
	turtle.turnLeft()
	for idx = 1, 16 do
		dat = turtle.getItemDetail(idx)
		if dat then
			turtle.select(idx)
			turtle.dropDown()
		end
	end
end

--------------------------------------------------
--                   | Station  From perspective, turtle faces me
--           S S S   | C . W
--   ^       |       | T . .
-- < T - - - +       | S S .
if hasEmptyInventory() then
	selectedTree = getTreeType()
	if refuelUntil(FUEL_REQ) then
		prepare(selectedTree)
		deforest()
		rebase()
	else
		print("Not enough fuel for this, i need " .. FUEL_REQ .. " and have " .. turtle.getFuelLevel())
	end
else
	print("Please empty my inventory")
end