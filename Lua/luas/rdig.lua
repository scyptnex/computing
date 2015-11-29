os.loadAPI("libs/args")

local fw = args.iArg(1, ...)
local ri = args.iArg(2, ...)
local up = args.iArg(3, ...)
local fwo = args.iArgDef(4, 0, ...)
local rio = args.iArgDef(5, 0, ...)
local upo = args.iArgDef(6, 0, ...)

if fw and ri and up then
	print("rdigging: ", fw, " " , ri, " ", up, " at ", fwo, " ", rio, " ", upo)
else
	print("usage: rdig <forwards> <up> <right> [off forwards] [off right] [off up]")
end