--
-- Create and harvest a tower of trees
--

local treeHeight = 11

function harvest()
	print("harvesting")
end

function setup(num)
	print("setting ", num)
end

local setupa = args.sArg(1, ...)
if setupa then
	local num = args.iArg(2, ...)
	if setupa == "setup" and num then
		setup(num)
	else
		print ("treeTower [setup <number>]")
		print (" - setup a tower of <number> trees")
		print (" - otherwise, harvests a tree tower")
	end
else
	harvest()
end