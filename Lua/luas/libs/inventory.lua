--
-- Inventory Management
--

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