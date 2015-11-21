function up()
	while not turtle.up() do
		if turtle.getFuelLevel() < 1 then
			turtle.select(1)
			if not turtle.refuel(1) then
				print("Out of fuel")
				exit()
			end
		else
			turtle.digUp()
		end
	end
end

function down()
	while not turtle.down() do
		if turtle.getFuelLevel() < 1 then
			turtle.select(1)
			if not turtle.refuel(1) then
				print("Out of fuel")
				exit()
			end
		else
			turtle.digDown()
		end
	end
end

function forward()
	while not turtle.forward() do
		if turtle.getFuelLevel() < 1 then
			turtle.select(1)
			if not turtle.refuel(1) then
				print("Out of fuel")
				exit()
			end
		else
			turtle.dig()
		end
	end
end

function placeMarker()
	down()
	down()
	up()
	turtle.select(2)
	turtle.placeDown()
	up()
	turtle.select(3)
	turtle.placeDown()
end

if turtle.getItemCount(2) <= 0 or turtle.getItemCount(3) <= 0 then
	print("place BLOCKS and MARKERS in slots 2 and 3 (fuel-blocks-markers-?)")
	exit()
end
print("The quarry will be in-front and to the right, 2blocks less than the ammount")
print("How many blocks forward")
forwards = tonumber(io.read())
print("How many blocks right")
right = tonumber(io.read())

placeMarker()
for dst=2,forwards do
	forward()
end
turtle.turnRight()
placeMarker()
for dst=2,right do
	forward()
end
turtle.turnRight()
placeMarker()
for dst=2,forwards do
	forward()
end
turtle.turnRight()
placeMarker()
for dst=2,right do
	forward()
end
turtle.turnRight()