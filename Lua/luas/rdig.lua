os.loadAPI("libs/args")
os.loadAPI("libs/move")

-- Choose the height of the current layer, return (digAbove, digBelow)
function chooseY(up)
	x, y, z, h = move.location()
	direction = up/math.abs(up)
	done = y + direction
	if x==0 and y==0 and z==0 then done = 0 end -- initial position has done nothing
	if done+direction >= up then return end
end

function digOut(fw, ri, up)
	move.reset()
	chooseY(up)
	--layer(fw, ri)
end

local fw = args.iArg(1, ...)
local ri = args.iArg(2, ...)
local up = args.iArg(3, ...)
local fwo = args.iArgDef(4, 0, ...)
local rio = args.iArgDef(5, 0, ...)
local upo = args.iArgDef(6, 0, ...)

if fw and ri and up then
	print("digging: f", fw, " r" , ri, " u", up, " at f", fwo, " r", rio, " u", upo)
	digOut(fw, ri, up)
else
	print("usage: rdig <forwards> <right> <up> [off forwards] [off right] [off up]")
end