--Places items to be cooked in a line of furnaces

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

function checkEmpty()
	for idx = 1, 16 do
		if turtle.getItemCount(idx) > 0 then
			print("Inventory must be empty")
			return false
		end
	end
	return true
end

function checkRefuel(amt, chestSuckFunction)
	while turtle.getFuelLevel() < amt do
		local famt = 1+(amt-turtle.getFuelLevel())/80
		if famt > 64 then famt = 64 end
		if not chestSuckFunction(famt) then
			print ("not enough fuel")
			return false
		end
		turtle.refuel()
	end
	return true
end

--pulls items, places excess in bottom chest
--return number of slots used
function loadItems(numFurn)
	local going = true
	for i=1,numFurn do
		turtle.select(i)
		turtle.suckUp(64)
		local dAmt = turtle.getItemCount(i)%8
		if dAmt > 0 then turtle.dropDown(dAmt) end
	end
end

-- get a slot to split from and a slot to split to
--returns a table with slot index of from slot and to slot
function getSplit(numFurn)
	local most = 0
	local empty = 0
	for i=1,numFurn do
		local sc = turtle.getItemCount(i)
		if sc == 0 then
			empty = i
		elseif sc > 8 and (most == 0 or sc > turtle.getItemCount(most)) then
			most = i
		end
	end
	if empty == 0 or most == 0 then return nil end
	tab = {}
	tab.from = most
	tab.to = empty
	return tab
end

--Splits stacks of items until all spaces are used
function splitStacks(numFurn)
	local spl = getSplit(numFurn)
	while spl do
		turtle.select(spl.from)
		local transAmt = 8*math.floor(turtle.getItemCount()/16)
		turtle.transferTo(spl.to, transAmt)
		spl = getSplit(numFurn)
	end
end

--Grabs sufficient fuel for the items to cook
--return false if we cant get enough
--TODO return items to top chest if not enough fuel
function sourceFuel(numFurn)
	local amt = 0
	for i=1,numFurn do
		amt = amt + (turtle.getItemCount(i)/8)
	end
	turtle.select(16)
	if turtle.getItemCount() ~= 0 then
		print("No space for fuel")
		return false
	end
	while turtle.getItemCount() < amt do
		if not turtle.suck(amt - turtle.getItemCount()) then
			print("not enough fuel to cook items")
			return false
		end
	end
	return true
end

function cookingRun(numFurn)
	turtle.turnRight()
	goF()
	local skipUnload = turtle.detectDown()
	goU()
	turtle.turnLeft()
	goF()
	turtle.turnRight()
	cooking={}
	for i=1,numFurn do
		if i ~= 1 then goF() end
		mostSlot = 1
		for slo=2,numFurn do
			if turtle.getItemCount(slo) > turtle.getItemCount(mostSlot) then mostSlot = slo end
		end
		turtle.select(mostSlot)
		cooking[i] = turtle.getItemCount()
		turtle.dropDown()
	end
	turtle.turnRight()
	goF()
	goD()
	turtle.turnRight()
	turtle.turnRight()
	turtle.select(16)
	for i=1,numFurn do
		turtle.drop(cooking[numFurn-i+1]/8)
		if i ~= numFurn then
			turtle.turnLeft()
			goF()
			turtle.turnRight()
		end
	end
	if skipUnload then
		turtle.turnLeft()
		goF()
		turtle.turnRight()
		return
	end
	goD()
	goF()
	turtle.turnRight()
	for i=2,numFurn do
		goF()
	end
	turtle.turnRight()
	turtle.turnRight()
	for i=1,numFurn do
		if i ~= 1 then goF() end
		turtle.select(i)
		turtle.suckUp()
		while turtle.getItemCount() < cooking[numFurn-i+1] do
			os.sleep(5)
			turtle.suckUp()
		end
	end
	turtle.turnLeft()
	goF()
	goU()
	turtle.turnRight()
	goF()
	turtle.turnRight()
end

function dropOff()
	for i=1,16 do if turtle.getItemCount(i) > 0 then
		turtle.select(i)
		if turtle.getItemDetail(i).name == "minecraft:coal" then
			turtle.drop()
		else
			turtle.dropDown()
		end
	end end
end

local tArgs = {...}
local numFurnaces = 8;
if(tArgs[1]) then
	numFurnaces = tonumber(tArgs[1])
end
if numFurnaces > 15 then numFurnaces = 15 end

print("Using " .. numFurnaces .. " furnaces")
if checkEmpty() then
	turtle.turnLeft()
	if checkRefuel(numFurnaces*10, turtle.suck) then
		loadItems(numFurnaces)
		splitStacks(numFurnaces)
		if sourceFuel(numFurnaces) then
			cookingRun(numFurnaces)
			dropOff()
		end
	end
	turtle.turnRight()
end