--
-- Tree-related functions
--

if(not move) then os.loadAPI("libs/move") end
if(not inventory) then os.loadAPI("libs/inventory") end

function treeInFront()
	success, data = turtle.inspect();
	return success and data.name == "minecraft:log"
end

--assumes the turtle is facing the central branch
function leaves()
	for i = 1,4 do
		turtle.digUp()
		turtle.digDown()
		turtle.turnRight()
		move.goF()
		turtle.digUp()
		turtle.digDown()
		turtle.turnRight()
		move.goF()
		turtle.digUp()
		turtle.digDown()
		turtle.turnLeft()
		move.goF()
		turtle.digUp()
		turtle.digDown()
		turtle.turnLeft()
		move.goF()
		turtle.digUp()
		turtle.digDown()
		move.goF()
		turtle.digUp()
		turtle.digDown()
		turtle.turnLeft()
		move.goF()
	end
end


--clear out a birch tree that is in front of you
function birch(replantSappling, cutLeaves)
	local toTop = 999;
	local foundLeaves = false
	local height = 0
	while(toTop >= 0) do
		toTop = toTop - 1
		local above, data = turtle.inspectUp()
		if above and (not foundLeaves) and data.name == "minecraft:leaves" then
			foundLeaves = true
			toTop = 1
		end
		if foundLeaves then print("To top:" .. toTop) end
		turtle.dig()
		move.goU()
		if toTop == 0 and cutLeaves then
		leaves()
		end
		height = height + 1
	end
	turtle.dig()
	for i = 1,height do move.goD() end
	move.goD()
	turtle.dig()
	if replantSappling and inventory.selectSlotWith("minecraft:sapling") then turtle.place() end
	move.goU()
end