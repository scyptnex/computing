os.loadAPI("libs/args")
os.loadAPI("libs/move")
os.loadAPI("libs/inventory")

function wall(ri, up, nam)
	move.reset()
	local goRight = true
	for r=1,ri do
		for u=1,up do
			if u~= 1 then if r%2 == 1 then move.softU() else move.softD() end end
			if not inventory.isNamed(nam) and not inventory.selectSlotWith(nam) then
				print("out of " .. nam)
			end
			local worked, dat = turtle.inspect()
			while not worked or dat.name ~= nam do
				if worked then turtle.dig() end
				turtle.place()
				worked, dat = turtle.inspect()
			end
		end
		if r ~= ri then
			turtle.turnRight()
			move.softF()
			turtle.turnLeft()
		end
	end
end

local ri = args.iArg(1, ...)
local up = args.iArg(2, ...)
local inv = turtle.getItemDetail(slot)

if ri and up and inv then
	if move.fuelCheck(ri*up + 5) then
		print("Wall:\n - " .. ri .. " wide " .. up .. " high\n - " .. inv.name)
		wall(ri, up, inv.name)
	else
		print("out of fuel")
	end
else
	print("usage: wall <right> <up>")
	print(" - Place the wall item in slot 1")
end