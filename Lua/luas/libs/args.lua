--
-- Processing command line arguments
--

-- read an integer argument or return nil
function iArg(i, ...)
	local tArgs = {...}
	if tArgs[i] then
		return tonumber(tArgs[i])
	end
	return nil
end

-- read a string argument or return nil
function sArg(i, ...)
	local tArgs = {...}
	if tArgs[i] then
		return tArgs[i]
	end
	return nil
end

-- read an integer argument or return def as the default
function iArgDef(i, def, ...)
	ret = iArg(i, ...)
	if ret then return ret end
	return def
end

-- read a string argument or return def as the default
function sArgDef(i, def, ...)
	ret = sArg(i, ...)
	if ret then return ret end
	return def
end
