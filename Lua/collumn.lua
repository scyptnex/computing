AMT=12

function goD()
	while not turtle.down() do
		turtle.digDown()
	end
end

function goU()
	while not turtle.up() do
		turtle.digUp()
	end
end

turtle.refuel()
turtle.select(2)
for i=1,AMT do
	goD()
end

for i=1,AMT do
	goU()
	turtle.placeDown()
end