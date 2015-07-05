--Do the chicken things

os.loadAPI("libs/inventory")

function activate()
	redstone.setOutput("top", true)
	sleep(0.2)
	redstone.setOutput("top", false)
end

function dropLava()
	if not inventory.selectSlotWith("minecraft:lava_bucket") then
		return
	end
	turtle.dropUp()
	activate()
	sleep(0.5)
	activate()
	turtle.suckUp()
end

function emptyChest()
	while true do
		turtle.select(1)
		if not turtle.suckDown() then break end
		while inventory.selectSlotWith("minecraft:cooked_chicken") do
			turtle.drop()
		end
		while inventory.selectSlotWith("minecraft:feather") do
			turtle.drop()
		end
		while inventory.getEmptySlot() == 0 do
			inventory.selectSlotWith("minecraft:egg")
			turtle.dropUp(1)
			activate()
		end
	end
end