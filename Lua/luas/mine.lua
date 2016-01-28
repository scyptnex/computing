os.loadAPI("libs/args")
os.loadAPI("libs/move")
os.loadAPI("libs/inventory")

function isUseful(name)
	return name ~= "minecraft:stone"
		and name ~= "minecraft:cobblestone"
		and name ~= "minecraft:gravel"
		and name ~= "minecraft:dirt"
		and name ~= "minecraft:grass"
		and name ~= "minecraft:bedrock"
		and name ~= "minecraft:flowing_lava"
		and name ~= "minecraft:flowing_water"
		and name ~= "minecraft:lava"
		and name ~= "minecraft:water"
		and name ~= "minecraft:chest"
end

function rebase(ba)
	cx, cy, cz, ch = move.location()
	move.softZ(0)
	move.softX(0)
	local bel, typ = turtle.inspectDown()
	if bel and typ.name == "minecraft:chest" then
		for i=1,16 do
			local inv = turtle.getItemDetail(i)
			if inv and isUseful(inv.name) then
				turtle.select(i)
				turtle.dropDown()
			end
		end
	end
	if ba and bel and typ.name == "minecraft:chest" then
		move.softX(cx)
		move.softZ(cz)
		move.turn(ch)
	else
		move.turn(0)
		print("mission complete")
		exit()
	end
end

-- returns to base if there aren't 2 waste slots
function cullOrRebase()
	local ms = -1
	local xms = -1
	for i=1,16 do
		local inv = turtle.getItemDetail(i)
		if turtle.getItemCount(i) == 0 then
			return
		elseif inv and (not isUseful(inv.name)) and (ms == -1 or turtle.getItemCount(i) <= turtle.getItemCount(ms)) then
			xms = ms
			ms = i
		end
	end
	if xms == -1 then
		rebase(true)
	else
		turtle.select(ms)
		turtle.drop()
		turtle.select(1)
	end
end

-- dig the above and below blocks if we have an empty
-- replace the above and below holes
function nxt()
	cullOrRebase()
	local success, block = turtle.inspectUp()
	if success and isUseful(block.name) then turtle.digUp() end
	if inventory.selectSlotWith("minecraft:cobblestone") or inventory.selectSlotWith("minecraft:dirt") then turtle.placeUp() end
	cullOrRebase()
	local success, block = turtle.inspectDown()
	if success and isUseful(block.name) then turtle.digDown() end
	if inventory.selectSlotWith("minecraft:cobblestone") or inventory.selectSlotWith("minecraft:dirt") then turtle.placeDown() end
	cullOrRebase()
end

function mine(fw, ri)
	move.reset()
	for r=1,ri do
		for f=1,fw do
			nxt()
			if f ~= fw then move.goF() end
		end
		if r ~= ri then
			if r%2 == 1 then move.right() else move.left() end
			move.goF()
			if r%2 == 1 then move.right() else move.left() end
		end
	end
end

local fw = args.iArg(1, ...)
local ri = args.iArg(2, ...)

if fw and ri then
	if move.fuelCheck(fw*ri + fw + ri + 5) then
		print("mining " .. fw .. " forward, " .. ri .. " right")
		mine(fw, ri)
		rebase(false)
	else
		print("out of fuel")
	end
else
	print("usage: mine <forward> <right>")
	print(" - place a chest under for rebasing")
end