import Mongoc, JSON

function getJSON(ID, beginning, final, tolerance=3600)
    client = Mongoc.Client("localhost", 27017)
    plumedb = client["YouiLabDataLayers"]
    positionCollection = plumedb["PlumePositions"]
    pollutantsCollection = plumedb["PlumePollutants"]
    rangeA = 0
    rangeB = 0
    discardedPositions = 0
    discardedPollutants = 0
    query = string("{\"\$and\" : [{\"timestamp\":{\"\$gt\": ", beginning, ", \"\$lte\": " , final,
                             "}}, {\"sensor-id\": \"", ID,"\"}]}")

    global resJSON = "["

    lastTimeStamp = 0


    cursor = Mongoc.find(positionCollection, Mongoc.BSON(query))

    positionsList = []
    for position in cursor
        push!(positionsList, position)
    end



    for (index, position) in enumerate(positionsList)
#= =
|A __________|B
if (lastTimeStamp - currentTimeStamp) > tolerancia
= =#



        (position["timestamp"] - lastTimeStamp) < (tolerance * 2) ? rangeA = position["timestamp"] - (position["timestamp"] - lastTimeStamp) / 2 : rangeA = position["timestamp"] - tolerance
        if (isnothing(positionsList[index + 1]))
            rangeB = position["timestamp"] - tolerance
        else
            (positionsList[index + 1]["timestamp"] -  position["timestamp"]) > (tolerance * 2) ? rangeB = position["timestamp"] + tolerance : rangeB = position["timestamp"] + (positionsList[index + 1]["timestamp"] -  position["timestamp"]) / 2
        end

        # rangeA = position["timestamp"] - tolerance
        # rangeB = position["timestamp"] + tolerance
        lastTimeStamp = position["timestamp"]

        # rangeA=rangeA>=rangeB ? position["timestamp"]-tolerance : rangeB
        # rangeB=rangeB<=rangeA ? position["timestamp"]+tolerance : rangeA
        resJSON *= """{ "ID_Flow" : """ * string(position["sensor-id"]) * """ , "Latitud" : """ * string(position["latitude"]) * """ , "Longitud" : """ * string(position["longitude"])
        resJSON *= """ , "rangeA": """ * string(rangeA) * """ , "rangeB": """ * string(rangeB) * """ , "Measures" : ["""
        for pollutants in Mongoc.find(pollutantsCollection, Mongoc.BSON(query))

            if rangeA <= pollutants["timestamp"] && rangeB >= pollutants["timestamp"]

            resJSON *= """{ "Fecha" : """ * string(pollutants["timestamp"]) * """ , "Contaminantes":""" * JSON.json(pollutants["pollutants"]) * " },"

            else
                discardedPollutants += 1
                continue
            end
        end

        println("ventana de tiempo: ", (rangeB - rangeA))
        resJSON[length(resJSON)] == ',' ?   resJSON = chop(resJSON) : false
    resJSON *= "]},"

    end
    resJSON = chop(resJSON)
    resJSON[length(resJSON)] == '[' ?   resJSON *= "]," : false
println(""" Posiciones descartadas: """ * string(discardedPositions))
    println(""" Contaminantes descartadas: """ * string(discardedPollutants))

    isnothing(resJSON) ? (return resJSON) : (return  resJSON * "]")
end

res = getJSON(13781, 1617207983, 1632639753)
resJson = JSON.parse(res)
# resJson = filter((x) -> x["Measures"] != Any[], resJson)

open("res.json", "w") do f
    JSON.print(f, resJson)
 end
## { Fecha: { $gt: 1601251240, $lt: 1601269199 } , ID_Flow:"15766" }