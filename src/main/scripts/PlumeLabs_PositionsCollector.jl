import HTTP, JSON, Mongoc
using Dates

sensors = ["13061", "13774", "13779", "13781", "14049", "15204", "15746"]
token = "IlFkezrcU2Jvs5fm9oDLhSbn"

function getStartTimestamp()
    descriptor = JSON.parsefile("parallel_descriptor.json")
    return descriptor["plume-positions-collector"]["start-timestamp"]
end

function updateStartTimestamp( timestamp )
    descriptor = JSON.parsefile("parallel_descriptor.json")
    descriptor["plume-positions-collector"]["start-timestamp"] = timestamp
    open("parallel_descriptor.json","w") do f
        JSON.print(f, descriptor)
    end
end

function constructDB(fromDateTime, collection)

    # Database connection
    db = Mongoc.Client("localhost", 27017)["YouiLabDataLayers"][collection]

    while fromDateTime < Dates.now(UTC)
        for sensor in sensors
            println("[PlumeLabs Positions Collector] - starting with sensor: ", sensor)
            timeWindow = [
                trunc(Int64, Dates.datetime2unix(fromDateTime)),
                trunc(Int64, Dates.datetime2unix(fromDateTime + Dates.Day(1)))
            ]
	    println("time window: ", timeWindow)
            httpRequest = string("https://api.plumelabs.com/2.0/organizations/",
                "41/sensors/positions?sensor_id=", sensor,
                "&token=", token,"&start_date=", timeWindow[1],
                "&end_date=", timeWindow[2])
            answer = JSON.parse(String(HTTP.get(httpRequest).body))

            # println(answer)


            if answer["total"] > 0
                for position in answer["positions"]
                    # println("position => ", position)
                    if position != nothing
                        criteria = Dict(
                            "sensor-id" => sensor,
                            "timestamp" => position["date"]
                        )
                        entry = merge(criteria, Dict(
                                "latitude" => position["latitude"],
                                "longitude" => position["longitude"]
                            )
                        )
                        reply = Mongoc.find_and_modify(
                            db,
                            Mongoc.BSON(criteria),
                            update=Mongoc.BSON(entry),
                            flags=Mongoc.FIND_AND_MODIFY_FLAG_UPSERT | Mongoc.FIND_AND_MODIFY_FLAG_RETURN_NEW
                        )
                    end
                end
            end
        end
	updateStartTimestamp(datetime2unix(fromDateTime))
        fromDateTime = fromDateTime + Dates.Day(1)
    end
end


function main()
    fromDateTime = Dates.unix2datetime(getStartTimestamp())
    println("[PlumeLabs Positions Collector] - from DateTime => ", fromDateTime)
    constructDB(fromDateTime, "PlumePositions")
    sleep(10*60)
end

while true
    println("[PlumeLabs Positions Collector] Starting")
    task = @async main()
    wait(task)
end
