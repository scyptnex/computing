--
-- Inventory Management
--

function getEmptySlot()
	for slot=1,16 do
		if turtle.getItemCount(slot) == 0 then
			return slot
		end
	end
	return 0
end

function selectEmptySlot()
	local id = getEmptySlot()
	if id == 0 then return false end
	turtle.select(id)
	return true
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

function isNamed(name)
	local inv = turtle.getItemDetail()
	return inv and (inv.name == name)
end