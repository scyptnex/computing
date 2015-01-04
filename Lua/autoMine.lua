--automatically rip resources from a vertical line

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

--goes forward
function softF()
	while not turtle.forward() do
		--do nothing
	end
end

--goes up
function softU()
	while not turtle.up() do
		--do nothing
	end
end

--goes down
function softD()
	while not turtle.down() do
		--do nothing
	end
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

function selectUselessSlot()
	if selectSlotWith("minecraft:cobblestone") then
		return true
	else
		return selectSlotWith("minecraft:dirt")
	end
end

function placeIfWaste()
	local success, block = turtle.inspect()
	if success and
			(block.name == "minecraft:flowing_lava"
			or block.name == "minecraft:flowing_water"
			or block.name == "minecraft:water"
			or block.name == "minecraft:lava")
		then
		if selectUselessSlot() then
			turtle.place()
		end
	end
end

function digExcluding()
	local success, block = turtle.inspect()
	if success
		and block.name ~= "minecraft:stone"
		and block.name ~= "minecraft:cobblestone"
		and block.name ~= "minecraft:gravel"
		and block.name ~= "minecraft:dirt"
		and block.name ~= "minecraft:grass"
		and block.name ~= "minecraft:bedrock"
		and block.name ~= "minecraft:flowing_lava"
		and block.name ~= "minecraft:flowing_water"
		and block.name ~= "minecraft:lava"
		and block.name ~= "minecraft:water"
		then
		turtle.dig()
	end
end

function downShaft()
	local success, block = turtle.inspectDown();
	local t_rot = 0
	local t_height = 0
	while (not success) or (block.name ~= "minecraft:bedrock") do
		goD()
		placeIfWaste()
		digExcluding()
		turtle.turnRight()
		placeIfWaste()
		digExcluding()
		turtle.turnRight()
		placeIfWaste()
		digExcluding()
		turtle.turnRight()
		placeIfWaste()
		digExcluding()
		t_rot = (t_rot+3)%4
		t_height = t_height - 1
		success, block = turtle.inspectDown();
	end
	while t_height ~= 0 do
		goU()
		t_height = t_height + 1
	end
	while t_rot ~= 0 do
		turtle.turnLeft()
		t_rot = t_rot-1
	end
end

function seekWell(side_off, far_off)
	turtle.turnRight()
	turtle.turnRight()
	dist = ((side_off-1)*2)%5 + 5*(far_off-1);
	print(side_off .. " " .. far_off .. " " .. dist)
	softF()
	for away=1,dist do goF() end
	for dwn=1,far_off-1 do goD() end
	downShaft()
	--selectUselessSlot()
	--turtle.placeDown()
	for up=1,far_off-1 do goU() end
	turtle.turnRight()
	turtle.turnRight()
	for back=1,dist do goF() end
	softF()
end

function dropOff()
	while selectUselessSlot() and turtle.drop() do
		--do nothing
	end
	while selectUselessSlot() do
		turtle.dropDown()
	end
	softU()
	up_count = 1
	while turtle.detect() do
		for slot=1,16 do
			if turtle.getItemCount(slot) ~= 0 then
				turtle.select(slot)
				turtle.drop()
			end
		end
		softU()
		up_count = up_count+1
	end
	turtle.turnLeft()
	softF()
	for d_i=1,up_count do softD() end
	-- put all remaining items in the overflow
	for slot=1,16 do
		if turtle.getItemCount(slot) ~= 0 then
			turtle.select(slot)
			turtle.dropDown()
		end
	end
end

function miningMission(side, far, side_skip, far_skip)
	turtle.turnLeft()
	softF()
	softF()
	turtle.turnLeft()
	for f_skp=1,far_skip do softU() end
	for s_skp=1,side_skip do softF() end
	softF()
	turtle.turnLeft()
	local missioned = false
	for far_i=1,far do
		for side_i=1,side do
			if not missioned and not turtle.detect() then
				selectUselessSlot()
				turtle.place()
				if far_i%2 == 1 then
					seekWell(side_i + side_skip, far_i + far_skip)
				else
					seekWell(side - side_i + 1 + side_skip, far_i + far_skip)
				end
				missioned = true
			end
			if side_i == side then
				softU()
			else
				if far_i%2 == 1 then turtle.turnRight() else turtle.turnLeft() end
				softF()
				if far_i%2 == 1 then turtle.turnLeft() else turtle.turnRight() end
			end
		end
	end
	softF()
	turtle.turnRight()
	if far%2 == 0 then
		for side_i=1,side-1 do softF() end
	end
	softF()
	turtle.turnLeft()
	softF()
	turtle.turnLeft()
	for far_i=1,far do softD() end
	for side_i=1,side do softF() end
	for f_skp=1,far_skip do softD() end
	for s_skp=1,side_skip do softF() end
	turtle.turnRight()
	dropOff()
	return missioned
end

-- Place a double chest of coal infront, double refuse to the right, overflow under and lava behind and below
--     r r  |      . .
-- c c <    |  . . x l
--     mine |
function miningStation()
	if turtle.suckDown() then
		turtle.dropDown()
		print ("Items in overflow")
		return false
	end
	turtle.select(1)
	while turtle.getFuelLevel() < 2000 do
		famt = 1 + (2000-turtle.getFuelLevel())/80
		if famt > 64 then famt = 64 end
		if not turtle.suck(famt) then
			print ("not enough fuel")
			return false
		end
		turtle.refuel()
	end
	turtle.turnRight()
	local amt = 0
	while amt < 64 do
		if not turtle.suck(64) then
			print ("not enough refuse")
			turtle.turnLeft()
			return false
		end
		amt = 0
		for slo=1,16 do
			amt = amt + turtle.getItemCount(slo)
		end
	end
	turtle.turnLeft()
	return true
end

local tArgs = {...}
local arg_side = tonumber(tArgs[1])
local arg_far = tonumber(tArgs[2])
if not tArgs[1] then
	print("autoMine.lua side fat [side_skip far_skip]")
	exit()
end
local arg_skip_s = 0
local arg_skip_f = 0
if tArgs[3] and tArgs[4] then
	arg_skip_s = tonumber(tArgs[3])
	arg_skip_f = tonumber(tArgs[4])
end
print("side " .. arg_side .. ", far " .. arg_far)
while miningStation() and miningMission(arg_side, arg_far, arg_skip_s, arg_skip_f) do
	--do nothing
end
turtle.suck()
turtle.dropDown()
while true do
	turtle.digUp()
end
